package crypto.wallet.data.domain;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.data.key.PkSymbolAddr;
import lombok.Data;


@Entity @Data @IdClass(PkSymbolAddr.class) public class TbOrphanAddress implements Serializable {

    private static final long serialVersionUID = 7502368016266912326L;
    
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
    @Temporal(TemporalType.TIMESTAMP) private Date updDt;
    @Id private String symbol;
    private double balance;
    private String account;
    @Id private String addr;
    private String tag;
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
    @Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
	public TbOrphanAddress() {};
	public TbOrphanAddress(PkSymbolAddr pk) {
	   this.symbol = pk.getSymbol();
       this.addr = pk.getAddr();
	}
	public TbOrphanAddress(String symbol, String addr) {
	    this.symbol = symbol;
	    this.addr = addr;
	}
	
	@Override public String toString() {
		return symbol + " " + addr + " " + balance;
	}

}
