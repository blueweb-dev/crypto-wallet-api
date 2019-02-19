package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.common.domain.req.TokenBuyRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data
public class TokenBuyResponse extends WalletResponse {
  
    private Result result;
    public TokenBuyResponse(TokenBuyRequest req) {
    	this.result = new Result(req);
    }
    
    @EqualsAndHashCode(callSuper = false) @Data 
    public class Result extends TokenBuyRequest {
    	private char status = 'F';
    	public Result(TokenBuyRequest req) {
    		super(req);
    	}
    }
}
