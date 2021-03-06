package at.jku.smartshopper.client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import at.jku.smartshopper.backend.IArticleService;
import at.jku.smartshopper.backend.IBasketService;
import at.jku.smartshopper.backend.IShopService;
import at.jku.smartshopper.backend.RemoteArticleService;
import at.jku.smartshopper.backend.RemoteBasketService;
import at.jku.smartshopper.backend.RemoteShopService;
import at.jku.smartshopper.listitems.ArticleListAdapter;
import at.jku.smartshopper.objects.Article;
import at.jku.smartshopper.objects.Basket;
import at.jku.smartshopper.objects.BasketRow;
import at.jku.smartshopper.objects.Shop;
import at.jku.smartshopper.scanner.IntentIntegrator;
import at.jku.smartshopper.scanner.IntentResult;

public class BasketOverview extends Activity {

	//list with all articles in basket
	List<BasketRow> meineliste;
	//Adapter zum Anzeigen der Artikel in der �bersicht
	ArticleListAdapter adapter;
	//Button for scanning a new barcode
	Button btnScanArt;
	Button btnCheckout;
	//total sum from basket
	TextView txtTotalAmount;
	//Dialog -> communication with server
	private ProgressDialog progressDialog;
	private boolean destroyed = false;
	//set true when user clicked OK on checkout dialog
	private boolean checkoutDialogResult = false;
	//Data of Shop 
	private long shopID;
	private Shop shop=null;


	/***
	 * Create the Overview
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basket_overview);

		//Setup der Elemente 
		meineliste = new ArrayList<BasketRow>();
		//Setup der Artikelliste
		setup();
		btnScanArt = (Button) findViewById(R.id.btnScanArticle);
		btnScanArt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//Durchf�hren des Scans
				scanArticle();
			}
		});

		btnCheckout = (Button) findViewById(R.id.btnCheckout);
		btnCheckout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				scanQRCode();
			}
		});
	}
	
	/**
	 * Create the options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basket_overview, menu);
		return true;
	}

	/**
	 * Chooses the correct method from the selected item in menu
	 */
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
	
	/**
	 * Starts the Progressdialog (usually with Server communication) 
	 * @param message the message that is shown 
	 */
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
	/**
	 * Goes to the Statistic screen 
	 */
	private void showStatistics() {
		final Intent intent = new Intent(this,
				at.jku.smartshopper.client.ShowStatistics.class);

		startActivity(intent);

	}
	/**
	 * Go to the about-Screen
	 */
	private void showAbout() {
		final Intent intent = new Intent(this,
				at.jku.smartshopper.client.About.class);

		startActivity(intent);
	}
	/**
	 * Einf�gen des Adapters sowie aktualisieren der Gesammtsumme
	 */
	public void setup() {
		adapter = new ArticleListAdapter(this, R.layout.article_list_item,
				meineliste);
		ListView articleListview = (ListView) findViewById(R.id.Basket_articleList);
		articleListview.setAdapter(adapter);
		updateTotal();
	};

	/**
	 *  removes Article from List 
	 * @param v to get the correct Item 
	 */
	public void removeArticleHandler(View v) {
		BasketRow itemToRemove = (BasketRow) v.getTag();
		adapter.remove(itemToRemove);
		updateTotal();
	}
	/**
	 * decreases Amount of the Item 
	 * @param v to get the correct Item from the View 
	 */
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
	/**
	 * increases  Amount of the Item 
	 * @param v to get the correct Item from the View 
	 */
	public void increaseAmount(View v) {
		BasketRow item = (BasketRow) v.getTag();
		int pos = adapter.getPosition(item);
		adapter.remove(item);
		item.setQuantity(item.getQuantity().add(BigInteger.ONE));
		adapter.insert(item, pos);
		updateTotal();
	}
	
	/**
	 * to scan an Article 
	 */
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
				try{
					shopID=Long.valueOf(code);
					//check shop id
					PerformGetShopTask performGetShopTask = new PerformGetShopTask();
					performGetShopTask.execute();
				}catch(NumberFormatException e){
					checkoutDialogResult = false;
					showDialog("Checkout error", "Scanned QR code is no shop identifier.");
				}
			} else {
				fetchArticle(code);
			}
		} else {
			checkoutDialogResult = false;
		}
	}

	/**
	 * starts chechout with the async Task Perfomcheckout 
	 */
	public void checkout() {
		PerformCheckoutTask performCheckoutTask = new PerformCheckoutTask();
		performCheckoutTask.execute();
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
		BasketRow newArticle = new BasketRow(article.getBarcode(),BigInteger.ONE, article.getName(),  article.getPrice());
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

	/**
	 * update the total sum from the list
	 */
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

	/**
	 * overrides the use of the Backbutton
	 */
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

	/**
	 * shows LogoutDialog after User Klicks Logout
	 */
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
	
	/**
	 * Starts Checkout Screen
	 */
	private void finishShopping(){
		final Intent intent = new Intent(this,
				at.jku.smartshopper.client.Checkout.class);
		intent.putExtra("total", txtTotalAmount.getText());
		finish();
		startActivity(intent);
	}

	/**
	 * does the Checkout
	 * and gives the data to the server 
	 */
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
			basket.setShopId(shop.getShopId());
			basket.setUserId("smartshopper");
			basket.getRows().addAll(meineliste);
			IBasketService service = new RemoteBasketService();

			service.putBasket(basket);

			return null;
		}

		@Override
		protected void onPostExecute(Void response) {
			BasketOverview.this.dismissProgressDialog();
			finishShopping();
		}

	}
	
	/**
	 * gehts single Article with the Ean (Barcode)
	 *
	 */
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

	/**
	 *  Get all Information about the Shop from the SErver with the data from the QR -code 
	 *
	 */
	private class PerformGetShopTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			BasketOverview.this.showProgressDialog("Getting shop info...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			IShopService service = new RemoteShopService();
			shop = service.getShop(shopID);
			return null;
		}

		@Override
		protected void onPostExecute(Void response) {
			BasketOverview.this.dismissProgressDialog();
			if(shop!=null){
				checkout();
			}else{
				showDialog("Checkout error", "No shop found for scanned QR code.");
			}
		}
	}
}
