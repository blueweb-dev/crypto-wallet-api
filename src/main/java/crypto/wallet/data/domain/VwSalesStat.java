package crypto.wallet.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import crypto.wallet.data.key.PkSymbolYyyymmddhhmi;
import lombok.Data;

@Entity @Data @IdClass(PkSymbolYyyymmddhhmi.class) public class VwSalesStat {
	
	@Id private String symbol;
	@Id private int yyyymmddhhmi;
	private double soldAmt;
	private double krwSoldAmt;
	private double ethSoldAmt;
	private double krwPaidAmt;
	private double ethPaidAmt;
	private double krwUnpaidAmt;
	private double ethUnpaidAmt;
	private int cpuUsage;
	private int memUsage;
	private int diskUsage;
	private Character apiStat; // 0:fail, 1:warn, 2:success
	@Column(columnDefinition="DATETIME", insertable=false, updatable=false) 
	@Temporal(TemporalType.TIMESTAMP) private Date regDt;
	
}