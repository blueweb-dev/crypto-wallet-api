package crypto.wallet.shared;

import java.util.concurrent.CountDownLatch;

import org.springframework.stereotype.Service;

import crypto.wallet.common.constant.WalletConst;
import lombok.extern.slf4j.Slf4j;

/**
 * 메시지 큐 수신
 * @author sungjoon.kim
 */
@Slf4j @Service public class KafkaReceiverTest implements WalletConst {
  
    private final String TAG = "[kafka][test][receive]";
    private CountDownLatch latch = new CountDownLatch(10);
    public CountDownLatch getLatch() {
        return latch;
    }
    
}
