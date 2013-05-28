package at.jku.smartshopper.objects;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import at.jku.smartshopper.client.BasketOverview;

public class UserInstance {

	private static User user = null;

	public static void setUser(User u) {
		user = u;
	}

	public static User getInstance() {
		if (user != null) {
			return user;
		}else{
			// show login
//			Activity act = (Activity)c;
//			final Intent intent = new Intent(c,
//					at.jku.smartshopper.client.Login.class);
//			act.finish();
//			c.startActivity(intent);
			return null;
		}
	}
}
