package crypto.wallet.data.gson.common;

import org.json.JSONObject;

import lombok.Data;

@Data 
public class FCMSendRes {

	private long multicast_id;
	private int success;
	private int failure;
	private int canonical_ids;
	private JSONObject[] results;
	// [{ "message_id": "0:1542338602006936%c6a3590bc6a3590b" }]
			
}

