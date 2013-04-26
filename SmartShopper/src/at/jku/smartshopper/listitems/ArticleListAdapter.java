package at.jku.smartshopper.listitems;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import at.jku.smartshopper.client.R;

public class ArticleListAdapter extends ArrayAdapter<Articletest> {

	private List<Articletest> items;
	private int layoutResourceId;
	private Context context;

	public ArticleListAdapter(Context context, int layoutResourceId, List<Articletest> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;

		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		AtomPaymentHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new AtomPaymentHolder();
		holder.article = items.get(position);
		holder.removePaymentButton = (Button)row.findViewById(R.id.atomPay_removePay);
		holder.removePaymentButton.setTag(holder.article);
		holder.name = (TextView)row.findViewById(R.id.articlename);
		holder.value = (TextView)row.findViewById(R.id.article_value);
		//holder.name = (TextView)row.findViewById(R.id.atomPay_name);
		//holder.value = (TextView)row.findViewById(R.id.atomPay_value);

		row.setTag(holder);

		setupItem(holder);
		return row;
	}

	private void setupItem(AtomPaymentHolder holder) {
		holder.name.setText(holder.article.getName());
		holder.value.setText(String.valueOf(holder.article.getValue()));
	}

	public static class AtomPaymentHolder {
		Articletest article;
		TextView name;
		TextView value;
		Button removePaymentButton;
	}
}