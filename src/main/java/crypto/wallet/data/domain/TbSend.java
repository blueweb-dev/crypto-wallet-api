package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.data.key.PkSymbolOrderIdToAddr;
import lombok.Data;

@Entity @Data @IdClass(PkSymbolOrderIdToAddr.class) public class TbSend {

	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	private char reNotify = 'N';
    @Id private String symbol;
    private String txid;
    private String txIdx;
    private char notifiable = 'Y';
	private long confirm;
	private int notiCnt;
    @Id private String toAddr;
    private String toTag;
    private double amount;
    @Id private String orderId;
    private int uid;
    private String brokerId;
    private double exptFee;
    private double realFee;
	private String fromAccount;
	private String fromAddr;
	private String fromTag;
	private String blockId;
	private long txTime;
	private String errMsg;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
	public TbSend() {}
	public TbSend(String orderId, String symbol, int uid, String toAddr, String toTag, double amount, String brokerId) {
	    this.orderId        = orderId;
        this.symbol         = symbol;
        this.uid            = uid;
        this.toAddr         = toAddr;
        this.toTag          = toTag;
        this.amount         = amount;
        this.brokerId       = brokerId;
    }
	public TbSend(SendRequest req) {
		this.orderId        = req.getOrderId();
        this.symbol         = req.getSymbol();
        this.uid            = req.getUid();
        this.toAddr         = req.getToAddress();
        this.toTag          = req.getToTag();
        this.amount         = req.getAmount();
        this.brokerId       = req.getBrokerId();
        this.exptFee        = req.getExpectFee();
        this.fromAccount    = req.getFromAccount();
        this.fromAddr       = req.getFromAddress();
        this.fromTag        = req.getFromTag();
		if (req.getUid()==WalletConst.UID_SYSTEM 
				|| req.getBrokerId()==WalletConst.BROKER_ID_SYSTEM) {
			this.notifiable = 'N';
		} else {
			this.notifiable = req.getNotifiable();
		}
	}
	
}