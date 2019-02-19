package crypto.wallet.data.gson.btc;

import lombok.Data;

@Data public class ListSinceBlock {
  
    private int id;
    private Result result;
    private BtcRpcError error;
    
    @Data public class Result {
      
        private Transaction[] transactions;
        private String lastblock;
        
        @Data public class Transaction {
            String account;
            String address;
            String category;    // send, receive
            double amount;
            String label;
            int vout;
            double fee;
            int confirmations;
            String blockhash;
            int blockindex;
            long blocktime;
            String txid;
            String[] walletconflicts;
            long time;
            long timereceived;
            boolean abandoned;
          
        }
    }
    
}
