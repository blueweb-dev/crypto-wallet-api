package crypto.wallet.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.common.domain.res.SendResponse;
import crypto.wallet.common.util.WalletUtil;
import crypto.wallet.constant.EthereumConst;
import crypto.wallet.data.domain.TbAddressBalance;
import crypto.wallet.data.domain.TbCryptoMaster;
import crypto.wallet.data.domain.TbRecv;
import crypto.wallet.data.domain.TbSend;
import crypto.wallet.data.domain.TbSendRequest;
import crypto.wallet.data.gson.common.AddressBalance;
import crypto.wallet.data.gson.eth.EthBlock;
import crypto.wallet.data.gson.eth.EthTransaction;
import crypto.wallet.data.gson.eth.EthTxReceipt;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.ERC20AbstractService;
import crypto.wallet.service.EthereumAbstractService;
import crypto.wallet.service.intf.TokenSupport;
import lombok.Getter;


/**
 * ETH services
 * @author sungjoon.kim
 */
@Service("ethRpcService") class ETHRpcServiceImpl extends EthereumAbstractService 
          implements EthereumConst, TokenSupport {

    @Getter private final String symbol = SYMBOL_ETH;
    @Getter @Value("${crypto.eth.rpcurl}") private String rpcurl;
    @Getter @Value("${crypto.eth.decimals}") private int decimals;
    @Getter @Value("${crypto.eth.pp}") private String pp;
    @Getter @Value("${crypto.eth.sendaddr}") private String sendaddr;
    @Getter @Value("${crypto.eth.minconfirm}") private long minconfirm;
    @Getter @Value("${crypto.eth.minamtgather}") private double minamtgather;
    @Getter @Value("${crypto.eth.initialblock}") private long initialblock;
    @Getter @Value("${crypto.eth.mingasamt}") private double mingasamt;
    
    @Autowired private EntityManagerFactory emf;
    
    @Override public Map<String, String> getContractAddressMap() {
        Map<String, String> ret = new HashMap<>();
        ERC20AbstractService service = null;
        for (String symbol : ENABLED_ERC20S) {
            service = (ERC20AbstractService) coinFactory.getService(symbol);
            ret.put(service.getContractaddr(), symbol);
        }
        return ret;
    }
    
    private final String SELECT_ERC20_NOTENOUGH_GAS = "select symbol, addr, balance from tb_address_balance where symbol = 'ETH'"
          + " and balance < ";
    
    /**
     * description: ERC20 토큰 송금하기에 부족한 ETH를 보유한 사용자에게 ETH 송금
     * how: 주소 테이블 기준으로 잔고가 min보다 큰 사용자 주소 GET
     *      select addr from tb_address_balance where symbol = 'ETH' and addr in 
     *        ( select distinct addr from tb_address_balance where ( symbol = 'BHPC' and balance >= 300 ) or ( symbol = 'ZIL' and balance >= 7000 ) );
     */
    @Override public boolean fillGasWhereNotEnough() {
        
        TbCryptoMaster master = getCryptoMaster();
        EntityManager em = null;
        try {
            // ERC20 중에 이더(GAS)가 0.01보다 작은 ETH 주소 추출
            StringBuilder queryStr = new StringBuilder(SELECT_ERC20_NOTENOUGH_GAS);
            queryStr.append(mingasamt);
            queryStr.append(" and addr in ( select distinct addr from tb_address_balance where ");
            for (int i=0; i<ENABLED_ERC20S.length; i++) {
                if (i>0 && i<ENABLED_ERC20S.length) {
                    queryStr.append(" or ");  
                }
                CryptoSharedService lservice = coinFactory.getService(ENABLED_ERC20S[i]);
                double minamt = lservice.getMinamtgather();
                queryStr.append("( symbol = '" + ENABLED_ERC20S[i] + "' and balance >= " + minamt + " )");  
            }
            queryStr.append(")");  
            em = emf.createEntityManager();
            Query q = em.createNativeQuery(queryStr.toString(), AddressBalance.class);
            List<AddressBalance> data = q.getResultList();
            if (em!=null && em.isOpen()) { em.close(); }
            
            if (data!=null && data.size()>0) {
                logInfo("fillGasWhereNotEnough", "q " + queryStr + "\nresult " + data.toString());
            } else {
                return true;
            }
            int successcount = 0, totalcount = 0;
            for (AddressBalance datum : data) {
                  double sendamt = mingasamt - datum.getBalance() + master.getActualFee()*1.1;
                  // 최소금액 방어로직
                  if (sendamt < 0.002) { continue; }
                  // Todo: 최근 중복인출 체크
                  TbSendRequest prevreq = sendReqRepo.findFirstBySymbolAndFromAddrAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
                      (getSymbol(), getSendaddr(), datum.getAddr(), getGatherLimitHour());
                  if (prevreq!=null) { continue; }
                  totalcount++;
                  SendRequest req = new SendRequest(getSymbol(), 
                        WalletUtil.getRandomSystemOrderId(), UID_SYSTEM, datum.getAddr(), null, 
                        sendamt, BROKER_ID_SYSTEM, 0);
                  req.setNotifiable('N');
                  SendResponse res = requestSendTransaction(req); 
                  if (res!=null && res.getCode()==CODE_SUCCESS) { successcount++; }
            }
            return totalcount==successcount;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
     
    }
      
    /**
     * ETH와 ERC20 모두 동기화
     */
    @Override public boolean openBlocksGetTxsThenSave() {
      
        TbCryptoMaster master = getCryptoMaster();
        long currentHeight    = master.getCurrSyncHeight();
        long lastestHeight    = master.getLatestHeight();
        
        if (currentHeight<lastestHeight) {
          
            logInfo("SYNC", currentHeight + "=>" + lastestHeight);

            // 동기화 할 블럭이 있으면          
            int blockcount = 0; 
            String masters = getSendaddr();
            // 토큰 스마트컨트랙트 주소 GET
            Map<String, String> contractMap = getContractAddressMap();
            Set<String> alladdrs = getAllAddressSetFromNode();
            if (alladdrs==null || alladdrs.size()<1) { return true; }
            
            EntityManager em = emf.createEntityManager();
            EntityTransaction etx = em.getTransaction();
            etx.begin();
            
            try {
            
                for (long i=currentHeight; i<=lastestHeight; i++) {
                    
                	// 1) 블록정보 가져오기 => 트랜잭션 배열
                    String blockid = "" + i;
                    String[] params2 = new String[2];
                    params2[0] = "\"" + WalletUtil.longToHex(i) + "\"";
                    params2[1] = "true";
                    EthBlock res2 = null;
                    try {
                        String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), 
                        		METHOD_BLOCKBYNUMBER, params2);
                        // 블럭해시 가져오기
                        res2 = gson.fromJson(resStr, EthBlock.class);
                        if (res2.getError()!=null) {
                            logError(METHOD_BLOCKBYNUMBER, res2.getError());
                            if (etx.isActive()) {
                                etx.rollback();
                            }
                            em.close();
                            return false;
                        } else if (res2==null || res2.getResult()==null) {
                            logError(METHOD_BLOCKBYNUMBER, params2[0] + " block is null " + res2.toString());
                            continue;
                        }
                    } catch (Exception e) {
                        logError(METHOD_BLOCKBYNUMBER, e);
                        if (etx.isActive()) {
                            etx.rollback();
                        }
                        em.close();
                        return false;
                    }
                    
                    // 블록 높이 차로 구함
                    long confirm = lastestHeight - i;
                    long time = WalletUtil.hexToLong(res2.getResult().getTimestamp());
                    
                    // 2) 반환된 모든 트랜잭션 반복해서 SEND/RECEIVE인지 판단 한 뒤 각 테이블에 CONFIRMATION 수 업데이트
                    for (EthTransaction tx : res2.getResult().getTransactions()) {
                      
                        boolean failed         = false;
                        boolean issend         = false;
                        boolean isreceive      = false;
                        boolean istosendmaster = false;
                        boolean isgenerated    = false;
                        boolean issystem       = false;
                        boolean smartcontract  = false;
                        double amount    = 0;
                        String method    = "";
                        String rawmethod = "";
                        String lsymbol   = getSymbol();
                        String toaddr    = tx.getTo();
                        String fromaddr  = tx.getFrom();
                        String txid 	 = tx.getHash();
                        
                        if (contractMap.containsKey(toaddr)) {
                            // ERC20 토큰이면 
                            smartcontract = true;
                            lsymbol = contractMap.get(toaddr);
                            if (tx.getInput().length()<74) {
                                // 
                                logError("smartcontract", "input " + tx.getInput());
                            } else {
                                toaddr  = "0x" + tx.getInput().substring(34, 74);  
                            }
                        }
                        
                        // 입금인지 출금인지 둘다 인지 확인
                        if (alladdrs.contains(fromaddr)) { issend = true; }
                        if (alladdrs.contains(toaddr)) {
                            isreceive = true;
                            if (masters!=null) { istosendmaster = masters.equals(toaddr); }
                        }
                        // 우리 거래소의 주소가 아님
                        if (!(isreceive || issend)) { continue; }
//                        logInfo("found wallet TX", tx.toString());
                        
                        EthTxReceipt res3 = getTransaction(txid);
                        if (res3.getResult()==null) {
                            logError("getTransaction", "result is null. check if ETH is in 'light' mode");
                            continue;
                        } else if (ERR_ETH_TX_FAIL.equals(res3.getResult().getStatus())) {
                            // 실패한 트랜잭션
                            failed = true;
                        }
                        
                        if (smartcontract) {
                            try {
                                rawmethod = tx.getInput().substring(0,  10);
                                if (!Arrays.asList(ENABLED_ERC_METHODS).contains(rawmethod)) {
                                	// 우리 주소라고 취급하지 않는 스마트큰트랙트 펑션이면 SKIP
                                	continue;
                                }
                            } catch (Exception e) { e.printStackTrace(); }
                            int decimals = ((ERC20AbstractService)coinFactory.getService(lsymbol)).getDecimals();
                            if (tx.getInput().length()<74) {
                                logError("smartcontract", "input " + tx.getInput());
                            }
                            amount = WalletUtil.hexTokenAmountToDouble(tx.getInput().substring(74).replaceFirst("^0+(?!$)", ""), decimals);
                            
                            if (tx.getInput().startsWith(ERC_TRANSFER_CODE) || tx.getInput().startsWith(ERC_TRANSFERFROM_CODE) 
                                    || tx.getInput().startsWith(ERC_APPROVE_CODE)) {
                                method = tx.getInput().startsWith(ERC_TRANSFER_CODE)?ERC_TRANSFER_NAME
                                    :tx.getInput().startsWith(ERC_APPROVE_CODE)?ERC_APPROVE_NAME
                                        :tx.getInput().startsWith(ERC_TRANSFERFROM_CODE)
                                        		?ERC_TRANSFERFROM_NAME:METHOD_ERC_UNKNOWN;
                                // 138자: 펑션:10자(0xa9059cbb)+수신주소(64자)+금액(64자)
                                // 64) 000000000000000000000000139ff6f0a40f9fa49a26175e4795584007adb7d5: 주신주소(24패딩->40자)
                                // 64) 0000000000000000000000000000000000000000000000056bc75e2d63100000: 금액(0패딩 정규식 제거, WEI), 
                                logInfo(lsymbol + "][smartcontract", "[" + method + "] " + tx.toString());
                            } else {
                                logWarn(lsymbol + "][smartcontract", "[" + rawmethod + "] " + tx.toString());
                            }
                        } else {
                            // 이더리움이면
                            amount = WalletUtil.hexToEth(tx.getValue());
                        }
                        
                        // 1) 출금건 
                        // 	(1) 출금자가 마스터어드레스인 경우: TXID가 있으면서 최종완료되지 않은 출금건 조회 
                        if (issend) {
                            double actualFeeEth = WalletUtil.hexToEth(res3.getResult().getGasUsed()) 
                                      * WalletUtil.hexToEth(tx.getGasPrice());
                            TbSend datum = sendRepo.findFirstBySymbolAndTxidAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
                                  (lsymbol, txid, toaddr, getLimitDate());
                            if (datum==null) {
                            	// 기 송금 요청 데이터가 없는 경우
                                TbSendRequest reqdatum = sendReqRepo
                                		.findFirstBySymbolAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
                                			(lsymbol, toaddr, getLimitDate());
                                if (reqdatum!=null) {
                                	// 출금요청 데이터가 있으면
                                    datum = new TbSend(reqdatum.getOrderId(), reqdatum.getSymbol(), 
                                        reqdatum.getUid(), reqdatum.getToAddr(), null, 
                                        reqdatum.getAmount(), reqdatum.getBrokerId());
                                } else {
                                    // 출금요청 테이블에 존재하지 않는 출금 건: 출금건의 경우 오히려 브로커 매핑이 간단
                                    datum = new TbSend(WalletUtil.getRandomSystemOrderId(), lsymbol, 
                                        UID_SYSTEM, toaddr, null, amount, BROKER_ID_SYSTEM);
                                   	datum.setNotifiable('N');
                                }
                            }
                            // 송금자 UID 찾기
                            List<TbAddressBalance> add = addrBalanceRepo.findBySymbolAndAddr(lsymbol, fromaddr);
                            if (add!=null && add.size()>0) {
                            	TbAddressBalance tbaddr = add.get(0);
                                datum.setBrokerId(tbaddr.getBrokerId());
                                datum.setUid(tbaddr.getUid());
                            }
                            datum.setFromAddr(fromaddr);
                            datum.setTxid(txid);
                            datum.setConfirm(confirm);
                            datum.setBlockId(blockid);
                            datum.setRealFee(actualFeeEth);
                            datum.setTxTime(time);
                            if (failed) {
                                if (res3.getError()!=null && res3.getError().getMessage()!=null) {
                                    datum.setErrMsg("[" + res3.getError().getCode() + "] " + res3.getError().getMessage());    
                                } else {
                                    datum.setErrMsg(MSG_BLOCK_TX_FAIL);
                                }
                            }
                            em.merge(datum);
                        }
    
                        if (isreceive || isgenerated) {
                            // 입금건) 입금자 유저  어드레스인 거래건:
                            TbRecv datum = recvRepo.findFirstBySymbolAndTxidAndToAddr(lsymbol, txid, toaddr);
                            if (datum==null) {
                                // 사용자 주소테이블에서 uid, addr, broker_id 찾기 
                                List<TbAddressBalance> add = addrBalanceRepo.findBySymbolAndAddr(lsymbol, toaddr);
                                datum = new TbRecv(lsymbol, fromaddr, null, toaddr, amount);
                                if (add!=null && add.size()>0) {
                                	// 관리되고 있는 주소이면
                                	TbAddressBalance tbaddr = add.get(0);
                                    datum.setBrokerId(tbaddr.getBrokerId());
                                    datum.setUid(tbaddr.getUid());
                                } else {
                                    // important) 주소 테이블에 존재하지 않는 주소에 대한 입금 건
                                    datum.setBrokerId(BROKER_ID_SYSTEM);
                                    datum.setUid(UID_SYSTEM);
                                }
                            }
                            if (istosendmaster || isgenerated || issystem) {
                                datum.setNotifiable('N');
                            } 
                            datum.setFromAddr(fromaddr);
                            datum.setTxid(txid);
                            datum.setConfirm(confirm);
                            datum.setBlockId(blockid);
                            datum.setTxTime(time);
                            if (failed) {
                            	if (res3.getError()!=null && res3.getError().getMessage()!=null) {
                                    datum.setErrMsg("[" + res3.getError().getCode() + "] " + res3.getError().getMessage());    
                                } else {
                                    datum.setErrMsg(MSG_BLOCK_TX_FAIL);
                                }
                            }
                            em.merge(datum);
                        }
                    }
                    blockcount++;
                    master.setCurrSyncHeight(i);
                    em.merge(master);
                    if (blockcount>99) {
                        logInfo("SYNC", blockid);
                        etx.commit();
                        etx.begin();
                        blockcount = 0;
                    }
                }
                
                master.setCurrSyncHeight(lastestHeight);
                em.merge(master);
                etx.commit();
                em.close();
                logInfo("SYNC", lastestHeight + " finished");
                
            } catch (Exception e) {
                e.printStackTrace();
                if (etx.isActive()) {
                    etx.rollback();
                }
                em.close();
            } finally {
                alladdrs.clear();
                alladdrs = null;
            }
            
            return true;
        } else {
            logDebug("SYNC", currentHeight + "=" + lastestHeight + " skip");
            return true;
        }
    }

}
