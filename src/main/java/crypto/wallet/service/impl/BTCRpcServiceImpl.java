package crypto.wallet.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import crypto.wallet.service.BitcoinAbstractService;
import lombok.Getter;


/**
 * BTC Services
 * @author sungjoon.kim
 */
@Service("btcRpcService") class BTCRpcServiceImpl extends BitcoinAbstractService {

	@Getter private String symbol = SYMBOL_BTC;
    @Getter @Value("${crypto.btc.rpcurl}") private String rpcurl;
    @Getter @Value("${crypto.btc.rpcid}") private String rpcid;
    @Getter @Value("${crypto.btc.rpcpw}") private String rpcpw;
	@Getter @Value("${crypto.btc.decimals}") private int decimals;
    @Getter @Value("${crypto.btc.sendaccount}") private String sendaccount;
    @Getter @Value("${crypto.btc.sendaddr}") private String sendaddr;
    @Getter @Value("${crypto.btc.pp}") private String pp;
    @Getter @Value("${crypto.btc.initialblock}") private long initialblock;
    @Getter @Value("${crypto.btc.minconfirm}") private long minconfirm;
    @Getter @Value("${crypto.btc.minamtgather}") private double minamtgather;
    
}
