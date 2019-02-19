package crypto.wallet.data.key;

import java.io.Serializable;

import lombok.Data;

@Data public class PkUidSymbol implements Serializable {
  
	private static final long serialVersionUID = 4311946119884324792L;
	private String symbol;
    private int uid;
      
    public PkUidSymbol() {}
    public PkUidSymbol(int uid, String symbol) {
        super();
        this.uid = uid;
        this.symbol = symbol;
    }
  
}