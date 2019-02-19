package crypto.wallet.coin;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.coin.abst.BitcoinTest;
import crypto.wallet.common.constant.WalletConst;


/**
 * BTC 계열 테스트
 */
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class) 
public class BTCTest extends BitcoinTest implements WalletConst {
  
    private final String symbol = SYMBOL_BTC;
    @Value("${crypto.btc.masteraccount}") private String masteraccount;
    @Value("${crypto.btc.masteraddr}") private String masteraddr;
    @Value("${crypto.btc.testuseraddr}") private String testuseraddr;
    @Override public String getSymbol() { return symbol; }
    @Override public String getLocalMasterAddress() { return masteraddr; }
    @Override public String getTestuseraddr() { return testuseraddr; }
    
}
