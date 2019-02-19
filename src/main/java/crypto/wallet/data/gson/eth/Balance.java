package crypto.wallet.data.gson.eth;

import lombok.Data;

@Data public class Balance implements Cloneable {
  
    private Double amount;
    private String error;
    
}
