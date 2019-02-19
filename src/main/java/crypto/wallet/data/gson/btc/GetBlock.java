package crypto.wallet.data.gson.btc;

import lombok.Data;

@Data public class GetBlock {
  
    private int id;
    private Block result;
    private BtcRpcError error;

    @Data public class Block {
        // height, txcount, confirmations, time
        private String hash;
        private String miner;
        private long confirmations;
        private long height;
        private long time;
        private String[] tx;
    }
    
}
