package crypto.wallet.data.gson.btc;

import lombok.Data;

@Data public class BitcoinListAddressBalanceResponse {
  
    private int id;
    private BtcRpcError error;
    private AddressBalance[] result;
    
    @Data public class AddressBalance {
    	private String address;
    	private String account;
    	private double amount;
    	private String label;
    	private String[] txtds;
    }

}
