package crypto.wallet.data.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;


@Entity @Data public class TbMarketPrice implements Serializable {

	private static final long serialVersionUID = -7505966628091908046L;
	
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
    @Temporal(TemporalType.TIMESTAMP) private Date updDt;
    @Id private String symbol;
    private double krw;
    private double usd;
    @Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
    @Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
    public TbMarketPrice() {}
	public TbMarketPrice(String symbol, double krw, double usd) {
	    this.symbol = symbol;
	    this.krw = krw;
	    this.usd = usd;
	}
	
	@Override public String toString() {
		return symbol + " " + krw + " " + usd;
	}

}
