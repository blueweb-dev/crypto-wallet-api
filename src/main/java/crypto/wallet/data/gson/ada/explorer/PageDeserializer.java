package crypto.wallet.data.gson.ada.explorer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class PageDeserializer implements JsonDeserializer<Page>{
	@Override
	public Page deserialize(JsonElement json, Type arg1, JsonDeserializationContext arg2)
			throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		Page page = new Page();
		page.setIndex(jsonObject.getAsJsonArray().get(0).getAsLong());
		
		List<Block> blocks = new ArrayList<>();
//		JsonArray blocksInJsonArrayFormat = json.getAsJsonArray();
		JsonArray blocksInJsonArrayFormat = json.getAsJsonArray().get(1).getAsJsonArray();
		Gson gson = new Gson();
		for(int i=0;i < blocksInJsonArrayFormat.size(); i++) {
			Block block = gson.fromJson(blocksInJsonArrayFormat.get(i), Block.class);
			blocks.add(block);
		}
		page.setBlocks(blocks);
		return page;
	}
}
