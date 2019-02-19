package crypto.wallet.data.gson.common;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class BitcoinStringResponse {
  
    private int id;
    private String result;
    private BtcRpcError error;

}
