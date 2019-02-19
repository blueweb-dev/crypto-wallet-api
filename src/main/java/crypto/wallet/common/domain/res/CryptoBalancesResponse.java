package crypto.wallet.common.domain.res;

import java.util.List;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.data.gson.common.CryptoBalanceByAddr;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class CryptoBalancesResponse extends WalletResponse {

	private List<CryptoBalanceByAddr> result;

	public CryptoBalancesResponse() {}
    
}
