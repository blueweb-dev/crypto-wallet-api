package crypto.wallet.data.gson.eth;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class EthListAccount implements Cloneable {
  
    private int id;
    private BtcRpcError error;
    private String[] result;
    
}
