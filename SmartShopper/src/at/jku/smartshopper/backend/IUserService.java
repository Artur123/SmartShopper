package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.User;

public interface IUserService {
	
	public User getUser(String username);

}
