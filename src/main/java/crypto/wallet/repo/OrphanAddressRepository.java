package crypto.wallet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbOrphanAddress;
import crypto.wallet.data.key.PkSymbolAddr;

public interface OrphanAddressRepository extends JpaRepository<TbOrphanAddress, PkSymbolAddr> {
	
    List<TbOrphanAddress> findBySymbol(String symbol);
    List<TbOrphanAddress> findBySymbolInAndAddr(List<String> symbols, String addr);
}