package crypto.wallet.data.gson.multichain;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class BlockchainInfo {
  
    private int id;
    private Result result;
    private BtcRpcError error;

    @Data public class Result {
      
        private long blocks;
        private long headers;
        private String bestblockhash;

//      "chain": "main",
//      "chainname": "kaicoin",
//      "description": "MultiChain kaicoin",
//      "protocol": "multichain",
//      "setupblocks": 60,
//      "reindex": false,
//      "blocks": 73865,
//      "headers": 73865,
//      "bestblockhash": "0000008d1afe21c9352b9addb89a53a73582cdebc3ceb97bf38d38567896c3e6",
//      "difficulty": 0.00456508,
//      "verificationprogress": 1,
//      "chainwork": "000000000000000000000000000000000000000000000000000000cd90687650"
        
    }
    
}
