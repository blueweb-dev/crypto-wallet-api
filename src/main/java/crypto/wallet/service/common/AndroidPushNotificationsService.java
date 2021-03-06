package crypto.wallet.service.common;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import crypto.wallet.common.util.HeaderRequestInterceptor;


@Service
public class AndroidPushNotificationsService {
	
	private static final String FIREBASE_SERVER_KEY 
			= "AAAAaPFCUJ4:APA91bGRFi4qsLEnMwqEo3h1cfJF5HmHENsGrPG0OjWPyTjqz-CesF2lWTiZT_cAlKwZcGMkjL5Qy_IWKo5vShkV3OgS4VXG-lOqr1NZtVR-hz1I8-aDJF3fXUFUE_V-jDAqIDJpoc-B";
	private static final String FIREBASE_API_URL    = "https://fcm.googleapis.com/fcm/send";
	
	@Async
	public CompletableFuture<String> send(HttpEntity<String> entity) {
 
		RestTemplate restTemplate = new RestTemplate();
		/** 
		 * https://fcm.googleapis.com/fcm/send
		 * Content-Type:application/json
		 * Authorization:key=FIREBASE_SERVER_KEY
		 */
		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
		interceptors.add(new HeaderRequestInterceptor("Content-Type",  "application/json;charset=UTF-8"));
		restTemplate.setInterceptors(interceptors);
		String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
 
		return CompletableFuture.completedFuture(firebaseResponse);
	}
}