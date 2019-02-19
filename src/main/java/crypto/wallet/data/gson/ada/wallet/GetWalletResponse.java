package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data public class GetWalletResponse {
	Wallet data;
	String status;
	Meta meta;
}
