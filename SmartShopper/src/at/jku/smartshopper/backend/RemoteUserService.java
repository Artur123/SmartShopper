package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.User;

public class RemoteUserService implements IUserService {

	@Override
	public User getUser(String username, String password) {
		// TODO Auto-generated method stub
		User u = new User();
		u.setUsername(username);
		u.setPassword(password);
		return u;
	}

}
