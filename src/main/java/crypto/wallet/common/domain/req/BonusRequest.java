package crypto.wallet.common.domain.req;

import lombok.Data;

@Data public class BonusRequest {
  
	private String orderId;
	private int uid;
    private String symbol;
    private double amount;
    
    public BonusRequest() {} 
    
    public BonusRequest(String orderId, int uid, String symbol, double amount) {
    	this.orderId = orderId;
        this.uid 	 = uid;
        this.symbol  = symbol;
        this.amount  = amount;
    }
    
    public BonusRequest(BonusRequest input) {
    	this.orderId = input.getOrderId();
        this.uid     = input.getUid();
        this.symbol  = input.getSymbol();
        this.amount  = input.getAmount();
    }
    
}
