package crypto.wallet.data.gson.btc;

import lombok.Data;

@Data public class GetTransaction {
  
    private int id;
    private BtcRpcError error;
    private Result result;

    public GetTransaction() {}
    public GetTransaction(BtcRpcError error) { this.error = error; }
    
	@Data public class Result {

        private int confirmations;
        private int blockindex;
        private double amount;
        private double fee;
        private boolean valid;
        private String comment;
        private String blockhash;
        private String txid;
        private String hex;
        private Detail[] details;
        private long time;
        private long timereceived;
        private long blocktime;
        
        public Result() { }
        
	}
	
	@Data public static class Detail {
        private int vout;
        private double amount;
        private String account;
        private String address;
        private String category;
        private String label;
        private double fee;
        public Detail() { }
    }

}
