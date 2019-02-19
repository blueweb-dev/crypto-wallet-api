package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data
public class GetAccountResponse {
	Account data;
	String status;
	Meta meta;
}
