package crypto.wallet.common.domain.res;

import java.util.List;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.data.gson.common.TotalBalance;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data
public class TotalBalancesResponse extends WalletResponse {
  
	private List<TotalBalance> result;
	
    public TotalBalancesResponse() {}
    
}
