package crypto.wallet.data.gson.ada.wallet;

import java.math.BigInteger;

import lombok.Data;

@Data public class Wallet {
	String createdAt;
	BigInteger balance;
	boolean hasSpendingPassword;
	String assuranceLevel;
	String name;
	String id;
	String spendingPasswordLastUpdate;
	
	@Data public class SyncState{
		String tag;
		String data;
	}
}
