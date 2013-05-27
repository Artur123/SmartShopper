package at.jku.smartshopper.client;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import at.jku.smartshopper.backend.IUserService;
import at.jku.smartshopper.backend.RemoteUserService;
import at.jku.smartshopper.objects.User;

public class Login extends Activity {

	Button btnLogin;
	EditText txtUsername;
	EditText txtPassword;

	private String username, password;
	private User user;

	private ProgressDialog progressDialog;
	private boolean destroyed = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);

		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username = txtUsername.getText().toString();
				password = txtPassword.getText().toString();

				PerformLoginTask performLoginTask = new PerformLoginTask();
				performLoginTask.execute();

				//startShopping();
			}
		});
	}

	/**
	 * successful login, continuing to BasketOverview
	 * 
	 * @param username
	 * @param password
	 */
	private void startShopping() {
		final Intent intent = new Intent(this,
				at.jku.smartshopper.client.Basket_Overview.class);

		intent.putExtra("username", user.getUsername());
		intent.putExtra("password", user.getPassword());
		
		startActivity(intent);
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

	private class PerformLoginTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			Login.this.showProgressDialog("Checking login data...");
		}

		@Override
		protected Void doInBackground(Void... params) {
			// send username and password
			IUserService service = new RemoteUserService();

			user = service.getUser(username, password);

			return null;
		}

		@Override
		protected void onPostExecute(Void response) {
			Login.this.dismissProgressDialog();
			//TODO: check if exceptions?
			if (user != null) {
				startShopping();
			}

		}
	}
}