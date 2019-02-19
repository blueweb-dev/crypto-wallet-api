package crypto.wallet.common.domain.res;

import crypto.wallet.common.domain.abst.WalletResponse;
import crypto.wallet.common.domain.req.PersonalInfoRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data
public class NewAddressResponse extends WalletResponse {
  
	private Result result;
    
	public NewAddressResponse() {}
    
	public void setResult(PersonalInfoRequest req) {
    	this.result = new Result(req);
    }
    
    @EqualsAndHashCode(callSuper = false) @Data 
    public class Result extends PersonalInfoRequest {
    	
        public Result(PersonalInfoRequest req) {
        	super(req);
        }
        
    }
    
}
