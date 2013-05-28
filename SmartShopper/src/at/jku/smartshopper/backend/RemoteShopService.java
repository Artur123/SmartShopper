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
//		if(shopId == 1234)
//			return new Shop();
//		else
//			return null;
		try {
			DisableSSLCertificateCheckUtil.disableChecks();
			
			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();

			// HttpEntity<String> requestEntity = new
			// HttpEntity<String>(username, header);

			RestTemplate restTemplate = provider.getRestTemplate();

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("shopId", shopId);

			ResponseEntity<Shop> shopEntity = restTemplate.getForEntity(
					RequestProvider.GET_SHOP_URL, Shop.class, urlVariables);

			Shop shop = shopEntity.getBody();
			return shop;
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RestClientException e) {
			//TODO: DISPLAY WRONG LOGIN DATA
			//return null shows "wrong login data dialog"
			return null;
		}
		return null;
	}

}
