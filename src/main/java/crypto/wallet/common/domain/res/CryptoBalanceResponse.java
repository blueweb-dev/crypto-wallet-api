package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class CryptoBalanceResponse extends WalletResponse {

	private double result;

	public CryptoBalanceResponse() {}
    
}
