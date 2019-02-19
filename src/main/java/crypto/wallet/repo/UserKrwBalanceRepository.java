package crypto.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.VwUserKrwBalance;
import crypto.wallet.data.key.PkUidSymbol;

public interface UserKrwBalanceRepository extends JpaRepository<VwUserKrwBalance, 
		PkUidSymbol> {

}