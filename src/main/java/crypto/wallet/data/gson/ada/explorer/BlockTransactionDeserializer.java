package crypto.wallet.data.gson.ada.explorer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import lombok.Data;

@Data
public class BlockTransactionDeserializer implements JsonDeserializer<BlockTransaction> {
	@Override
	public BlockTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		BlockTransaction blockTransaction = new BlockTransaction();
		
		blockTransaction.setCtbId(jsonObject.get("ctbId").getAsString());
		blockTransaction.setCtbTimeIssued(jsonObject.get("ctbTimeIssued").getAsLong());
		
		List<InputOutput> ctbInputs = new ArrayList<>();
		JsonArray ctbInputsInJsonForm = jsonObject.get("ctbInputs").getAsJsonArray();
		Gson gson = new GsonBuilder().registerTypeAdapter(InputOutput.class, new InputOutput()).create();
		for(int i=0; i < ctbInputsInJsonForm.size(); i++) {
			ctbInputs.add(gson.fromJson(ctbInputsInJsonForm.get(i), InputOutput.class));
		}
		blockTransaction.setCtbInputs(ctbInputs);
		
		List<InputOutput> ctbOutputs = new ArrayList<>();
		JsonArray ctbOutputsInJsonForm = jsonObject.get("ctbOutputs").getAsJsonArray();
		for(int i=0; i < ctbOutputsInJsonForm.size(); i++) {
			ctbOutputs.add(gson.fromJson(ctbInputsInJsonForm.get(i), InputOutput.class));
		}
		blockTransaction.setCtbInputs(ctbInputs);
		
		CCoin ctbInputSum = new Gson().fromJson(jsonObject.get("ctbInputSum"), CCoin.class);
		blockTransaction.setCtbInputSum(ctbInputSum);
		CCoin ctbOutputSum = new Gson().fromJson(jsonObject.get("ctbOutputSum"), CCoin.class);
		blockTransaction.setCtbInputSum(ctbOutputSum);
		
		return blockTransaction;
	}
}
