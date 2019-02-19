package crypto.wallet.data.gson.common;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import crypto.wallet.data.domain.TbAddressBalance;
import lombok.Data;


@Entity @Data public class CryptoBalanceByAddr implements Serializable {

	private static final long serialVersionUID = 2186714199878742755L;
	@Id private String symbol;
	private String addr;
	private double balance;
	private double recv;
	private double send;
	private double buy;
	private double actual;
	
	public CryptoBalanceByAddr() {}
	
	public CryptoBalanceByAddr(String symbol) {
		this.symbol = symbol;
	}
	
	public CryptoBalanceByAddr(TbAddressBalance datum) {
		this.symbol = datum.getSymbol();
		this.addr = datum.getAddr();
		this.balance = datum.getBalance();
		this.recv = datum.getRecv();
		this.send = datum.getSend();
		this.buy = datum.getBuy();
		this.actual = datum.getActual();
	}

}
