package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.common.domain.req.FiatDepositRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class FiatDepositResponse extends WalletResponse {
  
    private Result result;
    
    public FiatDepositResponse(FiatDepositRequest req) {
    	this.result = new Result(req);
    }
    
    public FiatDepositResponse() {}

    @EqualsAndHashCode(callSuper = false) @Data 
    public class Result extends FiatDepositRequest {
    	public Result(FiatDepositRequest req) {
    		super(req);
    	}
    }
    
}
