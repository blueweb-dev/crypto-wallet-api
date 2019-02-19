package crypto.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbMarketPrice;


public interface MarketPriceRepository extends JpaRepository<TbMarketPrice, String> { 
	
}