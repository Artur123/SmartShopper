package at.jku.smartshopper.client;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import at.jku.smartshopper.listitems.ArticleListAdapter;
import at.jku.smartshopper.listitems.Articletest;
import android.widget.Button;
import android.widget.TextView;
import at.jku.smartshopper.scanner.IntentIntegrator;
import at.jku.smartshopper.scanner.IntentResult;

public class Basket_Overview extends Activity {

	List<Articletest> meineliste;
	ArticleListAdapter adapter;
	Button btnScanArt;
	TextView tv1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basket_overview);
		 meineliste= new ArrayList<Articletest>();
	        setup();
		tv1 = (TextView)findViewById(R.id.textView1);
		
		btnScanArt = (Button) findViewById(R.id.btnScanArticle);
		btnScanArt.setOnClickListener(new View.OnClickListener() {   

			@Override
			public void onClick(View v) {
				scanArticle();
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.basket__overview, menu);
		return true;
	}
	
	public void setup()
    { 	
	    adapter = new ArticleListAdapter(this, R.layout.article_list_item, meineliste);
	    ListView articleListview = (ListView)findViewById(R.id.Basket_articleList);
	    articleListview.setAdapter(adapter);
  	
    	Articletest testarticle = new Articletest("Test", 13);
    	adapter.add(testarticle);
    	adapter.add(new Articletest("Hallo",15));
    	//adapter.remove(testarticle);
    };
    
    public void removeArticleHandler(View v) {
    	Articletest itemToRemove = (Articletest)v.getTag();
    	adapter.remove(itemToRemove);
    }


	public void scanArticle() {
		IntentIntegrator integrator = new IntentIntegrator(Basket_Overview.this);
		integrator.initiateScan();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(
				requestCode, resultCode, intent);
		if (scanResult != null) {
			// handle scan result
			tv1.setText(scanResult.getContents());
		}else{
			tv1.setText("blabla. blabla? bla!");
		}

	}

}
