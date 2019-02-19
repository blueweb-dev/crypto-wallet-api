package crypto.wallet.common.domain.abst;

import crypto.wallet.common.constant.WalletConst;
import lombok.Data;

@Data public class WalletResponse implements WalletConst {
  
    private int code = CODE_SUCCESS;
    private String error;

}
