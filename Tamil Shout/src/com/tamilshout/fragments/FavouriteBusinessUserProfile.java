package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.tamilshout.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.FullScreenViewActivity;
import com.tamilshout.HomeActivity;
import com.tamilshout.ImageZoomInZoomOutActivity;
import com.tamilshout.LoginActivity;
import com.tamilshout.model.BusinessPictureBean;
import com.tamilshout.model.BusinessUser;
import com.tamilshout.model.LoyaltyBean;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.utils.ImageLoader.ImageLoadedListener;
import com.tamilshout.web.GetDataFromWebService;
import com.viewpagerindicator.CirclePageIndicator;

public class FavouriteBusinessUserProfile extends BaseFragment {

	boolean create = false;
	ArrayList<BusinessUser> categoryList;
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	ViewPager viewPager;
	CirclePageIndicator circlePageIndicator;
	ListView listView;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem;
	String catId = "", addressStr = "";
	String addBannerImgUrl = "", busineMenuImgUrl = "";
	EditText passwordEdt;
	ArrayList<BusinessUser> userBeans;
	ArrayList<BusinessPictureBean> businessImageList;
	LinearLayout galleryLayout, businessOffersLayout, loyaltyLayout;
	RelativeLayout addImgLayput;
	View v;
	Bundle args;
	String businessUserId = "";
	TextView businessNameTxt, address1Txt, descriptionTxt, websiteTxt,
			emailTxt, servicedOfferedTxt, openingTimeTxt, phoneTxt,
			businessDescTxt, businessName;

	BusinessUser businessUser = null;
	String webUrl = "";
	ImageView addBannerView, videoPlayImage;
	ArrayList<LoyaltyBean> loyaltyBeans;
	RatingBar ratingBar;
	Button dialogButton, dialogButtonCancel;
	Dialog dialog;
	int givenLoyaltycount = 0;

	String timeStamp;

	Thread t1, t2, t3, t4, t5;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		setCustomDialoge();

		v = inflater.inflate(R.layout.business_user_profile, null);

		Constant.appContext = getActivity();
		setHeader(v, "Business Profile");
		setLeftButton(v, R.drawable.btn_back);

		setTouchNClick(v, R.id.btnTopLeft);
		Log.d("Home", "Category Lsit");

		args = getArguments();
		if (args != null && args.containsKey("userid"))
			businessUserId = args.getString("userid");

		viewPager = (ViewPager) v.findViewById(R.id.pager);
		galleryLayout = (LinearLayout) v.findViewById(R.id.gallaryLayout);
		loyaltyLayout = (LinearLayout) v.findViewById(R.id.loyaltyLayout);
		addImgLayput = (RelativeLayout) v.findViewById(R.id.addImgLayout);
		ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
		circlePageIndicator = (CirclePageIndicator) v
				.findViewById(R.id.pagerIndicator);

		// All Textview

		businessNameTxt = (TextView) v.findViewById(R.id.businessNameTxt);
		businessName = (TextView) v.findViewById(R.id.businessName);
		descriptionTxt = (TextView) v.findViewById(R.id.descriptionTxt);
		phoneTxt = (TextView) v.findViewById(R.id.phone_txt);
		address1Txt = (TextView) v.findViewById(R.id.address1Txt);

		websiteTxt = (TextView) v.findViewById(R.id.websiteTxt);
		emailTxt = (TextView) v.findViewById(R.id.emailTxt);
		servicedOfferedTxt = (TextView) v.findViewById(R.id.serviceOfferTxt);
		openingTimeTxt = (TextView) v.findViewById(R.id.openingTimeTxt);
		businessDescTxt = (TextView) v.findViewById(R.id.businessDescTxt);
		addBannerView = (ImageView) v.findViewById(R.id.addImgView);
		videoPlayImage = (ImageView) v.findViewById(R.id.videoPlayImage);

		// All Button

		setTouchNClick(v, R.id.websiteImg);
		setTouchNClick(v, R.id.messageImg);
		setTouchNClick(v, R.id.addImg);
		setTouchNClick(v, R.id.phoneImg);
		setTouchNClick(v, R.id.favouriteBtn);
		setTouchNClick(v, R.id.loyaltyLayout);
		setTouchNClick(v, R.id.businessMenuBtn);
		setTouchNClick(v, R.id.businessOffersBtn);
		setTouchNClick(v, R.id.dirextionImg);

		addBannerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (businessUser.getBusinessAddType().equals("1")) {
					Intent intent = new Intent(getActivity(),
							ImageZoomInZoomOutActivity.class);
					intent.putExtra("imgUrl", webUrl);
					startActivity(intent);

				} else {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(webUrl));
					intent.setDataAndType(Uri.parse(webUrl), "video/mp4");
					startActivity(intent);
				}

			}
		});

		loyaltyLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!AppPrefrence.isLogin(getActivity())) {

					Utils.showDialog(getActivity(),
							getString(R.string.login_required), "",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// dialog.dismiss();

									Intent intent = new Intent(getActivity(),
											LoginActivity.class);
									startActivity(intent);

								}
							}, true).show();

				} else {

					((EditText) dialog.findViewById(R.id.passwordEdt))
							.setText("");
					dialog.show();

				}

			}
		});
		
		
		

		return v;

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getBusinessProfileDetails();
	}

	void setValue() {
		if (businessUser != null) {

			businessNameTxt.setText(businessUser.getBusinessname());
			businessName.setText(businessUser.getContactperson());
			descriptionTxt.setText(businessUser.getAboutbusiness());
			phoneTxt.setText(businessUser.getContactno());
			websiteTxt.setText(businessUser.getWebsite());

			if (businessUser.getServicesOffered() != null
					&& businessUser.getServicesOffered().toString().length() > 0) {
				((LinearLayout) v.findViewById(R.id.serviceOfferdViewLayout))
						.setVisibility(View.VISIBLE);
			} else {
				((LinearLayout) v.findViewById(R.id.serviceOfferdViewLayout))
						.setVisibility(View.GONE);
			}
			if (businessUser.getOpeningtimings() != null
					&& businessUser.getOpeningtimings().toString().length() > 0) {
				((LinearLayout) v.findViewById(R.id.openingViewLayout))
						.setVisibility(View.VISIBLE);
			} else {
				((LinearLayout) v.findViewById(R.id.openingViewLayout))
						.setVisibility(View.GONE);
			}
			if (businessUser.getAboutbusiness() != null
					&& businessUser.getAboutbusiness().toString().trim()
							.length() > 0) {
				((LinearLayout) v.findViewById(R.id.businessDescViewLayout))
						.setVisibility(View.VISIBLE);
				((LinearLayout) v.findViewById(R.id.descLayout))
						.setVisibility(View.VISIBLE);
			} else {
				((LinearLayout) v.findViewById(R.id.businessDescViewLayout))
						.setVisibility(View.GONE);
				((LinearLayout) v.findViewById(R.id.descLayout))
						.setVisibility(View.GONE);
			}

			if (businessUser.getWebsite() != null
					&& businessUser.getWebsite().length() <= 0)
				((Button) v.findViewById(R.id.websiteImg))
						.setVisibility(View.GONE);

			if (businessUser.getContactemail() != null
					&& businessUser.getContactemail().length() <= 0)
				((Button) v.findViewById(R.id.messageImg))
						.setVisibility(View.GONE);

			if (businessUser.getLoyaltycount() != null
					&& businessUser.getLoyaltycount().toString().equals("0")) {
				loyaltyLayout.setVisibility(View.INVISIBLE);
			}

			if (businessUser.getWebsite() != null
					&& businessUser.getWebsite().length() > 0
					|| businessUser.getContactemail() != null
					&& businessUser.getContactemail().length() > 0) {
				((View) v.findViewById(R.id.emailTxtTop))
						.setVisibility(View.VISIBLE);

			} else {
				((View) v.findViewById(R.id.emailTxtTop))
						.setVisibility(View.GONE);
			}

			if (businessUser.getWebsite() != null
					&& businessUser.getWebsite().length() > 0
					|| businessUser.getContactemail() != null
					&& businessUser.getContactemail().length() > 0
					|| businessUser.isFlag_businessad()) {
				((View) v.findViewById(R.id.addImgTop))
						.setVisibility(View.VISIBLE);

			} else {
				((View) v.findViewById(R.id.addImgTop))
						.setVisibility(View.GONE);
			}

			emailTxt.setText(businessUser.getContactemail());
			servicedOfferedTxt.setText(businessUser.getServicesOffered());
			openingTimeTxt.setText(businessUser.getOpeningtimings());
			businessDescTxt.setText(businessUser.getAboutbusiness());

			addressStr="";
			if(businessUser.getAddress1()!=null && businessUser.getAddress1().length()>0)
				addressStr=businessUser.getAddress1();
			if(businessUser.getAddress2()!=null && businessUser.getAddress2().length()>0)
				addressStr=addressStr+", "+businessUser.getAddress2();
			if( businessUser.getCity()!=null &&  businessUser.getCity().length()>0)
				addressStr=addressStr+", "+ businessUser.getCity();
			if( businessUser.getState()!=null &&  businessUser.getState().length()>0)
				addressStr=addressStr+", "+ businessUser.getState();
			if( businessUser.getPostcode()!=null &&  businessUser.getPostcode().length()>0)
				addressStr=addressStr+", "+ businessUser.getPostcode();
			if( businessUser.getCountry()!=null &&  businessUser.getCountry().length()>0)
				addressStr=addressStr+", "+ businessUser.getCountry();
			
						
			if (addressStr.startsWith(","))
				addressStr = addressStr.substring(1);
			if (addressStr.endsWith(","))
				addressStr = addressStr.substring(0, addressStr.length() - 1);

			address1Txt.setText(addressStr.trim());

			// /http://windowsdemo.projectstatus.co.uk/DirectoryApp/Uploads/UserCoupen/CoupenVideo/217/Video_217.mp4
			if (businessUser.getBusinessAddType().equals("1")) {

				webUrl = getString(R.string.image_url)
						+ "UserCoupen/CoupenImage/" + businessUser.getId()
						+ "/original.jpg?id=" + businessUser.getTime();
				addBannerImgUrl = getString(R.string.image_url)
						+ "UserCoupen/CoupenImage/" + businessUser.getId()
						+ "/original.jpg?id=" + businessUser.getTime();
				videoPlayImage.setVisibility(View.GONE);
				getAddBannerImage();

			} else {
				videoPlayImage.setVisibility(View.VISIBLE);
				webUrl = getString(R.string.image_url)
						+ "UserCoupen/CoupenVideo/" + businessUser.getId()
						+ "/Video_" + businessUser.getId() + ".mp4";
				addBannerImgUrl = getString(R.string.image_url)
						+ "UserCoupen/CoupenVideo/" + businessUser.getId()
						+ "/thumb_" + businessUser.getId() + ".jpg?id="
						+ businessUser.getTime();
				getAddBannerImage();
			}

			busineMenuImgUrl = getString(R.string.image_url) + "/BusinessMenu/"
					+ businessUser.getId() + "/original.jpg?id="
					+ businessUser.getTime();

			addImgLayput.setVisibility(View.VISIBLE);
			v.findViewById(R.id.businessMenuBtn).setVisibility(View.VISIBLE);

			if (businessUser != null && businessUser.isFlag_businessad()) {
				addImgLayput.setVisibility(View.VISIBLE);
				v.findViewById(R.id.addImgTop).setVisibility(View.VISIBLE);
				v.findViewById(R.id.addImg).setVisibility(View.VISIBLE);
			} else {
				v.findViewById(R.id.addImgTop).setVisibility(View.GONE);
				addImgLayput.setVisibility(View.GONE);
				v.findViewById(R.id.addImg).setVisibility(View.GONE);
			}
			if (businessUser != null && businessUser.isFlag_businessmenu()) {
				v.findViewById(R.id.businessMenuBtn)
						.setVisibility(View.VISIBLE);
			} else {
				v.findViewById(R.id.businessMenuBtn).setVisibility(View.GONE);
			}

		}
	}

	private void setCustomDialoge() {
		dialog = new Dialog(getActivity());

		dialog.setContentView(R.layout.custom_dialoge);
		dialog.setTitle("Enter Password");

		// set the custom dialog components - text, image and button
		passwordEdt = (EditText) dialog.findViewById(R.id.passwordEdt);

		dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		dialogButtonCancel = (Button) dialog
				.findViewById(R.id.dialogButtonCancel);

		dialogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String passwordStr = passwordEdt.getText().toString();

				if (passwordStr != null && passwordStr.trim().length() > 0) {
					giveLoyaltyToAppUser(passwordStr);
					dialog.dismiss();
				} else {
					Toast.makeText(getActivity(),
							"Please enter your secret password",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		dialogButtonCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String passwordStr = passwordEdt.getText().toString();

				dialog.dismiss();

			}
		});

	}

	private void getBusinessLoyalty() {
		if (pd != null && !pd.isShowing()) {
			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);
		}

		if (Utils.isNetworkAvailableNew(getActivity())) {

			try {

				jsonObject = new JSONObject();

				jsonObject.put("buserid", businessUser.getId());

				jsonObject.put("appuserid",
						AppPrefrence.getProfile(getActivity()).getUserid());

				t4 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "SelectAppUserLoyalty",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									if (response != null
											&& response.length() > 0
											&& response.startsWith("{")) {

										Type listType = new TypeToken<ArrayList<LoyaltyBean>>() {
										}.getType();

										try {
											loyaltyBeans = new Gson()
													.fromJson(
															new JSONObject(
																	response)
																	.getJSONArray(
																			"responsepacketList")
																	.toString(),
															listType);

										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									}

									if (loyaltyBeans != null
											&& loyaltyBeans.size() > 0) {

										ratingBar.setRating(Float
												.parseFloat(loyaltyBeans.get(0)
														.getMessage()
														.toString()));
										givenLoyaltycount = Integer
												.parseInt(loyaltyBeans.get(0)
														.getMessage()
														.toString());

										if (givenLoyaltycount >= Integer
												.parseInt(businessUser
														.getLoyaltycount())) {
											loyaltyLayout.setClickable(false);
											loyaltyLayout.setFocusable(false);
										} else {
											loyaltyLayout.setClickable(true);
											loyaltyLayout.setFocusable(true);
										}
									}

									if (pd.isShowing())
										pd.dismiss();

								}
							});
						}

					}
				});
				t4.start();

			} catch (Exception exception) {
				if (pd != null)
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

	private void giveLoyaltyToAppUser(String password) {
		if (pd != null && !pd.isShowing()) {
			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);
		}

		if (Utils.isNetworkAvailableNew(getActivity())) {

			try {

				jsonObject = new JSONObject();

				jsonObject.put("buserid", businessUser.getId());

				jsonObject.put("appuserid",
						AppPrefrence.getProfile(getActivity()).getUserid());
				jsonObject.put("passoword", password);

				t5 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GiveLoyaltyToAppUser",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									if (response != null
											&& response.length() > 0
											&& response.startsWith("{")) {

										Type listType = new TypeToken<ArrayList<LoyaltyBean>>() {
										}.getType();

										try {
											loyaltyBeans = new Gson()
													.fromJson(
															new JSONObject(
																	response)
																	.getJSONArray(
																			"responsepacketList")
																	.toString(),
															listType);

										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();

										}

									}

									if (loyaltyBeans != null
											&& loyaltyBeans.size() > 0) {

										try {
											jsonObject = new JSONObject(
													response);
											if (jsonObject.getString(
													"responsecode").equals(
													"101")
													&& jsonObject
															.getString("status")
															.equalsIgnoreCase(
																	"Invalid Password")) {
												Utils.showDialog(getActivity(),
														"Invalid Password");

												getBusinessLoyalty();

											} else if (jsonObject.getString(
													"responsecode").equals(
													"100")) {
												Utils.showDialog(
														getActivity(),
														jsonObject
																.getString("status"));

												getBusinessLoyalty();

											} else if (jsonObject.getString(
													"responsecode").equals(
													"200")
													&& jsonObject.getString(
															"status")
															.equalsIgnoreCase(
																	"OK")) {

												ratingBar.setRating(Float
														.parseFloat(loyaltyBeans
																.get(0)
																.getLoyaltycount()
																.toString()));

												givenLoyaltycount = Integer
														.parseInt(loyaltyBeans
																.get(0)
																.getLoyaltycount()
																.toString());

												if (givenLoyaltycount == Integer.parseInt(businessUser
														.getLoyaltycount())) {

													loyaltyLayout
															.setClickable(false);
													loyaltyLayout
															.setFocusable(false);

													String imgUrl = getString(R.string.image_url)
															+ "LoyaltyCoupen/"
															+ businessUser
																	.getId()
															+ "/original.jpg?id="
															+ System.currentTimeMillis();

													Intent intent = new Intent(
															getActivity(),
															ImageZoomInZoomOutActivity.class);
													intent.putExtra("imgUrl",
															imgUrl);
													getActivity()
															.startActivity(
																	intent);

												} else {

													loyaltyLayout
															.setClickable(true);
													loyaltyLayout
															.setFocusable(true);
												}

												dialog.dismiss();

											} else {

												getBusinessLoyalty();
											}
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();

										}
									}

									if (pd.isShowing())
										pd.dismiss();
								}
							});
						}

					}
				});
				t5.start();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else {
			if (pd != null && pd.isShowing())
				pd.dismiss();
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));
		}

	}

	private void getBusinessGalleryPicture() {

		if (pd != null && !pd.isShowing()) {
			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);
		}

		if (Utils.isNetworkAvailableNew(getActivity())) {

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", businessUser.getId());
				jsonObject.put("isorderbyasc", true);
				jsonObject.put("orderbyprm", "addedon");
				jsonObject.put("pageno", 0);
				jsonObject.put("pagesize", 10);

				t3 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GalleryListByBusiness",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObject(response);

									if (AppPrefrence.isLogin(getActivity())) {

										getBusinessLoyalty();

									} else {
										if (pd.isShowing())
											pd.dismiss();
									}

									if (businessImageList != null
											&& businessImageList.size() > 0) {
										galleryLayout
												.setVisibility(View.VISIBLE);

										viewPager
												.setAdapter(new ImageAdapter());
										circlePageIndicator
												.setViewPager(viewPager);

										v.findViewById(R.id.gallaryTop)
												.setVisibility(View.VISIBLE);

									} else {
										v.findViewById(R.id.gallaryTop)
												.setVisibility(View.GONE);
										galleryLayout.setVisibility(View.GONE);
									}

								}
							});
						}

					}
				});
				t3.start();
			} catch (Exception exception) {

				if (pd != null)
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

	private void getBusinessProfileDetails() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", businessUserId);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GetBusinessDetailsById",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									parsJsonObjectForBusinessInfo(response);

									if (businessUser != null) {
										ratingBar.setMax(Integer
												.parseInt(businessUser
														.getLoyaltycount()
														.toString()));
										ratingBar.setNumStars(Integer
												.parseInt(businessUser
														.getLoyaltycount()
														.toString()));
										getUserBusinessImage();
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
		} else {
			if (pd != null && pd.isShowing())
				pd.dismiss();
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));
		}

	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<BusinessPictureBean>>() {
			}.getType();

			try {
				businessImageList = new Gson().fromJson(
						new JSONObject(response).getJSONArray(
								"responsepacketList").toString(), listType);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void parsJsonObjectForBusinessInfo(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<BusinessUser>>() {
			}.getType();

			try {
				JSONObject object = new JSONObject(response);

				if (object.getString("responsecode").equals("200")) {

					userBeans = new Gson().fromJson(
							object.getJSONArray("responsepacketList")
									.toString(), listType);

					if (userBeans != null && userBeans.size() > 0) {
						businessUser = userBeans.get(0);
						setValue();
					} else {
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

	private void getAddBannerImage() {
		final ImageLoader imageLoader = new ImageLoader();

		try {
			new Thread(new Runnable() {

				@Override
				public void run() {

					imageLoader.loadSingleImageBm(addBannerImgUrl,
							addBannerView);

					getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (pd.isShowing())
								pd.dismiss();

						}
					});

				}
			}).start();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	private void getUserBusinessImage() {
		final ImageLoader imageLoader = new ImageLoader();

		final ImageView business_img = (ImageView) v
				.findViewById(R.id.businessImg);

		if (pd != null && !pd.isShowing()) {
			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);
		}

		try {
			t2 = new Thread(new Runnable() {

				@Override
				public void run() {

					final String imgUrl = getString(R.string.image_url)
							+ "User/" + businessUser.getId()
							+ "/original.jpg?id=" + businessUser.getTime();

					final boolean isLogoImageExists = Utils
							.checkImageIsAvailable(imgUrl);

					if (v != null) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								if (isLogoImageExists) {
									((LinearLayout) v
											.findViewById(R.id.businessLogoImageLayout))
											.setVisibility(View.VISIBLE);
									imageLoader.loadSingleImageBm(imgUrl,
											business_img);

								} else {
									((LinearLayout) v
											.findViewById(R.id.businessLogoImageLayout))
											.setVisibility(View.GONE);
								}

								getBusinessGalleryPicture();
							}
						});

					}
				}
			});

			t2.start();

		} catch (Exception exception) {
			if (pd != null)
				pd.dismiss();
			exception.printStackTrace();
		}

	}

	/*
	 * private void parsJsonObject(String response) {
	 * 
	 * if (response != null && response.length() > 0 &&
	 * response.startsWith("{")) {
	 * 
	 * Type listType = new TypeToken<ArrayList<BusinessUser>>() { }.getType();
	 * 
	 * try { categoryList = new Gson().fromJson(new JSONObject(response)
	 * .getJSONArray("responsepacketList").toString(), listType); } catch
	 * (JSONException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * }
	 */

	@Override
	public void onClick(View v) {
		String url;
		Intent intent;
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopRight:
			// ((BaseContainerFragment)getParentFragment()).replaceFragment(new
			// AddDrinkFragment(), true);
			break;

		case R.id.businessMenuBtn:

			intent = new Intent(getActivity(), ImageZoomInZoomOutActivity.class);
			intent.putExtra("imgUrl", busineMenuImgUrl);
			startActivity(intent);

			/*
			 * intent = new Intent(getActivity(), DetailImageActivity.class);
			 * url = getString(R.string.image_url) + "/BusinessMenu/" +
			 * businessUser.getId() + "/medium.jpg"; intent.putExtra("url",
			 * url); startActivity(intent);
			 */
			break;
		case R.id.businessOffersBtn:

			OfferListFragment offerListFragment = new OfferListFragment();
			Bundle bundle = new Bundle();
			bundle.putBoolean("isFromHomeBusiness", true);
			bundle.putString("business_id", businessUser.getId());
			offerListFragment.setArguments(bundle);
			((FavouriteContainerFragment) getParentFragment()).replaceFragment(
					offerListFragment, true, "Favourite");

			break;
		case R.id.websiteImg:

			url = businessUser.getWebsite().toString();

			Utils.openWebsite(getActivity(), url);

			break;
		case R.id.messageImg:

			String emailId = businessUser.getContactemail().toString();

			Utils.sendMail(getActivity(), emailId);

			break;
		case R.id.addImg:

			if (businessUser.getBusinessAddType().equals("1")) {
				intent = new Intent(getActivity(),
						ImageZoomInZoomOutActivity.class);
				intent.putExtra("imgUrl", webUrl);
				startActivity(intent);

			} else {
				intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
				intent.setDataAndType(Uri.parse(webUrl), "video/mp4");
				startActivity(intent);
			}

			break;
		case R.id.phoneImg:

			Utils.makeCall(getActivity(), businessUser.getContactno()
					.toString());

			break;
		case R.id.dirextionImg:

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

					// String uri = "geo:" + Constant.LAT + "," + Constant.LONG;

					String uri = "geo:0,0?q=" + businessUser.getAddress1()
							+ " " + businessUser.getAddress2() + " "
							+ businessUser.getCity() + ","
							+ businessUser.getState() + ","
							+ businessUser.getCountry();
					if (uri.startsWith(","))
						uri = uri.substring(1);
					if (uri.endsWith(","))
						uri = uri.substring(0, uri.length() - 1);

					Log.d("uri", uri.trim());
					startActivity(new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(uri
									.trim())));
				}

			}

			break;
		case R.id.btnTopLeft:

			((HomeActivity) getActivity()).onBackPressed();
			break;
		case R.id.favouriteBtn:

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setCancelable(true);
			builder.setMessage("Already added to your favourites.");
			builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

							dialog.dismiss();

						}
					});

			builder.create().show();

			break;

		default:
			break;
		}

	}

	/*
	 * @Override public void onDestroyView() { // TODO Auto-generated method
	 * stub // dbh.closeDB(); super.onDestroyView();
	 * 
	 * 
	 * v = null;
	 * 
	 * 
	 * 
	 * if(t1!=null) t1.interrupt(); if(t2!=null) t2.interrupt(); if(t3!=null)
	 * t3.interrupt(); if(t4!=null) t4.interrupt();
	 * 
	 * }
	 */

	class ImageAdapter extends PagerAdapter {
		String imgUrl = "";
		String webUrl = "";
		ImageLoader imgLoader;

		public ImageAdapter() {
			// TODO Auto-generated constructor stub
			imgLoader = new ImageLoader();
		}

		@Override
		public int getCount() {
			return businessImageList.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, final int position) {

			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = (View) inflater.inflate(R.layout.row_image_item, null);

			final ImageView img_category = (ImageView) view
					.findViewById(R.id.imgView);
			final ImageView playBtnImage = (ImageView) view
					.findViewById(R.id.videoPlayImage);
			/*
			 * final TextView txt_categoryName = (TextView) view
			 * .findViewById(R.id.txt_categoryName);
			 */

			if (businessImageList != null && businessImageList.size() > 0) {
				String timestamp = businessImageList.get(position).getTime();
				if (businessImageList.get(position).getType().equals("2")) {

					playBtnImage.setVisibility(View.VISIBLE);
					imgUrl = getString(R.string.image_url) + "gallery/video/"
							+ businessImageList.get(position).getId()
							+ "/thumb_"
							+ businessImageList.get(position).getId()
							+ ".jpg?id=" + timestamp;
					webUrl = getString(R.string.image_url) + "gallery/video/"
							+ businessImageList.get(position).getId()
							+ "/Video_"
							+ businessImageList.get(position).getId() + ".mp4";
					img_category.setTag(webUrl);
				} else {
					imgUrl = getString(R.string.image_url) + "gallery/images/"
							+ businessImageList.get(position).getId()
							+ "/original.jpg?id=" + timestamp;
					playBtnImage.setVisibility(View.GONE);

					webUrl = imgUrl;
					img_category.setTag(webUrl);

				}
			}

			if (imgUrl.length() > 0
					&& (imgUrl.contains(".jpg") || imgUrl.contains(".gif")
							|| imgUrl.contains(".png") || imgUrl
								.contains(".jpeg"))) {

				Bitmap bitmap = imgLoader.loadImage(imgUrl,
						new ImageLoadedListener() {

							@Override
							public void imageLoaded(Bitmap imageBitmap) {

								if (imageBitmap != null) {
									img_category.setImageBitmap(imageBitmap);

									notifyDataSetChanged();
								}
								// TODO Auto-generated method stub

							}
						});
				if (bitmap != null)
					img_category.setImageBitmap(bitmap);
				else
					img_category.setImageDrawable(getResources().getDrawable(
							R.drawable.no_image));
			}

			img_category.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (businessImageList.get(position).getType().equals("2")) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(webUrl));
						intent.setDataAndType(Uri.parse(webUrl), "video/mp4");
						startActivity(intent);
					} else {

						/*
						 * Intent intent = new Intent(getActivity(),
						 * DetailImageActivity.class); intent.putExtra("url",
						 * v.getTag().toString()); startActivity(intent);
						 */

						Intent i = new Intent(getActivity(),
								FullScreenViewActivity.class);
						i.putExtra("position", position);
						Bundle bundle = new Bundle();

						bundle.putSerializable("imagePathList",
								businessImageList);

						i.putExtras(bundle);
						startActivity(i);

					}

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
		if (t3 != null)
			t3.interrupt();
		if (t4 != null)
			t4.interrupt();
		if (t5 != null)
			t5.interrupt();

		Thread.currentThread().interrupt();
		v = null;

	}

}
