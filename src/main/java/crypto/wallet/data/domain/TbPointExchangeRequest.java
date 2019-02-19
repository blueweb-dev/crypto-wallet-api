package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.common.domain.req.PointExchangeRequest;
import lombok.Data;

@Entity @Data public class TbPointExchangeRequest {
	
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	@Id private String orderId;
	private int uid;
	private String tokenSymbol;
	private double tokenAmount;
	private double pointAmount;
	private String fromAddr;
	private String fromTag;
	private char status;
	private String error;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
	public TbPointExchangeRequest() {}
	public TbPointExchangeRequest(String orderId, int uid, String tokenSymbol, double tokenAmount, double pointAmount
			, String fromAddr, String fromTag) {
	    this.orderId        = orderId;
        this.uid            = uid;
        this.tokenSymbol	= tokenSymbol;
        this.tokenAmount	= tokenAmount;
        this.pointAmount	= pointAmount;
        this.fromAddr		= fromAddr;
        this.fromTag		= fromTag;
    }
	
	public TbPointExchangeRequest(PointExchangeRequest req) {
	    this.orderId        = req.getOrderId();
        this.uid            = req.getUid();
        this.tokenSymbol	= req.getTokenSymbol();
        this.tokenAmount	= req.getTokenAmount();
        this.pointAmount	= req.getPointAmount();
	}
	
}