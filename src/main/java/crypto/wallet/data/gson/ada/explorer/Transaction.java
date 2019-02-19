package crypto.wallet.data.gson.ada.explorer;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Transaction {
  
	String ctsId;
	Long ctsTxTimeIssued;
	Long ctsBlockTimeIssued;
	Long ctsBlockHeight;
	Long ctsBlockEpoch;
	Long ctsBlockSlot;
	String ctsBlockHash;
	String ctsRelayedBy;
	CCoin ctsTotalInput;
	CCoin ctsTotalOutput;
	CCoin ctsFees;
	List<InputOutput> ctsInputs  = new ArrayList<>();
	List<InputOutput> ctsOutputs = new ArrayList<>();
	
}
