package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.shared.SharedTest;
import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j 
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class) public class SendOptionalTest implements WalletConst {
  
    private final String TAG = TAG_TEST + "[SyncBlocks]";
    @Autowired SharedTest testCase;
    
    @Test public void batchSend() throws Exception {
        String[] ENABLED_SYMBOLS = {SYMBOL_KAI};
        int success = 0;
        for (String symbol : ENABLED_SYMBOLS) {
            success += (testCase.gathering(symbol)==true)?1:0;
        }
        assertTrue(success==ENABLED_SYMBOLS.length);
    }

}
