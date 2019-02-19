package crypto.wallet.coin.abst;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.common.domain.res.SendResponse;
import crypto.wallet.common.util.WalletUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * ETH 계열 테스트
 */
@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class) 
public abstract class ERC20Test extends WalletTest implements WalletConst {
  
    protected final String TAG = "[TEST]["+ getSymbol() + "]";
    protected abstract String getLocalMasterAddress();
    
    @Test
    @Override public void validateAddress() {
      
        boolean success = false; 
        int isvalid = 0, isinvalid = 0;
//        String[] validcase   = {rpcService.getLocalMasterAddress(), rpcService.getUserAddressSetFromDB().iterator().next()};
//        String[] invalidcase = {"1PXg2k13YaKvUrpvLvg7r9sFz7VKPAgoWmEbZW", ""};
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
        
        assertTrue(success);
    }
    
    /**
     * TEST 완료: 6/18
     */
    @Test 
    @Override public void getMasterAddress() {
        String addr = rpcService.getSendaddr();
        log.debug(TAG + "[masteraddr] " + addr);
        assertTrue(getLocalMasterAddress().equals(addr));
    }
    
    /**
     * TEST 완료: 6/18
     */
    @Test
    @Override public void getUserAddresses() {
//        Set<String> addrs = rpcService.getUserAddressSetFromDB();
//        log.debug(TAG + "[useraddrs] " + addrs.toString());
//        assertTrue(addrs!=null && addrs.size()>0);
    }
    
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
    
    @Override protected void syncBlocks() { assert(true); }
    
}
