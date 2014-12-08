package com.tamilshout.fragments;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.CountryList;
import com.tamilshout.HomeActivity;
import com.tamilshout.LoginActivity;
import com.tamilshout.RegisterActivity;
import com.tamilshout.model.UserBean;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class HomeFragment extends BaseFragment {

	boolean create = false;
	private ProgressDialog pd;

	ArrayList<Object> drinkList;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem, welcomeTxt;
	EditText editText, serchEditText;
	Button selectCatBtn, searchBusinessBtn;
	Bundle bundle;
	LinearLayout signInLayout, signOutLayout, registerLayout;
	View v;
	int width = 0;
	int height = 0;
	WebView addevertiseWebView;
	JSONObject jsonObject;
	String response;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		System.out.println("-----HomeFragment-----onCreateViewiw--------");

		v = inflater.inflate(R.layout.home_fragment, null);

		editText = (EditText) v.findViewById(R.id.edt_select_country);
		serchEditText = (EditText) v.findViewById(R.id.edt_search);
		signInLayout = (LinearLayout) v.findViewById(R.id.signinLayout);
		signOutLayout = (LinearLayout) v.findViewById(R.id.logoutLayout);
		registerLayout = (LinearLayout) v.findViewById(R.id.registerLayout);
		welcomeTxt = (TextView) v.findViewById(R.id.welcomeTxt);
		selectCatBtn = (Button) v.findViewById(R.id.selectCatBtn);

		if (android.os.Build.VERSION.SDK_INT >= 13) {
			Display display = getActivity().getWindowManager()
					.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			width = size.x;
			height = size.y;
		} else {
			width = getActivity().getWindowManager().getDefaultDisplay()
					.getWidth();
			height = getActivity().getWindowManager().getDefaultDisplay()
					.getHeight();
		}
		addevertiseWebView = (WebView) v
				.findViewById(R.id.addevertiseImageView);
		addevertiseWebView.getSettings().setJavaScriptEnabled(true);
		addevertiseWebView.setVerticalScrollBarEnabled(false);
		addevertiseWebView.setHorizontalScrollBarEnabled(false);

		height = (height / 100) * 15;
		width = addevertiseWebView.getLayoutParams().width;
		addevertiseWebView.getLayoutParams().height = height;
		height = (int) Utils.convertPixelsToDp(height, getActivity());
		width = (int) Utils.convertPixelsToDp(width, getActivity());

		System.out.println("--------width---------" + width);
		System.out.println("--------height---------" + height);

		Log.d("drink", "Drink On Create View");

		setTouchNClick(v, R.id.btn_signin);
		setTouchNClick(v, R.id.btn_register);

		editText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getActivity(), CountryList.class);
				startActivityForResult(intent, 1);

			}
		});

		serchEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {

					if (serchEditText != null
							&& serchEditText.getText() != null
							&& serchEditText.getText().toString().length() > 0) {

						Utils.hideKeyboard(getActivity(), serchEditText);

						try {
							Thread.currentThread().sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						UserListFragment userListFragment = new UserListFragment();
						bundle = new Bundle();
						bundle.putBoolean("isComesFromHome", true);
						bundle.putString("seacrchKeyWord", serchEditText
								.getText().toString());
						userListFragment.setArguments(bundle);
						((HomeContainerFragment) getParentFragment())
								.replaceFragment(userListFragment, true, "HOME");
					} else {
						Utils.hideKeyboard(getActivity(), serchEditText);
					}

					return true;
				}
				return false;
			}
		});

		setTouchNClick(v, R.id.directoryBtn);
		setTouchNClick(v, R.id.dealBtn);
		setTouchNClick(v, R.id.eventBtn);
		setTouchNClick(v, R.id.itemForSaleBtn);
		setTouchNClick(v, R.id.btn_logout);
		setTouchNClick(v, R.id.searchBusinessBtn);
		setTouchNClick(v, R.id.selectCatBtn);

		return v;

	}

	@SuppressLint("NewApi")
	void loadHomeBanner() {

		Constant.appContext = getActivity();

		String location = "";
		if (Constant.SELECTED_COUNTRY_NAME != null
				&& Constant.SELECTED_COUNTRY_NAME.length() > 0) {
			location = Constant.SELECTED_COUNTRY_NAME;

		} else {
			location = Constant.CURRENT_COUNTRY;
		}

		String webUrl = getActivity().getString(R.string.add_url)
				+ "type=home&device=android&height=" + height + "&width="
				+ width + "&location=" + location;
		System.out.println("=========addurl======" + webUrl);
		addevertiseWebView.loadUrl(webUrl);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (AppPrefrence.isLogin(getActivity())) {

			signOutLayout.setVisibility(View.VISIBLE);
			signInLayout.setVisibility(View.GONE);
			registerLayout.setVisibility(View.GONE);
			// AppPrefrence.getProfile(getActivity()).getUsername().toString());
			welcomeTxt.setText("Welcome "
					+ AppPrefrence.getProfile(getActivity()).getUsersname()
							.toString() + " !");
		} else {

			signOutLayout.setVisibility(View.GONE);
			signInLayout.setVisibility(View.VISIBLE);
			registerLayout.setVisibility(View.VISIBLE);
		}
		System.out.println("---Constant.SELECTED_COUNTRY_NAME-----------"
				+ Constant.SELECTED_COUNTRY_NAME);

		if (Constant.SELECTED_COUNTRY_NAME != null
				&& Constant.SELECTED_COUNTRY_NAME.length() > 0) {
			System.out.println("---------------1");
			((EditText) v.findViewById(R.id.edt_select_country))
					.setText(Constant.SELECTED_COUNTRY_NAME);
			loadHomeBanner();

		} else {
			System.out.println("---------------2");

			if (Constant.CURRENT_COUNTRY == null
					|| Constant.CURRENT_COUNTRY.equals("")) {
				System.out.println("---------------3");

				getCurrenctLocation();

			} else {
				loadHomeBanner();
				System.out.println("---------------4");
				((EditText) v.findViewById(R.id.edt_select_country))
						.setText(Constant.CURRENT_COUNTRY);
			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == 2 && data != null) {
			Constant.SELECTED_COUNTRY_NAME = data.getStringExtra(
					"selectedCountry").toString();
			editText.setText(data.getStringExtra("selectedCountry").toString());

			loadHomeBanner();

		}
		// TODO Auto-generated method stub

	}

	private void getCurrenctLocation() {
		System.out.println("------------------5");

		pd = ProgressDialog.show(getActivity(), "", ""
				+ getString(R.string.processing));
		pd.setCancelable(true);

		LocationManager man = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

		System.out.println("---------------6");
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);

		String provider = LocationManager.NETWORK_PROVIDER;

		if (provider == null) {
			provider = LocationManager.GPS_PROVIDER;
		}

		if (provider != null)
			Constant.location = man.getLastKnownLocation(provider);

		if (Constant.location != null) {
			Constant.LAT = Constant.location.getLatitude();
			Constant.LONG = Constant.location.getLongitude();

			Log.d("location", Constant.LAT + "-" + Constant.LONG);

			Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
			List<Address> addresses = null;
			try {
				addresses = gcd.getFromLocation(Constant.LAT, Constant.LONG, 1);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (addresses != null && addresses.size() > 0) {
				Constant.CURRENT_COUNTRY = addresses.get(0).getCountryName();
				Constant.COUNTRY_CODE = addresses.get(0).getCountryCode();

				Constant.address = addresses.get(0);

				loadHomeBanner();
			}

			if (pd != null && pd.isShowing())
				pd.dismiss();
			((EditText) v.findViewById(R.id.edt_select_country))
					.setText(Constant.CURRENT_COUNTRY);

		} else {

			loadHomeBanner();
			// Constant.CURRENT_COUNTRY =
			// getActivity().getResources().getConfiguration().locale.getDisplayCountry();

			if (pd != null)
				pd.dismiss();
			((EditText) v.findViewById(R.id.edt_select_country))
					.setText(Constant.CURRENT_COUNTRY);

		}

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		Intent intent = null;
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopRight:
			// ((BaseContainerFragment)getParentFragment()).replaceFragment(new
			// AddDrinkFragment(), true);
			break;
		case R.id.selectCatBtn:
			intent = new Intent(getActivity(), CountryList.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.dealBtn:

			if (editText.getText().toString() != null
					&& !editText.getText().toString().equals("")
					&& editText.getText().toString().trim().length() > 0) {
				OfferListFragment offerListFragment = new OfferListFragment();
				bundle = new Bundle();
				bundle.putBoolean("isComesFromHome", true);
				offerListFragment.setArguments(bundle);
				((HomeContainerFragment) getParentFragment()).replaceFragment(
						offerListFragment, true, "HOME");
			} else {
				Utils.showDialog(getActivity(),
						getString(R.string.select_country));
			}

			break;
		case R.id.eventBtn:

			if (editText.getText().toString() != null
					&& !editText.getText().toString().equals("")
					&& editText.getText().toString().trim().length() > 0) {
				EventsListtFragment offerListFragment = new EventsListtFragment();
				bundle = new Bundle();
				bundle.putBoolean("isComesFromHome", true);
				offerListFragment.setArguments(bundle);
				((HomeContainerFragment) getParentFragment()).replaceFragment(
						offerListFragment, true, "HOME");
			} else {
				Utils.showDialog(getActivity(),
						getString(R.string.select_country));
			}

			break;
		case R.id.searchBusinessBtn:

			if (serchEditText != null && serchEditText.getText() != null
					&& serchEditText.getText().toString().length() > 0) {

				Utils.hideKeyboard(getActivity(), serchEditText);

				try {
					Thread.currentThread().sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				UserListFragment userListFragment = new UserListFragment();
				bundle = new Bundle();
				bundle.putBoolean("isComesFromHome", true);
				bundle.putString("seacrchKeyWord", serchEditText.getText()
						.toString());
				userListFragment.setArguments(bundle);
				((HomeContainerFragment) getParentFragment()).replaceFragment(
						userListFragment, true, "HOME");
			}
			break;

		case R.id.btn_signin:
			intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);

			break;
		case R.id.btn_logout:

			/*
			 * FragmentManager manager =
			 * getActivity().getSupportFragmentManager();
			 * 
			 * System.out.println("------------size----------------"+
			 * manager.getBackStackEntryCount());
			 */
			// manager.popBackStack("My Profile",FragmentManager.POP_BACK_STACK_INCLUSIVE);

			/*
			 * for (int
			 * i=0;i<getChildFragmentManager().getBackStackEntryCount();i++) {
			 * System.out.println("------------i----------------"+i);
			 * ((BaseContainerFragment)
			 * getChildFragmentManager().findFragmentByTag
			 * (HomeActivity.TAB_5_TAG)).popFragment(); }
			 */

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setCancelable(true);
			builder.setMessage("Do you want to Logout?");
			builder.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							// ((ProfileContainerFragment)
							// getFragmentManager().findFragmentByTag(HomeActivity.TAB_5_TAG)).popFragment();

							AppPrefrence.setLogin(getActivity(), false);
							signOutLayout.setVisibility(View.GONE);
							signInLayout.setVisibility(View.VISIBLE);
							registerLayout.setVisibility(View.VISIBLE);
							dialog.dismiss();

						}
					});

			builder.setNegativeButton("No",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

						}
					});
			builder.create().show();

			break;
		case R.id.btn_register:
			intent = new Intent(getActivity(), RegisterActivity.class);
			startActivity(intent);

			break;
		case R.id.directoryBtn:

			/*
			 * HomeActivity tabs = (HomeActivity) getActivity().getParent();
			 * tabs.getTabHost().setCurrentTab(6);
			 */

			if (editText.getText().toString() != null
					&& !editText.getText().toString().equals("")
					&& editText.getText().toString().trim().length() > 0) {
				((HomeContainerFragment) getParentFragment()).replaceFragment(
						new CategoryFragment(), true, "HOME");
			} else {
				Utils.showDialog(getActivity(),
						getString(R.string.select_country));
			}

			break;
		case R.id.itemForSaleBtn:
			ItemsForSaleFragment forSaleFragment = new ItemsForSaleFragment();
			bundle = new Bundle();

			bundle.putBoolean("isComesFromHome", true);
			forSaleFragment.setArguments(bundle);
			((HomeContainerFragment) getParentFragment()).replaceFragment(
					forSaleFragment, true, "HOME");

			break;

		default:
			break;
		}

	}

}

/*
 * if (Constant.location != null) { Constant.LAT =
 * Constant.location.getLatitude(); Constant.LONG =
 * Constant.location.getLongitude();
 * 
 * Constant.CURRENT_COUNTRY = Utils.getCountry(getActivity(), Constant.LAT,
 * Constant.LONG); getActivity().runOnUiThread(new Runnable() {
 * 
 * @Override public void run() { if (pd != null && pd.isShowing()) pd.dismiss();
 * ((EditText) v.findViewById(R.id.edt_select_country))
 * .setText(Constant.CURRENT_COUNTRY);
 * 
 * } });
 * 
 * } else { Log.d("else location", Constant.LAT + "-" + Constant.LONG);
 * getActivity().runOnUiThread(new Runnable() {
 * 
 * @Override public void run() { if (pd != null && pd.isShowing()) pd.dismiss();
 * 
 * } }); }
 */