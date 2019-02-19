package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.data.key.PkSymbolOrderIdToAddr;
import lombok.Data;

@Entity @Data @IdClass(PkSymbolOrderIdToAddr.class) public class TbSendRequest {
	
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	@Id private String symbol;
	@Id private String orderId;
	private int uid;
	private String brokerId;
	private String exptFee;
	private String toAddr;
	private String toTag;
	private double amount;
	private String fromAccount;
	private String fromAddr;
	private String fromTag;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
	public TbSendRequest() {}
	public TbSendRequest(String symbol, String orderId, int uid, String toAddr, 
			String toTag, double amount, String brokerId) {
	    this.orderId        = orderId;
        this.symbol         = symbol;
        this.uid            = uid;
        this.toAddr         = toAddr;
        this.toTag          = toTag;
        this.amount         = amount;
        this.brokerId       = brokerId;
    }
	
	public TbSendRequest(SendRequest req) {
		this.orderId        = req.getOrderId();
        this.symbol         = req.getSymbol();
        this.uid            = req.getUid();
        this.toAddr         = req.getToAddress();
        this.toTag          = req.getToTag();
        this.amount         = req.getAmount();
        this.brokerId       = req.getBrokerId();
        this.fromAccount    = req.getFromAccount();
        this.fromAddr       = req.getFromAddress();
        this.fromTag        = req.getFromTag();
	}
	
}