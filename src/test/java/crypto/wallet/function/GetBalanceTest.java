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


/**
 */
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class) public class GetBalanceTest implements WalletConst {
  
//    private final String TAG = TAG_TEST + "[getBalance]";
    @Autowired SharedTest testCase;
    
    String[] ENABLED_SYMBOLS = { SYMBOL_ADA, SYMBOL_CLO }; //, SYMBOL_BHPC };
    
    @Test public void getBalance() throws Exception {
        int success = 0;
        for (String symbol : ENABLED_SYMBOLS) {
            success += (testCase.getBalances(symbol)==true)?1:0;
        }
        assertTrue(success==ENABLED_SYMBOLS.length);
    }
    
}
