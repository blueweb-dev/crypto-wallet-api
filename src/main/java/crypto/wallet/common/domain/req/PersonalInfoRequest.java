package crypto.wallet.common.domain.req;

import lombok.Data;

@Data public class PersonalInfoRequest {
  
    private String symbol;
    private int uid;
    private String brokerId;
    private String orderId;
    private String address;
    private String addressTag;

    public PersonalInfoRequest() {}
    
    public PersonalInfoRequest(String symbol, int uid, String brokerId) {
        this.symbol = symbol;
        this.uid = uid;
        this.brokerId = brokerId;
    }
    
    public PersonalInfoRequest(PersonalInfoRequest req) {
        this.symbol = req.getSymbol();
        this.uid = req.getUid();
        this.address = req.getAddress();
        this.addressTag = req.getAddressTag();
        this.brokerId = req.getBrokerId();
    }
    
}
