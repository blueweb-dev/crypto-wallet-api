package crypto.wallet.common.domain.req;

import lombok.Data;

@Data public class ManualPayRequest {
  
	private String orderId;
	private int uid;
	private String tokenSymbol;
	private double tokenAmount;
	private char payReason;		// M/S
	private char snsCode;
	private String bountyUrl;
    
    public ManualPayRequest() {} 
    
    public ManualPayRequest(String orderId, int uid, String tokenSymbol, double tokenAmount
    		, char payReason, char snsCode, String bountyUrl) {
    	this.orderId 	 = orderId;
        this.uid 	 	 = uid;
        this.tokenSymbol = tokenSymbol;
        this.tokenAmount = tokenAmount;
        this.payReason 	 = payReason;
        this.snsCode 	 = snsCode;
        this.bountyUrl 	 = bountyUrl;
    }
    
    public ManualPayRequest(ManualPayRequest input) {
    	this.orderId 	 = input.getOrderId();
        this.uid     	 = input.getUid();
        this.tokenSymbol = input.getTokenSymbol();
        this.tokenAmount = input.getTokenAmount();
        this.payReason 	 = input.getPayReason();
        this.snsCode 	 = input.getSnsCode();
        this.bountyUrl 	 = input.getBountyUrl();
    }
    
}
    
