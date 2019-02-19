package crypto.wallet.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import com.google.gson.JsonSyntaxException;

import crypto.wallet.common.domain.req.PersonalInfoRequest;
import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.common.domain.res.NewAddressResponse;
import crypto.wallet.common.domain.res.SendResponse;
import crypto.wallet.common.domain.res.ValidateAddressResponse;
import crypto.wallet.common.util.WalletUtil;
import crypto.wallet.constant.EthereumConst;
import crypto.wallet.data.domain.TbAddressBalance;
import crypto.wallet.data.domain.TbCryptoMaster;
import crypto.wallet.data.domain.TbOrphanAddress;
import crypto.wallet.data.domain.TbRecv;
import crypto.wallet.data.domain.TbSend;
import crypto.wallet.data.domain.TbSendRequest;
import crypto.wallet.data.gson.btc.BtcRpcError;
import crypto.wallet.data.gson.common.BitcoinBooleanResponse;
import crypto.wallet.data.gson.common.BitcoinGeneralResponse;
import crypto.wallet.data.gson.common.BitcoinStringResponse;
import crypto.wallet.data.gson.eth.EthBlock;
import crypto.wallet.data.gson.eth.EthListAccount;
import crypto.wallet.data.gson.eth.EthTransaction;
import crypto.wallet.data.gson.eth.EthTxReceipt;
import crypto.wallet.data.key.PkSymbolAddr;
import crypto.wallet.data.key.PkUidSymbol;
import crypto.wallet.service.intf.GatherableWallet;
import crypto.wallet.service.intf.OwnChain;
import crypto.wallet.service.intf.PassPhraseAddressWallet;
import crypto.wallet.service.intf.TokenSupport;


