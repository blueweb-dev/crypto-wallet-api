package crypto.wallet.data.gson.eth;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class EthTransactionResponse {
  
    private int id;
    private EthTransaction result;
    private BtcRpcError error;
    
    public EthTransactionResponse(BtcRpcError error) { this.error = error; }
  
}
