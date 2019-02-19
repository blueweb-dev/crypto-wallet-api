package crypto.wallet.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import crypto.wallet.data.domain.TbManualPayRequest;

public interface ManualPayRequestRepository extends JpaRepository<TbManualPayRequest, String> {

}