package crypto.wallet.data.gson.ada.wallet;

import java.util.ArrayList;

import lombok.Data;

@Data
public class Transaction {
  
	String id;
	int confirmations;
	String amount;
	ArrayList<InputOutput> inputs;
	ArrayList<InputOutput> outputs;
	String type;
	String direction;
	Status status;
	String creationTime;
	Meta meta;
	
	@Data public class Status{
		String tag;
		Object data;
	}
	
}
