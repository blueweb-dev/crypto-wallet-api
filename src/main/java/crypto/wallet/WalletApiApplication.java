package crypto.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * @author 
 * @since 
 */
@SpringBootApplication @EnableScheduling 
public class WalletApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WalletApiApplication.class, args);
    }
    
}
