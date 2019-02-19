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


/**
 */
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class) public class ValidateAddressTest implements WalletConst {
  
    @Autowired SharedTest testCase;
    @Value("${app.enabledSymbols}") String[] ENABLED_SYMBOLS;
    
    @Test public void validateAddressBTC() {
        // KAI <= BTC, ADA <= BTC
//        String[] ENABLED_SYMBOLS = {SYMBOL_ETH};
        int success = 0;
        for (String symbol : ENABLED_SYMBOLS) {
            success += (testCase.validateAddress(symbol)==true)?1:0;
        }
        assertTrue(success==ENABLED_SYMBOLS.length);
    }
    
}
