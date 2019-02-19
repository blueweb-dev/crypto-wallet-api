package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.common.domain.req.BonusRequest;
import crypto.wallet.data.key.PkSymbolOrderIdToAddr;
import lombok.Data;

@Entity @Data @IdClass(PkSymbolOrderIdToAddr.class) public class TbBonusRequest {
	
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	@Id private String symbol;
	@Id private String orderId;
	private int uid;
	private String brokerId;
	private String toAddr;
	private String toTag;
	private double amount;
	private double bonusBal;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
	public TbBonusRequest() {}
	public TbBonusRequest(String symbol, String orderId, int uid, String toAddr, 
			String toTag, double amount, double bonusBal, String brokerId) {
	    this.orderId        = orderId;
        this.symbol         = symbol;
        this.uid            = uid;
        this.toAddr         = toAddr;
        this.toTag          = toTag;
        this.amount         = amount;
        this.brokerId       = brokerId;
        this.bonusBal		= bonusBal;
    }
	
	public TbBonusRequest(BonusRequest req) {
		this.orderId        = req.getOrderId();
        this.symbol         = req.getSymbol();
        this.uid            = req.getUid();
        this.amount         = req.getAmount();
	}
	
}