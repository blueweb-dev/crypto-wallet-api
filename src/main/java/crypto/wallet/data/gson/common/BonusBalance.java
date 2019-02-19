package crypto.wallet.data.gson.common;


import java.io.Serializable;

import lombok.Data;


@Data public class BonusBalance implements Serializable {

	private static final long serialVersionUID = 6286568953947287441L;
	private int uid;
	private String symbol;
	private double value;

}
