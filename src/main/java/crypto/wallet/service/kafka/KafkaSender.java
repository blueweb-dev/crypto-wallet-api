package crypto.wallet.service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import crypto.wallet.common.domain.res.TransactionResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author sungjoon.kim
 */
@Slf4j @Service public class KafkaSender {

    private final String TAG = "[kafka][send]";
    @Autowired private KafkaTemplate<String, String> kafkaTemplate;
    private Gson gson = new Gson();
    
    public void send(String topic, TransactionResponse req) {
        String payload = gson.toJson(req).toString();
        log.info(TAG + " topic='{}' contents='{}'", topic, payload);
        kafkaTemplate.send(topic, payload);
    } 
}
