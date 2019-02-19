package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data
public class Address {
	boolean used;
	boolean changeAddress;
	String id;
}