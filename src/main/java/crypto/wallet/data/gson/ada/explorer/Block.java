package crypto.wallet.data.gson.ada.explorer;

import lombok.Data;

@Data
public class Block {
	int cbeEpoch;
	int cbeSlot;
	String cbeBlkHash;
	long timeIssued;
	int cbeTxNum;
	CCoin cbeTotalSent;
	int cbeSize;
	String cbeBlockLead;
	CCoin cbeFees;
}
