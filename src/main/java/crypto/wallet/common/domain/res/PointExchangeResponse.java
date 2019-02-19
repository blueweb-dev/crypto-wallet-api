package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.common.domain.req.ManualPayRequest;
import crypto.wallet.common.domain.req.PointExchangeRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data
public class PointExchangeResponse extends WalletResponse {
  
    private Result result;
    public PointExchangeResponse(PointExchangeRequest req) {
    	this.result = new Result(req);
    }
    
    @EqualsAndHashCode(callSuper = false) @Data 
    public class Result extends PointExchangeRequest {
    	private char status = 'F';
    	public Result(PointExchangeRequest req) {
    		super(req);
    	}
    }
}
