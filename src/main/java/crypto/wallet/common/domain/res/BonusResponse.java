package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.common.domain.req.BonusRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data
public class BonusResponse extends WalletResponse {
  
    private Result result;
    public BonusResponse(BonusRequest req) {
    	this.result = new Result(req);
    }
    
    @EqualsAndHashCode(callSuper = false) @Data 
    public class Result extends BonusRequest {
    	private char status = 'F';
    	public Result(BonusRequest req) {
    		super(req);
    	}
    }
}
