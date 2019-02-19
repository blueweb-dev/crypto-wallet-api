package crypto.wallet.data.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


@Entity @Data public class TbCryptoMaster implements Serializable {

	private static final long serialVersionUID = 3428902633510996300L;
	
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
    @Temporal(TemporalType.TIMESTAMP) private Date updDt;
    @Id private String symbol;
    private int decimals;
    private String sendMastAddr;
    private long currSyncHeight;
    private long latestHeight;
    private double totalBal;
    private double sendMastBal;
    private double theOtherBal;
    private double gasPrice;
    private double gasUsed;
    private double actualFee;
    @Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
    @Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
    public TbCryptoMaster() {}
    public TbCryptoMaster(String symbol, String sendMastAddr) {
	    this.symbol = symbol;
	    this.sendMastAddr = sendMastAddr;
	}
	public TbCryptoMaster(String symbol, String sendMastAddr, long currSyncHeight
			, long latestHeight) {
	    this.symbol = symbol;
	    this.sendMastAddr = sendMastAddr;
	    this.currSyncHeight = currSyncHeight;
	    this.latestHeight = latestHeight;
	}
	
	@Override public String toString() {
		return symbol + " " + currSyncHeight + " " + latestHeight + " " + updDt;
	}

}
