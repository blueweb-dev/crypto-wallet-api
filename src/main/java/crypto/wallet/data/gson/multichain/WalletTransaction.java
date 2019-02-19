package crypto.wallet.data.gson.multichain;

import crypto.wallet.data.gson.btc.BtcRpcError;
import lombok.Data;

@Data public class WalletTransaction {
  
    private int id;
    private Transaction result;
    private BtcRpcError error;

    public WalletTransaction() {}
    public WalletTransaction(BtcRpcError error) { this.error = error; }
    
    @Data public class Transaction {
        private Balance balance;
        private String[] myaddresses;
        private String[] addresses;
        private long confirmations;
        private boolean generated;
        private String blockhash;
        private long blocktime;
        private String txid;
        private long time;
        private long timereceived;
        private int txcount;
        private boolean valid;
    }
    
    @Data public class Balance {
        private double amount;
        private String[] assets;
    }
    
}
