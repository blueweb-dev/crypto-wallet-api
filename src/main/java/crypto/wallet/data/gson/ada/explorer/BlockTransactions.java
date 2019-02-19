package crypto.wallet.data.gson.ada.explorer;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BlockTransactions {
	List<BlockTransaction> blockTransactions = new ArrayList<>();
}
