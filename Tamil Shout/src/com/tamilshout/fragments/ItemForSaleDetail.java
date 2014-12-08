package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.tamilshout.R;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.FullScreenViewActivity;
import com.tamilshout.HomeActivity;
import com.tamilshout.model.ItemForSaleBean;
import com.tamilshout.model.ItemPictureBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;
import com.viewpagerindicator.CirclePageIndicator;

public class ItemForSaleDetail extends BaseFragment {

	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	ListView listView;
	ViewPager viewPager;
	CirclePageIndicator circlePageIndicator;
	ArrayList<ItemPictureBean> itemPictureBeans;
	String catId = "";
	String imgUrl = "";
	View v;
	String itemId = "";
	ArrayList<ItemForSaleBean> myItemsList;
	TextView businessNameTxt, priceTxt, yearTxt, modeTxt, descriptionTxt,
			priceTxt2, countryTxt, websiteTxt, emailTxt;

	ItemForSaleBean businessUser = null;
	Thread t1, t2;
	Bundle args;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.item_for_sale_detail, null);

		setHeader(v, "Item Details");
		setLeftButton(v, R.drawable.btn_back);

		setTouchNClick(v, R.id.btnTopLeft);
		// setTouchNClick(v, R.id.gallaryBtn);

		// All Textview

		viewPager = (ViewPager) v.findViewById(R.id.pager);
		circlePageIndicator = (CirclePageIndicator) v
				.findViewById(R.id.pagerIndicator);

		businessNameTxt = (TextView) v.findViewById(R.id.itemTitleTxt);
		yearTxt = (TextView) v.findViewById(R.id.yearTxt);
		priceTxt = (TextView) v.findViewById(R.id.priceTxt);
		modeTxt = (TextView) v.findViewById(R.id.modeTxt);
		// priceTxt2 = (TextView) v.findViewById(R.id.price2Txt);
		descriptionTxt = (TextView) v.findViewById(R.id.descriptionTxt);

		// All Button

		setTouchNClick(v, R.id.emailBtn);
		setTouchNClick(v, R.id.messageBtn);
		setTouchNClick(v, R.id.phoneBtn);

		// listView = (ListView) v.findViewById(R.id.listView1);

		return v;
	}

	void setValue() {
		businessNameTxt.setText(businessUser.getItemName());
		yearTxt.setText(businessUser.getAddedOn());
		priceTxt.setText(businessUser.getCurrencycode() + " "
				+ businessUser.getPrice());

		// priceTxt2.setText("$" + businessUser.getPrice());
		modeTxt.setText(businessUser.getItemcategoryname());
		descriptionTxt.setText(businessUser.getDescription());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		args = getArguments();
		if (args != null && args.containsKey("itemid"))
			itemId = args.getString("itemid");

		Constant.appContext = getActivity();
		Log.d("Home", "Category Lsit");

		// listView = (ListView) v.findViewById(R.id.listView1);
		if (itemId != null)
			getItemDetailsById();

	}

	private void getItemDetailsById() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();

				jsonObject.put("itemid", itemId);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GetItemForSaleDetailsById",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObjectForItemDetails(response);

									if (businessUser != null) {
										setValue();
										getItemPicture();

									} else {
										if (pd != null)
											pd.dismiss();
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

	private void parsJsonObjectForItemDetails(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<ItemForSaleBean>>() {
			}.getType();

			try {
				JSONObject object = new JSONObject(response);
				if (object.getString("responsecode").equals("200")) {
					myItemsList = new Gson().fromJson(
							object.getJSONArray("responsepacketList")
									.toString(), listType);

					if (!(myItemsList != null && myItemsList.size() > 0)) {

						Utils.showDialog(getActivity(),
								object.getString("status"), "",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										((HomeActivity) getActivity())
												.onBackPressed();
									}
								}, false).show();

					} else {
						businessUser = myItemsList.get(0);
					}
				} else if (object.getString("responsecode").equals("100")) {
					Utils.showDialog(getActivity(), object.getString("status"),
							"", new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									((HomeActivity) getActivity())
											.onBackPressed();
								}
							}, false).show();
				} else {

					Utils.showDialog(getActivity(),
							getString(R.string.some_error), "",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

									((HomeActivity) getActivity())
											.onBackPressed();

								}
							}, false).show();

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Utils.showDialog(getActivity(), getString(R.string.server_error),
					"", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							((HomeActivity) getActivity()).onBackPressed();
						}
					}, false).show();

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {

		case R.id.emailBtn:

			String emailId = businessUser.getContact_email().toString();

			if (emailId != null && emailId.length() > 0) {
				Utils.sendMail(getActivity(), emailId);

			} else {
				Toast.makeText(getActivity(), "Email address not found",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.addImg:

			break;

		case R.id.phoneBtn:

			if (businessUser != null && businessUser.getContact_phone() != null
					&& businessUser.getContact_phone().length() > 0)
				Utils.makeCall(getActivity(), businessUser.getContact_phone()
						.toString());
			else
				Toast.makeText(getActivity(), "Phone number not available!",
						Toast.LENGTH_SHORT).show();

			break;
		case R.id.messageBtn:

			if (businessUser != null && businessUser.getContact_phone() != null
					&& businessUser.getContact_phone().length() > 0)
				Utils.sendMessage(getActivity(),
						businessUser.getContact_phone());
			else
				Toast.makeText(getActivity(), "Phone number not available!",
						Toast.LENGTH_SHORT).show();

			break;

		case R.id.btnTopLeft:

			((HomeActivity) getActivity()).onBackPressed();
			break;

		default:
			break;
		}

	}

	private void getItemPicture() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			if (pd != null && !pd.isShowing()) {
				pd = ProgressDialog.show(getActivity(), "", ""
						+ getString(R.string.processing));
				pd.setCancelable(true);
			}

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", businessUser.getItemid());

				t2 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "SelectItemForSaleImages",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObject(response);

									if (itemPictureBeans != null
											&& itemPictureBeans.size() > 0) {
										viewPager
												.setAdapter(new ImageAdapter());
										circlePageIndicator
												.setViewPager(viewPager);
									}

									if (pd != null && pd.isShowing())
										pd.dismiss();

								}

							});
						}
					}
				});
				t2.start();
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

			Type listType = new TypeToken<ArrayList<ItemPictureBean>>() {
			}.getType();

			try {
				itemPictureBeans = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	class ImageAdapter extends PagerAdapter {

		ImageLoader imgLoader;

		public ImageAdapter() {
			// TODO Auto-generated constructor stub
			imgLoader = new ImageLoader();
		}

		@Override
		public int getCount() {
			return itemPictureBeans.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {

			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = (View) inflater.inflate(R.layout.row_image_item, null);

			final ImageView img_category = (ImageView) view
					.findViewById(R.id.imgView);
			/*
			 * final TextView txt_categoryName = (TextView) view
			 * .findViewById(R.id.txt_categoryName);
			 */

			String timestamp = itemPictureBeans.get(position).getTime();

			String imgUrl = getString(R.string.image_url) + "ItemForSale/"
					+ itemPictureBeans.get(position).getItemid() + "/"
					+ itemPictureBeans.get(position).getItemimag()
					+ "/original.jpg?id=" + timestamp;

			if (imgUrl.length() > 0
					&& (imgUrl.contains(".jpg") || imgUrl.contains(".gif")
							|| imgUrl.contains(".png") || imgUrl
								.contains(".jpeg"))) {

				Bitmap bitmap = imgLoader.loadImage(imgUrl,
						new ImageLoader.ImageLoadedListener() {

							@Override
							public void imageLoaded(Bitmap imageBitmap) {
								if (imageBitmap != null) {
									img_category.setImageBitmap(imageBitmap);

									notifyDataSetChanged();
								}

							}
						});
				if (bitmap != null)
					img_category.setImageBitmap(bitmap);
				else
					img_category.setImageDrawable(getResources().getDrawable(
							R.drawable.no_image));

			}

			/*
			 * img_category.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) {
			 * 
			 * Intent i = new Intent(getActivity(),
			 * FullScreenViewActivity.class);
			 * 
			 * i.putExtra("position", position); startActivity(i);
			 * 
			 * Intent intent = new Intent(getActivity(),
			 * DetailImageActivity.class); intent.putExtra("url",
			 * v.getTag().toString()); startActivity(intent);
			 * 
			 * } });
			 */
			img_category.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent i = new Intent(getActivity(),
							FullScreenViewActivity.class);
					i.putExtra("position", position);
					Bundle bundle = new Bundle();

					bundle.putSerializable("imagePathList", itemPictureBeans);

					i.putExtras(bundle);
					startActivity(i);

				}
			});
			// txt_categoryName.setText(categoryList.get(position).getName());
			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
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
