package at.jku.smartshopper.objects;

import android.content.Context;
import android.content.Intent;

public class UserInstance {
	
	private static User user = null;
	
	public static void setUser(User u){
		user = u;
	}
	
	public static User getInstance(Context c){
		if(user == null){
			//show login
			final Intent intent = new Intent(c,
					at.jku.smartshopper.client.Login.class);
			c.startActivity(intent);
			return null;
		}else
			return user;
	}
}
