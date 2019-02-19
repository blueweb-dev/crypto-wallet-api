package crypto.wallet.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbRecv;
import crypto.wallet.data.key.PkSymbolTxidToAddr;

public interface RecvRepository extends JpaRepository<TbRecv, PkSymbolTxidToAddr> { 
	
    // 1) 블록에서 발견한 입금 건이 입금 테이블에 있는지 조회
    TbRecv findFirstBySymbolAndTxidAndToAddr(String symbol, String txid, String toaddr);
	// 2) 입금 confirm 업데이트 대상 조회
    List<TbRecv> findBySymbolAndErrMsgIsNullAndNotiCntLessThanAndRegDtGreaterThan(String symbol, int notiCnt, Date regDt);
    // 3) 입금 실패 알림 대상 목록
    List<TbRecv> findByErrMsgIsNotNullAndNotiCntLessThanAndRegDtGreaterThan(int notiCnt, Date regDt);
    // 4) 수기 처리한 재 알림 대상 트랜잭션들 조회
    List<TbRecv> findByReNotify(char reNotify);
    
    List<TbRecv> findByUid(int uid);
    
}