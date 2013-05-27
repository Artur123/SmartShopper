package at.jku.smartshopper.backend;

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
//		User u = new User();
//		u.setUsername(username);
//		u.setPassword(password);
//		return u;
		
		
		RequestProvider provider = new RequestProvider();
		HttpHeaders header = provider.getHttpHeader();
		HttpEntity<String> requestEntity = new HttpEntity<String>(username, header);
		
		RestTemplate restTemplate = provider.getRestTemplate();
		
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		urlVariables.put("username", username);
		
//		restTemplate.get(RequestProvider.GET_USER_URL, requestEntity, urlVariables);
		ResponseEntity<User> userEntity = restTemplate.getForEntity(RequestProvider.GET_USER_URL, User.class, urlVariables);
		
		
		User user = new User();
		user.setSortCode(userEntity.getBody().getSortCode());
		
		return user;
		
	}

}
