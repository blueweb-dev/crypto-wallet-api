package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.common.domain.req.ManualPayRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data
public class ManualPayResponse extends WalletResponse {
  
    private Result result;
    public ManualPayResponse(ManualPayRequest req) {
    	this.result = new Result(req);
    }
    
    @EqualsAndHashCode(callSuper = false) @Data 
    public class Result extends ManualPayRequest {
    	private char status = 'F';
    	public Result(ManualPayRequest req) {
    		super(req);
    	}
    }
}
