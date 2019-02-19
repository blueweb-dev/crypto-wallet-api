package crypto.wallet.service.intf;

import java.util.List;
import java.util.Set;

public interface OwnChain {
  
    boolean openBlocksGetTxsThenSave();
    long getLatestblockFromChain() throws Exception;
    long getInitialblock();
    long getMinconfirm();
    List<String> getAllAddressListFromNode();
    Set<String> getAllAddressSetFromNode();
    
}