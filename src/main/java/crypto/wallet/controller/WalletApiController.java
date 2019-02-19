package crypto.wallet.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.domain.req.BonusRequest;
import crypto.wallet.common.domain.req.FiatDepositRequest;
import crypto.wallet.common.domain.req.ManualPayRequest;
import crypto.wallet.common.domain.req.PersonalInfoRequest;
import crypto.wallet.common.domain.req.PointExchangeRequest;
import crypto.wallet.common.domain.req.SendRequest;
import crypto.wallet.common.domain.req.TokenBuyRequest;
import crypto.wallet.common.domain.res.BonusResponse;
import crypto.wallet.common.domain.res.CryptoBalancesResponse;
import crypto.wallet.common.domain.res.CryptoIndexResponse;
import crypto.wallet.common.domain.res.FiatDepositResponse;
import crypto.wallet.common.domain.res.ManualPayResponse;
import crypto.wallet.common.domain.res.NewAddressResponse;
import crypto.wallet.common.domain.res.PointExchangeResponse;
import crypto.wallet.common.domain.res.PrivateSaleTXResponse;
import crypto.wallet.common.domain.res.PurchaseResponse;
import crypto.wallet.common.domain.res.SendResponse;
import crypto.wallet.common.domain.res.SystemStatusResponse;
import crypto.wallet.common.domain.res.TokenBuyResponse;
import crypto.wallet.common.domain.res.TotalBalancesResponse;
import crypto.wallet.common.domain.res.TransactionResponse;
import crypto.wallet.common.domain.res.TxHistoryResponse;
import crypto.wallet.common.domain.res.ValidateAddressResponse;
import crypto.wallet.service.CryptoSharedService;
import crypto.wallet.service.common.CoinFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;


@Slf4j @Controller @RequestMapping(value="/api/v1")
public class WalletApiController implements WalletConst {

    @Getter @Value("${app.transfer-pp}") private String trasferPp;
    @Autowired private CoinFactory coinFactory;
    
    
    /**
     * 새 지갑 주소 생성
     */
    @RequestMapping(value="/address", method= RequestMethod.POST) @ResponseBody 
    public NewAddressResponse newaddress(@RequestBody PersonalInfoRequest param) {
        
        if (param.getSymbol()==null || param.getSymbol().length()>10 
                  || param.getUid()<0) {
            NewAddressResponse ret = new NewAddressResponse();
            ret.setCode(CODE_FAIL_PARAM);
            return ret;
        } else {
        	param.setBrokerId("KR00");
            CryptoSharedService service = coinFactory.getService(param.getSymbol());
            return service.newAddress(param);
        }
    }
    
    /**
     * 지갑주소 유효성 검증
     */
    @RequestMapping(value="/validateaddress", method= RequestMethod.POST) @ResponseBody 
    public ValidateAddressResponse validateaddress(@RequestBody PersonalInfoRequest param) {
    	
        if (param.getSymbol()==null || param.getAddress()==null) {
        	// 파라미터 오류
            ValidateAddressResponse ret = new ValidateAddressResponse();
            ret.setCode(CODE_FAIL_PARAM);
            return ret;
        } else if (!param.getAddress().equals(param.getAddress().trim())) {
        	// 공백이 있는 경우
    		ValidateAddressResponse ret = new ValidateAddressResponse(param);
    		ret.getResult().setValid(false);
    		return ret;
    	} else {
    		// 정상
            CryptoSharedService service = coinFactory.getService(param.getSymbol());
            return service.validateAddress(param);
    	}
    }
    
    /**
     * 출금요청
     */
    @RequestMapping(value="/send", method= RequestMethod.POST) @ResponseBody 
    public SendResponse send(@RequestBody SendRequest param) {

    	// trasferPp
    	if (!getTrasferPp().equals(param.getPp())) {
    		SendResponse res = new SendResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
    	} else if (param.getSymbol()==null || param.getOrderId()==null || param.getUid()==0 
        		|| param.getToAddress()==null || param.getAmount()<=0) {
            SendResponse res = new SendResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
        } else {
            // 앞뒤 공백주소 방어로직
            param.setToAddress(param.getToAddress().trim());
            if ("".equals(param.getToTag())) { param.setToTag(null); }
            CryptoSharedService service = coinFactory.getService(param.getSymbol());
            return service.requestSendTransaction(param);
        }
    }
    
    /**
     * 출금요청(내부용)
     */
    @RequestMapping(value="/sendfrom", method= RequestMethod.POST) @ResponseBody 
    public SendResponse sendfrom(@RequestBody SendRequest param) {
    	if (!getTrasferPp().equals(param.getPp())) {
    		SendResponse res = new SendResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
    	} else if (param.getSymbol()==null || param.getOrderId()==null || param.getToAddress()==null
            || (param.getFromAddress()==null && param.getFromAccount()==null) || param.getAmount()<=0 
            || param.getBrokerId()==null || param.getBrokerId().length()!=4) {
            SendResponse res = new SendResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
        } else {
            // 앞뒤 공백주소 방어로직
            if (param.getFromAccount()!=null && param.getFromAccount().length()>0) {
                param.setFromAccount(param.getFromAccount().trim());  
            }
            if (param.getFromAddress()!=null && param.getFromAddress().length()>0) {
                param.setFromAddress(param.getFromAddress().trim());  
            }
            if (param.getFromTag()!=null && param.getFromTag().length()>0) {
                param.setFromTag(param.getFromTag().trim());  
            }
            param.setToAddress(param.getToAddress().trim());
            if ("".equals(param.getToTag())) { param.setToTag(null); }
            CryptoSharedService service = coinFactory.getService(param.getSymbol());
            return service.requestSendTransaction(param);
        }
    }
    
    /**
     * 사용자용) 토큰, 암호화폐, 원화, 잔고 조회
     */
    @RequestMapping(value="/balance", method= RequestMethod.POST) @ResponseBody 
    public CryptoBalancesResponse balance(@RequestBody PersonalInfoRequest param) {
        
        if (param.getUid()!=0) {
        	// UID로 조회할 때
//        	CryptoBalancesResponse res = tokenSaleService.getBalancesByUid(
//        			param.getUid());
//        	return res;
            CryptoBalancesResponse res = new CryptoBalancesResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
        } else {
        	CryptoBalancesResponse res = new CryptoBalancesResponse();
            res.setCode(CODE_FAIL_PARAM);
            return res;
        }
    }
    

    
}
