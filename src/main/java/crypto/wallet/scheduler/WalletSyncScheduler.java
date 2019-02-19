package crypto.wallet.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import crypto.wallet.common.aop.LogExecutionTime;
import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import crypto.wallet.service.intf.OwnChain;
import lombok.extern.slf4j.Slf4j;


@Slf4j @Component 
@ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true", matchIfMissing = true)
public class WalletSyncScheduler implements WalletConst {

    private final String TAG 	= "[SCHED]";
	private final int INTERVAL 	= 5000;
	private final int SEC_15   	= 3;       // 5000 * 3
	private final int MIN_1    	= 12;      // 5000 * 12
	private final int HOUR_3   	= 12 * 60 * 3; // 5000 * 12 * 60 * 3
	private int count;
	
	@Autowired private CoinFactory coinFactory;
	@Value("${app.enabledSymbols}") String[] ENABLED_SYMBOLS;

	/**
	 * 동기화 배치 스케줄러
	 */
	@LogExecutionTime @Scheduled(fixedDelay=INTERVAL)   
    public void syncBlockTransactions() {
      
        boolean success1 = false;
        boolean success2 = false;
        CryptoSharedService service = null;
        
        // 1) 5초마다 => 암호화폐 기본 정보 업데이트 
        for (String symbol : ENABLED_SYMBOLS) {
            service = coinFactory.getService(symbol);
            try {
                success1 = service.updateCryptoMaster();
                if (!success1) {
                  log.error(TAG + "[" + symbol + "][updateCryptoMaster] fail");
                }
            } catch (Exception e) {
                log.error(TAG + "[" + symbol + "][updateCryptoMaster] " + e.getMessage());
            }
        }
        
        // 2) 1분 마다 => 사용자 잔고 업데이트 (10초 지연)
        if (count%MIN_1==2) {
			for (String symbol : ENABLED_SYMBOLS) {
	            service = coinFactory.getService(symbol);
	            try {
	                boolean success = service.syncWalletBalances();
	                if (!success) {
	                    log.error(TAG + "[" + symbol + "][syncWalletBalances] fail");
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	                log.error(TAG + "[" + symbol + "][syncWalletBalances] " 
	                				+ e.getMessage());
	            }
	        }				
        }
        
        // 3) 10초 마다 => 블록동기화 (5초 지연)
        // 블록을 열거나 내 트랜잭션 조회 API를 통해 거래소 주소 관련 TX들을 찾아내서 처리
        if (count%2==1) {
            for (String symbol : ENABLED_SYMBOLS) {
                service = coinFactory.getService(symbol);
                if (!(service instanceof OwnChain)) { continue; }
                try {
                    if (!((OwnChain)service).openBlocksGetTxsThenSave()) {
                        log.error(TAG + "[" + symbol + "][blockSync] fail");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(TAG + "[" + symbol + "][blockSync] " + e.getMessage());
                }
            }
        }

        // 4) 15초 마다 => 전송대기건 전송 (5초지연)
        if (count%SEC_15==1) {
            for (String symbol : ENABLED_SYMBOLS) {
                service = coinFactory.getService(symbol);
                try {
                    if (!service.batchSendTransaction()) {
                        log.error(TAG + "[" + symbol + "][batchSendTX] fail");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error(TAG + "[" + symbol + "][batchSendTX] " + e.getMessage());
                }
            }
        }

        // 5) 15초 마다 => 입금/출금 컨펌 수 업데이트 (10초 지연)
        if (count%SEC_15==2) {
        	for (String symbol : ENABLED_SYMBOLS) {
          	    service = coinFactory.getService(symbol);
                try {
                    success1 = service.updateSendConfirm();
                } catch (Exception e) { e.printStackTrace(); }
                try {
                    success2 = service.updateReceiveConfirm();
                } catch (Exception e) { e.printStackTrace(); }
                if (!(success1 && success2)) {
                    log.error(TAG + "[" + symbol + "][updateConfirmation] fail");
                }
        	}
            // (1) [공통] 실패한 트랜잭션 알림 - 전체 한번만 실행해야 하는 건 BTC에다 구현함
            service = coinFactory.getService(SYMBOL_BTC);
            success1 = service.notifyTXsFailedNotNotified();
            if (!success1) { log.error(TAG + "[notifyfailed] failed"); }
            // (2) 거래소에 KAFKA 알림 실패한 트랜잭션 재송신: 공통
            success2 = service.renotify();
            if (!success2) { log.error(TAG + "[renotify] failed"); }
        }

        // 6) 유저 계정들에서 마스터 계정으로 자금 옮기는 작업: 12시간 마다?
//        if (count%HOUR_3==6) {
//            for (String symbol : ENABLED_SYMBOLS) {
//                success1 = true;
//                service = coinFactory.getService(symbol);
//                if (service instanceof GatherableWallet) {
//                    // approve
//                    success1 = ((GatherableWallet)service).requestGathering();
//                    if (success1) {
//                        log.info(TAG + "[" + symbol + "][gatherCoin] success");
//                    } else {
//                        log.error(TAG + "[" + symbol + "][gatherCoin] fail");
//                    }
//                }
//            }
//        }
        count++;
        if (count>=HOUR_3) { count = 0; }
    }
    
}

