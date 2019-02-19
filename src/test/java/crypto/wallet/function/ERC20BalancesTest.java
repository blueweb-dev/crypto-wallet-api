package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.repo.AddressBalanceRepository;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.ERC20AbstractService;
import crypto.wallet.service.common.CoinFactory;
import lombok.extern.slf4j.Slf4j;


/**
 * ERC20 계열 테스트
 */
@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class)
public class ERC20BalancesTest  implements WalletConst { // extends ERC20Test
    
    @Value("${wallet.sync.enabledERC20s}") protected String[] ENABLED_ERC20S;
    @Autowired AddressBalanceRepository addrRepo;
    @Autowired protected CoinFactory coinFactory;
    
    @Test public void getERC20Addresses() {
      
        CryptoSharedService service = null;
        for (String symbol : ENABLED_ERC20S) {
            service = coinFactory.getService(symbol);
            ERC20AbstractService rpc = ((ERC20AbstractService)service);
//            TotalBalance bals = rpc.getTotalBalance();
//            log.info("balance " + bals.toString());
        }
        assertTrue(true);
    }
    
}
