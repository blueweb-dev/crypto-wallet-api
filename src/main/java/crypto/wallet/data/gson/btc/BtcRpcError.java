package crypto.wallet.data.gson.btc;

import lombok.Data;

@Data public class BtcRpcError {
    private int code;
    private String message;
    public BtcRpcError() { }
    public BtcRpcError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}