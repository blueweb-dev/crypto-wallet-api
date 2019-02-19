package crypto.wallet.coin;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.coin.abst.EthereumTest;
import crypto.wallet.common.constant.WalletConst;


/**
 * ETC 테스트
 */
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class)
public class ETCTest extends EthereumTest implements WalletConst {
  
    private static String symbol = SYMBOL_ETC;
    @Value("${crypto.etc.masteraddr}") private String masteraddr;
    @Value("${crypto.etc.testuseraddr}") private String testuseraddr;
    @Override public String getSymbol() { return symbol; }
    @Override public String getTestuseraddr() { return testuseraddr; }
    @Override public String getLocalMasterAddress() { return masteraddr; }
    
}
