package crypto.wallet.coin;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import crypto.wallet.coin.abst.WalletTest;
import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.common.util.WalletUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 */
@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class)
public class ADATest extends WalletTest implements WalletConst {
  
    private final String symbol = SYMBOL_ADA;
    private final String TAG = "[TEST][" + symbol + "] ";
    
    @Override public String getSymbol() { return symbol; }

	@Test
	@Override public void validateAddress() {
		BigInteger src = BigInteger.valueOf(6600000000L);
//		int digits = WalletUtil.integerDigits(new BigDecimal(src));
//		BigInteger biggestFirst = BigInteger.valueOf(Math.round(Math.pow(10, digits)));
//		BigInteger mod = src.mod(biggestFirst);
//		BigInteger srcFloor = src.subtract(mod);
//		BigInteger ret = src.subtract(mod).add(biggestFirst);
//		log.info("digits" + digits);
//		log.info("mod" + mod.toString());
//		log.info("srcFloor" + srcFloor.toString());
//		log.info("result" + ret.toString());
		log.info("bigIntegerCeil" + WalletUtil.bigIntegerAddFirst(src).toString());
		
		// datum - mod + digit
//	    boolean success = false; 
//	    int isvalid = 0, isinvalid = 0;
//	    String[] validcase   = {rpcService.getLocalMasterAddress(), rpcService.getUserAddressSetFromDB().iterator().next()};
//	    String[] invalidcase = {"1PXg2k13YaKvUrpvLvg7r9sFz7VKPAgoWmEbZW", ""};
//	    
//        ValidateAddressResponse ret = null;
//        ret = rpcService.validateAddress(validcase[0]);
//        isvalid = (ret.getCode()==SUCCESS && ret.isValid())?1:0;
//        
//        ret = rpcService.validateAddress(validcase[1]);
//        isvalid = (ret.getCode()==SUCCESS && ret.isValid())?isvalid+1:isvalid;
//        
//        ret = rpcService.validateAddress(invalidcase[0]);
//        isinvalid = (ret.getCode()==FAIL_LOGICAL || !ret.isValid())?1:0;
//
//        ret = rpcService.validateAddress(invalidcase[1]);
//        isinvalid = (ret.getCode()==FAIL_LOGICAL || !ret.isValid())?isinvalid+1:isinvalid;
//        
//        success = isvalid == 2 && isinvalid == 2;
//		log.debug(TAG + "[validateAddress] " + success);
//		
//		assertTrue(success);
	}

	@Override protected void requestSendTransaction() {
	    SendRequest req = new SendRequest(getSymbol(), WalletUtil.getTestRandomOrderId(), 
            UID_TEST,  getTestuseraddr(), null, TEST_AMOUNT, BROKER_ID_SYSTEM, TEST_EXPCT_FEE);
		rpcService.requestSendTransaction(req);
	}

	@Override protected void syncBlocks() {
	    
	}

    @Test
    @Override public void getMasterAddress() {
        String addr = rpcService.getSendaddr();
        log.debug(TAG + "master addr "+ addr);
        assertTrue(addr!=null && addr.length()>20);
    }
    
    @Test
    @Override public void getUserAddresses() {
//        Set<String> addrs = rpcService.getUserAddressSetFromDB();
//        log.debug(TAG + "user addrs "+ addrs.toString());
//        assertTrue(addrs!=null && addrs.size()>0);
    }

	@Override
	public String getTestuseraddr() {
		// TODO Auto-generated method stub
		return null;
	}

}
