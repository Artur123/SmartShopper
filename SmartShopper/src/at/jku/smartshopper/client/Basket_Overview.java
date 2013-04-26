package at.jku.smartshopper.client;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import at.jku.smartshopper.listitems.ArticleListAdapter;
import at.jku.smartshopper.listitems.Articletest;

public class Basket_Overview extends Activity {
	List<Articletest> meineliste;
	ArticleListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_overview);
         meineliste= new ArrayList<Articletest>();
        setup();
        
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.basket__overview, menu);
        return true;
    }
    
    
    
    
}
