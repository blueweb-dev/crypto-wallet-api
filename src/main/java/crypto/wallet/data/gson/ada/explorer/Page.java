package crypto.wallet.data.gson.ada.explorer;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Page {
	long index;
	List<Block> blocks = new ArrayList<>();
}
