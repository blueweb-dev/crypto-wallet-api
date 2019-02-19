package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data
public class GetAddressResponse {
	Address data;
	String status;
	Meta meta;
}
