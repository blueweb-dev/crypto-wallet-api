package crypto.wallet.data.gson.ada.explorer;

import lombok.Data;

@Data
public class InputOutput {
	String address;
	CCoin amount;
}
