package crypto.wallet.coin;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.coin.abst.EthereumTest;
import crypto.wallet.common.constant.WalletConst;

/**
 * ETH 계열 테스트
 */
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class)
public class ETHTest extends EthereumTest implements WalletConst {
  
    private static String symbol = SYMBOL_ETH;
    @Value("${crypto.eth.masteraddr}") private String masteraddr;
    @Value("${crypto.eth.testuseraddr}") private String testuseraddr;
    @Override public String getSymbol() { return symbol; }
    @Override public String getTestuseraddr() { return testuseraddr; }
    @Override public String getLocalMasterAddress() { return masteraddr; }
    
}
