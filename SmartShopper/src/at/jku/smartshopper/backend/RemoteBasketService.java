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
	// TODO: remove username from putBasket method as parameter
	public void putBasket(Basket basket) {
		try {
			DisableSSLCertificateCheckUtil.disableChecks();

			// TODO make method that creates request header that always includes
			// authHeader using user credentials
			// Set the username and password for creating a Basic Auth request
			/*HttpAuthentication authHeader = new HttpBasicAuthentication(
					"smartshopper", "smartshopper");
			HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAuthorization(authHeader);*/
			
			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();
			
			HttpEntity<Basket> requestEntity = new HttpEntity<Basket>(basket,
					header);
			
			// TODO: make class/method thaat holds/creates server URL
			// The URL for making the GET request
			// final String url =
			// "https://192.168.30.224:50443/smartshopper.backend/basket/smartshopper/basket/1366223833000/";
			//final String url = "https://192.168.178.20:50443/smartshopper.backend/basket/{username}/basket/{timestamp}/";

			/*// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();

			// Set a custom MappingJacksonHttpMessageConverter that supports the
			// text/javascript media type
			MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
			messageConverter.setSupportedMediaTypes(Collections
					.singletonList(new MediaType("application", "json")));
			restTemplate.getMessageConverters().add(messageConverter);
			 */
			RestTemplate restTemplate = provider.getRestTemplate();
			
			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("username", UserInstance.getInstance().getUsername());
			urlVariables.put("timestamp", new Date().getTime());
			

			// restTemplate.put(url, requestEntity);
			restTemplate.put(RequestProvider.PUT_BASKET_URL, requestEntity, urlVariables);
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

	}

	@Override
	public BasketList getAllBaskets() {
		try {
			DisableSSLCertificateCheckUtil.disableChecks();

			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();

			// HttpEntity<String> requestEntity = new
			// HttpEntity<String>(username, header);

			RestTemplate restTemplate = provider.getRestTemplate();

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("username", UserInstance.getInstance().getUsername());

			ResponseEntity<BasketList> basketListEntity = restTemplate
					.getForEntity(RequestProvider.GET_ALL_BASKETS_URL,
							BasketList.class, urlVariables);

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
		// TODO Auto-generated method stub
//		Basket b = new Basket();
//		BasketRow br = new BasketRow("123456789", "R�merquelle 0,5l", new BigInteger("3"), 10.99);
//		b.getRows().add(br);
//		b.getRows().add(br);
//		b.getRows().add(br);
//		b.getRows().add(br);
//		b.getRows().add(br);
//		return b;
		try {
			DisableSSLCertificateCheckUtil.disableChecks();

			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();

			// HttpEntity<String> requestEntity = new
			// HttpEntity<String>(username, header);

			RestTemplate restTemplate = provider.getRestTemplate();

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("username", UserInstance.getInstance().getUsername());

			ResponseEntity<Basket> basketEntity = restTemplate
					.getForEntity(RequestProvider.GET_LATEST_BASKET_URL,
							Basket.class, urlVariables);

			Basket basket = new Basket();
			basket = basketEntity.getBody();
//			basket.setShopId(basketEntity.getBody().getShopId());
//			basket.setUserId(basketEntity.getBody().getUserId());
//			
//			for(BasketRow b : basketEntity.getBody().getRows()){
//				basket.getRows().add(new BasketRow(b.getBarcode(), b.getArticleName(), b.getQuantity(), b.getPrice()));
//			}
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
		return null;
	}

}
