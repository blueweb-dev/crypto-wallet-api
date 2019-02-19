package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data public class GetWalletsResponse {
	Wallet[] data;
	String status;
	Meta meta;
}
