package crypto.wallet.shared;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.data.domain.TbCryptoMaster;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import crypto.wallet.service.intf.GatherableWallet;
import crypto.wallet.service.intf.OwnChain;
import crypto.wallet.service.intf.PassPhraseWallet;
import crypto.wallet.service.kafka.KafkaSender;
import lombok.extern.slf4j.Slf4j;


@Slf4j @Component public class SharedTest implements WalletConst {

    @Autowired private CoinFactory coinFactory;
    @Value("${app.test-topic-name}") private String TOPIC_NOTIFY_TX_CONFIRM;
    @Autowired private KafkaSender sender;
    private String[] invalidcaseBTC = {"0x8816ab870287bbf50d59d62583be1fdea75dc895", ""};
    private String[] invalidcaseETH = {"1PXg2k13YaKvUrpvLvg7r9sFz7VKPAgoWmEbZW", ""};
    protected CryptoSharedService service;
    
    public boolean validateAddress(String symbol) {
      
        int isvalid = 0, isinvalid = 0;
//        service = coinFactory.getService(symbol);
//        String[] validcase   = {service.getLocalMasterAddress(), service.getUserAddressSetFromDB().iterator().next()};
//        ValidateAddressResponse res = null;
//        res = service.validateAddress(validcase[0]);
//        isvalid += res.getCode()==SUCCESS && res.isValid() ? 1 : 0;
//        res = service.validateAddress(validcase[1]);
//        isvalid += res.getCode()==SUCCESS && res.isValid() ? 1 : 0;
//        // BTC, ETH 계열 분리
//        
//        res = service.validateAddress(invalidcaseBTC[0]);
//        isinvalid += res.getCode()!=SUCCESS || !res.isValid() ? 1 : 0;
//        res = service.validateAddress(invalidcaseBTC[1]);
//        isinvalid += res.getCode()!=SUCCESS || !res.isValid() ? 1 : 0;
//        
        return (isvalid==2 && isinvalid==2);
    }
    
    public boolean updateConfirm(String symbol) {
        service = coinFactory.getService(symbol);
        return service.updateSendConfirm();
    }
    
    public boolean syncBlocks(String symbol) {
        service = coinFactory.getService(symbol);
        if (service instanceof OwnChain) {
            return ((OwnChain)service).openBlocksGetTxsThenSave();
        } else {
            return true;
        }
    }
    
//    public boolean sendFromRequest(String symbol) {
//        service = coinFactory.getService(symbol);
//        SendToAddressRequest req = new SendToAddressRequest(symbol, WalletUtil.getTestRandomOrderId(), USER_TEST, 
//            service.getUserAddressSetFromDB().iterator().next(), null, TEST_AMOUNT, SYSTEM_BROKER_ID, TEST_EXPCT_FEE);
//        SendToAddressResponse res = service.requestSendTransaction(req);
//        return (res!=null && res.getCode()==SUCCESS);
//    }
    
    public boolean batchSendTransaction(String symbol) {
        service = coinFactory.getService(symbol);
        return service.batchSendTransaction();
    }
    
    public boolean gathering(String symbol) {
        service = coinFactory.getService(symbol);
        if (service instanceof GatherableWallet) {
            return ((GatherableWallet)service).requestGathering();  
        } else {
            return true;
        }
    }
    
    public boolean getBalances(String symbol) {
//        service = coinFactory.getService(symbol);
//        TotalBalance bal = service.getTotalBalance();
//        log.info("bal " + bal.toString());
//        return (bal!=null && bal.getCode()==CODE_SUCCESS);
    	return false;
    }
    
    public boolean passPhrase(String symbol) {
        service = coinFactory.getService(symbol);
        if (service instanceof PassPhraseWallet) {
            boolean success1 = ((PassPhraseWallet)service).walletpassphrase(WALLET_UNLOCK_SHORT);
            boolean success2 = ((PassPhraseWallet)service).walletlock();
            return (success1 && success2);
        } else {
            return true;
        }
    }
    
    public boolean getStartLastBlockHeight(String symbol) {
        service = coinFactory.getService(symbol);
        TbCryptoMaster master = service.getCryptoMaster();
        Long[] height = new Long[2];
        height[0] = master.getCurrSyncHeight();
        height[1] = master.getLatestHeight();
        return (height!=null && height[0]!=null && height[1]!=null);
    }
    
    public boolean newAddress(String symbol) {
        service = coinFactory.getService(symbol);
//        NewAddressResponse res = service.newAddress(UID_TEST, WalletUtil.getTestRandomOrderId());
//        return (res!=null && res.getCode()==CODE_SUCCESS && res.getAddress().length()>10);
        return false;
    }
    
    public boolean notifyProcess(String symbol) {
        service = coinFactory.getService(symbol);
//        int[] sendsuccess = service.notifySendConfirm(symbol);
//        int[] recvsuccess = service.notifyReceive(symbol);
//        return sendsuccess[0]==sendsuccess[1] && recvsuccess[0]==recvsuccess[1];
        return true;
    }
    
//    public boolean kafkaNotify(String symbol) {
//        service = coinFactory.getService(symbol);
//        if (service.getMasterAddress()!=null && service.getUserAddressSetFromDB()!=null && service.getUserAddressSetFromDB().size()>0) {
//            TransactionResponse item = new TransactionResponse(TXType.SEND, TXStatus.INPROGRESS, symbol, USER_TEST, 
//                WalletUtil.getTestRandomBrokerId(), WalletUtil.getTestRandomOrderId(),
//                WalletUtil.getTestRandomTxId(), TEST_AMOUNT, 1L,  
//                WalletUtil.getTestRandomTxTime(), null);
//            sender.send(TOPIC_NOTIFY_TX_CONFIRM, item);
//            return true;
//        } else {
//            return false;
//        }
//    }
  
}
