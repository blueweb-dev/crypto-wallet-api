package crypto.wallet.common.domain.req;

import lombok.Data;

@Data public class TokenBuyRequest {
  
	private String orderId;
	private int uid;
    private String paySymbol;
    private double payAmount;
    private String tokenSymbol;
    private double tokenAmount;
    
    public TokenBuyRequest() {} 
    
    public TokenBuyRequest(String orderId, int uid, String paySymbol, double payAmount
    		, String tokenSymbol, double tokenAmount) {
    	this.orderId = orderId;
        this.uid = uid;
        this.paySymbol = paySymbol;
        this.payAmount = payAmount;
        this.tokenSymbol = tokenSymbol;
        this.tokenAmount = tokenAmount;
    }
    
    public TokenBuyRequest(TokenBuyRequest input) {
    	this.orderId = input.getOrderId();
        this.uid     = input.getUid();
        this.paySymbol  = input.getPaySymbol();
        this.payAmount  = input.getPayAmount();
        this.tokenSymbol = input.getTokenSymbol();
        this.tokenAmount = input.getTokenAmount();
    }
    
}
