package crypto.wallet.data.gson.ada.wallet;

import java.math.BigInteger;
import java.util.ArrayList;

import lombok.Data;

@Data
public class Account {		
	BigInteger amount;
	ArrayList <Address> addresses;
	String name;
	String walletId;
	long index;
}
