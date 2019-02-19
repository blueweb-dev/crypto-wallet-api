package crypto.wallet.data.gson.common;

import lombok.Data;

/**
 * 1) https://api.coingecko.com/api/v3/coins/ethereum
 * 2) https://api.coingecko.com/api/v3/coins/bitcoin
 * market_data > current_price > krw
 * @author method76
 */
@Data public class CryptoIndex {
  
    private MarketData market_data;

    @Data public class MarketData {
    
    	private CurrentPrice current_price;
    	
    	@Data public class CurrentPrice {
    		
    		private String symbol;
    		private double krw;
    		private double usd;
    		
        }
    	
    }
    
}
