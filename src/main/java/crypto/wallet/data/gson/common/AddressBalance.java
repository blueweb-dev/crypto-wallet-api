package crypto.wallet.data.gson.common;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity @Data public class AddressBalance implements Serializable {

	private static final long serialVersionUID = 8764100713370149508L;
	
	@Id private String symbol;
    private String addr;
	private double balance;
	
	public AddressBalance() {};
	public AddressBalance(String symbol, String addr, double balance) {
	    this.symbol = symbol;
	    this.addr = addr;
	};
	
	@Override public String toString() {
		return symbol + " " + addr + " " + balance;
	}

}
