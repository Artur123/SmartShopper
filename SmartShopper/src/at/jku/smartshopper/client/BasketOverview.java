package at.jku.smartshopper.client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import at.jku.smartshopper.backend.IArticleService;
import at.jku.smartshopper.backend.IBasketService;
import at.jku.smartshopper.backend.RemoteArticleService;
import at.jku.smartshopper.backend.RemoteBasketService;
import at.jku.smartshopper.listitems.ArticleListAdapter;
import at.jku.smartshopper.objects.Article;
import at.jku.smartshopper.objects.Basket;
import at.jku.smartshopper.objects.BasketRow;
import at.jku.smartshopper.objects.UserInstance;
import at.jku.smartshopper.scanner.IntentIntegrator;
import at.jku.smartshopper.scanner.IntentResult;

public class BasketOverview extends Activity {

	List<BasketRow> meineliste;
	ArticleListAdapter adapter;
	Button btnScanArt;
	Button btnCheckout;
	TextView txtTotalAmount;
	private ProgressDialog progressDialog;
	private boolean destroyed = false;

	//private String username, password;

	//set true when user clicked OK on checkout dialog
	private boolean checkoutDialogResult = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basket_overview);

		// this.getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
		// Intent.FLAG_ACTIVITY_NEW_TASK);

		meineliste = new ArrayList<BasketRow>();		
		setup();
		btnScanArt = (Button) findViewById(R.id.btnScanArticle);
		btnScanArt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				scanArticle();
			}
		});

		btnCheckout = (Button) findViewById(R.id.btnCheckout);
		btnCheckout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO: move PerformCheckoutTask to checkout method
				//scanQRCode();
				PerformCheckoutTask performCheckoutTask = new PerformCheckoutTask();
				performCheckoutTask.execute();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basket_overview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_enter_barcode:
			enterBarcode();
			return true;
		case R.id.menu_stats:
			showStatistics();
			return true;
		case R.id.menu_about:
			showAbout();
			return true;
		case R.id.menu_close:
			closeApp();
			return true;
		case R.id.menu_logout:
			showLogoutDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

	private void showStatistics() {
		final Intent intent = new Intent(this,
				at.jku.smartshopper.client.ShowStatistics.class);

		startActivity(intent);

	}

	private void showAbout() {
		final Intent intent = new Intent(this,
				at.jku.smartshopper.client.About.class);

		startActivity(intent);
	}

	public void setup() {
		adapter = new ArticleListAdapter(this, R.layout.article_list_item,
				meineliste);
		ListView articleListview = (ListView) findViewById(R.id.Basket_articleList);
		articleListview.setAdapter(adapter);

		BasketRow testarticle = new BasketRow("90311185", "R�merquelle Prickelnd ", BigInteger.ONE,
				1.49);
		adapter.add(testarticle);
//		adapter.add(new BasketRow("1", "Merci Tafel Nugat", BigInteger.ONE,
//				15.0));
//		adapter.insert(new BasketRow("2", "Kinder Pinguin", BigInteger.ONE,
//				30.99), 0);
//		adapter.insert(new BasketRow("3", "V�slauer Mineralwasser",
//				BigInteger.ONE, 40.0), 0);
		updateTotal();
	};

	public void removeArticleHandler(View v) {
		BasketRow itemToRemove = (BasketRow) v.getTag();
		adapter.remove(itemToRemove);
		updateTotal();
	}

	public void decreaseAmount(View v) {
		BasketRow item = (BasketRow) v.getTag();
		int pos = adapter.getPosition(item);

		adapter.remove(item);
		BigInteger wert = item.getQuantity().subtract(BigInteger.ONE);
		if (wert.compareTo(BigInteger.ZERO) <= 0) {
			// do_nothing
		} else {
			item.setQuantity(wert);
			adapter.insert(item, pos);
		}
		updateTotal();
	}

	public void increaseAmount(View v) {
		BasketRow item = (BasketRow) v.getTag();
		int pos = adapter.getPosition(item);
		adapter.remove(item);
		item.setQuantity(item.getQuantity().add(BigInteger.ONE));
		adapter.insert(item, pos);
		updateTotal();
	}

	public void scanArticle() {
		IntentIntegrator integrator = new IntentIntegrator(BasketOverview.this);
		integrator.initiateScan();
	}

	/**
	 * Shows checkout dialog and starts qr code scanner for supermarket id
	 */
	public void scanQRCode() {
		AlertDialog.Builder checkoutDialog = new AlertDialog.Builder(this);

		checkoutDialog.setTitle("Got everything?");

		checkoutDialog.setPositiveButton("Checkout!",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// OK Button
						// set QR Result and start scan
						checkoutDialogResult = true;
						IntentIntegrator integrator = new IntentIntegrator(
								BasketOverview.this);
						// integrator.setMessage("Place the QR-code inside the viewfinder rectangle to checkout.");
						integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
					}
				});

		checkoutDialog.setNegativeButton("Continue Shopping",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// set dialog result and dismiss
						dialog.dismiss();
						checkoutDialogResult = false;
					}
				});
		checkoutDialog.show();
	}

	/**
	 * gets the scan results and adds article to basket or continues to checkout
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult.getContents() != null) {
			// handle scan result
			String code = scanResult.getContents();
			if ((scanResult.getFormatName().equals("QR_CODE"))
					&& checkoutDialogResult) {
				//TODO: check if valid supermarket id
				checkout();
			} else {
				fetchArticle(code);
			}
		} else {
			checkoutDialogResult = false;
		}
	}

	public void checkout() {
		// TODO: start asynchronous task
		Toast.makeText(this, txtTotalAmount.getText(), Toast.LENGTH_SHORT)
				.show();
		
		//PerformCheckoutTask performCheckoutTask = new PerformCheckoutTask();
		//performCheckoutTask.execute();
	}

	/**
	 * Starts task to get article from database
	 * @param barcode
	 */
	public void fetchArticle(String barcode) {
		// TODO: check if Article is valid (ask server) and add Article
		if (barcode == null) {
			showDialog("Exception", "Article not found.");
		} else {
			
			
			PerformGetArticleTask performGetArticleTask = new PerformGetArticleTask(barcode);
			performGetArticleTask.execute();
		}
	}
	
	/**
	 * Adds article to basket
	 * @param article
	 */
	public void addArticle(Article article) {
		BasketRow newArticle = new BasketRow(article.getBarcode(), article.getName(), BigInteger.ONE, article.getPrice());
		adapter.add(newArticle);
		updateTotal();
	}

	/**
	 * Displays an alertDialog with one 'OK' Button
	 * 
	 * @param title
	 * @param message
	 */
	private void showDialog(String title, String message) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setCancelable(true);
		alertDialog.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alertDialog.show();
	}

	private void enterBarcode() {
		AlertDialog.Builder enterBarcode = new AlertDialog.Builder(this);

		enterBarcode.setTitle("Enter Barcode:");

		final EditText input = new EditText(BasketOverview.this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		enterBarcode.setView(input);

		enterBarcode.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// OK Button
						fetchArticle(input.getText().toString());
					}
				});

		enterBarcode.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		enterBarcode
				.show()
				.getWindow()
				.setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	public void updateTotal() {
		double sum = 0;
		for (int i = 0; i < adapter.getCount(); i++) {
			sum = sum + adapter.getItem(i).getPrice().doubleValue()
					* adapter.getItem(i).getQuantity().doubleValue();
		}
		sum = sum * 100;
		sum = Math.round(sum);
		sum = sum / 100;

		txtTotalAmount = (TextView) findViewById(R.id.txtTotalAmount);
		txtTotalAmount.setText(sum + " Total");

	}

	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	/**
	 * Asks the user and then closes the application
	 */
	private void closeApp() {
		AlertDialog.Builder closeDialog = new AlertDialog.Builder(this);

		closeDialog.setTitle("Close smartshopper?");

		closeDialog.setNegativeButton("Close",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});

		closeDialog.setPositiveButton("Continue Shopping",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		closeDialog.show();
	}

	private void showLogoutDialog(){
		AlertDialog.Builder logoutDialog = new AlertDialog.Builder(this);

		logoutDialog.setTitle("Logout and discard basket?");
		
		logoutDialog.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {				
				logout();
			}
		});
		
		logoutDialog.setPositiveButton("Continue Shopping", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		logoutDialog.show();
	}
	
	/**
	 * returns to the login screen(=logout)
	 */
	private void logout(){
		final Intent intent = new Intent(this,
				at.jku.smartshopper.client.Login.class);
		finish();
		startActivity(intent);
	}

	private class PerformCheckoutTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			BasketOverview.this.showProgressDialog("Performing checkout...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			//TODO: replace "smartshopper", add shopID from scan
			// send meineListe
			Basket basket = new Basket();
			basket.setShopId(0L);
			basket.setUserId("smartshopper");
			basket.getRows().addAll(meineliste);
			IBasketService service = new RemoteBasketService();

			service.putBasket(basket);

			return null;
		}

		@Override
		protected void onPostExecute(Void response) {
			BasketOverview.this.dismissProgressDialog();
		}

	}
	
	private class PerformGetArticleTask extends AsyncTask<Void, Void, Void> {
		String barcode;
		Article article;
		
		public PerformGetArticleTask(String barcode)
		{
			this.barcode = barcode;
		}
		
		@Override
		protected void onPreExecute() {
			BasketOverview.this.showProgressDialog("gettingArticle...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			IArticleService service = new RemoteArticleService();
			article = service.getArticleById(barcode);
			return null;
		}

		@Override
		protected void onPostExecute(Void response) {
			BasketOverview.this.dismissProgressDialog();
			//TODO: check if exceptions?
			if (article != null) {
				addArticle(article); 
			}else{
				showDialog("Article not found", "Please try again.");
			}
		}
		
	}
}
