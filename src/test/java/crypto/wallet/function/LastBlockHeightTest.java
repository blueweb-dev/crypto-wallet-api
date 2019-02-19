package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.data.domain.TbCryptoMaster;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import crypto.wallet.service.intf.OwnChain;
import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j
@SpringBootTest @RunWith(SpringRunner.class) public class LastBlockHeightTest implements WalletConst {
  
    private final String TAG = TAG_TEST + "[syncLastBlockHeight]";
    @Autowired private CoinFactory coinFactory;
    @Value("${app.enabledSymbols}") String[] ENABLED_SYMBOLS;
    
    @Test
    public void getLastBlockHeight() throws Exception {
        int count = 0;
        int successcount = 0;
        CryptoSharedService service = null;
        Long[] height = null;
        for (String symbol : ENABLED_SYMBOLS) {
            height = new Long[2];
            service = coinFactory.getService(symbol);
            if (service instanceof OwnChain) {
                try {
                    TbCryptoMaster master = service.getCryptoMaster();
                    height[0] = master.getCurrSyncHeight();
                    height[1] = master.getLatestHeight();
                    if (height!=null && height[0]!=null && height[1]!=null) {
                        log.info(TAG + "[" + symbol + "]" + height[0] + "-" + height[1]);
                        successcount++; 
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("getStartLastBlockHeight " + e.getMessage());
                }
                count++;
            }
        }
        assertTrue(count==0 || successcount==count);
    }

}
