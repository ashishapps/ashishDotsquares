package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.LoginActivity;
import com.tamilshout.model.BusinessUser;
import com.tamilshout.model.EditUserBean;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;
import com.viewpagerindicator.CirclePageIndicator;

public class ProfileFragment extends BaseFragment {

	boolean create = false;
	ArrayList<BusinessUser> categoryList;
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	Thread t1, t2;
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
	EditText firstNameEdt, lastNameEdt, emailEdt, mobileNoEdt, dobEdt;
	RadioButton maleRadio, femaleRadio;

	BusinessUser businessUser = null;
	String webUrl = "";
	ImageView addBannerView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.profile, null);

		firstNameEdt = (EditText) v.findViewById(R.id.edt_fname);
		lastNameEdt = (EditText) v.findViewById(R.id.edt_lnam);
		emailEdt = (EditText) v.findViewById(R.id.edt_email);
		mobileNoEdt = (EditText) v.findViewById(R.id.edt_mobile);
		dobEdt = (EditText) v.findViewById(R.id.edt_dob);
		maleRadio = (RadioButton) v.findViewById(R.id.radio_male);
		femaleRadio = (RadioButton) v.findViewById(R.id.radio_female);

		return v;
	}

	private void getUserProfileInfo() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();
				System.out.println("==========Constant.COUNTRY_NAME====="
						+ Constant.SELECTED_COUNTRY_NAME);
				jsonObject.put("id", AppPrefrence.getProfile(getActivity())
						.getUserid());

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService
								.readJsonFeed(getString(R.string.service_url)
										+ "EditProfile", jsonObject.toString());

						if(v!=null)
						{
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObject(response);

								if (pd != null && pd.isShowing())
									pd.dismiss();

								if (userBean != null) {
									firstNameEdt.setText(userBean
											.getFirstname());
									lastNameEdt.setText(userBean.getLastname());
									emailEdt.setText(userBean.getEmail());
									mobileNoEdt.setText(userBean.getMobileno());
									dobEdt.setText(userBean.getDob());

									if (userBean.getGender().equalsIgnoreCase(
											"m")) {
										maleRadio.setChecked(true);
										femaleRadio.setChecked(false);
									} else {
										maleRadio.setChecked(false);
										femaleRadio.setChecked(true);
									}

								}
								// Utils.showDialog(getActivity(),getString(R.string.no_record);

							}

							

						});
						}
					}
				});t1.start();
			} catch (Exception exception) {
				if (pd != null && pd.isShowing())
					pd.dismiss();
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));

	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<EditUserBean>>() {
			}.getType();

			try {
				editUserBeans = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);
				if (editUserBeans != null && editUserBeans.size() > 0)
					userBean = editUserBeans.get(0);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btn_editProfile:
			ProfileEditFragment fragment = new ProfileEditFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("userEditBean", userBean);
			fragment.setArguments(bundle);
			((ProfileContainerFragment) getParentFragment()).replaceFragment(
					fragment, true, "Profile");
			break;

		default:
			break;
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (!AppPrefrence.isLogin(getActivity())) {

			Utils.showDialog(getActivity(), getString(R.string.login_required),
					"", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							Intent intent = new Intent(getActivity(),
									LoginActivity.class);
							intent.putExtra("isGoHome", true);
							startActivity(intent);
							getActivity().finish();

						}
					}, false).show();

			firstNameEdt.setText("");
			lastNameEdt.setText("");
			dobEdt.setText("");
			emailEdt.setText("");
			mobileNoEdt.setText("");

		} else {

			firstNameEdt.setText("");
			lastNameEdt.setText("");
			dobEdt.setText("");
			emailEdt.setText("");
			mobileNoEdt.setText("");

			Constant.appContext = getActivity();
			Log.d("USer Profile", "USer Profile");

			setHeader(v, "My Profile");

			setTouchNClick(v, R.id.btn_editProfile);

			getUserProfileInfo();
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
