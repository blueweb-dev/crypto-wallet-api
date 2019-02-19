package crypto.wallet.data.gson.ada.explorer;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionDeserializer implements JsonDeserializer<Transaction>{
	@Override
	public Transaction deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		log.debug(jsonObject.toString());
		Transaction outputTransaction = new Transaction();
		outputTransaction.setCtsId(jsonObject.get("ctsId").getAsString());
		outputTransaction.setCtsTxTimeIssued(jsonObject.get("ctsTxTimeIssued").getAsLong());
		outputTransaction.setCtsBlockTimeIssued(jsonObject.get("ctsBlockTimeIssued").getAsLong());
		outputTransaction.setCtsBlockHeight(jsonObject.get("ctsBlockHeight").getAsLong());
		outputTransaction.setCtsBlockEpoch(jsonObject.get("ctsBlockEpoch").getAsLong());
		outputTransaction.setCtsBlockSlot(jsonObject.get("ctsBlockSlot").getAsLong());
		outputTransaction.setCtsBlockHash(jsonObject.get("ctsBlockHash").getAsString());
		try {
			outputTransaction.setCtsRelayedBy(jsonObject.get("ctsRelayedBy").getAsString());
		} catch(UnsupportedOperationException e) {
			outputTransaction.setCtsRelayedBy(null);
		}
		outputTransaction.setCtsTotalInput(new Gson().fromJson(jsonObject.get("ctsTotalInput"), CCoin.class));
		outputTransaction.setCtsTotalOutput(new Gson().fromJson(jsonObject.get("ctsTotalOutput"), CCoin.class));
		outputTransaction.setCtsFees(new Gson().fromJson(jsonObject.get("ctsFees"), CCoin.class));
		JsonArray ctsInputsOfObject = jsonObject.get("ctsInputs").getAsJsonArray();
		for(int i=0; i < ctsInputsOfObject.size(); i++) {
			JsonArray tempArray = ctsInputsOfObject.get(i).getAsJsonArray();
			
			InputOutput io = new InputOutput();
			io.setAddress(tempArray.get(0).getAsString());
			io.setAmount(new Gson().fromJson(tempArray.get(1), CCoin.class));
			
			outputTransaction.getCtsInputs().add(io);
		}
		
		JsonArray ctsOuputsOfObject = jsonObject.get("ctsOutputs").getAsJsonArray();
		for(int i=0; i < ctsOuputsOfObject.size(); i++) {
			JsonArray tempArray = ctsOuputsOfObject.get(i).getAsJsonArray();
			
			InputOutput io = new InputOutput();
			io.setAddress(tempArray.get(0).getAsString());
			io.setAmount(new Gson().fromJson(tempArray.get(1), CCoin.class));
			
			outputTransaction.getCtsOutputs().add(io);
		}
		return outputTransaction;
	}
}
