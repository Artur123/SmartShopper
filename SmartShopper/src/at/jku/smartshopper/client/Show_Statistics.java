package at.jku.smartshopper.client;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class Show_Statistics extends Activity{

	Button btnClose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_stats);
		
		//getActionBar().setDisplayHomeAsUpEnabled(true);

		
//		btnClose = (Button)findViewById(R.id.btnCloseStats);
//		btnClose.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				onBackPressed();
//			}
//		});
	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			backToBasket();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void backToBasket(){
		Intent parentActivityIntent = new Intent(this, Basket_Overview.class);
        parentActivityIntent.addFlags(
                Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
        startActivity(parentActivityIntent);
        finish();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

	}
}
