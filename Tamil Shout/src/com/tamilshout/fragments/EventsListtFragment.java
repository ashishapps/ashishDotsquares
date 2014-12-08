package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.model.EventBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class EventsListtFragment extends BaseFragment {

	boolean create = false;
	ArrayList<Object> drinkList;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem;
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	ArrayList<EventBean> eventList, temp;
	EditText search_edit_text;
	ListView listView;
	ItemForSaleAdapter objAdapter;
	Bundle args;
	private Button btnLoadMore;
	private int pageNo = 1;
	View v;
	Thread t1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.event_list_fragment, null);
		temp = new ArrayList<EventBean>();

		args = getArguments();
		if (args != null && args.containsKey("isComesFromHome")
				&& args.getBoolean("isComesFromHome", false))
			setLeftButton(v, R.drawable.btn_back);

		setHeader(v, "Events");
		// setRightButton(v, R.drawable.btn_filter);

		listView = (ListView) v.findViewById(R.id.listView1);

		btnLoadMore = new Button(getActivity());
		btnLoadMore.setText("Load More");

		// Adding Load More button to lisview at bottom

		listView.setFooterDividersEnabled(false);
		btnLoadMore.setTextAppearance(getActivity(),
				android.R.style.TextAppearance_Medium);

		btnLoadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pageNo = pageNo + 1;

				geteventList();

			}
		});
		search_edit_text = (EditText) v.findViewById(R.id.edt_search);
		search_edit_text
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							Utils.hideKeyboard(getActivity(), search_edit_text);
							return true;
						}
						return false;
					}
				});
		search_edit_text.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				search(s);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

		});

		return v;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Utils.hideKeyboard(getActivity(), search_edit_text);

		try {
			Thread.currentThread().sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (listView == null) {
			listView = (ListView) v.findViewById(R.id.listView1);
			// LoadMore button
			btnLoadMore = new Button(getActivity());
			btnLoadMore.setText("Load More");

			// Adding Load More button to lisview at bottom
			listView.addFooterView(btnLoadMore);
			listView.setFooterDividersEnabled(false);
			btnLoadMore.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Medium);
		}

		pageNo = 1;
		if (eventList != null)
			eventList.clear();
		if (temp != null)
			temp.clear();

		geteventList();
	}

	public void search(CharSequence str) {

		if (str.length() > 0) {
			temp.clear();
			if (eventList != null) {
				listView.removeFooterView(btnLoadMore);
				for (Iterator it = eventList.iterator(); it.hasNext();) {
					EventBean type = (EventBean) it.next();
					if (type.getEventname().toLowerCase()
							.contains(str.toString().toLowerCase()))
						temp.add(type);
				}
			}

			System.out.println("------------" + temp);
			listView.setAdapter(null);
			objAdapter = new ItemForSaleAdapter(getActivity(), temp);
			listView.setAdapter(objAdapter);

		} else {
			if (eventList != null && eventList.size() > 0) {

				if (eventList.size() == 10)
					listView.addFooterView(btnLoadMore);

				else
					listView.removeFooterView(btnLoadMore);

				listView.setAdapter(null);
				ItemForSaleAdapter objAdapter = new ItemForSaleAdapter(
						getActivity(), eventList);
				listView.setAdapter(objAdapter);
			}
		}
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopRight:
			String names[] = { "All", "Vehicles", "Businesses",
					"Clothing & Accessories", "Books CDs & Hobbies",
					"Electronic & Computer", "Poperties", "Home & Furniture",
					"Shop & Equipment", "Other Items" };
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View convertView = (View) inflater.inflate(
					R.layout.dialog_custom_titile, null);
			alertDialog.setView(convertView);
			alertDialog.setTitle("Select category");

			alertDialog.setCancelable(true);
			ListView lv = (ListView) convertView.findViewById(R.id.listView1);
			lv.setSelection(Constant.SELECTED_FILTER);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1, names);
			lv.setAdapter(adapter);
			final AlertDialog ad = alertDialog.show();

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ad.dismiss();
					Constant.SELECTED_FILTER = arg2;
					geteventList();
				}
			});

			break;
		case R.id.btnTopLeft:
			((HomeActivity) getActivity()).onBackPressed();
			break;

		default:
			break;
		}

	}

	private void geteventList() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();
				if (args != null && args.containsKey("isComesFromHome")
						&& args.getBoolean("isComesFromHome", false)) {
					if (Constant.SELECTED_COUNTRY_NAME != null
							&& Constant.SELECTED_COUNTRY_NAME.length() > 0)
						jsonObject.put("countryname",
								Constant.SELECTED_COUNTRY_NAME);
					else
						jsonObject.put("countryname", Constant.CURRENT_COUNTRY);
				} else
					jsonObject.put("id", 0);

				if (Constant.SELECTED_COUNTRY_NAME != null
						&& Constant.CURRENT_COUNTRY != null
						&& Constant.SELECTED_COUNTRY_NAME
								.equals(Constant.CURRENT_COUNTRY)) {
					jsonObject.put("issamecountry", true);
					jsonObject.put("distance", 1000);
					jsonObject.put("orderbyprm", "distance");
				} else {
					if (Constant.SELECTED_COUNTRY_NAME == null) {
						jsonObject.put("issamecountry", true);
						jsonObject.put("distance", 1000);
						jsonObject.put("orderbyprm", "distance");
					} else {
						jsonObject.put("issamecountry", false);
						jsonObject.put("distance", 0);
						jsonObject.put("orderbyprm", "eventname");
					}

				}

				jsonObject.put("isorderbyasc", true);

				jsonObject.put("latitude", Constant.LAT);
				jsonObject.put("longitude", Constant.LONG);
				jsonObject.put("pageno", pageNo);
				jsonObject.put("pagesize", 10);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "EventListByCountry",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObject(response);

									if (eventList != null
											&& eventList.size() > 0) {

										int currentPosition = listView
												.getFirstVisiblePosition();

										if (temp.size() == 10)
											listView.addFooterView(btnLoadMore);

										else
											listView.removeFooterView(btnLoadMore);

										listView.setAdapter(null);
										ItemForSaleAdapter objAdapter = new ItemForSaleAdapter(
												getActivity(), eventList);
										listView.setAdapter(objAdapter);

										/*
										 * listView.setSelectionFromTop(
										 * currentPosition + 1, 0);
										 */

										if (pd != null && pd.isShowing())
											pd.dismiss();
									} else {
										if (pd != null && pd.isShowing())
											pd.dismiss();
										Utils.showDialog(
												getActivity(),
												getString(R.string.no_record),
												"",
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {
														((HomeActivity) getActivity())
																.onBackPressed();

													}

												}, true).show();
									}

								}

							});
						}
					}
				});
				t1.start();
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

			Type listType = new TypeToken<ArrayList<EventBean>>() {
			}.getType();

			try {

				if (temp != null)
					if (eventList == null)
						temp.clear();
				eventList = new ArrayList<EventBean>();

				temp = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);

				eventList.addAll(temp);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class ItemForSaleAdapter extends BaseAdapter {
		Context context;
		ArrayList<EventBean> arrayList;
		ViewHolder viewHolder;
		ImageLoader imageLoader = new ImageLoader();

		public ItemForSaleAdapter(Context context,
				ArrayList<EventBean> arrayList) {
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
						R.layout.event_list_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.itemNameTxt = (TextView) row
						.findViewById(R.id.itemTitleTxt);
				viewHolder.dateTxt = (TextView) row.findViewById(R.id.dateTxt);
				viewHolder.dateStartOn = (TextView) row
						.findViewById(R.id.dateStartOn);
				viewHolder.addressTx = (TextView) row
						.findViewById(R.id.addressTxt);
				viewHolder.distanceTxt = (TextView) row
						.findViewById(R.id.distanceTxt);
				viewHolder.business_img = (ImageView) row
						.findViewById(R.id.business_img);
				row.setTag(viewHolder);

			} else
				viewHolder = (ViewHolder) row.getTag();

			viewHolder.itemNameTxt.setText(arrayList.get(position)
					.getEventname());
			viewHolder.position = "" + position;
			viewHolder.dateTxt.setText(arrayList.get(position).getEventendon());

			double dist = Utils.roundMyData(
					Double.parseDouble(arrayList.get(position).getDistance()
							.toString()), 2);

			if (Constant.SELECTED_COUNTRY_NAME != null
					&& Constant.CURRENT_COUNTRY != null
					&& Constant.SELECTED_COUNTRY_NAME
							.equals(Constant.CURRENT_COUNTRY)) {

				viewHolder.distanceTxt.setVisibility(View.VISIBLE);
				if (arrayList.get(position).getDistance().toString()
						.contains(".")) {

					viewHolder.distanceTxt.setText(dist + " MI");
				} else {
					viewHolder.distanceTxt.setText(arrayList.get(position)
							.getDistance().toString()
							+ " MI");
				}
			} else {

				if (Constant.SELECTED_COUNTRY_NAME == null) {
					viewHolder.distanceTxt.setVisibility(View.VISIBLE);
					if (arrayList.get(position).getDistance().toString()
							.contains(".")) {

						viewHolder.distanceTxt.setText(dist + " MI");
					} else {
						viewHolder.distanceTxt.setText(arrayList.get(position)
								.getDistance().toString()
								+ " MI");
					}
				} else {
					viewHolder.distanceTxt.setVisibility(View.GONE);
				}

			}

			viewHolder.dateStartOn.setText(arrayList.get(position)
					.getEventstarton());

			String addressStr = "";
			if (arrayList.get(position).getAddress1() != null
					&& arrayList.get(position).getAddress1().length() > 0)
				addressStr = arrayList.get(position).getAddress1();
			if (arrayList.get(position).getAddress2() != null
					&& arrayList.get(position).getAddress2().length() > 0)
				addressStr = addressStr + ", "
						+ arrayList.get(position).getAddress2();

			if (addressStr.startsWith(","))
				addressStr = addressStr.substring(1);
			if (addressStr.endsWith(","))
				addressStr = addressStr.substring(0, addressStr.length() - 1);

			viewHolder.addressTx.setText(addressStr);

			String timestamp = arrayList.get(position).getTime();
			String imgUrl = getString(R.string.image_url) + "Event/image/"
					+ arrayList.get(position).getEventid() + "/medium.jpg?id="
					+ timestamp;

			if (imgUrl.length() > 0
					&& (imgUrl.contains(".jpg") || imgUrl.contains(".gif")
							|| imgUrl.contains(".png") || imgUrl
								.contains(".jpeg"))) {

				Bitmap bitmap = imageLoader.loadImage(imgUrl,
						new ImageLoader.ImageLoadedListener() {

							@Override
							public void imageLoaded(Bitmap imageBitmap) {
								if (imageBitmap != null) {
									viewHolder.business_img
											.setImageBitmap(imageBitmap);

									notifyDataSetChanged();
								}

							}
						});
				if (bitmap != null)
					viewHolder.business_img.setImageBitmap(bitmap);
				else
					viewHolder.business_img.setImageDrawable(getResources()
							.getDrawable(R.drawable.no_image));

			}

			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {// BusinessUserProfile

					if (Utils.isOnline()) {
						EventDetail fragment = new EventDetail();
						Bundle bundle = new Bundle();
						bundle.putSerializable(
								"eventid",
								arrayList.get(
										Integer.parseInt(((ViewHolder) v
												.getTag()).position))
										.getEventid());
						fragment.setArguments(bundle);
						if (args != null && args.containsKey("isComesFromHome")
								&& args.getBoolean("isComesFromHome", false)) {

							bundle.putBoolean("isComesFromHome", true);
							fragment.setArguments(bundle);
							((HomeContainerFragment) getParentFragment())
									.replaceFragment(fragment, true, "HOME");
						} else {
							Utils.showDialog(getActivity(),
									getString(R.string.no_network_available));
						}
					}
				}
			});

			return row;
		}

		class ViewHolder {
			TextView itemNameTxt, dateTxt, dateStartOn, addressTx, distanceTxt;
			ImageView business_img;
			String position;
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
