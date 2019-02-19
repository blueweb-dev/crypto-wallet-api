package crypto.wallet.common.domain.res;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.data.domain.TbTokenBuyRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false) @Data 
public class PrivateSaleTXResponse implements WalletConst {

    private String orderId;
	private int uid;
	private String tokenSymbol;
	private double tokenAmount;
	private String paySymbol;
	private double payAmount;
	private String fromAddr;
	private String fromTag;
	private char status;
	private String error;
    
    public PrivateSaleTXResponse() {}
    
    public PrivateSaleTXResponse(TbTokenBuyRequest datum) {
	    this.orderId        = datum.getOrderId();
	    this.uid            = datum.getUid();
	    this.tokenSymbol	= datum.getTokenSymbol();
	    this.tokenAmount    = datum.getTokenAmount();
        this.paySymbol      = datum.getPaySymbol();
        this.payAmount      = datum.getPayAmount();
        this.fromAddr       = datum.getFromAddr();
        this.fromTag        = datum.getFromTag();
        this.status			= datum.getStatus();
        this.error			= datum.getError();
    }
    
}
