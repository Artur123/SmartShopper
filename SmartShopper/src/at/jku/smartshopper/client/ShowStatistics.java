package at.jku.smartshopper.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.jku.smartshopper.backend.IBasketService;
import at.jku.smartshopper.backend.RemoteBasketService;
import at.jku.smartshopper.objects.Basket;
import at.jku.smartshopper.objects.BasketRow;
import at.jku.smartshopper.objects.UserInstance;

public class ShowStatistics extends Activity {

	Button btnLatestBasket;
	EditText txtBasket;

	private ProgressDialog progressDialog;
	private boolean destroyed = false;

	private Basket latestBasket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_stats);

		// getActionBar().setDisplayHomeAsUpEnabled(true);

		txtBasket = (EditText) findViewById(R.id.txtBasket);

		btnLatestBasket = (Button) findViewById(R.id.btnGetLatestBasket);
		btnLatestBasket.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PerformGetBasketTask performBasketTask = new PerformGetBasketTask();
				performBasketTask.execute();
			}
		});

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

	private void backToBasket() {
		Intent parentActivityIntent = new Intent(this, BasketOverview.class);
		parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(parentActivityIntent);
		finish();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

	}

	/**
	 * shows content of latest basket in text box
	 */
	private void showLatestBasket() {
		double total = 0;

		String content = "";
		for (BasketRow b : latestBasket.getRows()) {
			total += b.getPrice() * b.getQuantity().doubleValue();
			content += b.getQuantity().toString() + "x " + b.getName()
					+ "; " + b.getPrice() + '\n';
		}
		content += "---------\nTotal: " + total;
		txtBasket.setText(content);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.destroyed = true;
	}

	public void showProgressDialog(CharSequence message) {
		if (this.progressDialog == null) {
			this.progressDialog = new ProgressDialog(this);
			this.progressDialog.setIndeterminate(true);
		}

		this.progressDialog.setMessage(message);
		this.progressDialog.show();
	}

	public void dismissProgressDialog() {
		if (this.progressDialog != null && !this.destroyed) {
			this.progressDialog.dismiss();
		}
	}

	private class PerformGetBasketTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			ShowStatistics.this.showProgressDialog("Getting latest basket...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			IBasketService service = new RemoteBasketService();

			// TODO: remove Basket parameter?
			latestBasket = service.getLatestBasket();

			return null;
		}

		@Override
		protected void onPostExecute(Void response) {
			ShowStatistics.this.dismissProgressDialog();
			// TODO: check if exceptions?
			if (latestBasket != null)
				showLatestBasket();
		}

	}

}
