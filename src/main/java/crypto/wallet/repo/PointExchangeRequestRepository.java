package crypto.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbPointExchangeRequest;

public interface PointExchangeRequestRepository extends JpaRepository<TbPointExchangeRequest, String> {

}