package crypto.wallet.common.domain.res;

import java.io.Serializable;
import java.util.List;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.abst.WalletResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class TxHistoryResponse extends WalletResponse implements WalletConst, Serializable {

	private static final long serialVersionUID = -7170384455007161689L;
	private Txs result;
    
    public TxHistoryResponse() {
    	this.result = new Txs();
    }
    public TxHistoryResponse(List<TransactionResponse> txs, List<PrivateSaleTXResponse> buy) {
    	this.result = new Txs(txs, buy);
    }
    
    @Data public class Txs implements Serializable {
    	
		private static final long serialVersionUID = -3046862810265929816L;
		List<TransactionResponse> txs;
    	List<PrivateSaleTXResponse> buy;
    	
    	public Txs() {}
    	public Txs(List<TransactionResponse> txs, List<PrivateSaleTXResponse> buy) {
    		this.txs = txs;
    		this.buy = buy;
    	}
    }
    
}
