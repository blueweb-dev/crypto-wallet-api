package crypto.wallet.data.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import crypto.wallet.data.key.PkUidSymbol;
import lombok.Data;

@Entity @Data @IdClass(PkUidSymbol.class) public class VwUserKrwBalance {
	
	@Id private int uid;
	@Id private String symbol;
	private double recv;
	private double buy;
	
	public VwUserKrwBalance() {}
	
}