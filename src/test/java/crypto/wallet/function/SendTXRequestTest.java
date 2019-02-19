package crypto.wallet.function;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.common.domain.res.SendResponse;
import crypto.wallet.common.util.WalletUtil;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import lombok.extern.slf4j.Slf4j;


/**
 */
@Slf4j
@SpringBootTest @RunWith(SpringRunner.class) public class SendTXRequestTest implements WalletConst {
  
    private final String TAG = TAG_TEST + "[sendRequest]";
    @Autowired private CoinFactory coinFactory;
    @Value("${app.enabledSymbols}") String[] ENABLED_SYMBOLS;
    
    @Test
    public void main() throws Exception {
        int count = 0;
        String[] ENABLED_SYMBOLS = {SYMBOL_BHPC};
        for (String symbol : ENABLED_SYMBOLS) {
            CryptoSharedService service = coinFactory.getService(symbol);
//            String masteraddress = service instanceof BitcoinAbstractService?null:service.getMasterAddress();
            SendRequest req = new SendRequest(symbol, WalletUtil.getRandomSystemOrderId()
                  , WalletUtil.getTestRandomInt(), "0xa447d425d098fe282a71eaffa97012e236f67045", null
                  , 23, WalletUtil.getTestRandomBrokerId(), TEST_EXPCT_FEE);
            req.setFromAddress("0x32d14834d6bb0d50dfbb5fe1c75548a380687238");
            req.setExpectFee(TEST_EXPCT_FEE);
            SendResponse res = service.requestSendTransaction(req);
            service.batchSendTransaction();
            if (res!=null && res.getCode()==CODE_SUCCESS) {
                log.info(TAG + "[" + service.getSymbol() + "] success");  
            } else {
                log.error(TAG + "[" + service.getSymbol() + "] " + res.getCode() + "," + res.getError());  
            }
        }
        assertTrue(count==0 || count==ENABLED_SYMBOLS.length);
    }

}
