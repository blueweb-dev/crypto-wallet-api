package crypto.wallet.data.gson.common;

import org.json.JSONObject;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class BitcoinJsonResultResponse {
  
    private int id;
    private BtcRpcError error;
    private JSONObject result;

}
