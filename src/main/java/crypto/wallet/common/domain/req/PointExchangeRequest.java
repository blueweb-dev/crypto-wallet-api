package crypto.wallet.common.domain.req;

import lombok.Data;

@Data public class PointExchangeRequest {
  
	private String orderId;
	private int uid;
	private String tokenSymbol;
	private double tokenAmount;
	private double pointAmount;
    
    public PointExchangeRequest() {} 
    
    public PointExchangeRequest(String orderId, int uid, String tokenSymbol, double tokenAmount) {
    	this.orderId 	 = orderId;
        this.uid 	 	 = uid;
        this.tokenSymbol = tokenSymbol;
        this.tokenAmount = tokenAmount;
    }
    
    public PointExchangeRequest(PointExchangeRequest input) {
    	this.orderId 	 = input.getOrderId();
        this.uid     	 = input.getUid();
        this.tokenSymbol = input.getTokenSymbol();
        this.tokenAmount = input.getTokenAmount();
    }
    
}
    
