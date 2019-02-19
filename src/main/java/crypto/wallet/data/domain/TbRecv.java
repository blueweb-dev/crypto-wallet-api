package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.data.key.PkSymbolTxidToAddr;
import lombok.Data;

@Entity @Data @IdClass(PkSymbolTxidToAddr.class)  public class TbRecv {

	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	private char reNotify = 'N';
	@Id private String symbol;
	@Id private String txid;
	private String txIdx;
	private String orderId;
	private char notifiable = 'Y';
	private long confirm;
	private int notiCnt;
	private String toAccount;
	@Id private String toAddr;
	private String toTag;
	private double amount;
	private int uid;
	private String brokerId;
	private String fromAddr;
	private String fromTag;
	private String blockId;
	private long txTime;
	private String errMsg;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
    
	public TbRecv() { }
	public TbRecv(String symbol, String fromAddr, String toAccount, String toAddr, double amount) {
        this.symbol         = symbol;
        this.fromAddr       = fromAddr;
        this.toAccount      = toAccount;
        this.toAddr         = toAddr;
        this.amount         = amount;
	}
	
}
