package crypto.wallet.common.domain.res;

import java.io.Serializable;
import java.util.List;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.abst.WalletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class PurchaseResponse extends WalletResponse implements WalletConst, Serializable {

	private static final long serialVersionUID = -7170384455011161689L;
	private Txs result;
    
    public PurchaseResponse() {
    	this.result = new Txs();
    }
    
    public PurchaseResponse(List<PrivateSaleTXResponse> result) {
    	this.result = new Txs();
    	this.result.setBuy(result);
    }
    
    @Data public class Txs implements Serializable {
    	
		private static final long serialVersionUID = -3046862833265929816L;
		private long totalCount;
		private List<PrivateSaleTXResponse> buy;
		
    }
    
}
