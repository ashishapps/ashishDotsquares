package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.model.CategoryBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class CategoryFragment extends BaseFragment {

	boolean create = false;
	ArrayList<CategoryBean> categoryList, temp;
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	CategoryListAdapter objAdapter;
	GridView listView;
	EditText search_edit_text;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem;
	Thread t1;

	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.category_fragment, null);

		temp = new ArrayList<CategoryBean>();
		Constant.appContext = getActivity();
		Log.d("Home", "Category Lsit");

		setHeader(v, "Business Category");
		setLeftButton(v, R.drawable.btn_back);

		setTouchNClick(v, R.id.btnTopLeft);

		listView = (GridView) v.findViewById(R.id.gridView);
		search_edit_text = (EditText) v.findViewById(R.id.edt_search);

		getAllCategoryList();

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

	public void search(CharSequence str) {

		temp.clear();

		for (Iterator it = categoryList.iterator(); it.hasNext();) {
			CategoryBean type = (CategoryBean) it.next();
			if (type.getName().toLowerCase()
					.contains(str.toString().toLowerCase()))
				temp.add(type);
		}

		System.out.println("------------" + temp);
		listView.setAdapter(null);
		objAdapter = new CategoryListAdapter(getActivity(), temp);
		listView.setAdapter(objAdapter);
	}

	private void getAllCategoryList() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();
				System.out.println("==========Constant.COUNTRY_NAME====="
						+ Constant.SELECTED_COUNTRY_NAME);

				if (Constant.SELECTED_COUNTRY_NAME != null
						&& Constant.SELECTED_COUNTRY_NAME.length() > 0)
					jsonObject.put("countryname",
							Constant.SELECTED_COUNTRY_NAME);
				else
					jsonObject.put("countryname", Constant.CURRENT_COUNTRY);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GetBusinessCategoryList",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObject(response);

									if (pd.isShowing())
										pd.dismiss();

									if (categoryList != null
											&& categoryList.size() > 0) {
										listView.setAdapter(null);
										objAdapter = new CategoryListAdapter(
												getActivity(), categoryList);
										listView.setAdapter(objAdapter);
									} else {
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

												}, false).show();
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

			Type listType = new TypeToken<ArrayList<CategoryBean>>() {
			}.getType();

			try {
				categoryList = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);
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
		case R.id.btnTopRight:
			// ((BaseContainerFragment)getParentFragment()).replaceFragment(new
			// AddDrinkFragment(), true);
			break;
		case R.id.btnTopLeft:

			((HomeActivity) getActivity()).onBackPressed();
			break;

		default:
			break;
		}

	}

	private class CategoryListAdapter extends BaseAdapter {
		Context context;
		ArrayList<CategoryBean> arrayList;
		ViewHolder viewHolder;

		ImageLoader imageLoader = new ImageLoader();

		public CategoryListAdapter(Context context,
				ArrayList<CategoryBean> arrayList) {
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
						R.layout.category_item, parent, false);
				viewHolder = new ViewHolder();

				viewHolder.txtName = (TextView) row
						.findViewById(R.id.category_title_Txt);

				viewHolder.imgImageView = (ImageView) row
						.findViewById(R.id.category_img);

				row.setTag(viewHolder);

			} else {
				viewHolder = (ViewHolder) row.getTag();
			}

			viewHolder.txtName.setText(arrayList.get(position).getName());
			viewHolder.position = "" + position;

			String timestamp = arrayList.get(position).getTime();
			String imgUrl = getString(R.string.image_url) + "BusinessCategory/"
					+ categoryList.get(position).getId() + "/medium.jpg?id="
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
									viewHolder.imgImageView
											.setImageBitmap(imageBitmap);

									notifyDataSetChanged();
								}

							}
						});
				if (bitmap != null)
					viewHolder.imgImageView.setImageBitmap(bitmap);
				else
					viewHolder.imgImageView.setImageDrawable(getResources()
							.getDrawable(R.drawable.no_image));

			}

			// imgImageView.setVisibility(View.VISIBLE);
			// imgImageView.setBackgroundResource(R.drawable.merchandise_normal);

			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (Utils.isOnline()) {
						UserListFragment fragment = new UserListFragment();
						Bundle bundle = new Bundle();
						bundle.putString(
								"catid",
								categoryList.get(
										Integer.parseInt(((ViewHolder) v
												.getTag()).position)).getId());
						bundle.putBoolean("isComesFromHome", true);
						fragment.setArguments(bundle);

						((HomeContainerFragment) getParentFragment())
								.replaceFragment(fragment, true, "HOME");
					} else {
						Utils.showDialog(getActivity(),
								getString(R.string.no_network_available));
					}

				}
			});

			return row;
		}

		class ViewHolder {
			TextView txtName;
			ImageView imgImageView;
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
