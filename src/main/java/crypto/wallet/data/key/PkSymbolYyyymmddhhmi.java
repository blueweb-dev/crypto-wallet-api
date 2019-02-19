package crypto.wallet.data.key;

import java.io.Serializable;

import lombok.Data;

@Data public class PkSymbolYyyymmddhhmi implements Serializable {
  
	private static final long serialVersionUID = 4311945119884324792L;
	private String symbol;
    private int yyyymmddhhmi;
      
    public PkSymbolYyyymmddhhmi() {}
    public PkSymbolYyyymmddhhmi(String symbol, int yyyymmddhhmi) {
        super();
        this.symbol = symbol;
        this.yyyymmddhhmi = yyyymmddhhmi;
    }
  
}