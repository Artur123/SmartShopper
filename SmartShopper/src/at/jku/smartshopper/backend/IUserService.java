package at.jku.smartshopper.backend;

import at.jku.smartshopper.objects.Article;

public interface IUserService {
	
	public Article getUser(String username, String password);

}
