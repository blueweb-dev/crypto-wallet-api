package crypto.wallet.coin.abst;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.test.context.TestPropertySource;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.common.domain.res.SendResponse;
import crypto.wallet.common.util.WalletUtil;
import crypto.wallet.service.intf.OwnChain;
import crypto.wallet.service.intf.PassPhraseWallet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
public abstract class BitcoinTest extends WalletTest implements WalletConst {

    protected final String TAG = "[TEST]["+ getSymbol() + "] ";
    protected abstract String getLocalMasterAddress();
    
    @Test
    @Override public void getMasterAddress() {
        String addr = rpcService.getSendaddr();
        log.debug(TAG + "master addr "+ addr);
        assertTrue(addr!=null && addr.length()>20);
    }
    
    @Test
    @Override public void getUserAddresses() {
//        Set<String> addrs = rpcService.getUserAddressSetFromDB();
//        log.debug(TAG + "user addrs "+ addrs.toString());
//        assertTrue(addrs!=null && addrs.size()>0);
    }
    
    @Test 
    public void walletlock() {
        int successcount = 0;
        try {
            boolean success1 = ((PassPhraseWallet)rpcService).walletpassphrase(WALLET_UNLOCK_SHORT);
            if (success1) { successcount++; }
            boolean success2 = ((PassPhraseWallet)rpcService).walletlock();
            if (success2) { successcount++; }
            if (success1 && success2) {
                log.debug(TAG + "unlock & lock = " + successcount + " true");
            } else {
                log.error(TAG + "unlock & lock = " + successcount + " false");
            }
        } catch(Exception e) { e.printStackTrace(); }
        assertTrue(successcount==2);
    }
    
    /**
     * 출금 요청(DB저장) 테스트
     * @throws Exception
     */
    @Test
    @Override public void requestSendTransaction() {
        SendRequest req = new SendRequest(getSymbol(), WalletUtil.getTestRandomOrderId(), 
            UID_TEST,  getTestuseraddr(), null, TEST_AMOUNT, BROKER_ID_SYSTEM, TEST_EXPCT_FEE);
        SendResponse res = rpcService.requestSendTransaction(req);
        if (res!=null && res.getCode()==CODE_SUCCESS) {
            log.info(TAG + "requestSendTransaction success");
            assertTrue(true);
        } else {
            log.error(TAG + "requestSendTransaction fail");
            assertTrue(false);
        }
    }
    
    @Test
    @Override public void syncBlocks() {
        boolean result = ((OwnChain)rpcService).openBlocksGetTxsThenSave();
        assertTrue(result);
    }
    
    @Test 
    @Override public void validateAddress() {
      
        boolean success = false; 
//        int isvalid = 0, isinvalid = 0;
//        String[] validcase   = {rpcService.getLocalMasterAddress(), rpcService.getUserAddressSetFromDB().iterator().next()};
//        String[] invalidcase = {"0x8816ab870287bbf50d59d62583be1fdea75dc895", ""};
//        
//        ValidateAddressResponse ret = null;
//        ret = rpcService.validateAddress(validcase[0]);
//        isvalid = (ret.getCode()==SUCCESS && ret.isValid())?1:0;
//        
//        ret = rpcService.validateAddress(validcase[1]);
//        isvalid = (ret.getCode()==SUCCESS && ret.isValid())?isvalid+1:isvalid;
//        
//        ret = rpcService.validateAddress(invalidcase[0]);
//        isinvalid = (ret.getCode()==FAIL_LOGICAL || !ret.isValid())?1:0;
//
//        ret = rpcService.validateAddress(invalidcase[1]);
//        isinvalid = (ret.getCode()==FAIL_LOGICAL || !ret.isValid())?isinvalid+1:isinvalid;
//        
//        success = isvalid == 2 && isinvalid == 2;
//        log.debug(TAG + "[validateAddress] " + success);
//        
        assertTrue(success);
    }
    
}
