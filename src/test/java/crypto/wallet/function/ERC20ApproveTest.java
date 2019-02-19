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
import crypto.wallet.service.common.CoinFactory;
import crypto.wallet.service.intf.TokenSupport;
import lombok.extern.slf4j.Slf4j;


/**
 * ERC20 계열 테스트
 */
@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class)
public class ERC20ApproveTest  implements WalletConst { // extends ERC20Test
    
    @Value("${wallet.sync.enabledERC20s}") protected String[] ENABLED_ERC20S;
    
    @Autowired AddressBalanceRepository addrRepo;
    @Autowired protected CoinFactory coinFactory;
    
    @Test public void getERC20Addresses() {
        CryptoSharedService lservice = coinFactory.getService(SYMBOL_ETH);
        boolean success = ((TokenSupport)lservice).fillGasWhereNotEnough();
//        String symbol = "BHPC";
//        WalletService service = coinFactory.getService(symbol);
//        TbSend datum = new TbSend(WalletUtil.getRandomSystemOrderId(), 
//            symbol, SYSTEM_UID, "0x9b041333c265a63c9b5af340e4ab086c2c3abf2e", null, 67.36, 
//            WalletUtil.getTestRandomBrokerId()); 
//        boolean success = service.sendOneTransaction(datum);
        assertTrue(success);
    }
    
}
