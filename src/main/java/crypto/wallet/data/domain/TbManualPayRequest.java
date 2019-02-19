package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.common.domain.req.ManualPayRequest;
import lombok.Data;

@Entity @Data public class TbManualPayRequest {
	
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	@Id private String orderId;
	private int uid;
	private String tokenSymbol;
	private double tokenAmount;
	private char payReason;
	private char snsCode;
	private String bountyUrl;
	private String toAddr;
	private String toTag;
	private char status;
	private String error;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
	public TbManualPayRequest() {}
	public TbManualPayRequest(String orderId, int uid, String tokenSymbol, double tokenAmount, char payReason,
			char snsCode, String bountyUrl, String toAddr, String toTag) {
	    this.orderId        = orderId;
        this.uid            = uid;
        this.tokenSymbol	= tokenSymbol;
        this.tokenAmount	= tokenAmount;
        this.payReason		= payReason;
        this.snsCode		= snsCode;
        this.bountyUrl		= bountyUrl;
        this.toAddr         = toAddr;
        this.toTag          = toTag;
    }
	
	public TbManualPayRequest(ManualPayRequest req) {
	    this.orderId        = req.getOrderId();
        this.uid            = req.getUid();
        this.tokenSymbol	= req.getTokenSymbol();
        this.tokenAmount	= req.getTokenAmount();
        this.payReason		= req.getPayReason();
        this.snsCode		= req.getSnsCode();
        this.bountyUrl		= req.getBountyUrl();
	}
	
}