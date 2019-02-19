package crypto.wallet.data.gson.ada.wallet;

import org.json.JSONObject;

import lombok.Data;

@Data public class ADATransactions {
	Transaction[] data;
	String status;
	Meta meta;
	String message;
	JSONObject diagnostic;
}
