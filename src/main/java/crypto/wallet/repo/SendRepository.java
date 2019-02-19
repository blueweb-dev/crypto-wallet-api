package crypto.wallet.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbSend;
import crypto.wallet.data.key.PkSymbolOrderId;

public interface SendRepository extends JpaRepository<TbSend, PkSymbolOrderId> {
	
    // 1) 블록에서 발견한 관련 출금 건이 출금 테이블에 있는지 조회
    TbSend findFirstBySymbolAndTxidAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
            (String symbol, String txid, String toAddr, Date regDt);
    // 2) 최근 전송 Fee 조회
    TbSend findFirstBySymbolAndRealFeeGreaterThanOrderByRegDtDesc(String symbol, double realFee);
	// 3) 출금 처리 대상 조회 => TXID가 없고 에러 메시지가 없는 건
    List<TbSend> findBySymbolAndTxidIsNullAndErrMsgIsNullAndRegDtGreaterThan
    			(String symbol, Date regDt);
    // 4) 출금 confirm 업데이트 대상 조회
    List<TbSend> findBySymbolAndTxidIsNotNullAndErrMsgIsNullAndNotiCntLessThanAndRegDtGreaterThan
              (String symbol, int notiCnt, Date regDt);
    // 5) 출금 실패 알림 대상 조회
    List<TbSend> findByErrMsgIsNotNullAndNotiCntLessThanAndRegDtGreaterThan(int notiCnt, Date regDt);
    // 6) 수기 처리한 재 알림 대상 트랜잭션들 조회
    List<TbSend> findByReNotify(char reNotify);
    
    // 7) 시스템 내부 송금건 추출
    List<TbSend> findBySymbolAndUidAndBrokerIdAndTxidIsNotNullAndRegDtGreaterThan(
        String symbol, int uid, String brokerId, Date regDt);
    
    // 잔고 수거 요청건 조회
    TbSend findFirstBySymbolAndFromAddrAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
          (String symbol, String fromAddr, String toAddr, Date regDt);
    
    List<TbSend> findByUid(int uid);
    List<TbSend> findByOrderIdOrderByRegDtDesc(String orderId);
    
}