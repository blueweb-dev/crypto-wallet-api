package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import java.time.Instant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.service.common.CoinFactory;
import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j 
@SpringBootTest @RunWith(SpringRunner.class) public class UTCTimeTest implements WalletConst {
  
    private final String TAG = TAG_TEST + "[batchSendTX]";
    @Autowired private CoinFactory coinFactory;
    
    @Test public void batchSend() throws Exception {
//        long now = Long.parseLong(Instant.now().getEpochSecond().replaceAll("[-]", "").replaceAll("[:]","").replaceAll("T", ""));
//        log.info("Instant.now().toString() " + Instant.now().toString());
//        log.info("Instant.now().toString() " + Instant.now().getEpochSecond());
        log.info("now " + Long.parseLong(Instant.now().toString().substring(0,19).replaceAll("[-]", "").replaceAll("[:]","").replaceAll("T", "")));
        assertTrue(true);
    }

}
