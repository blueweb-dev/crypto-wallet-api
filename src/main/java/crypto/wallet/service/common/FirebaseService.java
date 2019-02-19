package crypto.wallet.service.common;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;

import crypto.wallet.data.gson.common.FCMSendRes;
import lombok.extern.slf4j.Slf4j;

@Slf4j @Service("firebaseService") 
public class FirebaseService {
	
	private static FirebaseApp fapp;
    @Autowired AndroidPushNotificationsService aService;
    
	@PostConstruct
	public void init() {
		// 1) FireStore: 
		try {
			Resource resource = new ClassPathResource("serviceAccount.json");
			FirebaseOptions options = new FirebaseOptions.Builder()
					  .setCredentials(GoogleCredentials.fromStream(resource.getInputStream()))
					  .setDatabaseUrl("https://cellpinda-native-mobile.firebaseio.com")
					  .build();
			fapp = FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FirebaseApp getFirebaseApp() {
		return fapp;
	}

	public Firestore getFirestore() {
		return FirestoreClient.getFirestore(fapp);
	}
	
	public DocumentSnapshot getDocument(String address) throws InterruptedException, ExecutionException {
		return getFirestore().document("device-token/" + address).get().get();
	}
	
	public String getToken(String address) {
		try {
			DocumentSnapshot doc = getFirestore().document("device-token/" + address).get().get();
			if (doc.exists()) {
				return doc.getString("token");
	    	} else {
	    		return null;
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean sendFcmMessage(String token, String title, String contents) {
		JSONObject body = new JSONObject();
		JSONObject notification = new JSONObject();
		body.put("to", token);
		try {
			notification.put("title", title);
			notification.put("body",  contents);
			body.put("data", notification);
			HttpEntity<String> entity = new HttpEntity<>(body.toString());
			CompletableFuture<String> push = aService.send(entity);
			CompletableFuture.allOf(push).join();
			String resStr = push.get();
			log.debug("firebase res " + resStr);
			FCMSendRes res = new Gson().fromJson(resStr, FCMSendRes.class);
			// {"multicast_id":4931787219601636292,"success":1,"failure":0,"canonical_ids":0
			// 		,"results":[{"message_id":"0:1542338602006936%c6a3590bc6a3590b"}]}
			log.debug("firebase res " + res);
			if (res.getSuccess()==1) {
				return true;
			} else { 
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
