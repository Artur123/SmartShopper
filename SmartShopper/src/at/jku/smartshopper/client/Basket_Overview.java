package at.jku.smartshopper.client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Basket_Overview extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_overview);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.basket__overview, menu);
        return true;
    }
    
}
