package crypto.wallet.common.domain.req;

import lombok.Data;

@Data public class FiatDepositRequest {
  
    private String symbol;
    private int uid;
    private double amount;
    private String orderId;
    private String regUsr;
    private String password;
    
    public FiatDepositRequest() {}
    public FiatDepositRequest(FiatDepositRequest req) {
    	this.symbol = req.getSymbol();
    	this.orderId = req.getOrderId();
    	this.uid = req.getUid();
    	this.amount = req.getAmount();
    	this.regUsr = req.getRegUsr();
    }
    
    
    
}
