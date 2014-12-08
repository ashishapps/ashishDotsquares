package com.tamilshout.fragments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tamilshout.R;

import com.tamilshout.HomeActivity;
import com.tamilshout.LoginActivity;
import com.tamilshout.RegisterActivity;
import com.tamilshout.model.BusinessUser;
import com.tamilshout.model.EditUserBean;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;
import com.viewpagerindicator.CirclePageIndicator;

public class ProfileEditFragment extends BaseFragment {

	boolean create = false;
	ArrayList<BusinessUser> categoryList;
	private ProgressDialog pd;
	boolean flagForDateSelection = false;
	JSONObject jsonObject = null;

	Thread t1;
	ViewPager viewPager;
	CirclePageIndicator circlePageIndicator;
	ListView listView;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem;
	String catId = "";
	EditUserBean userBean;
	String addBannerImgUrl = "";
	ArrayList<EditUserBean> editUserBeans;
	LinearLayout galleryLayout;
	View v;
	EditText firstNameEdt, lastNameEdt, emailEdt, reemailEdt, mobileNoEdt,
			dobEdt, passwordEdt, confirmPasswordEdt;
	RadioButton maleRadio, femaleRadio;
	private String reemailStr, emailStr, passwordStr, response, firstNameStr,
			lastNameStr, confirmPasswordStr, prviousPassword, mobileNoStr,
			dobStr, genderStr;

