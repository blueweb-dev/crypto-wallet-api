package crypto.wallet.coin;

import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.common.util.WalletUtil;
import crypto.wallet.service.common.AndroidPushNotificationsService;
import crypto.wallet.service.common.FirebaseService;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 */
@Slf4j
@TestPropertySource(properties = "scheduling.enabled=false")
@SpringBootTest @RunWith(SpringRunner.class)
public class FCMTest implements WalletConst {
  
    private final String symbol = SYMBOL_ADA;
    private final String TAG = "[TEST][" + symbol + "] ";
    
    @Autowired private FirebaseService fsvc;
    
	@Test public void test() {
		
		boolean success = false;
		// 1) Get Token
    	String token = fsvc.getToken("0x1641c455868064aa0fdc03f916abdb14688da6eb");
    	if (token==null) { assertTrue(success); }
    	
		//2) FCM: 메시지 전송
    	success = fsvc.sendFcmMessage(token, "purchase", "W" + "|" 
    			+ WalletUtil.toCurrencyFormat8Int(200D)
    			+ "|" + WalletUtil.toCurrencyFormat8(10D) );
    	
		assertTrue(success);
	}


}
