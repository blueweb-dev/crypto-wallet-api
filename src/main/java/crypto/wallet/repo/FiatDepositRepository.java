package crypto.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbFiatDeposit;
import crypto.wallet.data.key.PkUidSymbolOrderId;

public interface FiatDepositRepository extends JpaRepository<TbFiatDeposit
		, PkUidSymbolOrderId> {
	
}