package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.model.ItemPictureBean;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class ItemsForSaleGallary extends BaseFragment {

	private ProgressDialog pd;
	ImageLoader imageLoader = new ImageLoader();
	String response;
	JSONObject jsonObject = null;
	ArrayList<ItemPictureBean> itemForSaleList;
	EditText search_edit_text;
	String itemId = "";
	View v;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.item_for_sale_gallary, null);

		Log.d("drink", "Drink On Create View");

		Bundle args = getArguments();
		if (args != null && args.containsKey("itemid"))
			itemId = args.getString("itemid");
		
		

		setHeader(v, "Item Picture");

		setLeftButton(v, R.drawable.btn_back);

		getItemPicture();

		return v;
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopRight:

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

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", itemId);

				new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "SelectItemForSaleImages",
								jsonObject.toString());

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObject(response);

								if (pd!=null && pd.isShowing())
									pd.dismiss();
							}

						});

					}
				}).start();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),getString(R.string.no_network_available));

	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<ItemPictureBean>>() {
			}.getType();

			try {
				itemForSaleList = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);

				if (itemForSaleList != null && itemForSaleList.size() > 0) {
					ImageView imageView;
					for (int i = 0; i < itemForSaleList.size(); i++) {
						String imgUrl = getString(R.string.image_url)
								+ "ItemForSale/"
								+ itemForSaleList.get(i).getItemid() + "/"
								+ itemForSaleList.get(i).getItemimag()
								+ "/smallthumb.jpg";
						switch (i) {
						case 0:

							imageView = (ImageView) v
									.findViewById(R.id.imageView1);
							setImageForView(imageView, imgUrl);
							break;
						case 1:
							imageView = (ImageView) v
									.findViewById(R.id.imageView2);
							setImageForView(imageView, imgUrl);
							break;
						case 2:
							imageView = (ImageView) v
									.findViewById(R.id.imageView3);
							setImageForView(imageView, imgUrl);
							break;
						case 3:
							imageView = (ImageView) v
									.findViewById(R.id.imageView4);
							setImageForView(imageView, imgUrl);
							break;
						case 4:
							imageView = (ImageView) v
									.findViewById(R.id.imageView5);
							setImageForView(imageView, imgUrl);
							break;
						default:
							break;

						}

					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void setImageForView(final ImageView business_img, String imgUrl) {

		imageLoader.loadSingleImageBm(imgUrl, business_img);

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		// dbh.closeDB();
		super.onDestroyView();
	}

}
