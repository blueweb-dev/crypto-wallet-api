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
import com.google.gson.JsonParseException;

import lombok.Data;

@Data
public class BlockTransactionsDeserializer implements JsonDeserializer<BlockTransactions>{

	@Override
	public BlockTransactions deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		JsonArray jsonArray = json.getAsJsonArray();
		Gson gson = new GsonBuilder().registerTypeAdapter(BlockTransaction.class, new BlockTransactionDeserializer()).create();
		
		List<BlockTransaction> blockTransactionList = new ArrayList<>();
		BlockTransactions blockTransactions = new BlockTransactions();
		for(int i=0; i < jsonArray.size(); i++) {
			BlockTransaction blockTransaction = gson.fromJson(jsonArray.get(i), BlockTransaction.class);
			blockTransactionList.add(blockTransaction);
		}
		blockTransactions.setBlockTransactions(blockTransactionList);
		return blockTransactions;
	}
}
