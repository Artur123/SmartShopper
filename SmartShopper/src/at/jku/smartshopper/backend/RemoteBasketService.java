package at.jku.smartshopper.backend;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.util.Log;
import at.jku.smartshopper.objects.Basket;
import at.jku.smartshopper.objects.BasketList;

public class RemoteBasketService implements IBasketService {
	protected static final String TAG = RemoteBasketService.class.getSimpleName();

	@Override
	//TODO: remove username from putBasket method as parameter
	public void putBasket(Basket basket, String username) {
		try {
			DisableSSLCertificateCheckUtil.disableChecks();
			
			
			
			
			
			
			
			
			//TODO make method that creates request header that always includes authHeader using user credentials
			// Set the username and password for creating a Basic Auth request
			HttpAuthentication authHeader = new HttpBasicAuthentication("smartshopper", "smartshopper");
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAuthorization(authHeader);
			HttpEntity<Basket> requestEntity = new HttpEntity<Basket>(basket, requestHeaders);
			
			//TODO: make class/method that holds/creates server URL
			// The URL for making the GET request
//			final String url = "https://192.168.30.224:50443/smartshopper.backend/basket/smartshopper/basket/1366223833000/";
			final String url = "https://192.168.178.20:50443/smartshopper.backend/basket/{username}/basket/{timestamp}/";

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();

			// Set a custom MappingJacksonHttpMessageConverter that supports the
			// text/javascript media type
			MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
			messageConverter.setSupportedMediaTypes(Collections
					.singletonList(new MediaType("application", "json")));
			restTemplate.getMessageConverters().add(messageConverter);
			
			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("username", username);
			urlVariables.put("timestamp", new Date().getTime());
			
//			restTemplate.put(url, requestEntity);
			restTemplate.put(url, requestEntity, urlVariables);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

	}

	@Override
	public BasketList getAllBaskets(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Basket getLatestBasket(Basket basket, String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Basket getBasketById(long timeStamp, String username) {
		// TODO Auto-generated method stub
		return null;
	}

}
