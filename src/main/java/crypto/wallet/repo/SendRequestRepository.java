package crypto.wallet.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbSendRequest;
import crypto.wallet.data.key.PkUidSymbolOrderId;

public interface SendRequestRepository extends JpaRepository<TbSendRequest, PkUidSymbolOrderId> {

    TbSendRequest findFirstBySymbolAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
            (String symbol, String toAddr, Date regDt);
    TbSendRequest findFirstBySymbolAndFromAddrAndToAddrAndRegDtGreaterThanOrderByRegDtDesc
            (String symbol, String fromAddr, String toAddr, Date regDt);
    
}