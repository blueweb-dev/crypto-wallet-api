package crypto.wallet.service.intf;

public interface PassPhraseAddressWallet {

    boolean walletpassphraseWithAddress(String address);
    boolean walletlock(String address);
    String getPp();
    
}