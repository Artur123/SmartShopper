package at.jku.smartshopper.client;

import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.content.Intent;

public class About extends Activity {

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
	}


	/**
	 *  chooses the action that happens with each menuitem
	 */
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
            //start the Parentactivity (Basketoverview) 
            startActivity(parentActivityIntent);
            finish();
            return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
