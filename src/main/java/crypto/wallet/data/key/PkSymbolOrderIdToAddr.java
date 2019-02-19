package crypto.wallet.data.key;

import java.io.Serializable;

import lombok.Data;

@Data public class PkSymbolOrderIdToAddr implements Serializable {
  
    private static final long serialVersionUID = 8735431110586516770L;
    private String symbol;
    private String orderId;
    private String toAddr;
      
    public PkSymbolOrderIdToAddr() {}
    public PkSymbolOrderIdToAddr(String symbol, String orderId, String toAddr) {
        super();
        this.symbol = symbol;
        this.orderId = orderId;
        this.toAddr = toAddr;
    }
  
}