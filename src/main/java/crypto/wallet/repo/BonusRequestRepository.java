package crypto.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbBonusRequest;
import crypto.wallet.data.key.PkSymbolOrderId;

public interface BonusRequestRepository extends JpaRepository<TbBonusRequest, PkSymbolOrderId> {

}