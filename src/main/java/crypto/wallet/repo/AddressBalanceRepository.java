package crypto.wallet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import crypto.wallet.data.domain.TbAddressBalance;
import crypto.wallet.data.key.PkUidSymbol;

public interface AddressBalanceRepository extends JpaRepository<TbAddressBalance, PkUidSymbol> {
	
    List<TbAddressBalance> findBySymbol(String symbol);
    List<TbAddressBalance> findBySymbolAndBalanceGreaterThanEqual(String symbol, double balance);
    List<TbAddressBalance> findBySymbolAndAddrIn(String symbol, List<String> addr);
    List<TbAddressBalance> findByUid(int uid);
    List<TbAddressBalance> findBySymbolAndUidAndBrokerId(String symbol, int uid, String brokerId);
    List<TbAddressBalance> findBySymbolAndAddr(String symbol, String addr);
    
    @Query("select addr from TbAddressBalance where symbol = ?1")
    List<String> findAddrBySymbol(String symbol);
    
}