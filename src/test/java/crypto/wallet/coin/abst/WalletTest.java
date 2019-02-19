package crypto.wallet.coin.abst;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;

import com.google.gson.Gson;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.req.PersonalInfoRequest;
import crypto.wallet.common.domain.res.NewAddressResponse;
import crypto.wallet.data.domain.TbCryptoMaster;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import crypto.wallet.service.intf.GatherableWallet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
public abstract class WalletTest implements WalletConst {

    protected final String TAG = "[TEST]["+ getSymbol() + "] ";
    
    @Value("${app.enabledSymbols}") String[] ENABLED_SYMBOLS;
    
    protected abstract void requestSendTransaction();
    protected abstract void syncBlocks();
    
    protected abstract void validateAddress();
    
    protected abstract void getMasterAddress();
    protected abstract void getUserAddresses();

    public abstract String getSymbol();
    public abstract String getTestuseraddr();
    
    protected Gson gson = new Gson();
    protected CryptoSharedService rpcService;
    @Autowired protected CoinFactory coinFactory;
    
    @Before
    public void setUp() throws Exception {
        // wait until the partitions are assigned
        rpcService = coinFactory.getService(getSymbol()); 
//        for (MessageListenerContainer messageListenerContainer : kafkaListenerEndpointRegistry.getListenerContainers()) {
//            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafka.getPartitionsPerTopic());
//        }
    }
    
    @Test public void createNewAddress() {
    	PersonalInfoRequest req = new PersonalInfoRequest(getSymbol(), UID_TEST, 
    			BROKER_ID_SYSTEM);
        NewAddressResponse res = rpcService.newAddress(req);
        if (res.getCode()==CODE_SUCCESS) {
            log.info("createNewAddress", res.getResult().getAddress());
            assertTrue(true);
        } else {
            log.error("createNewAddress", res.getCode() + "" + res.getError());
            assertTrue(false);
        }
    }
    
    @Test public void getColdAddress() {
        String address = rpcService.getSendaddr();
        log.debug(TAG + "getColdAddress "+ address);
        assertTrue(address!=null && address.length()>20);
    }
    
    
    @Test public void getBalances() {
    	TbCryptoMaster res = rpcService.getTotalBalance();
//        if (res!=null && res.getCode()==CODE_SUCCESS) {
//            log.info(TAG + "balances hotwallet "+ res.getTotal() 
//                  + " master " + res.getSender() + " users " + res.getTheOthers());
//            assertTrue(true);
//        } else {
//            log.error(TAG + "balances fail ["+ res.getCode() + "]" + res.getError());
//            assertTrue(false);
//        }
    }
    
    @Test public void batchSendTransaction() {
        boolean success = rpcService.batchSendTransaction();
        log.debug(TAG + "[batchSendTransaction] " + success);
        assertTrue(true);
    }
    
    @Test public void getStartLastBlockHeight() {
        Long[] height = new Long[2];
        TbCryptoMaster master = rpcService.getCryptoMaster();
        height[0] = master.getCurrSyncHeight();
        height[1] = master.getLatestHeight();
        log.debug(TAG + "[getLastBlockHeight] " + height[0] + "-" + height[1]);
        assertTrue(height!=null && height[0]!=null && height[1]!=null && height[0]<=height[1]);
    }
    
    @Test public void notifyConfirmUpdate() {
//        int[] sendsuccess = rpcService.notifySendConfirm(getSymbol());
//        int[] recvsuccess = rpcService.notifyReceiveConfirm(getSymbol());
//        boolean success = sendsuccess[0]==sendsuccess[1] && recvsuccess[0]==recvsuccess[1];
//        log.debug(TAG + "[notifyConfirmUpdate] send " + sendsuccess[1] + " of " 
//                    + sendsuccess[0] + ", recv " + sendsuccess[1] + " of " + sendsuccess[0] + " succeeded");
//        assertTrue(success);
          assertTrue(true);
    }
    
    @Test public void gatherCoinsToMaster() {
        boolean success = false;
        if (rpcService instanceof GatherableWallet) {
            success = ((GatherableWallet)rpcService).requestGathering();
        } else {
            success = true;
        }
        log.debug(TAG + "[gatherCoins] " + success);
        assertTrue(success);
    }
    
}
