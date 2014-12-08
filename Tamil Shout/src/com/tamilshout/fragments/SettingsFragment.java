package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.LoginActivity;
import com.tamilshout.model.SettingsBean;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class SettingsFragment extends BaseFragment {

	boolean create = false;
	ArrayList<Object> drinkList;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem;
	private ProgressDialog pd;
	String response;
	ArrayList<SettingsBean> settingsBeans;
	JSONObject jsonObject = null;
	ListView listView;
	ToggleButton outerToggleBtn;
	NotificationAdapter objAdapter;
	String catId;
	boolean isStatus, flagForChecked = false;
	Button bttn;
	Thread t1,t2;
	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		 v = inflater.inflate(R.layout.settings_fragment, null);

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

		} else {

			setHeader(v, "Settings");
			listView = (ListView) v.findViewById(R.id.listView1);

			outerToggleBtn = (ToggleButton) v.findViewById(R.id.outerToggleBtn);

			outerToggleBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setAlertNotificationForALLCategory();

				}
			});

			getAllNotifiactionList(true);
		}

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopRight:
			// ((BaseContainerFragment)getParentFragment()).replaceFragment(new
			// AddDrinkFragment(), true);
			break;

		default:
			break;
		}

	}

	private void getAllNotifiactionList(boolean flag) {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			if (flag) {
				pd = ProgressDialog.show(getActivity(), "", ""
						+ getString(R.string.processing));
				pd.setCancelable(true);
			}

			try {

				jsonObject = new JSONObject();
				jsonObject.put("id", AppPrefrence.getProfile(getActivity())
						.getUserid());

				t1=new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "CategoryNotificationList",
								jsonObject.toString());

						if(v!=null)
						{
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObject(response);

								if (settingsBeans != null
										&& settingsBeans.size() > 0) {
									listView.setAdapter(null);
									objAdapter = new NotificationAdapter(
											getActivity(), settingsBeans);
									listView.setAdapter(objAdapter);
									objAdapter.notifyDataSetChanged();

									outerToggleBtn.setChecked(flagForChecked);
								}

								if (pd.isShowing())
									pd.dismiss();
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

	private void setAlertNotificationForALLCategory() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {
				jsonObject = new JSONObject();
				if (outerToggleBtn.isChecked()) {
					jsonObject.put("appuserid",
							AppPrefrence.getProfile(getActivity()).getUserid());
					jsonObject.put("categoryid", 0);
					jsonObject.put("status", true);
				} else {
					jsonObject.put("appuserid",
							AppPrefrence.getProfile(getActivity()).getUserid());
					jsonObject.put("categoryid", 0);
					jsonObject.put("status", false);
				}

				t2=new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "AddUpdateAllCategoryNotification",
								jsonObject.toString());

						if(v!=null)
						{
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								getAllNotifiactionList(false);

							}

						});
						}

					}
				});t2.start();
			} catch (Exception exception) {
				if (pd != null && pd.isShowing())
					pd.dismiss();
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));

	}

	private void setAlertNotificationForCategory() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {
				jsonObject = new JSONObject();

				jsonObject.put("appuserid",
						AppPrefrence.getProfile(getActivity()).getUserid());
				jsonObject.put("categoryid", catId);
				jsonObject.put("status", isStatus);

				new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "AddUpdateCategoryNotification",
								jsonObject.toString());

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								getAllNotifiactionList(false);

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

	private void parsJsonObject(String response) {
		if (settingsBeans != null)
			settingsBeans.clear();

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<SettingsBean>>() {
			}.getType();

			try {
				settingsBeans = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);

				if (settingsBeans != null && settingsBeans.size() > 0) {
					for (SettingsBean bean : settingsBeans) {
						if (!bean.isStatus()) {
							flagForChecked = false;
							break;
						} else {
							flagForChecked = true;
						}

					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class NotificationAdapter extends BaseAdapter {
		Context context;
		ArrayList<SettingsBean> arrayList;

		public NotificationAdapter(Context context,
				ArrayList<SettingsBean> arrayList) {
			this.context = context;
			this.arrayList = arrayList;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			View row = convertView;

			if (row == null) {
				row = getActivity().getLayoutInflater().inflate(
						R.layout.settings_item, parent, false);
			}

			TextView itemNameTxt = (TextView) row
					.findViewById(R.id.categoryTxt);
			ToggleButton toggleBtn = (ToggleButton) row
					.findViewById(R.id.toggleBtn);

			itemNameTxt.setText(arrayList.get(position).getCategoryname());
			toggleBtn.setChecked(arrayList.get(position).isStatus());

			toggleBtn.setTag(position);

			toggleBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					catId = arrayList.get(
							Integer.parseInt(v.getTag().toString()))
							.getCategoryid();
					isStatus = ((ToggleButton) v).isChecked();

					setAlertNotificationForCategory();

				}
			});

			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {// BusinessUserProfile

				}
			});

			return row;
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
		if (t2 != null)
			t2.interrupt();
		Thread.currentThread().interrupt(); 

		v = null;

	}


}
