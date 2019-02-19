package crypto.wallet.data.gson.common;

import lombok.Data;

@Data public class NewAddress {
  
	private String symbol;
    private int uid;
    private String brokerId;
    private String address;
    private String addressTag;
    
    public NewAddress() {}
    
    public NewAddress(String symbol, int uid, String brokerId) {
    	this.symbol = symbol;
    	this.uid = uid;
    	this.brokerId = brokerId;
    }
        
}
