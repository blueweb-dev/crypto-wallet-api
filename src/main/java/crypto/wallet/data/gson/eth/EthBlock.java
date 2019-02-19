package crypto.wallet.data.gson.eth;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class EthBlock {
  
    private int id;
    private BtcRpcError error;
    private Result result;
    
    @Data
    public class Result {
        private String gasLimit;
        private String gasUsed;
        private String hash;
        private String number;
        private String timestamp;
        private EthTransaction[] transactions;
    }
    
}
