package crypto.wallet.data.gson.btc;

import lombok.Data;

@Data public class ValidateAddress {
	
    private int id;
    private Result result;
    private BtcRpcError error;
	
	@Data public class Result {
		boolean isvalid;
		String address;
		String scriptPubKey;
		boolean ismine;
		boolean iswatchonly;
		boolean isscript;
		String pubkey;
		boolean iscompressed;
		String account;
		long timestamp;
		String hdkeypath;
	}
//    "isvalid": true,
//    "address": "mimXJSjdxbvweWVWbiRSykKaotxB7A2hMN",
//    "scriptPubKey": "76a91423a9c0e20f989849a896acc3bacf954e0bb054b488ac",
//    "ismine": true,
//    "iswatchonly": false,
//    "isscript": false,
//    "pubkey": "03f0532e6708d9934a7d6a4d3965b733e96c71acda82820f48d6ec8bb6e8dba34a",
//    "iscompressed": true,
//    "account": "",
//    "timestamp": 1516694322,
//    "hdkeypath": "m/0'/0'/1'",
//    "hdmasterkeyid": "928baa26320d474a3e03068a0fe9e8db4547078a"
    	
}
