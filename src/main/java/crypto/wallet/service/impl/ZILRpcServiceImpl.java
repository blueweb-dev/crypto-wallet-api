package crypto.wallet.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import crypto.wallet.service.ERC20AbstractService;
import lombok.Getter;

/**
 * CPD service
 * @author sungjoon.kim
 */
@Service("zilRpcService") class ZILRpcServiceImpl extends ERC20AbstractService {

	@Getter private final String symbol = SYMBOL_ZIL;
	@Getter @Value("${crypto.zil.minamtgather}") private double minamtgather;
	@Getter @Value("${crypto.zil.contractaddr}") private String contractaddr;
	@Getter @Value("${crypto.zil.decimals}") private int decimals;
	
}
