package crypto.wallet.shared;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import crypto.wallet.common.constant.WalletConst;


/**
 * 메시지 큐 수신 설정
 * @author sungjoon.kim
 */
@EnableKafka @Configuration public class ReceiverTest implements WalletConst {
  
    @Value("${spring.kafka.bootstrap-servers}") private String bootstrapServers;
    private String listeningGroup = "WLT_TRANSMIT";
  
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, listeningGroup);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
    
//    @Bean public Map<String, Object> consumerConfigs() {
//        // 나는 송신자이므로 이 테스트 설정은 중요치 않음
//        Map<String, Object> props = new HashMap<>();
//        // list of host:port pairs used for establishing the initial connections to the Kafka cluster
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,          bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,     StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,   StringDeserializer.class);
//        // allows a pool of processes to divide the work of consuming and processing records
//        props.put(ConsumerConfig.GROUP_ID_CONFIG,                   listeningGroup);
//        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,         false);
//        // 8192 바이트까지 큐가 찰 것을 기다림.. 대략 75개?
//        props.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG,            8192);
//        // 25초까지 8192바이트가 다 차지 않으면 즉시 메시지 전송
//        props.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG,          15000);
//        // maximum records per poll
//        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,           MAX_QUEUE_POLL_SIZE);
//        // automatically reset the offset to the earliest offset
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,          "earliest");
//        return props;
//    }
//  
//    @Bean public ConsumerFactory<String, String> consumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
//    }
//  
//    @Bean public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> 
//                    kafkaListenerContainerFactory() {
//          ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                        new ConcurrentKafkaListenerContainerFactory<>();
//          factory.setConsumerFactory(consumerFactory());
//          factory.setMessageConverter(new BatchMessagingMessageConverter(converter()));
////          factory.setConcurrency(15);
//          factory.getContainerProperties().setPollTimeout(15000);
//          // enable batch listening
//          factory.setBatchListener(true);
//          return factory;
//    }
//    
//    @Bean public StringJsonMessageConverter converter() {
//      return new StringJsonMessageConverter();
//    }
    
//    @Bean public MQReceiveService receiver() {
//        return new MQReceiveService();
//    }
//    
    
}
