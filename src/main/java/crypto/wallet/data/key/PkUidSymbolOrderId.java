package crypto.wallet.data.key;

import java.io.Serializable;

import lombok.Data;

@Data public class PkUidSymbolOrderId implements Serializable {
  
	private static final long serialVersionUID = 5324934761374534596L;
	private String orderId;
	private String symbol;
    private int uid;
      
    public PkUidSymbolOrderId() {}
    public PkUidSymbolOrderId(String orderId, int uid, String symbol) {
        super();
        this.orderId = orderId;
        this.uid = uid;
        this.symbol = symbol;
    }
  
}