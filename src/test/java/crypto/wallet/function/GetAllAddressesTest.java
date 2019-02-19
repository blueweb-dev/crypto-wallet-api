package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

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
@SpringBootTest @RunWith(SpringRunner.class) public class GetAllAddressesTest implements WalletConst {
  
//    private final String TAG = TAG_TEST + "[getBalance]";
    @Autowired SharedTest testCase;
    @Autowired CoinFactory coinFactory;
    
    String[] ENABLED_SYMBOLS = { SYMBOL_ADA };
    
    @Test public void getBalance() throws Exception {
        int success = 0;
        CryptoSharedService service = null;
        for (String symbol : ENABLED_SYMBOLS) {
            service = coinFactory.getService(symbol);
            if (!(service instanceof OwnChain)) { continue; }
            Set<String> ret1 = ((OwnChain)service).getAllAddressSetFromNode();
            List<String> ret2 = ((OwnChain)service).getAllAddressListFromNode();
            log.info("addressSet " + ret1.toString());
            log.info("addressList " + ret2.toString());
            success += (ret1.size()>0 && ret2.size()>0 && ret1.size()==ret2.size()?1:0);
        }
        assertTrue(success==ENABLED_SYMBOLS.length);
    }
    
}
