package crypto.wallet.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.service.CryptoSharedService;

/**
 */
@Component public class CoinFactory implements WalletConst {
  
    // BTC
	@Autowired private CryptoSharedService btcRpcService;
    // LTC
	@Autowired private CryptoSharedService ltcRpcService;
	// ETH
	@Autowired private CryptoSharedService ethRpcService;
	// ERC20 - ZIL
	@Autowired private CryptoSharedService zilRpcService;
	
	/**
	 * 심볼명에 대한 서비스 반환
	 * @param symbol
	 * @return
	 */
    public CryptoSharedService getService(String symbol) {
        CryptoSharedService ret = null;
        switch (symbol) {
            case SYMBOL_BTC: ret  = btcRpcService; break;
            case SYMBOL_LTC: ret  = ltcRpcService; break;
            case SYMBOL_ETH: ret  = ethRpcService; break;
			case SYMBOL_ZIL: ret  = zilRpcService; break;
        }
        return ret;
    }
    
}
