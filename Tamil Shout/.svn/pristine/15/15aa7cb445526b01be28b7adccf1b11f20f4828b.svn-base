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
import android.util.Log;
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
import com.tamilshout.model.ItemForSaleBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class ItemsForSaleFragment extends BaseFragment {

	Thread t1;
	boolean create = false;
	ArrayList<Object> drinkList;
	Button btnLoadMore;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem;
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	ArrayList<ItemForSaleBean> itemForSaleList, temp;
	EditText search_edit_text, edt_select_cat;
	ListView listView;
	ItemForSaleAdapter objAdapter;
	Bundle args;
	View v;
	int pageNo = 1, pageSize = 11;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.itemsforsale_fragment, null);
		temp = new ArrayList<ItemForSaleBean>();

		Constant.SELECTED_FILTER = 0;
		Constant.SELECTED_MY_ITEMS_FILTER = 0;

		Log.d("drink", "Drink On Create View");
		Constant.appContext = getActivity();

		args = getArguments();
		if (args != null && args.containsKey("isComesFromHome")
				&& args.getBoolean("isComesFromHome", false))
			setLeftButton(v, R.drawable.btn_back);

		setHeader(v, "Items For Sale");
		// setRightButton(v, R.drawable.btn_filter);

		listView = (ListView) v.findViewById(R.id.listView1);

		// LoadMore button
		btnLoadMore = new Button(getActivity());
		btnLoadMore.setTextColor(getResources().getColor(R.color.black));
		btnLoadMore.setText("Load More");

		// Adding Load More button to lisview at bottom

		listView.setFooterDividersEnabled(false);
		btnLoadMore.setTextAppearance(getActivity(),
				android.R.style.TextAppearance_Medium);

		btnLoadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pageNo = pageNo + 1;

				getItemsForSaleList();

			}
		});

		search_edit_text = (EditText) v.findViewById(R.id.edt_search);
		edt_select_cat = (EditText) v.findViewById(R.id.edt_select_cat);

		edt_select_cat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String names[] = { "All", "Vehicles", "Businesses",
						"Clothing & Accessories", "Books CDs & Hobbies",
						"Electronic & Computer", "Poperties",
						"Home & Furniture", "Shop & Equipment", "Other Items" };
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
						Constant.SELECTED_FILTER = arg2;
						edt_select_cat.setText(names[Constant.SELECTED_FILTER]);

						pageNo = 1;
						if (itemForSaleList != null)
							itemForSaleList.clear();
						getItemsForSaleList();
					}
				});

			}
		});

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

		pageNo = 1;
		if (itemForSaleList != null)
			itemForSaleList.clear();
		if (temp != null)
			temp.clear();
		getItemsForSaleList();

		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		edt_select_cat.setText("");
		pageNo = 1;

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

	public void search(CharSequence str) {
		if (str.length() > 0) {
			temp.clear();
			if (itemForSaleList != null) {
				listView.removeFooterView(btnLoadMore);
				for (Iterator it = itemForSaleList.iterator(); it.hasNext();) {
					ItemForSaleBean type = (ItemForSaleBean) it.next();
					if (type.getItemName().toLowerCase()
							.contains(str.toString().toLowerCase()))
						temp.add(type);
				}

				System.out.println("------------" + temp);
				listView.setAdapter(null);
				objAdapter = new ItemForSaleAdapter(getActivity(), temp);
				listView.setAdapter(objAdapter);
			}
		} else {
			if (itemForSaleList != null && itemForSaleList.size() > 0) {

				if (itemForSaleList.size() == 10)
					listView.addFooterView(btnLoadMore);

				else
					listView.removeFooterView(btnLoadMore);

				listView.setAdapter(null);
				ItemForSaleAdapter objAdapter = new ItemForSaleAdapter(
						getActivity(), itemForSaleList);
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
		/*
		 * case R.id.btnTopRight: String names[] = { "All", "Vehicles",
		 * "Businesses", "Clothing & Accessories", "Books CDs & Hobbies",
		 * "Electronic & Computer", "Poperties", "Home & Furniture",
		 * "Shop & Equipment", "Other Items" }; final AlertDialog.Builder
		 * alertDialog = new AlertDialog.Builder( getActivity()); LayoutInflater
		 * inflater = getActivity().getLayoutInflater(); View convertView =
		 * (View) inflater.inflate( R.layout.dialog_custom_titile, null);
		 * alertDialog.setView(convertView);
		 * alertDialog.setTitle("Select category");
		 * 
		 * alertDialog.setCancelable(true); ListView lv = (ListView)
		 * convertView.findViewById(R.id.listView1);
		 * lv.setSelection(Constant.SELECTED_FILTER); ArrayAdapter<String>
		 * adapter = new ArrayAdapter<String>( getActivity(),
		 * android.R.layout.simple_list_item_1, names); lv.setAdapter(adapter);
		 * final AlertDialog ad = alertDialog.show();
		 * 
		 * lv.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
		 * arg2, long arg3) { ad.dismiss(); Constant.SELECTED_FILTER = arg2;
		 * getItemsForSaleList(); } });
		 * 
		 * break;
		 */
		case R.id.btnTopLeft:
			((HomeActivity) getActivity()).onBackPressed();
			break;

		default:
			break;
		}

	}

	private void getItemsForSaleList() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();
				jsonObject.put("categoryid", Constant.SELECTED_FILTER);
				jsonObject.put("isorderbyasc", false);
				jsonObject.put("pageno", pageNo);
				jsonObject.put("pagesize", 10);
				jsonObject.put("orderbyprm", "AddedOn");

				if (Constant.SELECTED_COUNTRY_NAME != null
						&& Constant.SELECTED_COUNTRY_NAME.length() > 0)
					jsonObject.put("country", Constant.SELECTED_COUNTRY_NAME);
				else
					jsonObject.put("country", Constant.CURRENT_COUNTRY);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GetItemForSaleList",
								jsonObject.toString());
						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObject(response);

									if (itemForSaleList != null
											&& itemForSaleList.size() > 0) {

										if (temp.size() == 10) {
											listView.addFooterView(btnLoadMore);

										} else
											listView.removeFooterView(btnLoadMore);

										int currentPosition = listView
												.getFirstVisiblePosition();

										listView.setAdapter(null);
										ItemForSaleAdapter objAdapter = new ItemForSaleAdapter(
												getActivity(), itemForSaleList);
										listView.setAdapter(objAdapter);

										// Appending new data to menuItems
										// ArrayList

										// Setting new scroll position

										if (pd != null && pd.isShowing())
											pd.dismiss();

									} else {

										if (pd != null && pd.isShowing())
											pd.dismiss();
										listView.removeFooterView(btnLoadMore);

										if (args != null
												&& args.containsKey("isComesFromHome")
												&& args.getBoolean(
														"isComesFromHome",
														false)) {
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
										} else {
											listView.setAdapter(null);
											Utils.showDialog(
													getActivity(),
													getString(R.string.no_record));
										}
									}

									/*
									 * AddvertisementUtils.setAdvertisementView(
									 * getActivity(),addvertiseImage,playImage);
									 */

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

			Type listType = new TypeToken<ArrayList<ItemForSaleBean>>() {
			}.getType();

			try {

				if (temp != null)
					temp.clear();
				if (itemForSaleList == null)
					itemForSaleList = new ArrayList<ItemForSaleBean>();

				temp = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);

				itemForSaleList.addAll(temp);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class ItemForSaleAdapter extends BaseAdapter {
		Context context;
		ArrayList<ItemForSaleBean> arrayList;
		ViewHolder viewHolder;
		ImageLoader imageLoader = new ImageLoader();

		public ItemForSaleAdapter(Context context,
				ArrayList<ItemForSaleBean> arrayList) {
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

			View row = convertView;

			if (row == null) {
				row = getActivity().getLayoutInflater().inflate(
						R.layout.items_for_sale_item, parent, false);

				viewHolder = new ViewHolder();
				viewHolder.itemNameTxt = (TextView) row
						.findViewById(R.id.itemTitleTxt);
				viewHolder.dateTxt = (TextView) row.findViewById(R.id.dateTxt);
				viewHolder.addedOnTxt = (TextView) row
						.findViewById(R.id.addedOnTxt);
				viewHolder.priceTxt = (TextView) row
						.findViewById(R.id.priceTxt);
				viewHolder.business_img = (ImageView) row
						.findViewById(R.id.business_img);

				row.setTag(viewHolder);
			} else
				viewHolder = (ViewHolder) row.getTag();

			viewHolder.itemNameTxt.setText(arrayList.get(position)
					.getItemName());
			viewHolder.dateTxt.setText(arrayList.get(position)
					.getItemcategoryname());
			viewHolder.addedOnTxt.setText(arrayList.get(position).getAddedOn());
			viewHolder.priceTxt.setText(arrayList.get(position)
					.getCurrencycode()
					+ " "
					+ arrayList.get(position).getPrice());

			viewHolder.itemId = "" + position;

			String timestamp = arrayList.get(position).getTime();

			String imgUrl = getString(R.string.image_url) + "ItemForSale/"
					+ arrayList.get(position).getItemid() + "/"
					+ arrayList.get(position).getItempictureid()
					+ "/medium.jpg?id=" + timestamp;

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
						ItemForSaleDetail fragment = new ItemForSaleDetail();
						Bundle bundle = new Bundle();
						bundle.putString(
								"itemid",
								arrayList.get(
										Integer.parseInt(((ViewHolder) v
												.getTag()).itemId)).getItemid());
						fragment.setArguments(bundle);
						if (args != null && args.containsKey("isComesFromHome")
								&& args.getBoolean("isComesFromHome", false)) {

							bundle.putBoolean("isComesFromHome", true);
							fragment.setArguments(bundle);
							((HomeContainerFragment) getParentFragment())
									.replaceFragment(fragment, true, "HOME");
						} else
							((ItemsforSaleContainerFragment) getParentFragment())
									.replaceFragment(fragment, true,
											"Items for Sale");
					} else {
						Utils.showDialog(getActivity(),
								getString(R.string.no_network_available));
					}

				}
			});

			return row;
		}

		public class ViewHolder {
			TextView itemNameTxt, addedOnTxt, priceTxt, dateTxt;
			ImageView business_img;
			String itemId;
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
