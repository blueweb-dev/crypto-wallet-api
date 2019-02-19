package crypto.wallet.service.intf;

public interface PassPhraseWallet {

    boolean walletpassphrase();
    boolean walletpassphraseshort();
    boolean walletpassphrase(String interval);
    boolean walletlock();
    String getPp();
    
}