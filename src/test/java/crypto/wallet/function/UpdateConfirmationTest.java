package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import crypto.wallet.service.intf.OwnChain;
import crypto.wallet.shared.SharedTest;
import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j 
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class) public class UpdateConfirmationTest implements WalletConst {
  
    private final String TAG = TAG_TEST + "[confirmationNotify]";
    @Autowired SharedTest testCase;
    @Autowired CoinFactory coinFactory;
    
    @Test
    public void updateConfirm() throws Exception {
        String[] ENABLED_SYMBOLS = {SYMBOL_KAI};
        int success = 0;
        boolean success1 = false, success2 = false;
        CryptoSharedService service = null;
        for (String symbol : ENABLED_SYMBOLS) {
//            success += (testCase.updateConfirm(symbol)==true)?1:0;
            service = coinFactory.getService(symbol);
            if (!(service instanceof OwnChain)) { continue; }
            try {
                log.info(TAG + "[updateConfirm] updateSendConfirm");
                success1 = service.updateSendConfirm();
                log.info(TAG + "[updateConfirm] updateReceiveConfirm");
                success2 = service.updateReceiveConfirm();
            } catch (Exception e) { e.printStackTrace(); }
            // 2) 출금 실패 건 중 알리지 않은 건이 있는지 조회
            
            if (success1 && success2) {
                log.info(TAG + "[" + symbol + "][updateConfirm] success");
            } else {
                log.error(TAG + "[" + symbol + "][updateConfirm] fail");
            }
        }
        assertTrue(success==ENABLED_SYMBOLS.length);
    }

}
