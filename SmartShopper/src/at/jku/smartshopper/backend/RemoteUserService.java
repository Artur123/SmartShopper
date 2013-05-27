package at.jku.smartshopper.backend;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import at.jku.smartshopper.objects.User;

public class RemoteUserService implements IUserService {

	@Override
	public User getUser(String username, String password) {
		// TODO Auto-generated method stub
		// User u = new User();
		// u.setUsername(username);
		// u.setPassword(password);
		// return u;
		User user = new User();
		try {
			DisableSSLCertificateCheckUtil.disableChecks();

			RequestProvider provider = new RequestProvider();
			HttpHeaders header = provider.getHttpHeader();
			HttpEntity<String> requestEntity = new HttpEntity<String>(username,
					header);

			RestTemplate restTemplate = provider.getRestTemplate();

			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("username", username);

			// restTemplate.get(RequestProvider.GET_USER_URL, requestEntity,
			// urlVariables);
			ResponseEntity<User> userEntity = restTemplate.getForEntity(
					RequestProvider.GET_USER_URL, User.class, urlVariables);

			user.setSortCode(userEntity.getBody().getSortCode());

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;

	}

}
