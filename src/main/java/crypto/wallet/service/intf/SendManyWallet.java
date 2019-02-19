package crypto.wallet.service.intf;

import java.util.List;

import crypto.wallet.data.domain.TbSend;

public interface SendManyWallet {

	int sendMany(List<TbSend> data);
	
}