	BusinessUser businessUser = null;
	String webUrl = "";
	ImageView addBannerView;
	Bundle args;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.edit_profile, null);

		return v;
	}

	
	private void setUIValues() {
		if (userBean != null) {
			firstNameEdt.setText(userBean.getFirstname());
			lastNameEdt.setText(userBean.getLastname());
			emailEdt.setText(userBean.getEmail());
			reemailEdt.setText(userBean.getEmail());
			mobileNoEdt.setText(userBean.getMobileno());
			dobEdt.setText(userBean.getDob());
			prviousPassword = userBean.getPassword();

			if (userBean.getGender().equalsIgnoreCase("m")) {
				maleRadio.setChecked(true);
				femaleRadio.setChecked(false);
			} else {
				maleRadio.setChecked(false);
				femaleRadio.setChecked(true);
			}

		}

	}

	@Override
	public void onClick(View v) {
		String url;
		Intent intent;
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {

		case R.id.btn_editProfile:
			addUser();
			break;
		case R.id.btnTopLeft:
			((HomeActivity) getActivity()).onBackPressed();
			break;

		default:
			break;
		}

	}

	

	private void addUser() {

		RadioGroup rg = (RadioGroup) v.findViewById(R.id.radio_gender);
		String gender = ((RadioButton) v.findViewById(rg
				.getCheckedRadioButtonId())).getTag().toString();

		if (!isEmpty()) {

			if (Utils.isNetworkAvailableNew(getActivity())) {

				pd = ProgressDialog.show(getActivity(), "", ""
						+ getString(R.string.processing));
				pd.setCancelable(true);

				jsonObject = new JSONObject();
				try {

					jsonObject.put("email", emailStr);
					jsonObject.put("userid", userBean.getUserid());
					jsonObject.put("dob", dobStr);

					if (passwordStr.isEmpty())
						jsonObject.put("password", prviousPassword);
					else
						jsonObject.put("password", passwordStr);

					jsonObject.put("firstname", firstNameStr);
					jsonObject.put("gender", gender);
					jsonObject.put("lastname", lastNameStr);
					jsonObject.put("mobile", mobileNoStr);

					new Thread(new Runnable() {

						public void run() {

							response = GetDataFromWebService.readJsonFeed(
									getString(R.string.service_url)
											+ "UpdateProfile",
									jsonObject.toString());

							System.out.println("response" + response);
							getActivity().runOnUiThread(new Runnable() {

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
				Utils.showDialog(getActivity(),
						getString(R.string.no_network_available));

		}
	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			try {
				jsonObject = new JSONObject(response);
				if (jsonObject.getString("responsecode").equals("100")) {

					Utils.showDialog(getActivity(),
							jsonObject.getString("ResponseMessage"));
				} else if (jsonObject.getString("responsecode").equals("200")
						&& jsonObject.getString("status").equals("OK")) {
					Toast.makeText(getActivity(),
							"Profile updated successfully!", Toast.LENGTH_SHORT)
							.show();

					((HomeActivity) getActivity()).onBackPressed();

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
					if (jsonObject.getString("status").contains("Already"))
						Utils.showDialog(getActivity(), "Email already exists!");
					else
						Utils.showDialog(getActivity(),
								jsonObject.getString("status"));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Utils.showDialog(getActivity(),getString(R.string.server_error));
		}

	}

	private boolean isEmpty() {
		emailStr = emailEdt.getText().toString();
		reemailStr = reemailEdt.getText().toString();
		firstNameStr = firstNameEdt.getText().toString();
		lastNameStr = lastNameEdt.getText().toString();
		mobileNoStr = mobileNoEdt.getText().toString();
		passwordStr = passwordEdt.getText().toString();
		dobStr = dobEdt.getText().toString();
		confirmPasswordStr = confirmPasswordEdt.getText().toString();

		if (firstNameStr.isEmpty()) {
			return showErrorDialog("Please enter first name");
		} else if (lastNameStr.isEmpty()) {
			return showErrorDialog("Please enter last name");
		} else if (emailStr.isEmpty()) {
			return showErrorDialog("Please enter email address");
		} else if (!Utils.isValidEmail(emailStr)) {
			return showErrorDialog(getString(R.string.invalid_email));

		} else if (reemailStr.isEmpty()) {
			return showErrorDialog("Please enter re email address");
		} else if (!Utils.isEmailMatch(emailEdt, reemailEdt)) {
			return showErrorDialog("Email should be same!");

		} else if (!passwordStr.isEmpty()) {
			if (confirmPasswordStr.isEmpty()) {
				return showErrorDialog("Enter re password.");

			} else if (!Utils.isPasswordMatch(passwordEdt, confirmPasswordEdt)) {
				return showErrorDialog("Password should be same");

			}
		}
		return false;
	}

	private boolean showErrorDialog(String message) {

		Utils.showDialog(getActivity(), message);
		return true;
	}

	@SuppressLint({ "NewApi", "ValidFragment" })
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

			if (diff <= 0) {
				if (flagForDateSelection) {
					flagForDateSelection = false;
					Utils.showDialog(getActivity(),
							"Please select correct date!");
				}

			} else {
				dobEdt.setText(formattedDate);
			}

		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!AppPrefrence.isLogin(getActivity())) {
			((HomeActivity) getActivity()).onBackPressed();

		} else {

			args = getArguments();
			if (args != null && args.containsKey("userEditBean"))
				userBean = (EditUserBean) args.getSerializable("userEditBean");

			if (userBean.getEmail().equalsIgnoreCase(
					AppPrefrence.getProfile(getActivity()).getEmail())) {

				firstNameEdt = (EditText) v.findViewById(R.id.edt_fname);
				lastNameEdt = (EditText) v.findViewById(R.id.edt_lnam);
				emailEdt = (EditText) v.findViewById(R.id.edt_email);
				reemailEdt = (EditText) v.findViewById(R.id.edt_reemail);
				mobileNoEdt = (EditText) v.findViewById(R.id.edt_mobile);
				dobEdt = (EditText) v.findViewById(R.id.edt_dob);
				passwordEdt = (EditText) v.findViewById(R.id.edt_password);
				confirmPasswordEdt = (EditText) v
						.findViewById(R.id.edt_confirmPassword);
				maleRadio = (RadioButton) v.findViewById(R.id.radio_male);
				femaleRadio = (RadioButton) v.findViewById(R.id.radio_female);

				Constant.appContext = getActivity();
				Log.d("USer Profile", "USer Profile");

				setHeader(v, "Update Profile");
				setLeftButton(v, R.drawable.btn_back);

				setTouchNClick(v, R.id.btn_editProfile);

				dobEdt.setOnClickListener(new OnClickListener() {

					@SuppressLint("NewApi")
					@Override
					public void onClick(View v) {
						flagForDateSelection = true;
						DialogFragment picker = new DatePickerFragment();
						picker.show(getFragmentManager(), "DatePicker");

					}
				});

				setUIValues();
			}
			else
			{
				((HomeActivity) getActivity()).onBackPressed();
			}

		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		// dbh.closeDB();
		super.onDestroyView();
		if (pd != null && pd.isShowing())
			pd.dismiss();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (pd != null && pd.isShowing())
			pd.dismiss();

		if (t1 != null)
			t1.interrupt();
		

		Thread.currentThread().interrupt();
		v = null;

	}
}
