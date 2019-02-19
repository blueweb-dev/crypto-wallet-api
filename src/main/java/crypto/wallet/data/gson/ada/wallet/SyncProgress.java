package crypto.wallet.data.gson.ada.wallet;

import lombok.Data;

@Data
public class SyncProgress {
	Info blockchainHeight;
	Info localTimeInformation;
	Info syncProgress;
	Info localBlockChainHeight;

	@Data
	public class Info{
		String quantity;
		String unit;
	}
}
