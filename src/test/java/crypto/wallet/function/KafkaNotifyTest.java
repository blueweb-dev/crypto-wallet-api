package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.shared.KafkaReceiverTest;
import crypto.wallet.shared.SharedTest;
import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j
@RunWith(SpringRunner.class) @SpringBootTest
@TestPropertySource(properties = "scheduling.enabled=false")
public class KafkaNotifyTest implements WalletConst {
  
    private final String TAG          = TAG_TEST + "[sendRequest]";
    @Autowired SharedTest testCase;
    @Autowired private KafkaReceiverTest receiver;
    @Value("${app.enabledSymbols}") String[] ENABLED_SYMBOLS;
    
    @Test
    public void kafkaNotify() throws Exception {
        for (String symbol : ENABLED_SYMBOLS) {
//            testCase.kafkaNotify(symbol);
        }
        receiver.getLatch().await(10L, TimeUnit.SECONDS);
        assertTrue(receiver.getLatch().getCount()==0);
    }

}
