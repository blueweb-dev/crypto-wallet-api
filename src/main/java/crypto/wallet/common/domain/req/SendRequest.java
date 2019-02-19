package crypto.wallet.common.domain.req;

import lombok.Data;

@Data public class SendRequest {
  
    private String symbol;
    private int uid;
    private String orderId;
    private String fromAccount;
    private String fromAddress;
    private String fromTag;
    private String toAddress;
    private String toTag;
    private double amount;
    private char notifiable = 'Y';
    private String brokerId; 
    private String txid;      // 전송 요청결과 TXID
    private boolean finished; // 전송결과 최종 CONFIRM/FAIL 통지여부
    private double expectFee; // 거래소 수수료
    private double realFee;   // 실 전송 수수료
    private int dupeCount;    // 중복 수신자 개수
    private String errMsg;    // 에러 메시지
    private String pp;
    
    public SendRequest() {} 
    
    public SendRequest(String symbol, String orderId, int uid, String toaddress, String totag, 
            double amount, String brokerId, double expectFee) {
        this.uid = uid;
        this.orderId = orderId;
        this.symbol = symbol;
        this.toAddress = toaddress;
        this.toTag = totag;
        this.amount = amount;
        this.brokerId = brokerId;
        this.expectFee = expectFee;
    }
    
    public SendRequest(int uid, String symbol, String toaddress, double amount, 
    		String orderId, String brokerId) {
        this.uid = uid;
        this.symbol = symbol;
        this.toAddress = toaddress;
        this.amount = amount;
        this.orderId = orderId;
        this.brokerId = brokerId;
    }
    
    public SendRequest(int uid, String symbol, String toaddress, double amount) {
        this.uid = uid;
        this.symbol = symbol;
        this.toAddress = toaddress;
        this.amount = amount;
    }
    
    public SendRequest(SendRequest input) {
        this.uid = input.uid;
        this.symbol = input.symbol;
        this.toAddress = input.toAddress;
        this.amount = input.amount;
    }
    
}
