package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.common.domain.res.FiatDepositResponse;
import crypto.wallet.data.key.PkUidSymbolOrderId;
import lombok.Data;

@Entity @Data @IdClass(PkUidSymbolOrderId.class) public class TbFiatDeposit {

	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	@Id private String orderId;
	@Id private int uid;
    @Id private String symbol;
    private double amount;
	private String brokerId;
	private String fromAddr;
	private String regUsr;
	private String errMsg;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
	public TbFiatDeposit() {}
	
	public TbFiatDeposit(int uid, String symbol, double amount, String regUsr) {
		this.uid = uid;
		this.symbol = symbol;
		this.amount = amount;
		this.regUsr = regUsr;
	}
	
	public TbFiatDeposit(FiatDepositResponse res) {
		this.uid = res.getResult().getUid();
		this.symbol = res.getResult().getSymbol();
		this.amount = res.getResult().getAmount();
		this.regUsr = res.getResult().getRegUsr();
		this.orderId = res.getResult().getOrderId();
		this.brokerId = "KR00";
	}
	
}