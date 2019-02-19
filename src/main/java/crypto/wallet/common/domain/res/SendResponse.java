package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.common.domain.req.SendRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class SendResponse extends WalletResponse {
  
    private Result result;
    public void setResult(SendRequest req) {
    	this.result = new Result(req);
    }
    
    @EqualsAndHashCode(callSuper = false) @Data 
    public class Result extends SendRequest {
    	private char status;
    	public Result(SendRequest req) {
    		super(req);
    	}
    }
    
}
