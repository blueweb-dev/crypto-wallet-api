package crypto.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbCryptoMaster;


public interface CryptoMasterRepository extends JpaRepository<TbCryptoMaster, 
		String> { 
}