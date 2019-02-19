package crypto.wallet.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbTokenBuyRequest;

public interface TokenBuyRequestRepository extends JpaRepository<TbTokenBuyRequest, String> {

	List<TbTokenBuyRequest> findByStatusAndPaySymbolAndErrorIsNull(char status, String paySymbol);
	List<TbTokenBuyRequest> findByStatusAndErrorIsNullOrderByPaySymbolAsc(char status);

	List<TbTokenBuyRequest> findByUidOrderByRegDtDesc(int uid);
	
	List<TbTokenBuyRequest> findByPaySymbolAndStatusIn(String symbol, List<Character> status);
	List<TbTokenBuyRequest> findByPaySymbolAndStatus(String symbol, Character status);
	List<TbTokenBuyRequest> findByStatus(Character status);
	
	List<TbTokenBuyRequest> findByTokenSymbolOrderByRegDtDesc(String symbol);
	
}