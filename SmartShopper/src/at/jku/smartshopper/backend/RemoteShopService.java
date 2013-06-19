package at.jku.smartshopper.backend;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import at.jku.smartshopper.objects.Shop;

public class RemoteShopService implements IShopService{

	@Override
	public Shop getShop(Long shopId) {
		try {
			
			DisableSSLCertificateCheckUtil.disableChecks();
			
			//create instance of request provider which helps with some convencience methods
			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();

			RestTemplate restTemplate = provider.getRestTemplate();
			//create map which contains url data

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("shopId", shopId);
			//make rest request

			ResponseEntity<Shop> shopEntity = restTemplate.getForEntity(
					RequestProvider.GET_SHOP_URL, Shop.class, urlVariables);
			//map request data to business objects
			Shop shop = shopEntity.getBody();
			return shop;
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RestClientException e) {
			//return null shows "something went wrong"
			return null;
		}
		return null;
	}

}
