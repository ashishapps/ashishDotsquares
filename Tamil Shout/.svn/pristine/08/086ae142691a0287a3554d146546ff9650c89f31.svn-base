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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.model.CategoryBean;
import com.tamilshout.model.OfferBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class OfferListFragment extends BaseFragment {
	ArrayList<String> names = new ArrayList<String>();
	boolean create = false;
	String metodName = "";
	ArrayList<Object> drinkList;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem;
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	ArrayList<OfferBean> offerList, temp;
	EditText select_cat_edt;
	ListView listView;
	ItemForSaleAdapter objAdapter;
	Bundle args;
	private Button btnLoadMore;
	private int pageNo = 1;
	View v;
	ArrayList<CategoryBean> categoryBeans;
	Thread t1,t2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.offer_list_fragment, null);
		temp = new ArrayList<OfferBean>();

		args = getArguments();

		Constant.SELECTED_OFFER_FILTER = 0;

		/*
		 * if (args != null && args.containsKey("isComesFromHome") &&
		 * args.getBoolean("isComesFromHome", false))
		 */

		setLeftButton(v, R.drawable.btn_back);

		setHeader(v, "Offer List");
		// setRightButton(v, R.drawable.btn_filter);

		listView = (ListView) v.findViewById(R.id.listView1);

		// LoadMore button
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

				getOfferList("1000");

			}
		});

		select_cat_edt = (EditText) v.findViewById(R.id.edt_select_cat);

		select_cat_edt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						getActivity());
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View convertView = (View) inflater.inflate(
						R.layout.dialog_custom_titile, null);
				alertDialog.setView(convertView);
				// alertDialog.setTitle("Filter By");

				alertDialog.setCancelable(true);
				ListView lv = (ListView) convertView
						.findViewById(R.id.listView1);
				lv.setSelection(Constant.SELECTED_FILTER);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						getActivity(), android.R.layout.simple_list_item_1,
						names);
				lv.setAdapter(adapter);
				final AlertDialog ad = alertDialog.show();

				lv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						ad.dismiss();

						Constant.SELECTED_OFFER_FILTER = arg2;
						select_cat_edt.setText(names
								.get(Constant.SELECTED_OFFER_FILTER));

						if (arg2 > 0) {
							Constant.SELECTED_OFFER_FILTER = Integer
									.parseInt(categoryBeans.get(arg2 - 1)
											.getId().toString());
						}
						if (offerList != null)
							offerList.clear();
						getOfferList("1000");
					}
				});

			}
		});

		if (Constant.SELECTED_COUNTRY_NAME != null
				&& Constant.CURRENT_COUNTRY != null
				&& Constant.SELECTED_COUNTRY_NAME
						.equals(Constant.CURRENT_COUNTRY)) {

			((Button) v.findViewById(R.id.distanceBtn))
					.setVisibility(View.VISIBLE);
		} else {

			if (Constant.SELECTED_COUNTRY_NAME == null) {
				((Button) v.findViewById(R.id.distanceBtn))
						.setVisibility(View.VISIBLE);
			} else {
				((Button) v.findViewById(R.id.distanceBtn))
						.setVisibility(View.INVISIBLE);
			}

		}

		setTouchNClick(v, R.id.distanceBtn);

		pageNo = 1;
		if (offerList != null)
			offerList.clear();
		if (temp != null)
			temp.clear();

		getOfferList("1000");

		return v;
	}

	public void search(CharSequence str) {

		if (temp != null)
			temp.clear();

		for (Iterator it = offerList.iterator(); it.hasNext();) {
			OfferBean type = (OfferBean) it.next();
			if (type.getTitle().toLowerCase()
					.contains(str.toString().toLowerCase()))
				temp.add(type);
		}

		System.out.println("------------" + temp);
		listView.setAdapter(null);
		objAdapter = new ItemForSaleAdapter(getActivity(), temp);
		listView.setAdapter(objAdapter);
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
		select_cat_edt.setText("");

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopLeft:
			((HomeActivity) getActivity()).onBackPressed();
			break;

		case R.id.distanceBtn:

			final String[] namess = { "1 MILE", "5 MILES", "10 MILES",
					"50 MILES", "100 MILES", "NATIONAL" };
			AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(
					getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View convertView = (View) inflater.inflate(
					R.layout.dialog_custom_titile, null);
			alertDialog1.setView(convertView);
			// alertDialog.setTitle("Sort y Distance");

			alertDialog1.setCancelable(true);
			ListView lv = (ListView) convertView.findViewById(R.id.listView1);
			lv.setSelection(Constant.SELECTED_FILTER);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1, namess);
			lv.setAdapter(adapter);
			final AlertDialog add = alertDialog1.show();

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					add.dismiss();
					int miles = 0;

					if (!namess[arg2].split(" ")[0]
							.equalsIgnoreCase("national")) {
						miles = Integer.parseInt(namess[arg2].split(" ")[0]);

					} else {
						miles = 1000;
					}

					pageNo = 1;

					if (offerList != null)
						offerList.clear();

					getOfferList("" + miles);
				}
			});

			// sortByDistance();
			break;

		default:
			break;
		}

	}

	private void getOfferList(String distance) {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();
				if (args != null && !args.containsKey("isFromHomeBusiness")) {
					metodName = "GetOfferListByCountryName";

					if (Constant.SELECTED_COUNTRY_NAME != null
							&& Constant.SELECTED_COUNTRY_NAME.length() > 0)
						jsonObject.put("countryname",
								Constant.SELECTED_COUNTRY_NAME);
					else
						jsonObject.put("countryname", Constant.CURRENT_COUNTRY);

				} else {
					jsonObject.put("id", args.getString("business_id"));
					metodName = "GetOfferListByBusiness";
				}

				if (Constant.SELECTED_COUNTRY_NAME != null
						&& Constant.CURRENT_COUNTRY != null
						&& Constant.SELECTED_COUNTRY_NAME
								.equals(Constant.CURRENT_COUNTRY)) {

					jsonObject.put("orderbyprm", "distance");
					jsonObject.put("distance", "" + distance);
					jsonObject.put("issamecountry", true);
				} else {

					if (Constant.SELECTED_COUNTRY_NAME == null) {
						jsonObject.put("orderbyprm", "distance");
						jsonObject.put("distance", "" + distance);
						jsonObject.put("issamecountry", true);
					} else {
						jsonObject.put("orderbyprm", "title");
						jsonObject.put("distance", "0");
						jsonObject.put("issamecountry", false);
					}

				}

				jsonObject.put("categoryid", Constant.SELECTED_OFFER_FILTER);
				jsonObject.put("isorderbyasc", true);
				jsonObject.put("pageno", pageNo);
				jsonObject.put("pagesize", 10);
				jsonObject.put("latitude", Constant.LAT);
				jsonObject.put("longitude", Constant.LONG);
				jsonObject.put("pagesize", 10);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url) + metodName,
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObject(response);
									getOfferCategories();

									if (offerList != null
											&& offerList.size() > 0) {

										int currentPosition = listView
												.getFirstVisiblePosition();

										if (temp.size() == 10)
											listView.addFooterView(btnLoadMore);

										else
											listView.removeFooterView(btnLoadMore);

										listView.setAdapter(null);
										ItemForSaleAdapter objAdapter = new ItemForSaleAdapter(
												getActivity(), offerList);
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
										listView.setAdapter(null);
										Utils.showDialog(
												getActivity(),
												getString(R.string.no_record),
												"",
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {
														/*
														 * ((HomeActivity)
														 * getActivity())
														 * .onBackPressed();
														 */
														dialog.dismiss();

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

	private void getOfferCategories() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			if (pd != null && !pd.isShowing()) {
				pd = ProgressDialog.show(getActivity(), "", ""
						+ getString(R.string.processing));
				pd.setCancelable(true);
			}

			try {

				jsonObject = new JSONObject();

				if (Constant.SELECTED_COUNTRY_NAME != null
						&& Constant.SELECTED_COUNTRY_NAME.length() > 0)
					jsonObject.put("countryname",
							Constant.SELECTED_COUNTRY_NAME);
				else
					jsonObject.put("countryname", Constant.CURRENT_COUNTRY);

				t2=new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GetBusinessCategoryList",
								jsonObject.toString());

						if(v!=null)
						{
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								if (names != null)
									names.clear();
								parsJsonCategoryList(response);

								if (categoryBeans != null
										&& categoryBeans.size() > 0) {
									for (int i = 0; i < categoryBeans.size(); i++) {
										names.add(categoryBeans.get(i)
												.getName());
									}
									if (pd != null && pd.isShowing())
										pd.dismiss();

									names.add(0, "All");
								} else {

									if (pd != null && pd.isShowing())
										pd.dismiss();
								}
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
		} else {
			if (pd != null && pd.isShowing())
				pd.dismiss();
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));
		}

	}

	private void parsJsonCategoryList(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<CategoryBean>>() {
			}.getType();

			try {
				categoryBeans = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Utils.showDialog(getActivity(), getString(R.string.server_error));
		}

	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<OfferBean>>() {
			}.getType();

			try {

				if (temp != null)
					temp.clear();
				if (offerList == null)
					offerList = new ArrayList<OfferBean>();

				temp = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);

				offerList.addAll(temp);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class ItemForSaleAdapter extends BaseAdapter {
		Context context;
		ArrayList<OfferBean> arrayList;
		ViewHolder viewHolder;
		ImageLoader imageLoader = new ImageLoader();

		public ItemForSaleAdapter(Context context,
				ArrayList<OfferBean> arrayList) {
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
						R.layout.offer_list_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.itemNameTxt = (TextView) row
						.findViewById(R.id.itemTitleTxt);
				viewHolder.categoryTxt = (TextView) row
						.findViewById(R.id.categoryTxt);
				viewHolder.addedOnTxt = (TextView) row
						.findViewById(R.id.addedOnTxt);
				viewHolder.priceTxt = (TextView) row
						.findViewById(R.id.priceTxt);
				viewHolder.dateTxt = (TextView) row.findViewById(R.id.dateTxt);
				viewHolder.business_img = (ImageView) row
						.findViewById(R.id.business_img);

				row.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) row.getTag();

			}

			viewHolder.itemNameTxt.setText(arrayList.get(position).getTitle());
			viewHolder.priceTxt.setText(arrayList.get(position)
					.getCurrencycode()
					+ " "
					+ arrayList.get(position).getDiscountedPrice()
					+ "/"
					+ arrayList.get(position).getNormalPrice());
			viewHolder.addedOnTxt.setText(arrayList.get(position)
					.getDealValidDate());

			double dist = Utils.roundMyData(
					Double.parseDouble(arrayList.get(position).getDistance()
							.toString()), 2);

			if (Constant.SELECTED_COUNTRY_NAME != null
					&& Constant.CURRENT_COUNTRY != null
					&& Constant.SELECTED_COUNTRY_NAME
							.equals(Constant.CURRENT_COUNTRY)) {

				viewHolder.dateTxt.setVisibility(View.VISIBLE);
				if (arrayList.get(position).getDistance().toString()
						.contains(".")) {

					viewHolder.dateTxt.setText(dist + " MI");
				} else {
					viewHolder.dateTxt.setText(arrayList.get(position)
							.getDistance().toString()
							+ " MI");
				}
			} else {

				if (Constant.SELECTED_COUNTRY_NAME == null) {
					viewHolder.dateTxt.setVisibility(View.VISIBLE);
					if (arrayList.get(position).getDistance().toString()
							.contains(".")) {

						viewHolder.dateTxt.setText(dist + " MI");
					} else {
						viewHolder.dateTxt.setText(arrayList.get(position)
								.getDistance().toString()
								+ " MI");
					}
				} else {
					viewHolder.dateTxt.setVisibility(View.GONE);
				}

			}

			viewHolder.position = "" + position;

			// priceTxt.setText("$" + arrayList.get(position).getPrice());

			String timestamp = arrayList.get(position).getTime();
			String imgUrl = getString(R.string.image_url) + "Offer/"
					+ arrayList.get(position).getId() + "/medium.jpg?id="
					+ timestamp;

			if (arrayList.get(position).getOfferCategoryId() != null
					&& arrayList.get(position).getOfferCategoryId().equals("1")) {

				viewHolder.categoryTxt.setText("Normal Offer");

			} else {
				viewHolder.categoryTxt.setText("Special Offer");
			}

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
						OfferDetail fragment = new OfferDetail();
						Bundle bundle = new Bundle();
						bundle.putSerializable(
								"offerid",
								arrayList.get(
										Integer.parseInt(((ViewHolder) v
												.getTag()).position)).getId());
						fragment.setArguments(bundle);
						if (args != null && args.containsKey("isComesFromHome")
								&& args.getBoolean("isComesFromHome", false)) {

							bundle.putBoolean("isComesFromHome", true);
							fragment.setArguments(bundle);
							((HomeContainerFragment) getParentFragment())
									.replaceFragment(fragment, true, "HOME");
						} else {

							fragment.setArguments(bundle);
							((FavouriteContainerFragment) getParentFragment())
									.replaceFragment(fragment, true,
											"Favourite");
						}
					} else {
						Utils.showDialog(getActivity(),
								getString(R.string.no_network_available));
					}
				}
			});

			return row;
		}

		class ViewHolder {
			TextView itemNameTxt, categoryTxt, addedOnTxt, priceTxt, dateTxt;
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
		if (t2 != null)
			t2.interrupt();
		
		Thread.currentThread().interrupt(); 
		v = null;

	}

}
