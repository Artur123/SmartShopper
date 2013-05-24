package at.jku.smartshopper.listitems;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.jku.smartshopper.client.R;
import at.jku.smartshopper.objects.BasketRow;

public class ArticleListAdapter extends ArrayAdapter<BasketRow> {

	private List<BasketRow> items;
	private int layoutResourceId;
	private Context context;
	ArticlerHolder holder;
	
	public ArticleListAdapter(Context context, int layoutResourceId, List<BasketRow> items) {
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
		holder.decreaseButton = (ImageView)row.findViewById(R.id.amount_decrease);
		holder.decreaseButton.setTag(holder.article);
		holder.increaseButton = (ImageView)row.findViewById(R.id.amount_increase);
		holder.increaseButton.setTag(holder.article);
		
		holder.name = (TextView)row.findViewById(R.id.articlename);
		holder.value = (TextView)row.findViewById(R.id.article_value);
		holder.quantity = (TextView)row.findViewById(R.id.article_count);
		row.setTag(holder);

		setupItem(holder);
		return row;
	}
	

	private void setupItem(ArticlerHolder holder) {
		holder.name.setText(holder.article.getArticleName());
		holder.value.setText(String.valueOf(holder.article.getPrice()));
		holder.quantity.setText(String.valueOf(holder.article.getQuantity()));
	}

	public static class ArticlerHolder {
		BasketRow article;
		TextView quantity;
		TextView name;
		TextView value;
		ImageView increaseButton;
		ImageView decreaseButton;
		ImageView removePaymentButton;
	}
}