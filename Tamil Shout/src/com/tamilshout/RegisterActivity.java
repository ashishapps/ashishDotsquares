package com.tamilshout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.tamilshout.R;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class RegisterActivity extends BaseClass {

	boolean create = false;
	private JSONObject jsonObject;
	private ProgressDialog pd;
	private String reemailStr, emailStr, passwordStr, response, firstNameStr,
			lastNameStr, confirmPasswordStr, mobileNoStr, dobStr, genderStr;
	boolean flagForDateSelection = false;
	EditText mobileEdt;
	TextView termAndCondTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.register_fragment);

		setHeader("Register");
		setLeftButton(R.drawable.btn_back);

		// setTouchNClick(R.id.btn_login);

		setTouchNClick(R.id.btn_register);
		// setTouchNClick(R.id.btn_forgot);

		((EditText) findViewById(R.id.edt_dob))
				.setOnClickListener(new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						flagForDateSelection = true;
						DialogFragment picker = new DatePickerFragment();

						picker.show(getFragmentManager(), "DatePicker");

					}
				});

		// emailEdt = (EditText) findViewById(R.id.edt_email);
		// passwordEdt = (EditText) findViewById(R.id.edt_password);

		termAndCondTxt = (TextView) findViewById(R.id.termsAndConditionsTxt);

		termAndCondTxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(RegisterActivity.this,
						TermsAndConditionsActivity.class));

			}
		});

		mobileEdt = (EditText) findViewById(R.id.edt_mobile);
		mobileEdt
				.setOnEditorActionListener(new EditText.OnEditorActionListener() {
					@SuppressLint("NewApi")
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (actionId == EditorInfo.IME_ACTION_DONE
								|| actionId == EditorInfo.IME_ACTION_NEXT) {
							DialogFragment picker = new DatePickerFragment();
							picker.show(getFragmentManager(), "DatePicker");
							return false;
						}
						return false;
					}
				});

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

		case R.id.btnTopLeft:
			finish();
			break;

		case R.id.btn_register:
			addUser();
			break;
		/*
		 * case R.id.btn_forgot: Intent intent = new
		 * Intent(RegisterActivity.this, ForgotPassword.class);
		 * startActivity(intent); break;
		 */
		default:
			break;
		}

	}

	private void addUser() {

		RadioGroup rg = (RadioGroup) findViewById(R.id.radio_gender);
		String gender = ((RadioButton) findViewById(rg
				.getCheckedRadioButtonId())).getTag().toString();

		if (!isEmpty()) {
			if (!((CheckBox) findViewById(R.id.check_accept)).isChecked()) {
				showErrorDialog("Please accept Terms & Conditions");

			} else {

				if (Utils.isNetworkAvailableNew(this)) {

					pd = ProgressDialog.show(this, "", ""
							+ getString(R.string.processing));
					pd.setCancelable(true);

					jsonObject = new JSONObject();
					try {

						if (Constant.SELECTED_COUNTRY_NAME != null
								&& Constant.SELECTED_COUNTRY_NAME.length() > 0)
							jsonObject.put("country",
									Constant.SELECTED_COUNTRY_NAME);
						else if(Constant.CURRENT_COUNTRY != null
								&& Constant.CURRENT_COUNTRY.length() > 0)
							jsonObject.put("country", Constant.CURRENT_COUNTRY);
						else
						{
							jsonObject.put("country", RegisterActivity.this.getResources().getConfiguration().locale.getDisplayCountry());
							 
						}



						jsonObject.put("email", emailStr);
						jsonObject.put("dob", dobStr);
						jsonObject.put("password", passwordStr);
						jsonObject.put("firstname", firstNameStr);
						jsonObject.put("gender", gender);
						jsonObject.put("lastname", lastNameStr);
						jsonObject.put("mobile", mobileNoStr);

						new Thread(new Runnable() {

							public void run() {

								response = GetDataFromWebService.readJsonFeed(
										getString(R.string.service_url)
												+ "UserRegistration",
										jsonObject.toString());

								System.out.println("response" + response);
								runOnUiThread(new Runnable() {

									@Override
									public void run() {

										if (pd.isShowing())
											pd.dismiss();

										parsJsonObject(response);

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
				if (jsonObject.getString("responsecode").equals("100")) {
					Utils.showDialog(this,
							jsonObject.getString("ResponseMessage"));
				} else if (jsonObject.getString("responsecode").equals("200")
						&& jsonObject.getString("status").equals("OK")) {
					Toast.makeText(this,
							jsonObject.getString("ResponseMessage").toString(),
							Toast.LENGTH_SHORT).show();
					finish();

					/*
					 * JSONArray jsonArray = jsonObject
					 * .getJSONArray("responsepacketList"); if (jsonArray !=
					 * null && jsonArray.length() > 0) {
					 * 
					 * 
					 * Type listType = new TypeToken<UserBean>() { }.getType();
					 * UserBean userBean = new Gson().fromJson(jsonArray
					 * .getJSONObject(0).toString(), listType);
					 * 
					 * System.out
					 * .println("----------username/email--------------" +
					 * userBean.getEmail());
					 * 
					 * 
					 * }
					 */
				} else {
					Utils.showDialog(this, jsonObject.getString("status"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private boolean isEmpty() {
		emailStr = getEditTextText(R.id.edt_email);
		reemailStr = getEditTextText(R.id.edt_reemail);
		firstNameStr = getEditTextText(R.id.edt_fname);
		lastNameStr = getEditTextText(R.id.edt_lnam);
		mobileNoStr = getEditTextText(R.id.edt_mobile);
		passwordStr = getEditTextText(R.id.edt_password);
		confirmPasswordStr = getEditTextText(R.id.edt_confirmPassword);
		dobStr = getEditTextText(R.id.edt_dob);

		if (firstNameStr.isEmpty()) {
			return showErrorDialog("Please enter first name.");
		} else if (lastNameStr.isEmpty()) {
			return showErrorDialog("Please enter last name.");
		} else if (emailStr.isEmpty()) {
			return showErrorDialog("Please enter email address");
		} else if (!Utils.isValidEmail(emailStr)) {
			return showErrorDialog(getString(R.string.invalid_email));

		} else if (!Utils.isEmailMatch(emailStr, reemailStr)) {
			return showErrorDialog("Email doesn't match");

		} else if (passwordStr.isEmpty()) {
			return showErrorDialog("Please enter password.");
		} else if (confirmPasswordStr.isEmpty()) {
			return showErrorDialog("Please enter confirm password.");
		} else if (!Utils.isPasswordMatch(passwordStr, confirmPasswordStr)) {
			return showErrorDialog("Password doesn't match");

		}

		return false;
	}

	private boolean showErrorDialog(String message) {

		Utils.showDialog(this, message);
		return true;
	}

	@SuppressLint({ "ValidFragment", "NewApi" })
	class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@Override
		public void onDateSet(DatePicker view, int year, int month, int day) {

			Calendar c = Calendar.getInstance();
			c.set(year, month, day);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String formattedDate = sdf.format(c.getTime());

			Calendar date = Calendar.getInstance();
			long diff = date.getTimeInMillis() - c.getTimeInMillis();

			if (diff < 0) {
				if (flagForDateSelection) {
					flagForDateSelection = false;
					Utils.showDialog(RegisterActivity.this,
							"Please select correct date!");
				}

			} else {
				((EditText) findViewById(R.id.edt_dob)).setText(formattedDate);
			}

		}
	}
}
