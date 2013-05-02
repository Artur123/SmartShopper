package at.jku.smartshopper.client;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import at.jku.smartshopper.listitems.ArticleListAdapter;
import at.jku.smartshopper.listitems.Articletest;
import at.jku.smartshopper.scanner.IntentIntegrator;
import at.jku.smartshopper.scanner.IntentResult;

public class Basket_Overview extends Activity {

	List<Articletest> meineliste;
	ArticleListAdapter adapter;
	Button btnScanArt;
	Button btnCheckout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basket_overview);

		this.getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
		
		meineliste = new ArrayList<Articletest>();
		setup();
		btnScanArt = (Button) findViewById(R.id.btnScanArticle);
		btnScanArt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				scanArticle();
			}
		});
		
		btnCheckout = (Button) findViewById(R.id.btnCheckout);
		btnScanArt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO
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
		case R.id.about:
			showAbout();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void showStatistics() {
		final Intent intent = new Intent(this,
				at.jku.smartshopper.client.Show_Statistics.class);

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

		Articletest testarticle = new Articletest("Test", 13);
		adapter.add(testarticle);
		adapter.add(new Articletest("Hallo", 15));
		adapter.insert(new Articletest("zweiter", 30), 0);
		adapter.insert(new Articletest("Vöslauer Mineralwasser", 40), 0);
		// Test bezüglich verhalten der Listview
		/*
		 * adapter.add(new Articletest("test1",15)); adapter.add(new
		 * Articletest("familie",15)); adapter.add(new
		 * Articletest("gratis",30)); adapter.add(new Articletest("test2",25));
		 * adapter.add(new Articletest("todo",15)); adapter.add(new
		 * Articletest("test1",15)); adapter.add(new Articletest("familie",15));
		 * adapter.add(new Articletest("gratis",30)); adapter.add(new
		 * Articletest("test2",25)); adapter.add(new Articletest("todo",15));
		 */
		// adapter.remove(testarticle);
	};

	public void removeArticleHandler(View v) {
		Articletest itemToRemove = (Articletest) v.getTag();
		adapter.remove(itemToRemove);
	}

	public void decreaseAmount(View v) {
		// Furchtbare Lösung !!!
		// Achtung bei 0
		Articletest item = (Articletest) v.getTag();
		int pos = adapter.getPosition(item);
		
		adapter.remove(item);
		int wert = item.getCount() - 1;
		if (wert <= 0) {
			// do_nothing
		} else {
			item.setCount(wert);
			adapter.insert(item, pos);
		}
	}
	public void About(View v){
		  //TODO
	}

	public void increaseAmount(View v) {
		// Furchtbare Lösung !!!
		Articletest item = (Articletest) v.getTag();
		int pos = adapter.getPosition(item);
		adapter.remove(item);
		int wert = item.getCount() + 1;
		item.setCount(wert);
		adapter.insert(item, pos);
	}

	public void scanArticle() {
		IntentIntegrator integrator = new IntentIntegrator(Basket_Overview.this);
		integrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult.getContents() != null) {
			// handle scan result
			addArticle(scanResult.getContents());
		} else {
			// TODO: scan failed or aborted
		}
	}

	public void addArticle(String barcode) {
		// TODO: check if Article is valid and add Article
		if (barcode == null) {
			showDialog("Exception", "Article not found.");
		} else {
			Articletest newArticle = new Articletest(barcode, 14.99);
			adapter.add(newArticle);
			Toast.makeText(this, "Barcode: " + barcode, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void enterBarcode() {
		AlertDialog.Builder enterBarcode = new AlertDialog.Builder(this);

		enterBarcode.setTitle("Enter Barcode:");

		final EditText input = new EditText(Basket_Overview.this);
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		enterBarcode.setView(input);

		enterBarcode.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// OK Button
						addArticle(input.getText().toString());
					}
				});

		enterBarcode.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		enterBarcode.show().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);

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
	
}
