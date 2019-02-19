package crypto.wallet.data.gson.common;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Entity @Data public class TotalBalance {
  
	@Id private String symbol;
	private double total;
	private double owners;
	private double users;
	private double avail;
    
    public TotalBalance() {}
    
    public TotalBalance(String symbol) {
        this.symbol = symbol;
    }
    
    public TotalBalance(String symbol, double total, double owners, double users) {
        this.symbol = symbol;
        this.total = total;
        this.owners = owners;
        this.users = users;
    }
    
}