public abstract class EthereumAbstractService extends CryptoSharedService implements 
		EthereumConst, OwnChain, PassPhraseAddressWallet, GatherableWallet {
  
    private BigInteger gasPrice;
    private Web3j web3j;
    public Web3j getWeb3j() {
        if (web3j==null) { web3j = Web3j.build(new HttpService(getRpcurl())); }
        return web3j;
    }
    @Autowired private EntityManagerFactory emf;
    
    /**
     * 주소를 발급하고 DB에 저장 후 반환
     * 0x3cc4f01623de56d8fb204c4fd8a81ecab3724410
     * @param 사용자고유ID
     */
    @Transactional @Override public NewAddressResponse newAddress(PersonalInfoRequest req) {
      
        NewAddressResponse ret = getSavedAddress(req);
        if (ret.getResult().getAddress()!=null) { return ret; }
        ret.setResult(req);
        
        // CASE2) 새 요청인 경우 생성
        BitcoinStringResponse res = null;
        String[] params = new String[1];
        params[0] = "\"" + getPp() + "\"";
        try {
            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), METHOD_NEWADDR, params);
            res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getError()!=null) {
                logError(METHOD_NEWADDR, res.getError());
                ret.setCode(CODE_FAIL_LOGICAL);
                ret.setError(res.getError().getMessage());
                return ret;
            }
        } catch (Exception e) {
            logError(METHOD_NEWADDR, e);
            ret.setCode(CODE_FAIL_LOGICAL);
            ret.setError(e.getMessage());
            return ret;
        }
        TbAddressBalance datum = new TbAddressBalance(getSymbol(), 
        		ret.getResult().getUid(), ret.getResult().getBrokerId(), 
        		res.getResult());
        addrBalanceRepo.save(datum);
        ret.setCode(CODE_SUCCESS);
        ret.getResult().setAddress(res.getResult());
        return ret;      
    }

    private String[] getAllAddressArray() {
        String method = SYMBOL_ETC.equals(getSymbol())?METHOD_ACCOUNTS:METHOD_LISTACCOUNTS;
        EthListAccount res = null;
        try {
            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), method, null); // 
            res = gson.fromJson(resStr, EthListAccount.class);
            if (res.getError()!=null) {
                return null;
            } else {
                return res.getResult();
            }
        } catch (IOException | IllegalStateException | JsonSyntaxException e) {
              logError(METHOD_LISTACCOUNTS, e);
              return null;
        }
    }
    
    @Override public List<String> getAllAddressListFromNode() {
        String[] ret = getAllAddressArray();
        if (ret==null) { return null; }
        return (List<String>) Arrays.asList(ret);
    }
    
    @Override public Set<String> getAllAddressSetFromNode() {
        // gets all addresses from ETH node
        String[] userarr = getAllAddressArray();
        if (userarr==null) { return null; }
        Set<String> users = new HashSet<>();
        for (String user : userarr) {
            users.add(user);
        }
        return users;
    }
    
    /**
     * Any 42 character string starting with 0x, and following with 0-9, A-F, a-f (valid hex characters)
     * represent a valid Ethereum address. Pattern is ^(0x)?[a-zA-Z0-9]*$
     */
    @Override public ValidateAddressResponse validateAddress(
    		PersonalInfoRequest param) {
        ValidateAddressResponse ret = new ValidateAddressResponse(param);
        if (WalletUtils.isValidAddress(ret.getResult().getAddress())) {
            ret.getResult().setValid(true);
        } else {
            ret.getResult().setValid(false);
        }
        return ret;
    }
    
    @Override public boolean isSendAddrExists() {
    	List<String> alladdrs = getAllAddressListFromNode();
    	if (alladdrs!=null && alladdrs.contains(getSendaddr())) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    /**
     * raw call examples METHOD_GETBALANCE (eth_getBalance)
     * {"jsonrpc":"2.0","method":"eth_call","params":[
     * {"to": "0xee74110fb5a1007b06282e0de5d73a61bf41d9cd", 
     * "data":"0x70a08231000000000000000000000000c9fbb1691def4d2a54a3eb22e0164359628aa98b"}
     * , "latest"],"id":67}
     */
    @Override public double getAddressBalance(String addr) {
        if (addr==null) { return 0; }
        BigInteger res = null;
        try {
            res = getWeb3j().ethGetBalance(addr, 
            		DefaultBlockParameterName.LATEST).send().getBalance();
            return Convert.fromWei(new BigDecimal(res), Unit.ETHER).doubleValue();
        } catch (Exception e) {
            logError("balanceOf", e);
            return 0;
        }
    }
    
    /**
     * Long gasPrice, Long gasLimit 
     * @param datum
     * @return
     */
    @Override public boolean sendOneTransaction(TbSend datum) {
      
        BitcoinStringResponse res = null;
        JSONObject params = new JSONObject();
        
        String from = (datum.getFromAddr()==null||"".equals(datum.getFromAddr()))?getSendaddr()
        		:datum.getFromAddr();
        double amountEth = datum.getAmount(); 
        params.put("from",     from);
        params.put("to",       datum.getToAddr());
        params.put("value",    WalletUtil.ethToWeiHex(amountEth)); 		      // 단위 WEI
        params.put("gas",      Numeric.toHexStringWithPrefix(getGasLimit())); // 단위 WEI
        params.put("gasPrice", Numeric.toHexStringWithPrefix(WalletUtil.bigIntegerAddFirst(gasPrice))); // 단위 WEI
        
        JSONArray paramArr = new JSONArray();
        paramArr.put(params);
        // 노드에 전송 요청
        try {
            String resStr = WalletUtil.sendJsonRpcJson(getRpcurl(), METHOD_SENDFROMADDR, 
            		paramArr);
            res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getError()!=null) {
                logError(METHOD_SENDFROMADDR, res.getError());
                datum.setErrMsg("[" + res.getError().getCode() + "] " 
                			+ res.getError().getMessage());
                datum.setTxid(res.getResult());
                return false;
            } else {
                datum.setTxid(res.getResult());
                return true;
            }
        } catch (Exception e) {
            logError(METHOD_SENDFROMADDR, e);
            datum.setErrMsg("[-1] " + e.getMessage());
            return false;
        }
    }
    
    @Transactional @Override public boolean syncWalletBalances() {

        List<String> list = getAllAddressListFromNode();
        if (list==null || list.size()<1) { return true; }
        
        String senderaddr = getSendaddr();
        double totalbal = 0, senderbal = 0;
        int successcount = 0;
        int totalcount = list.size();
        
        for (String addr : list) {

            double actualbal = getAddressBalance(addr);
            totalbal += actualbal;
            if (addr.equals(senderaddr)) { senderbal = actualbal; }
            
            // 주소로 잔고찾기
            List<TbAddressBalance> data = addrBalanceRepo.findBySymbolAndAddr(
            		getSymbol(), addr);
            
            if (data!=null && data.size()>0) {
            	if (data.size()>1) {
            		logError("findBySymbolAndAddr", "this should not be happened");
            	}
            	TbAddressBalance datum = data.get(0);
            	datum.setActual(actualbal);
                datum.setBalance(actualbal);
                addrBalanceRepo.save(datum);
            } else if ((data==null || data.size()<1) && actualbal>0) {
            	// 잔고가 있을 때만 고아주소 테이블에 INSERT
            	PkSymbolAddr id = new PkSymbolAddr(getSymbol(), addr);
                Optional<TbOrphanAddress> oraw = orphanAddrRepo.findById(id);
                TbOrphanAddress odatum = null;
                if (oraw.isPresent()) {
                	odatum = oraw.get();
                	odatum.setBalance(actualbal);
                	orphanAddrRepo.save(odatum);
                } else {
                	odatum = new TbOrphanAddress(id);
                	odatum.setBalance(actualbal);
                	orphanAddrRepo.save(odatum);
                }
            }
            successcount++;
        }
        
        Optional<TbCryptoMaster> mraw = cryptoMasterRepo.findById(
        		getSymbol());
        if (mraw.isPresent()) {
            TbCryptoMaster master = mraw.get();
            master.setTotalBal(totalbal);
            master.setSendMastBal(senderbal);
            master.setTheOtherBal(totalbal - senderbal);
            cryptoMasterRepo.save(master);
        }

        return (totalcount==successcount);
    }
    
    
    @Transactional (propagation=Propagation.REQUIRED)
    @Override public boolean syncWalletBalance(int uid) {
        try {
	        PkUidSymbol id = new PkUidSymbol(uid, getSymbol());
	        Optional<TbAddressBalance> rawdatum = addrBalanceRepo.findById(id);
	        if (!rawdatum.isPresent()) { 
	        	return false; 
	        }
	        TbAddressBalance datum = rawdatum.get();
	    	double actual = getAddressBalance(datum.getAddr());
	    	datum.setActual(actual);
	    	datum.setBalance(actual);
	    	addrBalanceRepo.save(datum);
//	        Query q = em.createNativeQuery(QUERY_CALC_BALANCE, CryptoBalance.class);
//	        q.setParameter("uid", 			uid);
//	        q.setParameter("pay_symbol", 	getSymbol());
//	        CryptoBalance item = (CryptoBalance) q.getSingleResult();
//	        datum.setSend(item.getSend());
//	        datum.setRecv(item.getRecv());
//	        datum.setBuy(item.getBuy());
//	        etx.commit();
//	        em.close();
	        return true;
        } catch(Exception e) {
        	e.printStackTrace();
        	return false;
    	}
    }
    
    /**
     * 출금 CONFIRM수 변경 알림
     */
    @Transactional @Override public boolean updateSendConfirm() {
        return updateSendConfirm(getSymbol());
    }
    
    public boolean updateSendConfirm(String symbol) {
        
        // 1) 출금 진행중인 건 조회: TXID가 있고 거래소에 알리지 않은 건
        boolean success = true;
        TbCryptoMaster master   = getCryptoMaster();
        long lastestHeight      = master.getLatestHeight();
        List<TbSend> data       = getSendTXToUpdate(symbol);
        
        if (data!=null && data.size()>0) {
            for (TbSend datum : data) {
                boolean notify = false;
                long currHeight = 0, confirm = 0;
                // 트랜잭션 confirm 조회
                EthTxReceipt res = getTransaction(datum.getTxid());
                if (res.getError()!=null) {
                	// 트랜잭션 에러
                    String errMsg = "[" + res.getError().getCode() + "] " 
                    				+ res.getError().getMessage();
                    logError(METHOD_GETTX, errMsg);
                    if (res.getError().getCode()==-1) {
                        // 에러 코드가 -1이면 노드에러가 아니고 IO익셉션인 경우이므로 다음번에 재발송 될 가능성 있음
                        continue;
                    } else {
                    	// 복구할 수 없는 에러인 경우
                        datum.setErrMsg(errMsg);
                        datum.setNotiCnt(NOTI_CNT_FINISHED);
                        notify = true;
                    }
                    success = false;
                } else if (res.getResult()!=null && ERR_ETH_TX_FAIL.equals(
                		res.getResult().getStatus())) {
                    // 실패한 트랜잭션
                    datum.setNotiCnt(NOTI_CNT_FINISHED);
                    datum.setErrMsg(MSG_BLOCK_TX_FAIL);
                    success = false;
                    notify = true;
                } else {
                    // 성공한 트랜잭션
                    try {
                        currHeight = WalletUtil.hexToLong(
                        		res.getResult().getBlockNumber());
                    } catch (NullPointerException e) {
                        logError("blocknumber", "is null");
                        // 주로 펜딩 프랜잭션
                        // Todo: 어떻게 처리할지 고민
                        continue;
                    }
                    confirm = lastestHeight - currHeight;
                    datum.setConfirm(confirm);
                    if (res.getResult()!=null) {
                        datum.setRealFee(Convert.fromWei(
                        		new BigDecimal(Numeric.toBigInt(res.getResult().getGasUsed()))
                        		, Unit.ETHER).doubleValue());
                    }
                    // 미완료 건 중 알림회수 0인 경우
                    if (datum.getNotiCnt()<NOTI_CNT_PROGRESS) {
                    	// 미완료 건 중 알림회수 0인 경우
                        datum.setNotiCnt(NOTI_CNT_PROGRESS);
                        notify = true;
                    } else if (confirm>=getMinconfirm() && datum.getNotiCnt()<NOTI_CNT_FINISHED) {
                        // confirm 완료된 건 중 완료알림 처리해야 할 것 있는지 조회
                        datum.setNotiCnt(NOTI_CNT_FINISHED);
                        notify = true;
                    }
                }
                if (notify) {
                    notifySendKafka(datum);
                }
                sendRepo.save(datum);
            }
        }
        return success;
    }
    
    @Transactional @Override public boolean updateReceiveConfirm() {
        return updateReceiveConfirm(getSymbol());
    }
    
    /**
     * 입금건 중에 완료건 있는지 조회
     */
    public boolean updateReceiveConfirm(String symbol) {
      
        boolean success = true;
        TbCryptoMaster master = getCryptoMaster();
        long lastestHeight    = master.getLatestHeight();
        List<TbRecv> data     = getRecvTXToUpdate(symbol);
        
        if (data!=null && data.size()>0) {
            for (TbRecv datum : data) {
                boolean notify = false;
                long currHeight = 0, confirm = 0;
                EthTxReceipt res = getTransaction(datum.getTxid());
                if (res.getError()!=null) {
                    String errMsg = "[" + res.getError().getCode() + "] " + res.getError().getMessage();
                    logError(METHOD_GETTX, errMsg);
                    if (res.getError().getCode()==-1) {
                        // 에러 코드가 -1이면 노드에러가 아니고 IO익셉션인 경우이므로 다음번에 재발송 될 가능성 있음
                        continue;
                    } else {
                        datum.setErrMsg(errMsg);
                        datum.setNotiCnt(NOTI_CNT_FINISHED);
                        notify = true;
                    }
                    success = false;
                } else if (res.getResult()!=null && ERR_ETH_TX_FAIL.equals(res.getResult().getStatus())) {
                    // 실패한 트랜잭션
                    datum.setNotiCnt(NOTI_CNT_FINISHED);
                    datum.setErrMsg(MSG_BLOCK_TX_FAIL);
                    success = false;
                    notify = true;
                } else {
                	// 성공한 트랜잭션
                    currHeight = Long.parseLong(datum.getBlockId());
                    confirm = lastestHeight - currHeight;
                    if (confirm>=getMinconfirm()) {
                        // 완료처리해야 할 것 있는지 조회
                        datum.setNotiCnt(NOTI_CNT_FINISHED);
                        notify = true;
                    } else {
                        // 미완료 건 중 알림회수 0인 경우
                        if (datum.getNotiCnt()<NOTI_CNT_PROGRESS) {
                            datum.setNotiCnt(NOTI_CNT_PROGRESS);
                            notify = true;
                        }
                    }
                    datum.setConfirm(confirm);
                    if (datum.getNotifiable()=='N') {
                        notify = false;
                    }
                }
                if (notify) {
                    notifyRecvKafka(datum);
                }
            }
            recvRepo.saveAll(data);
        }
        return success;
    }
    
    @Override public EthTxReceipt getTransaction(String txid) {
        String[] params = new String[1];
        params[0] = "\"" + txid + "\"";
        try {
            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), METHOD_TXRECEIPT, params);
            return gson.fromJson(resStr, EthTxReceipt.class);
        } catch (Exception e) {
            return new EthTxReceipt(new BtcRpcError(-1, e.getMessage()));            
        }
    }
    
    @Override public long getLatestblockFromChain() {
        BitcoinStringResponse res = null;
        try {
            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), 
            		METHOD_GETBLOCKCOUNT, null);
            res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getError()!=null) {
                logError(METHOD_GETBLOCKCOUNT, res.getError());
                return 0;
            } else {
                return Long.decode(res.getResult());
            }
        } catch (Exception e) {
            // 에러 HTML 502 Bad Gateway 등 => com.google.gson.JsonSyntaxException: java.lang.IllegalStateException
            logError(METHOD_GETBLOCKCOUNT, e);
            return 0;
        }
    }
    
    public EthBlock getBlockByHash(String hash) {
        EthBlock res = null;
        String[] params = new String[2];
        params[0] = "\"" + hash + "\"";
        params[1] = "true";
        try {
            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), METHOD_BLOCKBYHASH, params);
            res = gson.fromJson(resStr, EthBlock.class);
            if (res.getError()!=null) {
                logError(METHOD_BLOCKBYHASH, res.getError());
                return null;
            } else {
                return res;
            }
        } catch (Exception e) {
            // 에러 HTML 502 Bad Gateway 등 => com.google.gson.JsonSyntaxException: java.lang.IllegalStateException
            logError(METHOD_BLOCKBYHASH, e);
            return null;
        }
    }
    
    @Transactional @Override public void beforeBatchSend() {
        gasPrice = getGasPrice();
        if (gasPrice.compareTo(BigInteger.ZERO)==1) {
        	// 0보다 크면
            TbCryptoMaster master = getCryptoMaster();
            master.setGasPrice(Convert.fromWei(new BigDecimal(gasPrice), 
            		Unit.GWEI).doubleValue());
            cryptoMasterRepo.save(master);
        }
    }

    /**
     * BigInteger.valueOf(90000).longValue();
     * @return
     */
    public BigInteger getGasLimit() {
        return BigInteger.valueOf(200000); 
    }
    
    /**
     * @return
     */
    public synchronized BigInteger getGasPrice() {
        try {
            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), METHOD_GAS_PRICE, null);
            BitcoinStringResponse res = gson.fromJson(resStr, BitcoinStringResponse.class);
            if (res.getResult()!=null) {
                return Numeric.decodeQuantity(res.getResult());
            } else {
                logError(METHOD_GAS_PRICE, res.getError());
                return BigInteger.ZERO;
            }
        } catch(Exception e) {
            logError(METHOD_GAS_PRICE, e);
            return BigInteger.ZERO;
        }
    }
    
    @Override public boolean walletpassphraseWithAddress(String address) {
    	BitcoinBooleanResponse res = null; 
        String[] params = new String[3];
        params[0] = "\"" + address + "\"";
        params[1] = "\"" + getPp() + "\"";
        params[2] = "null";
//        params[2] = WALLET_UNLOCK_SHORT; // 패리티에서 에러 발생
//        params[2] = "\"" + Numeric.toHexStringWithPrefix(new BigInteger(WALLET_UNLOCK_SHORT)) + "\"";
        
        try {
            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), METHOD_WALLETPP, params);
            res = gson.fromJson(resStr, BitcoinBooleanResponse.class);
            if (res.getError()!=null) {
            	logError("walletpassphraseWithAddress", res.getError());
                logError(METHOD_WALLETPP, res.getError());
                return false;
            } else {
                boolean success = res.isResult();
                if (success) {
                    logSuccess(METHOD_WALLETPP, "success");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            logError(METHOD_WALLETPP, e);
            return false;
        }
    }
    
    /**
     * 지갑 잠금
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    @Override public boolean walletlock(String address) {
        BitcoinGeneralResponse res = null; 
        String[] params = new String[1];
        params[0] = "\"" + address + "\"";
        try {      
            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), METHOD_WALLETLOCK, params);
            res = gson.fromJson(resStr, BitcoinGeneralResponse.class);
            if (res.getError()==null) {
                logSuccess(METHOD_WALLETLOCK, "success");
                return true;
            } else {
                logError(METHOD_WALLETLOCK, res.getError());
                return false; 
            }
        } catch (Exception e) {
            logError(METHOD_WALLETLOCK, e);
            return false;
        }
    }
    
    /**
     * 사용자 잔고 모으기
     */
    @Transactional @Override public boolean requestGathering() {
        
        String masteraddr     = getSendaddr();
        if (masteraddr==null) { return true; }
        TbCryptoMaster master = getCryptoMaster();
        // 주소 테이블 기준으로 잔고가 어느 이상 된 사용자 주소 GET
        List<TbAddressBalance> data = addrBalanceRepo.findBySymbolAndBalanceGreaterThanEqual(getSymbol(), 
        		getMinamtgather());
        if (data==null || data.size()<1) { return true; }
        
        int successcnt = 0, totalcnt = 0;
        
        double estimatedFeeEth = Convert.fromWei(
        		new BigDecimal(getGasLimit().multiply(gasPrice))
        		, Unit.ETHER).doubleValue();
        if (this instanceof TokenSupport) {
            estimatedFeeEth += ((TokenSupport)this).getMingasamt();
        }
        
        for (TbAddressBalance datum : data) {
            // 동일한 출금 요청 건 있는지 검사
            TbSendRequest prevReq = sendReqRepo.findFirstBySymbolAndFromAddrAndToAddrAndRegDtGreaterThanOrderByRegDtDesc(
                getSymbol(), datum.getAddr(), masteraddr, getGatherLimitHour());
            if (prevReq!=null) { continue; } 
            totalcnt++;
            double balance = datum.getBalance();
            String orderId = WalletUtil.getRandomSystemOrderId();
            double realamt = balance - estimatedFeeEth;
            
            SendRequest req = new SendRequest(getSymbol(), orderId, UID_SYSTEM, 
            		getSendaddr(), null, realamt, BROKER_ID_SYSTEM, master.getActualFee());
            req.setFromAddress(datum.getAddr());
            req.setNotifiable('N');
            // 출금요청 저장
            SendResponse res = requestSendTransaction(req);
            if (res!=null) { successcnt++; }
        }
        return totalcnt==successcnt;
    }
    
    /**
     * CLO인 경우나 TOKEN없이 ETH 싱글모드로만 할 때 이용
     * @return
     */
    public boolean openBlocksGetTxsThenSaveSingle() {
        
        TbCryptoMaster master = getCryptoMaster();
        long currentHeight = master.getCurrSyncHeight();
        long lastestHeight = master.getLatestHeight();
        
        if (currentHeight<lastestHeight) {

            // 동기화 할 블럭이 있으면          
            int blockcount = 0; 
            logInfo("SYNC", currentHeight + "=>" + lastestHeight);
            String masteraddr    = getSendaddr();
            Set<String> alladdrs = getAllAddressSetFromNode();
            
            EntityManager em = emf.createEntityManager();
            EntityTransaction etx = em.getTransaction();
            etx.begin();
            
            try {
              
                for (long i=currentHeight; i<=lastestHeight; i++) {
                  
                    // 블럭 다 열어봐
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
                        }
                    } catch (Exception e) {
                        logError(METHOD_BLOCKBYNUMBER, e);
                        if (etx.isActive()) {
                            etx.rollback();
                        }
                        em.close();
                        return false;
                    }
                    
                    // 블록 높이 차로 confirmation 수 구함
                    long confirm   = lastestHeight - i;
                    long time      = WalletUtil.hexToLong(res2.getResult().getTimestamp());
                    String blockid = "" + i;
                    
                    for (EthTransaction tx : res2.getResult().getTransactions()) {
                        // 반환된 모든 트랜잭션 반복해서 SEND/RECEIVE인지 판단 한 뒤 각 테이블에 CONFIRMATION 수 업데이트
                        // 송신주소나 수신주소가 ETH거나 ERC20이 아니면
                        if (!(alladdrs.contains(tx.getFrom()) || alladdrs.contains(tx.getTo()))) {
                            // 우리 주소가 아니면
                            continue;
                        }
                        boolean failed = false;
                        EthTxReceipt res3 = getTransaction(tx.getHash());
                        if (res3.getError()!=null || ERR_ETH_TX_FAIL.equals(res3.getResult().getStatus())) {
                            // 실패한 트랜잭션
                            failed = true;
                        } else if (res3.getResult()==null) { 
                            continue;
                        }
                        
                        double amount       = WalletUtil.hexToEth(tx.getValue());
                        boolean issend      = alladdrs.contains(tx.getFrom());
                        boolean isreceive   = alladdrs.contains(tx.getTo());
                        boolean isgenerated = false;
                        boolean istomaster  = tx.getTo()!=null && tx.getTo().equals(masteraddr);
                        String toaddr       = tx.getTo();
                        
                        if (issend) {
                            // 출금) 마스터 어카운트에서 어드레스로
                            double actualFee = WalletUtil.hexToWei(res3.getResult().getGasUsed()) 
                                    / 1 * WalletUtil.hexToWei(tx.getGasPrice());
                            TbSend datum = sendRepo.findFirstBySymbolAndTxidAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
                                      (getSymbol(), tx.getHash(), toaddr, getLimitDate());
                            if (datum==null) {
                                TbSendRequest reqdatum = sendReqRepo.findFirstBySymbolAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
                                    (getSymbol(), toaddr, getLimitDate());
                                if (reqdatum!=null) {
                                    datum = new TbSend(reqdatum.getOrderId(), reqdatum.getSymbol(), 
                                        reqdatum.getUid(), reqdatum.getToAddr(), null, 
                                        reqdatum.getAmount(), reqdatum.getBrokerId());
                                } else {
                                    // important) 출금/출금요청 테이블에 존재하지 않는 출금 건: 출금건의 경우 오히려 브로커 매핑이 간단
                                    datum = new TbSend(WalletUtil.getRandomSystemOrderId(), getSymbol(), 
                                        UID_SYSTEM, toaddr, null, amount, BROKER_ID_SYSTEM);
                                    datum.setNotifiable('N');
                                }
                            }
                            datum.setFromAddr(tx.getFrom());
                            datum.setConfirm(confirm);
                            datum.setBlockId(blockid);
                            datum.setRealFee(actualFee);
                            datum.setTxTime(time);
                            datum.setTxid(tx.getHash());
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
                            TbRecv datum = recvRepo.findFirstBySymbolAndTxidAndToAddr(
                            		getSymbol(), tx.getHash(), tx.getTo());
                            if (datum==null) {
                                // find broker_id, uid, From addr 
                            	List<TbAddressBalance> data = addrBalanceRepo.findBySymbolAndAddr(getSymbol(), toaddr);
                                datum = new TbRecv(getSymbol(), null, null, toaddr, amount);
                                if (data!=null && data.size()>0) {
                                	TbAddressBalance tbaddr = data.get(0);
                                    datum.setBrokerId(tbaddr.getBrokerId());
                                    datum.setUid(tbaddr.getUid());
                                } else {
                                    // important) 주소 테이블에 존재하지 않는 주소에 대한 입금 건
                                    datum.setBrokerId(BROKER_ID_SYSTEM);
                                    datum.setUid(UID_SYSTEM);
                                }
                            }
                            datum.setFromAddr(tx.getFrom());
                            datum.setTxid(tx.getHash());
                            datum.setConfirm(confirm);
                            datum.setBlockId(blockid);
                            datum.setTxTime(time);
                            
                            if (istomaster || isgenerated) {
                                // 마스터주소로 입금건은 알리지 않음
                                datum.setNotifiable('N');
                            }
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
                    // TXs end
                    blockcount++;
                    if (blockcount>99) {
                        logInfo("SYNC", blockid);
                        master.setCurrSyncHeight(i);
                        em.merge(master);
                        etx.commit();
                        etx.begin();
                        blockcount = 0;
                    }
                }
                master.setCurrSyncHeight(lastestHeight);
                em.merge(master);
                if (etx.isActive()) {
                    etx.commit();
                }
                em.close();
            } catch (Exception e) {
                if (etx.isActive()) {
                    etx.rollback();
                }
                em.close();
            }
            return true;
        } else {
            logDebug("SYNC", currentHeight + "=" + lastestHeight + " skip");
            return true;
        }
    }
    
    /**
     * 테스트 완료
     */
//    @Override public String getSendaddr() { 
//        String localMasterAddr = getPropSendaddr();
//        String method = SYMBOL_ETC.equals(getSymbol())?METHOD_ACCOUNTS:METHOD_LISTACCOUNTS;
//        try {
//            String resStr = WalletUtil.sendJsonRpc2(getRpcurl(), method, null);
//            BitcoinGeneralResponse res = gson.fromJson(resStr, BitcoinGeneralResponse.class);
//            if (res.getError()==null) {
//                List<String> result = (ArrayList<String>) res.getResult();
//                if (result.contains(localMasterAddr)) {
//                    return localMasterAddr;
//                }
//                return null;
//            } else {
//                logError("masterAddress", res.getError());
//                return null;
//            }
//        } catch(Exception e) {
//            logError("masterAddress", e);
//            return null;
//        }
//    }
    
}
