package at.jku.smartshopper.backend;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import at.jku.smartshopper.objects.User;

public class RemoteUserService implements IUserService {


	@Override
	public User getUser(String username, String password) {
		try {
			
			DisableSSLCertificateCheckUtil.disableChecks();
			
			//create instance of request provider which helps with some convencience methods
			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader(username, password);

			RestTemplate restTemplate = provider.getRestTemplate();
			//create map which contains url data
			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("username", username);

			//make rest request
			ResponseEntity<User> userEntity = restTemplate.getForEntity(
					RequestProvider.GET_USER_URL, User.class, urlVariables);

			//map request data to business objects
			User user = new User();
			user = userEntity.getBody();
			return user;
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RestClientException e) {
			//return null shows "wrong login data dialog"
			return null;
		}
		return null;

	}

}
