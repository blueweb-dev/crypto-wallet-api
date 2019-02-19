package crypto.wallet.service.intf;

import java.util.Map;

public interface TokenSupport {

    Map<String, String> getContractAddressMap();
    boolean fillGasWhereNotEnough();
    double getMingasamt();
    
}