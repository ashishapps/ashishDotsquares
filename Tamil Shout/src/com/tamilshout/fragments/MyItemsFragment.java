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
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.LoginActivity;
import com.tamilshout.model.BusinessUser;
import com.tamilshout.model.ItemForSaleBean;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class MyItemsFragment extends BaseFragment {

	boolean create = false;
	ArrayList<Object> drinkList;
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
	private Button btnLoadMore;
	private int pageNo = 1;
	View v;
	Thread t1, t2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		System.out.println("------MyItemsFragment-----onCreateView------");

		v = inflater.inflate(R.layout.myitems_fragment, null);
		setHeader(v, "My Items");

		Constant.SELECTED_FILTER = 0;
		Constant.SELECTED_MY_ITEMS_FILTER = 0;

		args = getArguments();
		if (args != null && args.containsKey("isComesFromHome")
				&& args.getBoolean("isComesFromHome", false))
			setLeftButton(v, R.drawable.btn_back);

		// setRightButton(v, R.drawable.btn_filter);

		setTouchNClick(v, R.id.addItemBtn);

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

				getMyItemsList();

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
				lv.setSelection(Constant.SELECTED_MY_ITEMS_FILTER);
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
						Constant.SELECTED_MY_ITEMS_FILTER = arg2;
						edt_select_cat
								.setText(names[Constant.SELECTED_MY_ITEMS_FILTER]);

						pageNo = 1;
						if (itemForSaleList != null)
							itemForSaleList.clear();
						getMyItemsList();
					}
				});

			}
		});

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

			search_edit_text.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {
					search(s);

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {

				}

			});

			temp = new ArrayList<ItemForSaleBean>();

			search_edit_text
					.setOnEditorActionListener(new OnEditorActionListener() {

						@Override
						public boolean onEditorAction(TextView v, int actionId,
								KeyEvent event) {
							if (actionId == EditorInfo.IME_ACTION_DONE) {
								Utils.hideKeyboard(getActivity(),
										search_edit_text);
								return true;
							}
							return false;
						}
					});

			pageNo = 1;
			if (itemForSaleList != null)
				itemForSaleList.clear();
			if (temp != null)
				temp.clear();

			getMyItemsList();

		}

		return v;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		edt_select_cat.setText("");
		pageNo = 1;

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
		 * case R.id.btnTopRight:
		 * 
		 * String names[] = { "All", "Vehicles", "Businesses",
		 * "Clothing & Accessories", "Books CDs & Hobbies",
		 * "Electronic & Computer", "Poperties", "Home & Furniture",
		 * "Shop & Equipment", "Other Items" }; final AlertDialog.Builder
		 * alertDialog = new AlertDialog.Builder( getActivity()); LayoutInflater
		 * inflater = getActivity().getLayoutInflater(); View convertView =
		 * (View) inflater.inflate( R.layout.dialog_custom_titile, null);
		 * alertDialog.setView(convertView); alertDialog.setTitle("Filter By");
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
		 * arg2, long arg3) { ad.dismiss(); Constant.SELECTED_MY_ITEMS_FILTER =
		 * arg2; getMyItemsList(); } });
		 * 
		 * break;
		 */
		case R.id.btnTopLeft:
			((HomeActivity) getActivity()).onBackPressed();
			break;
		case R.id.addItemBtn:
			if (Constant.imagePathHashMap != null) {
				Constant.imagePathHashMap.clear();
				Constant.imagePathHashMap = null;
			}
			if (Constant.imagePathList != null) {
				Constant.imagePathList.clear();
				Constant.imagePathList = null;
			}
			if (Constant.itemImageList != null) {
				Constant.itemImageList.clear();
				Constant.itemImageList = null;
			}

			((MyItemsContainerFragment) getParentFragment()).replaceFragment(new AddMyItemFragment(), true, "My Items");
			break;

		default:
			break;
		}

	}

	private void getMyItemsList() {

		if (Utils.isNetworkAvailableNew(getActivity())) {
			
			
			if(pd!=null && !pd.isShowing())
			{

			pd = ProgressDialog.show(getActivity(), "", ""+ getString(R.string.processing));
			pd.setCancelable(true);
			}

			try {

				jsonObject = new JSONObject();
				jsonObject.put("appuserid",
						AppPrefrence.getProfile(getActivity()).getUserid()
								.toString());
				jsonObject.put("categoryid", Constant.SELECTED_MY_ITEMS_FILTER);
				jsonObject.put("isorderbyasc", true);
				jsonObject.put("pageno", pageNo);
				jsonObject.put("pagesize", 10);
				jsonObject.put("orderbyprm", "itemname");

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url) + "GetMyItems",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObject(response);
									if (itemForSaleList != null
											&& itemForSaleList.size() > 0) {

										int currentPosition = listView
												.getFirstVisiblePosition();

										if (temp.size() == 10)
											listView.addFooterView(btnLoadMore);

										else
											listView.removeFooterView(btnLoadMore);

										listView.setAdapter(null);
										ItemForSaleAdapter objAdapter = new ItemForSaleAdapter(
												getActivity(), itemForSaleList);
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
										Utils.showDialog(getActivity(),
												getString(R.string.no_record));
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
		ViewHolder viewHolder;
		ArrayList<ItemForSaleBean> arrayList;

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
			// TODO Auto-generated method stub

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
						MyItemDetail fragment = new MyItemDetail();
						Bundle bundle = new Bundle();
						// bundle.putSerializable("ite",
						// arrayList.get(Integer.parseInt(((ViewHolder)
						// v.getTag()).itemId)));
						bundle.putString(
								"itemid",
								arrayList.get(
										Integer.parseInt(((ViewHolder) v
												.getTag()).itemId)).getItemid());
						fragment.setArguments(bundle);
						((MyItemsContainerFragment) getParentFragment())
								.replaceFragment(fragment, true, "My Items");
					} else {
						Utils.showDialog(getActivity(),
								getString(R.string.no_network_available));
					}

				}
			});

			row.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					int position = Integer.parseInt(((ViewHolder) v.getTag()).itemId);
					showLongPressOptions(position);
					return false;
				}
			});

			return row;
		}

		class ViewHolder {
			TextView itemNameTxt, addedOnTxt, priceTxt, dateTxt;
			ImageView business_img;
			String itemId;
		}
	}

	public void showLongPressOptions(final int id) {
		String[] items = { "Remove Item", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Select one");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// Do something with the selection
				if (item == 0) {
					Utils.showDialog(getActivity(), "Are you sure!",
							"Do you want to remove "
									+ itemForSaleList.get(id).getItemName()
									+ " from my items?", "Yes", "No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									deleteMyItems(id);

									/*
									 * dbHandler.removeFromFav(clubList.get(id).
									 * getId()); setClubList();
									 */
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							});

				}
				if (item == 1) {
					dialog.dismiss();
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void deleteMyItems(int id) {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			
			  pd = ProgressDialog.show(getActivity(), "", "" + getString(R.string.processing));
			  pd.setCancelable(false);
			 

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", itemForSaleList.get(id).getItemid());

				new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "DeleteItemForSale",
								jsonObject.toString());

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								// parsJsonObject(response);

								pageNo = 1;
								if (itemForSaleList != null)
									itemForSaleList.clear();
								if (temp != null)
									temp.clear();
								

								getMyItemsList();

								
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
