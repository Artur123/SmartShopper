package at.jku.smartshopper.listitems;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import at.jku.smartshopper.client.R;

public class ArticleListAdapter extends ArrayAdapter<Articletest> {

	private List<Articletest> items;
	private int layoutResourceId;
	private Context context;
	ArticlerHolder holder;
	
	public ArticleListAdapter(Context context, int layoutResourceId, List<Articletest> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;

		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new ArticlerHolder();
		holder.article = items.get(position);
		holder.removePaymentButton = (ImageView)row.findViewById(R.id.atomPay_removePay);
		holder.removePaymentButton.setTag(holder.article);
		holder.name = (TextView)row.findViewById(R.id.articlename);
		holder.value = (TextView)row.findViewById(R.id.article_value);
		holder.count = (TextView)row.findViewById(R.id.article_count);
		holder.count.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
			    if(hasFocus){
			       //Toast.makeText(context, "got the focus", Toast.LENGTH_LONG).show();
			    }else
			        //Toast.makeText(context, "lost the focus", Toast.LENGTH_LONG).show();
			    	holder.article.setCount(Integer.parseInt(holder.count.getText().toString()));
			    }
			});

		row.setTag(holder);

		setupItem(holder);
		return row;
	}
	

	private void setupItem(ArticlerHolder holder) {
		holder.name.setText(holder.article.getName());
		holder.value.setText(String.valueOf(holder.article.getValue()));
		holder.count.setText(String.valueOf(holder.article.getCount()));
	}

	public static class ArticlerHolder {
		Articletest article;
		TextView count;
		TextView name;
		TextView value;
		ImageView removePaymentButton;
	}
}