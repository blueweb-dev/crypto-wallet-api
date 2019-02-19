package crypto.wallet.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import crypto.wallet.service.BitcoinAbstractService;
import lombok.Getter;


/**
 * LTC Services
 * @author sungjoon.kim
 */
@Service("ltcRpcService") class LTCRpcServiceImpl extends BitcoinAbstractService {

	@Getter private String symbol = SYMBOL_LTC;
    @Getter @Value("${crypto.ltc.rpcurl}") private String rpcurl;
    @Getter @Value("${crypto.ltc.rpcid}") private String rpcid;
    @Getter @Value("${crypto.ltc.rpcpw}") private String rpcpw;
	@Getter @Value("${crypto.ltc.decimals}") private int decimals;
    @Getter @Value("${crypto.ltc.sendaccount}") private String sendaccount;
    @Getter @Value("${crypto.ltc.sendaddr}") private String sendaddr;
    @Getter @Value("${crypto.ltc.pp}") private String pp;
    @Getter @Value("${crypto.ltc.initialblock}") private long initialblock;
    @Getter @Value("${crypto.ltc.minconfirm}") private long minconfirm;
    @Getter @Value("${crypto.ltc.minamtgather}") private double minamtgather;
    
}
