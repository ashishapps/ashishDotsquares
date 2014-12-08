package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.model.ItemPictureBean;
import com.tamilshout.model.OfferBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class OfferDetail extends BaseFragment {

	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;

	ListView listView;

	String catId = "";
	String imgUrl = "";
	View v;

	Thread t1, t2;
	TextView businessNameTxt, discountedPriceTxt, priceTxt, validTillTxt,
			priceTxt2, countryTxt, descriptionTxt, emailTxt,
			offerBusinessNameTxt;

	OfferBean offerbean = null;

	ArrayList<ItemPictureBean> itemPictureBeans;
	Bundle args;

	String offerid = null;
	ArrayList<OfferBean> offerlist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.offers_detail, null);

		setHeader(v, "Offer Details");
		setLeftButton(v, R.drawable.btn_back);
		// setRightButton(v, R.drawable.btn_edit);

		setTouchNClick(v, R.id.btnTopLeft);
		// setTouchNClick(v, R.id.gallaryBtn);
		// setTouchNClick(v, R.id.btnTopRight);

		// All Textview

		businessNameTxt = (TextView) v.findViewById(R.id.itemTitleTxt);
		discountedPriceTxt = (TextView) v.findViewById(R.id.discountedPriceTxt);
		offerBusinessNameTxt = (TextView) v
				.findViewById(R.id.offerBusinessNameTxt);

		priceTxt = (TextView) v.findViewById(R.id.priceTxt);
		validTillTxt = (TextView) v.findViewById(R.id.offerValidTxt);

		descriptionTxt = (TextView) v.findViewById(R.id.descriptionTxt);

		// All Button

		setTouchNClick(v, R.id.emailBtn);
		setTouchNClick(v, R.id.messageBtn);
		setTouchNClick(v, R.id.phoneBtn);
		// setTouchNClick(v, R.id.gallaryBtn);

		// listView = (ListView) v.findViewById(R.id.listView1);

		return v;
	}

	void setValue() {
		businessNameTxt.setText(offerbean.getTitle());
		offerBusinessNameTxt.setText(offerbean.getBusinessname());
		descriptionTxt.setText(offerbean.getDescription());

		validTillTxt.setText(offerbean.getDealValidDate());
		priceTxt.setText(offerbean.getCurrencycode() + " "
				+ offerbean.getNormalPrice());

		discountedPriceTxt.setText(offerbean.getCurrencycode() + " "
				+ offerbean.getDiscountedPrice());
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		args = getArguments();
		if (args != null && args.containsKey("offerid"))
			offerid = args.getString("offerid");

		Constant.appContext = getActivity();
		Log.d("Home", "Category Lsit");

		// listView = (ListView) v.findViewById(R.id.listView1);
		if (offerid != null)
			getOfferDetailsById();

	}

	private void parsJsonObjectForEventDetails(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<OfferBean>>() {
			}.getType();

			try {
				JSONObject object = new JSONObject(response);
				if (object.getString("responsecode").equals("200")) {
					offerlist = new Gson().fromJson(
							object.getJSONArray("responsepacketList")
									.toString(), listType);

					if (!(offerlist != null && offerlist.size() > 0)) {

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
						offerbean = offerlist.get(0);
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

	private void getOfferDetailsById() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();

				jsonObject.put("offerid", offerid);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GetOfferDetailsById",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObjectForEventDetails(response);

									if (offerbean != null) {
										getUserOfferImage();
										setValue();

									} else {
										if (pd != null && pd.isShowing())
											pd.dismiss();
									}
								}

							});
						}

					}
				});
				t1.start();
			} catch (Exception exception) {
				if (pd != null)
					pd.dismiss();
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));
	}

	private void getUserOfferImage() {
		final ImageLoader imageLoader = new ImageLoader();

		if (pd != null && !pd.isShowing()) {
			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);
		}

		final ImageView business_img = (ImageView) v
				.findViewById(R.id.offerImageView);

		try {
			t2 = new Thread(new Runnable() {

				@Override
				public void run() {
					String imgUrl = getString(R.string.image_url) + "Offer/"
							+ offerbean.getId() + "/original.jpg?id="
							+ offerbean.getTime();

					imageLoader.loadSingleImageBm(imgUrl, business_img);

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (pd != null && pd.isShowing())
								pd.dismiss();

						}
					});

				}
			});
			t2.start();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<ItemPictureBean>>() {
			}.getType();

			try {
				Constant.itemImageList = new Gson().fromJson(
						new JSONObject(response).getJSONArray(
								"responsepacketList").toString(), listType);

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

		case R.id.emailBtn:

			String emailId = offerbean.getEmail().toString();

			if (emailId != null && emailId.length() > 0
					&& Utils.isValidEmail(emailId)) {
				Utils.sendMail(getActivity(), emailId);

			} else {
				Toast.makeText(getActivity(), "Email address not found",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.addImg:

			break;
		/*
		 * case R.id.btnTopRight: AddMyItemFragment addMyItemFragment = new
		 * AddMyItemFragment(); Bundle bundle = new Bundle();
		 * bundle.putSerializable("offerbean", offerbean);
		 * addMyItemFragment.setArguments(bundle); ((MyItemsContainerFragment)
		 * getParentFragment()).replaceFragment( addMyItemFragment, true,
		 * "My Items"); break;
		 */
		case R.id.phoneBtn:

			if (offerbean != null && offerbean.getContactno() != null
					&& offerbean.getContactno().length() > 0)
				Utils.makeCall(getActivity(), offerbean.getContactno()
						.toString());
			else
				Toast.makeText(getActivity(), "Phone number not available!",
						Toast.LENGTH_SHORT).show();

			break;
		case R.id.messageBtn:

			if (offerbean != null && offerbean.getContactno() != null
					&& offerbean.getContactno().length() > 0)
				Utils.sendMessage(getActivity(), offerbean.getContactno());
			else
				Toast.makeText(getActivity(), "Phone number not available!",
						Toast.LENGTH_SHORT).show();

			break;
		/*
		 * case R.id.gallaryBtn:
		 * 
		 * MyItemsGallary fragment = new MyItemsGallary(); bundle = new
		 * Bundle(); bundle.putString("itemid",
		 * offerbean.getItemid().toString()); fragment.setArguments(bundle); if
		 * (args != null && args.containsKey("isComesFromHome") &&
		 * args.getBoolean("isComesFromHome", false)) ((HomeContainerFragment)
		 * getParentFragment()).replaceFragment( fragment, true, "HOME"); else
		 * ((MyItemsContainerFragment) getParentFragment())
		 * .replaceFragment(fragment, true, "My Items");
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
		public View instantiateItem(ViewGroup container, int position) {

			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = (View) inflater.inflate(R.layout.row_image_item, null);

			final ImageView img_category = (ImageView) view
					.findViewById(R.id.imgView);
			/*
			 * final TextView txt_categoryName = (TextView) view
			 * .findViewById(R.id.txt_categoryName);
			 */

			String imgUrl = getString(R.string.image_url) + "ItemForSale/"
					+ itemPictureBeans.get(position).getItemid() + "/"
					+ itemPictureBeans.get(position).getItemimag()
					+ "/smallthumb.jpg";

			imgLoader.loadSingleImageBm(imgUrl, img_category);
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
