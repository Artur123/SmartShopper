package at.jku.smartshopper.client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class About extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		// Show the Up button in the action bar.
//		setupActionBar();
	}

//	/**
//	 * Set up the {@link android.app.ActionBar}, if the API is available.
//	 */
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//	private void setupActionBar() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			getActionBar().setDisplayHomeAsUpEnabled(true);
//		}
//	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This is called when the Home (Up) button is pressed
            // in the Action Bar.
            Intent parentActivityIntent = new Intent(this, BasketOverview.class);
            parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    //Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    //Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(parentActivityIntent);
            finish();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
