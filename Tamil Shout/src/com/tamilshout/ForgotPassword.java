package com.tamilshout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.tamilshout.R;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class ForgotPassword extends BaseClass {

	boolean create = false;
	private JSONObject jsonObject;
	private ProgressDialog pd;
	private EditText emailEdt, passwordEdt;
	private String emailStr, passwordStr, response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.forgot_fragment);

		setHeader("Forgot Password");
		setLeftButton(R.drawable.btn_back);

		setTouchNClick(R.id.btn_login);

		emailEdt = (EditText) findViewById(R.id.edt_email);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopRight:
			// ((BaseContainerFragment)getParentFragment()).replaceFragment(new
			// AddDrinkFragment(), true); vc
			break;
		case R.id.btn_login:
			verifyUserLogin();
			break;
		case R.id.btnTopLeft:
			finish();
			break;

		default:
			break;
		}

	}

	@SuppressLint("NewApi")
	private void verifyUserLogin() {

		emailStr = emailEdt.getText().toString();

		if (emailStr.isEmpty()) {
			Utils.showDialog(ForgotPassword.this, "Please enter email address.");
			return;
		} else if (!Utils.isValidEmail(emailStr)) {
			Utils.showDialog(ForgotPassword.this,
					getString(R.string.invalid_email));

			return;
		} else {

			if (Utils.isNetworkAvailableNew(this)) {

				pd = ProgressDialog.show(this, "", ""
						+ getString(R.string.processing));
				pd.setCancelable(false);

				jsonObject = new JSONObject();

				try {
					jsonObject.put("email", emailStr);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "ForgotPassword",
								jsonObject.toString());

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObject(response);

								if (pd.isShowing())
									pd.dismiss();
							}

						});

					}
				}).start();

			} else
				Utils.showDialog(this, getString(R.string.no_network_available));

		}
	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			try {
				jsonObject = new JSONObject(response);
				if (jsonObject.getString("responsecode").equals("301")) {
					Utils.showDialog(this,
							jsonObject.getString("ResponseMessage"));
				} else if (jsonObject.getString("responsecode").equals("200")
						&& jsonObject.getString("status").equals("OK")) {
					Utils.showDialog(ForgotPassword.this,
							jsonObject.getString("ResponseMessage"), "",
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();

								}
							}, true).show();

					JSONArray jsonArray = jsonObject
							.getJSONArray("responsepacketList");
					if (jsonArray != null && jsonArray.length() > 0) {

					}
				} else {
					Utils.showDialog(this,
							jsonObject.getString("ResponseMessage"));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
