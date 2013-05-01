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
		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.about, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			// This is called when the Home (Up) button is pressed
            // in the Action Bar.
//            Intent parentActivityIntent = new Intent(this, Basket_Overview.class);
//            parentActivityIntent.addFlags(
//                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(parentActivityIntent);
            finish();
            return true;

		case R.id.menu_stats:
			NavUtils.navigateUpTo(this, new Intent(this,
					at.jku.smartshopper.client.Show_Statistics.class));
			return true;
		case R.id.basket_overview:
			finish();
//			Intent parentActivityIntent = new Intent(this, Basket_Overview.class);
//			 parentActivityIntent.addFlags( 
//                   Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                   Intent.FLAG_ACTIVITY_NEW_TASK);
//			NavUtils.navigateUpTo(this, parentActivityIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
