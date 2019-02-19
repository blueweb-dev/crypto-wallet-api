package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j 
@SpringBootTest @RunWith(SpringRunner.class) public class BatchSendTest implements WalletConst {
  
    private final String TAG = TAG_TEST + "[batchSendTX]";
    @Autowired private CoinFactory coinFactory;
    
    @Test public void batchSend() throws Exception {
        int successcount = 0;
        String[] ENABLED_SYMBOLS = {SYMBOL_BHPC};
        for (String symbol : ENABLED_SYMBOLS) {
            CryptoSharedService service = coinFactory.getService(symbol);
            try {
              if (service.batchSendTransaction()) {
                  log.debug(TAG + "[" + symbol + "] true");
                  successcount++; 
              } else {
                  log.error(TAG + "[" + symbol + "] false");
              }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        assertTrue(successcount==ENABLED_SYMBOLS.length);
    }

}
