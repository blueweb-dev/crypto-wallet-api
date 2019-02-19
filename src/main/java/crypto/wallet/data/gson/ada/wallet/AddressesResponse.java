package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data
public class AddressesResponse {
	Address[] data;
	String status;
	Meta meta;
}
