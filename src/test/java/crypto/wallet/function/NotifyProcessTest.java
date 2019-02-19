package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.shared.SharedTest;
import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j
@RunWith(SpringRunner.class) @SpringBootTest
@TestPropertySource(properties = "scheduling.enabled=false")
public class NotifyProcessTest implements WalletConst {
  
    private final String TAG = TAG_TEST + "[notifyProcess]";
    @Autowired SharedTest testCase;
    @Value("${app.enabledSymbols}") String[] ENABLED_SYMBOLS;
    
    @Test
    public void notifyProcess() throws Exception {
        int success = 0;
        for (String symbol : ENABLED_SYMBOLS) {
            success += (testCase.notifyProcess(symbol)==true)?1:0;
        }
        assertTrue(success==0 || success==ENABLED_SYMBOLS.length);
    }
    
}
