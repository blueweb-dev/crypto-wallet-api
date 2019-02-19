package crypto.wallet.data.gson.ada.explorer;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class BlockTransaction {
	String ctbId;
	long ctbTimeIssued;
	List<InputOutput> ctbInputs = new ArrayList<>();
	List<InputOutput> ctbOutputs = new ArrayList<>();
	CCoin ctbInputSum;
	CCoin ctbOutputSum;
}
