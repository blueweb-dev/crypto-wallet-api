package crypto.wallet.data.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.data.key.PkUidSymbol;
import lombok.Data;


@Entity @Data @IdClass(PkUidSymbol.class) public class TbAddressBalance implements Serializable {

	private static final long serialVersionUID = 5127319640626022175L;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date updDt;
	@Id private int uid;
	@Id private String symbol;
	private String addr;
	private double balance;
	private double actual;
	private double recv;
	private double send;
	private double buy;
    private String brokerId;
    private String tag;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;

	public TbAddressBalance() {};
	
	public TbAddressBalance(String symbol, int uid, String brokerId, String addr) {
	    this.symbol = symbol;
	    this.uid = uid;
	    this.brokerId = brokerId;
	    this.addr = addr;
	};
	
	@Override public String toString() {
		return symbol + " " + uid + " calcbal " + balance + " actual " + actual + " " + addr;
	}
	
}
