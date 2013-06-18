package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.User;
/**
 * Interface to get the Userdata from Server
 *
 */
public interface IUserService {
	
	public User getUser(String username, String password);

}
