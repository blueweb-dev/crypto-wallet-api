package crypto.wallet.data.gson.common;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class BitcoinGeneralResponse {
  
    private int id;
    private BtcRpcError error;
    private Object result;

}
