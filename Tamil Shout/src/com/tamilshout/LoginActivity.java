package com.tamilshout;

import java.lang.reflect.Type;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.model.UserBean;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class LoginActivity extends BaseClass {

	boolean create = false;
	private JSONObject jsonObject;
	private ProgressDialog pd;
	private EditText emailEdt, passwordEdt;
	private String emailStr, passwordStr, response;
	CheckBox isRememberChk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.login_fragment);

		setHeader("Login");
		setLeftButton(R.drawable.btn_back);

		setTouchNClick(R.id.btn_login);
		setTouchNClick(R.id.btn_register);
		setTouchNClick(R.id.btn_forgot);

		emailEdt = (EditText) findViewById(R.id.edt_email);
		passwordEdt = (EditText) findViewById(R.id.edt_password);
		isRememberChk = (CheckBox) findViewById(R.id.check_remeber);

		if (AppPrefrence.isRemember(LoginActivity.this)) {
			if (AppPrefrence.getProfile(LoginActivity.this) != null) {
				emailEdt.setText(AppPrefrence.getProfile(LoginActivity.this)
						.getEmail());
				passwordEdt.setText(AppPrefrence.getProfile(LoginActivity.this)
						.getPassword());
				isRememberChk.setChecked(true);
			}
		}

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
		case R.id.btn_register:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.btnTopLeft:
			intent = new Intent(this, HomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			break;
		case R.id.btn_forgot:
			intent = new Intent(LoginActivity.this, ForgotPassword.class);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

	private void verifyUserLogin() {

		if (!isEmpty()) {
			if (!Utils.isValidEmail(emailStr)) {
				showErrorDialog(getString(R.string.invalid_email));

			} else {

				if (Utils.isNetworkAvailableNew(this)) {

					pd = ProgressDialog.show(this, "", ""
							+ getString(R.string.processing));
					pd.setCancelable(true);

					jsonObject = new JSONObject();
					try {

						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(LoginActivity.this);
						jsonObject.put("email", emailStr);
						jsonObject.put("password", passwordStr);
						jsonObject.put("devicetoken",
								preferences.getString("deviceTokenId", ""));
						jsonObject.put("devicetype", "android");

						new Thread(new Runnable() {

							public void run() {

								response = GetDataFromWebService.readJsonFeed(
										getString(R.string.service_url)
												+ "UserLogin",
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
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} else
					Utils.showDialog(this,
							getString(R.string.no_network_available));

			}
		}
	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			try {
				jsonObject = new JSONObject(response);
				if (jsonObject.getString("responsecode").equals("101")) {
					Utils.showDialog(this,
							jsonObject.getString("ResponseMessage"));
					passwordEdt.setText("");
				} else if (jsonObject.getString("responsecode").equals("100")) {
					passwordEdt.setText("");
					Utils.showDialog(this,
							jsonObject.getString("ResponseMessage"));
				} else if (jsonObject.getString("responsecode").equals("200")) {
					/*
					 * Utils.showDialog(this,
					 * jsonObject.getString("ResponseMessage"));
					 */

					/*
					 * Toast.makeText(this, "" +
					 * jsonObject.getString("ResponseMessage"),
					 * Toast.LENGTH_LONG).show();
					 */

					/*
					 * Utils.showDialog(this,
					 * jsonObject.getString("ResponseMessage"));
					 */
					JSONArray jsonArray = jsonObject
							.getJSONArray("responsepacketList");
					if (jsonArray != null && jsonArray.length() > 0) {

						Type listType = new TypeToken<UserBean>() {
						}.getType();
						UserBean userBean = new Gson().fromJson(jsonArray
								.getJSONObject(0).toString(), listType);

						AppPrefrence.setProfile(this, userBean);
						AppPrefrence.setLogin(LoginActivity.this, true);
						AppPrefrence.setRemember(LoginActivity.this,
								isRememberChk.isChecked());

						if (getIntent().getBooleanExtra("isGoHome", false)) {
							System.out.println("-------isGoHome----"
									+ getIntent().getBooleanExtra("isGoHome",
											false));
							Intent intent = new Intent(this, HomeActivity.class);
							startActivity(intent);
							finish();
						} else {
							System.out.println("-------isGoHome----"
									+ getIntent().getBooleanExtra("isGoHome",
											false));
							finish();
						}

					}
				} else {
					Utils.showDialog(this, getString(R.string.server_error));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}`1	`s

	}

	private boolean isEmpty() {
		emailStr = emailEdt.getText().toString();
		passwordStr = passwordEdt.getText().toString();

		if (emailStr.isEmpty()) {
			return showErrorDialog("Please enter email address");
		} else if (passwordStr.isEmpty()) {
			return showErrorDialog("Please enter password");
		}
		return false;
	}

	private boolean showErrorDialog(String message) {

		Utils.showDialog(this, message);
		return true;
	}

}
