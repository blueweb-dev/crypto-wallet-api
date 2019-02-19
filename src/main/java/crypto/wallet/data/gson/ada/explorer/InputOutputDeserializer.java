package crypto.wallet.data.gson.ada.explorer;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class InputOutputDeserializer implements JsonDeserializer<InputOutput>{
	
	@Override
	public InputOutput deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonArray jsonArray = json.getAsJsonArray();
		InputOutput data = new InputOutput();
		data.setAddress(jsonArray.get(0).getAsString());
		CCoin ccoin = new Gson().fromJson(jsonArray.get(1), CCoin.class);
		data.setAmount(ccoin);
		return data;
	}
}
