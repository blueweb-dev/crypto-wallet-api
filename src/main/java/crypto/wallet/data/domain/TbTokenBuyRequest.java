package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.common.domain.req.TokenBuyRequest;
import lombok.Data;

@Entity @Data public class TbTokenBuyRequest {
	
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	@Id private String orderId;
	private int uid;
	private String tokenSymbol;
	private String paySymbol;
	private double payAmount;
	private double tokenAmount;
	private String fromAddr;
	private String fromTag;
	private char status;
	private String error;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
	public TbTokenBuyRequest() {}
	public TbTokenBuyRequest(String orderId, int uid, String tokenSymbol, 
			double tokenAmount, String paySymbol, double payAmount) {
	    this.orderId        = orderId;
        this.paySymbol      = paySymbol;
        this.tokenSymbol	= tokenSymbol;
        this.uid            = uid;
        this.payAmount      = payAmount;
        this.tokenAmount    = tokenAmount;
    }
	
	public TbTokenBuyRequest(TokenBuyRequest req) {
	    this.orderId        = req.getOrderId();
        this.paySymbol      = req.getPaySymbol();
        this.tokenSymbol	= req.getTokenSymbol();
        this.uid            = req.getUid();
        this.payAmount      = req.getPayAmount();
        this.tokenAmount    = req.getTokenAmount();
	}
	
}