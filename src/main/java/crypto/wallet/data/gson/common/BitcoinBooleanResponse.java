package crypto.wallet.data.gson.common;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class BitcoinBooleanResponse {
  
    private int id;
    private boolean result;
    private BtcRpcError error;

}
