package crypto.wallet.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class AdaJsonUtil {

	public static JsonElement getRightMemberOfExplorerResponse(String jsonString) {
		JsonParser jsonParser = new JsonParser();
		if(jsonParser.parse(jsonString).isJsonPrimitive()) {
			return jsonParser.parse(jsonString).getAsJsonObject().getAsJsonPrimitive("Right");
		}
		return jsonParser.parse(jsonString).getAsJsonObject().getAsJsonObject("Right");
	}
}
