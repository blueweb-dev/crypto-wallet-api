package crypto.wallet.common.domain.res;

import java.util.List;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.data.domain.TbMarketPrice;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class CryptoIndexResponse extends WalletResponse {
  
    private List<TbMarketPrice> result;
    
}
