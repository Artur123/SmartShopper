package at.jku.smartshopper.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Checkout extends Activity {

	Button btnClose;
	
	EditText txtGoodbye;

	/** Called when the activity is first created. */
	/**
	 * is loaded after checkout and simple gives the user a message
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout);
		// TODO Auto-generated method stub

		btnClose = (Button) findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		String text;
		
		txtGoodbye = (EditText)findViewById(R.id.txtGoodbye);
		text = "Thank you for using SmartShopper\n\n" + getIntent().getExtras().getString("total"); 
		txtGoodbye.setText(text);
	}

}
