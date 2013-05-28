package at.jku.smartshopper.backend;

import java.util.Collections;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RequestProvider {
	
	
	private static String PROTOCOL = "https";
	private static String IP_ADDRESS = "25.166.49.26";
	private static String PORT = "50443";
	private static String SERVICE_NAME = "smartshopper.backend";
	
	private static String SERVICE_URL = PROTOCOL + "://" + IP_ADDRESS + ":" + PORT + "/" + SERVICE_NAME;
	
	public static String PUT_BASKET_URL = SERVICE_URL + "/basket/{username}/basket/{timestamp}";
	public static String GET_ALL_BASKETS_URL = SERVICE_URL + "/basket/{username}/basket/all";
	public static String GET_LATEST_BASKET_URL = SERVICE_URL + "/basket/{username}/basket/latest";
		
	public static String GET_USER_URL = SERVICE_URL + "/user/{username}";
	
	public static String GET_ARTICLE_URL = SERVICE_URL + "/article/{barcode}";
	
	
//	public static String PUT_BASKET_URL = "https://192.168.30.224:50443/smartshopper.backend/basket/{username}/basket/{timestamp}/";
//	public static String GET_ALL_BASKET_URL = "https://192.168.30.224:50443/smartshopper.backend/basket/{username}/basket/all/";
	
	
	public HttpHeaders getHttpHeader() {
//		String username = UserInstance.getUser().getUsername();
//		String password = UserInstance.getUser().getPassword();
		String username = "smartshopper";
		String password = "smart";
		HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		return requestHeaders;
	}
	
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate();
		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		messageConverter.setSupportedMediaTypes(Collections
				.singletonList(new MediaType("application", "json")));
		restTemplate.getMessageConverters().add(messageConverter);
		return restTemplate;
	}
	
	
	

}
