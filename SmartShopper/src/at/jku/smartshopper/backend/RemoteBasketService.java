package at.jku.smartshopper.backend;

import java.math.BigInteger;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import android.util.Log;
import at.jku.smartshopper.objects.Basket;
import at.jku.smartshopper.objects.BasketList;
import at.jku.smartshopper.objects.BasketRow;
import at.jku.smartshopper.objects.User;
import at.jku.smartshopper.objects.UserInstance;

public class RemoteBasketService implements IBasketService {
	protected static final String TAG = RemoteBasketService.class
			.getSimpleName();

	@Override
	public void putBasket(Basket basket) {
		try {
			DisableSSLCertificateCheckUtil.disableChecks();

			//create instance of request provider which helps with some convencience methods
			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();
			//make rest request

			HttpEntity<Basket> requestEntity = new HttpEntity<Basket>(basket,
					header);
					
			RestTemplate restTemplate = provider.getRestTemplate();
			
			//create map which contains url data

			Map<String, Object> urlVariables = new HashMap<String, Object>();

			urlVariables.put("username", UserInstance.getInstance().getUsername());
			urlVariables.put("timestamp", new Date().getTime());
			
			restTemplate.put(RequestProvider.PUT_BASKET_URL, requestEntity, urlVariables);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

	}

	@Override
	public BasketList getAllBaskets() {
		try {
			DisableSSLCertificateCheckUtil.disableChecks();
			//create instance of request provider which helps with some convencience methods

			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();

			RestTemplate restTemplate = provider.getRestTemplate();
			//create map which contains url data

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("username", UserInstance.getInstance().getUsername());
			//make rest request

			ResponseEntity<BasketList> basketListEntity = restTemplate
					.getForEntity(RequestProvider.GET_ALL_BASKETS_URL,
							BasketList.class, urlVariables);
			
			//map request data to business objects
			BasketList baskets = basketListEntity.getBody();
			return baskets;
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Basket getLatestBasket() {
		try {
			DisableSSLCertificateCheckUtil.disableChecks();
			//create instance of request provider which helps with some convencience methods

			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();

			RestTemplate restTemplate = provider.getRestTemplate();
			//create map which contains url data

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("username", UserInstance.getInstance().getUsername());

			ResponseEntity<Basket> basketEntity = restTemplate.getForEntity(RequestProvider.GET_LATEST_BASKET_URL,
					Basket.class, urlVariables);
			//map request data to business objects

			Basket basket;
			basket = basketEntity.getBody();
			return basket;
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

	@Override
	public Basket getBasketById(long timeStamp) {
		// TODO Auto-generated method stub
		// not implemented
		return null;
	}

}
