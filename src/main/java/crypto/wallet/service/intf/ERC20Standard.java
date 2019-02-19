package crypto.wallet.service.intf;

import java.math.BigInteger;

import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

public interface ERC20Standard {
  
    RemoteCall<BigInteger> balanceOf(String _owner);
    RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value);
    RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value);
    RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value);
    
}
