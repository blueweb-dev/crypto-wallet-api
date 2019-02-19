package crypto.wallet.coin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.gson.Gson;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.repo.AddressBalanceRepository;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import lombok.extern.slf4j.Slf4j;


/**
 * ERC20 계열 테스트
 */
@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class)
public class ZILTest implements WalletConst { // extends ERC20Test 
  
    private final String symbol = SYMBOL_BHPC;
    @Value("${crypto.bhpc.masteraddr}") private String masteraddr;
    @Value("${crypto.bhpc.testuseraddr}") private String testuseraddr;
    public String getSymbol() { return symbol; }
    public String getTestuseraddr() { return testuseraddr; }
    public String getLocalMasterAddress() { return masteraddr; }
    @Autowired AddressBalanceRepository addrRepo;
    protected Gson gson = new Gson();
    protected CryptoSharedService rpcService;
    @Autowired protected CoinFactory coinFactory;
    
    @Test public void getERC20Addresses() {
        log.debug("getERC20Addresses");
        CryptoSharedService service = coinFactory.getService(SYMBOL_BHPC);
//        ((ERC20AbstractService)service).scanWalletBalances();
    }
    
}
