package crypto.wallet.data.gson.common;


import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;


@Entity @Data public class CryptoBalance implements Serializable {

	private static final long serialVersionUID = 6286568553947287441L;
	@Id private int uid;
	@Id private String symbol;
	private double balance;
	private double recv;
	private double send;
	private double buy;

}
