package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tamilshout.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.model.EventBean;
import com.tamilshout.model.ItemForSaleBean;
import com.tamilshout.model.ItemPictureBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class EventDetail extends BaseFragment {

	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	ListView listView;
	String catId = "";
	String imgUrl = "";
	View v;
	Thread t1,t2,t3;
	TextView businessNameTxt, venuNameTxt, venueDescTxt, organizerNameTxt,
			organizerAddress, priceTxt, startDateTxt, endDateTxt, eventDescTxt,
			organizerTxt, descriptionTxt, emailTxt, eventTypeTxt;

	EventBean eventbean = null;
	String videoUrl = "";
	ArrayList<ItemPictureBean> itemPictureBeans;
	Bundle args;
	RelativeLayout videoImageView;
	ImageView eventImageView, event_img, videoPlayImage;

	String eventid = null;
	ArrayList<EventBean> eventList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.event_detail, null);

		setHeader(v, "Event Details");
		setLeftButton(v, R.drawable.btn_back);
		// setRightButton(v, R.drawable.btn_edit);

		setTouchNClick(v, R.id.btnTopLeft);

		// setTouchNClick(v, R.id.gallaryBtn);
		// setTouchNClick(v, R.id.btnTopRight);

		// All Textview

		businessNameTxt = (TextView) v.findViewById(R.id.itemTitleTxt);
		venuNameTxt = (TextView) v.findViewById(R.id.venuNameTxt);
		descriptionTxt = (TextView) v.findViewById(R.id.venuDscTxt);
		eventTypeTxt = (TextView) v.findViewById(R.id.eventTypeTxt);
		// organizerTxt = (TextView) v.findViewById(R.id.organizerTxt);
		organizerNameTxt = (TextView) v.findViewById(R.id.organizerNamext);
		organizerAddress = (TextView) v.findViewById(R.id.organizerAddressTxt);
		venueDescTxt = (TextView) v.findViewById(R.id.venuDscTxt);
		priceTxt = (TextView) v.findViewById(R.id.priceTxt);
		startDateTxt = (TextView) v.findViewById(R.id.eventStartTxt);
		endDateTxt = (TextView) v.findViewById(R.id.eventEndTxt);
		eventDescTxt = (TextView) v.findViewById(R.id.eventDescTxt);
		event_img = (ImageView) v.findViewById(R.id.event_img);
		videoPlayImage = (ImageView) v.findViewById(R.id.videoPlayImage);
		videoImageView = (RelativeLayout) v.findViewById(R.id.videoImageView);

		// All Button

		setTouchNClick(v, R.id.emailBtn);
		setTouchNClick(v, R.id.directionBtn);
		setTouchNClick(v, R.id.phoneBtn);
		setTouchNClick(v, R.id.websiteBtn);
		// setTouchNClick(v, R.id.gallaryBtn);

		// listView = (ListView) v.findViewById(R.id.listView1);

		event_img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("-----videoUrl---"+videoUrl);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl));
				intent.setDataAndType(Uri.parse(videoUrl), "video/*");
				startActivity(intent);

			}
		});
		
		
		

		return v;
	}

	void setValue() {
		businessNameTxt.setText(eventbean.getEventname());
		startDateTxt.setText("Start On: " + eventbean.getEventstarton());
		endDateTxt.setText("  End   On: " + eventbean.getEventendon());
		eventDescTxt.setText(eventbean.getEventdesc());
		priceTxt.setText(eventbean.getCurrencycode() + " "
				+ eventbean.getCost_offer());
		venuNameTxt.setText(eventbean.getVanuename());
		// organizerTxt.setText(eventbean.getOrganisername());
		venueDescTxt.setText(eventbean.getVanuedesc());
		organizerNameTxt.setText(eventbean.getOrganisername());
		eventTypeTxt.setText(eventbean.getEventtype());
		
		String addressStr=""; /*= eventbean.getAddress1() + ", "
				+ eventbean.getAddress2() + ", n" + eventbean.getCity() + ", "
				+ eventbean.getState() + ", " + eventbean.getCountry()+ ", " + eventbean.getPostcode();*/

		
		
		if(eventbean.getAddress1()!=null && eventbean.getAddress1().length()>0)
			addressStr=addressStr+eventbean.getAddress1();
		if(eventbean.getAddress2()!=null && eventbean.getAddress2().length()>0)
			addressStr=addressStr+", "+eventbean.getAddress2();
		if( eventbean.getCity()!=null &&  eventbean.getCity().length()>0)
			addressStr=addressStr+", "+ eventbean.getCity();
		if( eventbean.getState()!=null &&  eventbean.getState().length()>0)
			addressStr=addressStr+", "+ eventbean.getState();
		if( eventbean.getPostcode()!=null &&  eventbean.getPostcode().length()>0)
			addressStr=addressStr+", "+ eventbean.getPostcode();
		if( eventbean.getCountry()!=null &&  eventbean.getCountry().length()>0)
			addressStr=addressStr+", "+ eventbean.getCountry();
		
		
		
		
		if (addressStr.startsWith(","))
			addressStr = addressStr.substring(1);
		if (addressStr.endsWith(","))
			addressStr = addressStr.substring(0, addressStr.length() - 1);

		
		 organizerAddress.setText(addressStr.trim());
		


		videoUrl = getString(R.string.image_url) + "Event/Video/"
				+ eventbean.getEventid() + "/Video_" + eventbean.getEventid()
				+ ".mp4";

		t3=new Thread(new Runnable() {

			@Override
			public void run() {

				final boolean isAddImgExists = Utils
						.checkImageIsAvailable(videoUrl);

				if(v!=null)
				{
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {

						if (isAddImgExists) {
							videoImageView.setVisibility(View.VISIBLE);
							videoPlayImage.setVisibility(View.VISIBLE);

						} else {
							videoImageView.setVisibility(View.GONE);
							videoPlayImage.setVisibility(View.GONE);
						}

					}
				});
				}

			}
		});t3.start();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		args = getArguments();
		if (args != null && args.containsKey("eventid"))
			eventid = args.getString("eventid");
		System.out.println("-------------eventid----------" + eventid);

		Constant.appContext = getActivity();
		Log.d("Home", "Category Lsit");

		// listView = (ListView) v.findViewById(R.id.listView1);
		if (eventid != null)
			getEventDetailsById();

		

	}

	private void parsJsonObjectForEventDetails(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<EventBean>>() {
			}.getType();

			try {
				JSONObject object = new JSONObject(response);
				if (object.getString("responsecode").equals("200")) {
					eventList = new Gson().fromJson(
							object.getJSONArray("responsepacketList")
									.toString(), listType);

					if (!(eventList != null && eventList.size() > 0)) {

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
						eventbean = eventList.get(0);
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

	private void getEventDetailsById() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", eventid);

				t1=new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "EventDetailsById",
								jsonObject.toString());

						if(v!=null)
						{
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObjectForEventDetails(response);

								if (eventbean != null) {
									getEventImage();
									setValue();

								} else {
									if (pd != null)
										pd.dismiss();
								}
							}

						});

					}}
				});t1.start();
			} catch (Exception exception) {
				if (pd != null)
					pd.dismiss();
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));
	}

	private void getEventImage() {
		final ImageLoader imageLoader = new ImageLoader();

		if (pd != null && !pd.isShowing()) {
			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);
		}
		final ImageView business_img = (ImageView) v
				.findViewById(R.id.eventImageView);

		try {
			t2=new Thread(new Runnable() {

				@Override
				public void run() {
					String imgUrl = getString(R.string.image_url)
							+ "Event/image/" + eventbean.getEventid()
							+ "/original.jpg?id=" + eventbean.getTime();

					imageLoader.loadSingleImageBm(imgUrl, business_img);

					if(v!=null)
					{
					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {

							if (pd.isShowing())
								pd.dismiss();

						}
					});
					}

				}
			});t2.start();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {

		case R.id.emailBtn:

			if (eventbean != null && eventbean.getOrganiseremail() != null
					&& eventbean.getOrganiseremail().length() > 0)
				Utils.sendMail(getActivity(), eventbean.getOrganiseremail()
						.toString());
			else
				Toast.makeText(getActivity(), "Email address not available!",
						Toast.LENGTH_LONG).show();

			break;
		case R.id.websiteBtn:

			if (eventbean != null && eventbean.getOrganiserwebsite() != null
					&& eventbean.getOrganiserwebsite().length() > 0)
				Utils.openWebsite(getActivity(), eventbean
						.getOrganiserwebsite().toString());
			else
				Toast.makeText(getActivity(), "Website link not available!",
						Toast.LENGTH_LONG).show();

			break;
		case R.id.btnTopRight:
			AddMyItemFragment addMyItemFragment = new AddMyItemFragment();
			Bundle bundle = new Bundle();
			bundle.putSerializable("eventbean", eventbean);
			addMyItemFragment.setArguments(bundle);
			((MyItemsContainerFragment) getParentFragment()).replaceFragment(
					addMyItemFragment, true, "My Items");
			break;
		case R.id.phoneBtn:

			if (eventbean != null && eventbean.getOrganisercontact() != null
					&& eventbean.getOrganisercontact().length() > 0)
				Utils.makeCall(getActivity(), eventbean.getOrganisercontact()
						.toString());
			else
				Toast.makeText(getActivity(), "Phone number not available!",
						Toast.LENGTH_LONG).show();

			break;
		case R.id.directionBtn:

			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(getActivity());

			if (status != ConnectionResult.SUCCESS) { // Google Play Services
														// are not available
				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
						getActivity(), requestCode);
				dialog.show();

			} else {
				if (Constant.location == null) {
					DialogInterface.OnClickListener lis = new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							startActivity(new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							dialog.dismiss();
						}
					};

					Utils.showDialog(
							getActivity(),
							"Unable to track your location. Please turn on your location both Wireless Networks and GPS Satellites.",
							"Location Not Found", lis, true).show();
				} else {

					//String uri = "geo:" + Constant.LAT + "," + Constant.LONG; //For Current location
					
					String uri="geo:0,0?q="+eventbean.getAddress1() + " "
							+ eventbean.getAddress2() + " " + eventbean.getCity() + ","
							+ eventbean.getState() + "," + eventbean.getCountry();
					if(uri.startsWith(","))
						uri=uri.substring(1);
					if(uri.endsWith(","))
						uri=uri.substring(0,uri.length()-1);			
					
					Log.d("uri", uri.trim());
					startActivity(new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri.trim())));
				}

			}

			break;
		/*
		 * case R.id.gallaryBtn:
		 * 
		 * MyItemsGallary fragment = new MyItemsGallary(); bundle = new
		 * Bundle(); bundle.putString("itemid",
		 * eventbean.getItemid().toString()); fragment.setArguments(bundle); if
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
		if (t3 != null)
			t3.interrupt();
		
		Thread.currentThread().interrupt(); 

		v = null;

	}

}
