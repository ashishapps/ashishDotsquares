package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.R;
import com.tamilshout.model.ItemForSaleBean;
import com.tamilshout.model.ItemPictureBean;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class AddMyItemFragment extends BaseFragment {

	private ProgressDialog pd;

	JSONObject jsonObject = null;

	EditText categoryEdt, categoryIdEdt;;

	String catId = "";
	String imgUrl = "";
	String itemId = "0";
	View v;

	Thread t1, t2;

	ArrayList<ItemPictureBean> itemPictureBeans;
	private String emailStr, descriptionStr, response, titleNameStr, priceStr,
			contactNameStr, mobileNoStr, categoryStr, genderStr, categoryID;

	ItemForSaleBean businessUser = null, itembean = null;
	String itemIdForUpload = "";
	Bundle args;
	String res;

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.add_item_fragment, null);
		
	
		

		args = getArguments();
		if (args != null && args.containsKey("itembean")) {
			itembean = (ItemForSaleBean) args.getSerializable("itembean");
		} else {
			itembean = null;
			itemId = "" + 0;
		}

		Constant.appContext = getActivity();
		Log.d("Home", "Category Lsit");

		setHeader(v, "Add Item For Sale");
		setLeftButton(v, R.drawable.btn_back);
		// setRightButton(v, "Gallary", R.drawable.icon_gallery);

		setTouchNClick(v, R.id.btnTopLeft);
		// setTouchNClick(v, R.id.btnTopRight);

		// All Textview

		// All Button

		setTouchNClick(v, R.id.uploadImageBtn);
		setTouchNClick(v, R.id.saveBtn);

		categoryEdt = (EditText) v.findViewById(R.id.categoryEdt);
		categoryIdEdt = (EditText) v.findViewById(R.id.categoryIdEdt);

		categoryEdt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final String names[] = { "Vehicles", "Businesses",
						"Clothing & Accessories", "Books CDs & Hobbies",
						"Electronic & Computer", "Poperties",
						"Home & Furniture", "Shop & Equipment", "Other Items" };
				final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						getActivity());
				LayoutInflater inflater = getActivity().getLayoutInflater();
				View convertView = (View) inflater.inflate(
						R.layout.dialog_custom_titile, null);
				alertDialog.setView(convertView);
				alertDialog.setTitle("Select Category");

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
						categoryEdt.setText(names[arg2]);
						arg2 = arg2 + 1;
						categoryEdt.setTag(arg2);
						categoryID = "" + arg2;
						categoryIdEdt.setText("" + arg2);

					}
				});

			}
		});

		if (itembean != null) {
			setDataToForm();

		}
		
		if (!itemId.equals("0")) {

			getItemPicture();
		} else {
			if (itemPictureBeans != null && itemPictureBeans.size() > 0) {
				itemPictureBeans.clear();

			}

		}

		return v;
	}

	private void getItemPicture() {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", itemId);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "SelectItemForSaleImages",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									if (response != null
											&& response.length() > 0
											&& response.startsWith("{")) {

										Type listType = new TypeToken<ArrayList<ItemPictureBean>>() {
										}.getType();

										try {
											itemPictureBeans = new Gson()
													.fromJson(
															new JSONObject(
																	response)
																	.getJSONArray(
																			"responsepacketList")
																	.toString(),
															listType);
										} catch (Exception exception) {

										}
										if (pd.isShowing())
											pd.dismiss();
									} else {
										if (pd.isShowing())
											pd.dismiss();
									}
								}

							});
						}
					}
				});
				t1.start();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		if (Constant.imagePathHashMap != null)
			getPathListFromHashMap();
		else {
			Constant.imagePathHashMap = null;
			Constant.imagePathList = null;

		}

		

	}

	private void setDataToForm() {

		((EditText) v.findViewById(R.id.categoryEdt)).setText(itembean
				.getItemcategoryname());
		((EditText) v.findViewById(R.id.categoryEdt)).setTag(itembean
				.getItemcategoryid());
		((EditText) v.findViewById(R.id.itemNameEdt)).setText(itembean
				.getItemName());
		((EditText) v.findViewById(R.id.priceEdt)).setText(itembean.getPrice());
		((EditText) v.findViewById(R.id.descriptionEdt)).setText(itembean
				.getDescription());
		((EditText) v.findViewById(R.id.contactNameEdt)).setText(itembean
				.getContact_name());
		((EditText) v.findViewById(R.id.emailEdt)).setText(itembean
				.getContact_email());
		((EditText) v.findViewById(R.id.phoneEdt)).setText(itembean
				.getContact_phone());
		((EditText) v.findViewById(R.id.categoryIdEdt)).setText(""
				+ itembean.getItemcategoryid());
		categoryID = itembean.getItemcategoryid();

		itemId = itembean.getItemid();

	}

	private void getPathListFromHashMap() {

		if (Constant.imagePathList != null)
			Constant.imagePathList.clear();

		Constant.imagePathList = new ArrayList<String>();

		for (Map.Entry<Integer, String> entry : Constant.imagePathHashMap
				.entrySet()) {

			Integer key = entry.getKey();
			String value = entry.getValue();

			if (value.toString().contains("http://")
					|| value.contains("https://")) {

			} else
				Constant.imagePathList.add(value);
		}

	}

	private void addUser() {

		if (!isEmpty()) {

			if (!((Constant.imagePathHashMap != null && Constant.imagePathHashMap
					.size() > 0) || (Constant.imagePathList != null && Constant.imagePathList
					.size() > 0))) {
				if (itemPictureBeans != null && itemPictureBeans.size() < 0) {
					Utils.showDialog(getActivity(),
							"Please select at least one image for item.");
					return;
				} else if (itemPictureBeans == null) {
					Utils.showDialog(getActivity(),
							"Please select at least one image for item.");
					return;
				}
			}

			if (Utils.isNetworkAvailableNew(getActivity())) {

				pd = ProgressDialog.show(getActivity(), "", ""
						+ getString(R.string.processing));
				pd.setCancelable(true);

				jsonObject = new JSONObject();
				try {

					/*
					 * if (Constant.SELECTED_COUNTRY_NAME != null &&
					 * Constant.SELECTED_COUNTRY_NAME.length() > 0)
					 * jsonObject.put("country",
					 * Constant.SELECTED_COUNTRY_NAME); else
					 * jsonObject.put("country", Constant.CURRENT_COUNTRY);
					 */

					jsonObject.put("contactemail", emailStr);
					jsonObject.put("categoryid", categoryIdEdt.getText()
							.toString());

					jsonObject.put("appuserid",
							AppPrefrence.getProfile(getActivity()).getUserid());
					jsonObject.put("title", titleNameStr);
					jsonObject.put("isactive", false);
					jsonObject.put("description", descriptionStr);
					if (itembean != null && itembean.getItemid() != null
							&& itembean.getItemid().toString().length() > 0)
						jsonObject.put("itemid", itemId);
					else
						jsonObject.put("itemid", 0);

					jsonObject.put("contactname", contactNameStr);
					jsonObject.put("price", priceStr);
					jsonObject.put("contactphone", mobileNoStr);

					t2 = new Thread(new Runnable() {

						public void run() {

							response = GetDataFromWebService.readJsonFeed(
									getString(R.string.service_url)
											+ "AddUpdateMyItem",
									jsonObject.toString());

							if (v != null) {
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {

										res = parsJsonObject(response);

										if (itemId != null) {
											new Thread(new Runnable() {

												@Override
												public void run() {

													itemIdForUpload = itemId
															+ "@";

													int i = 0;
													if (Constant.imagePathHashMap != null
															&& Constant.imagePathHashMap
																	.size() > 0) {
														for (Map.Entry<Integer, String> entry : Constant.imagePathHashMap
																.entrySet()) {

															Integer key = entry
																	.getKey();
															String value = entry
																	.getValue();

															if (value
																	.toString()
																	.contains(
																			"http://")
																	|| value.contains("https://")) {

															} else {

																if (Constant.itemImageList != null
																		&& Constant.itemImageList
																				.size() > 0
																		&& Constant.itemImageList
																				.size() > i) {
																	itemIdForUpload = itemIdForUpload
																			+ Constant.itemImageList
																					.get(i)
																					.getItemimag()
																			+ "_";
																} else {
																	itemIdForUpload = itemIdForUpload
																			+ "0_";
																}

															}
															i++;
														}

														if (itemIdForUpload
																.charAt(itemIdForUpload
																		.length() - 1) == '_') {
															itemIdForUpload = itemIdForUpload
																	.substring(
																			0,
																			itemIdForUpload
																					.length() - 1);
														}

														String uploadImageUrl = getString(R.string.images_upload_url)
																+ itemIdForUpload;

														if (Constant.imagePathList
																.size() > 0) {
															response = GetDataFromWebService
																	.uploadImagesOnServer(
																			Constant.imagePathList,
																			uploadImageUrl);

														}

														getActivity()
																.runOnUiThread(
																		new Runnable() {

																			@Override
																			public void run() {

																				Utils.showDialog(
																						getActivity(),
																						res
																								+ " and request has been sent to admin for approval.",
																						"",
																						new DialogInterface.OnClickListener() {

																							@Override
																							public void onClick(
																									DialogInterface dialog,
																									int which) {
																								dialog.dismiss();
																								((HomeActivity) getActivity())
																										.onBackPressed();

																							}
																						},
																						false)
																						.show();
																				if (pd.isShowing())
																					pd.dismiss();

																			}
																		});
													} else {
														getActivity()
																.runOnUiThread(
																		new Runnable() {

																			@Override
																			public void run() {

																				Utils.showDialog(
																						getActivity(),
																						"Item updated successfully and request has been sent to admin for approval.",
																						"",
																						new DialogInterface.OnClickListener() {

																							@Override
																							public void onClick(
																									DialogInterface dialog,
																									int which) {
																								dialog.dismiss();
																								((HomeActivity) getActivity())
																										.onBackPressed();

																							}
																						},
																						false)
																						.show();

																				if (pd.isShowing())
																					pd.dismiss();

																			}
																		});
														if (pd.isShowing())
															pd.dismiss();
													}

												}

											}).start();

										} else {
											if (pd.isShowing())
												pd.dismiss();
										}

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

	}

	private String parsJsonObject(String response) {

		String res = "";
		itemId = null;
		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			try {
				jsonObject = new JSONObject(response);
				if (jsonObject.getString("responsecode").equals("200")
						&& jsonObject.getString("status").equals("OK")) {

					JSONArray jsonArray = jsonObject
							.getJSONArray("responsepacketList");
					if (jsonArray != null && jsonArray.length() > 0) {

						itemId = jsonArray.getJSONObject(0).getString("id");

					}
					res = jsonObject.getString("ResponseMessage");
					// Utils.showDialog(getActivity(),
					// jsonObject.getString("ResponseMessage"));
				} else
					Utils.showDialog(getActivity(),
							jsonObject.getString("ResponseMessage"));

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			res = "Server error occured!";
			Utils.showDialog(getActivity(), "Server error occured!");
		}

		return res;

	}

	private boolean isEmpty() {
		emailStr = getEditTextText(R.id.emailEdt);
		titleNameStr = getEditTextText(R.id.itemNameEdt);
		priceStr = getEditTextText(R.id.priceEdt);
		mobileNoStr = getEditTextText(R.id.phoneEdt);
		descriptionStr = getEditTextText(R.id.descriptionEdt);
		contactNameStr = getEditTextText(R.id.contactNameEdt);
		categoryStr = getEditTextText(R.id.categoryEdt);
		if (categoryStr.isEmpty()) {
			return showErrorDialog("Please select category");
		} else if (titleNameStr.isEmpty()) {
			return showErrorDialog("Please enter item title");
		} else if (priceStr.isEmpty()) {
			return showErrorDialog("Please enter item price");
		} else if (descriptionStr.isEmpty()) {
			return showErrorDialog("Please enter item description");
		} else if (contactNameStr.isEmpty()) {
			return showErrorDialog("Please enter contact name");
		} else if (emailStr.isEmpty()) {
			return showErrorDialog("Please enter email address");
		} else if (!Utils.isValidEmail(emailStr)) {
			return showErrorDialog("Please enter valid email");
		} else if (mobileNoStr.isEmpty()) {
			return showErrorDialog("Please enter phone no.");
		}
		return false;
	}

	private boolean showErrorDialog(String message) {

		Utils.showDialog(getActivity(), message);
		return true;
	}

	public String getEditTextText(int id) {
		return ((EditText) v.findViewById(id)).getText().toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopRight:

			MyItemsGallary fragment = new MyItemsGallary();
			Bundle bundle = new Bundle();
			bundle.putString("itemid", businessUser.getItemid().toString());
			fragment.setArguments(bundle);
			if (args != null && args.containsKey("isComesFromHome")
					&& args.getBoolean("isComesFromHome", false))
				((HomeContainerFragment) getParentFragment()).replaceFragment(
						fragment, true, "HOME");
			else
				((MyItemsContainerFragment) getParentFragment())
						.replaceFragment(fragment, true, "My Items");

			break;

		case R.id.emailBtn:

			String emailId = businessUser.getContact_email().toString();

			if (emailId != null && emailId.length() > 0
					&& Utils.isValidEmail(emailId)) {
				Utils.sendMail(getActivity(), emailId);

			} else {
				Toast.makeText(getActivity(), "Invalid Email Address found",
						Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.saveBtn:

			if (Constant.imagePathHashMap == null
					&& Constant.imagePathList == null) {
				addUser();
			} else if ((Constant.imagePathHashMap != null && Constant.imagePathHashMap
					.size() > 0)
					|| (Constant.imagePathList != null && Constant.imagePathList
							.size() > 0)) {
				addUser();
			} else if ((itemPictureBeans != null && itemPictureBeans.size() > 0)) {
				addUser();
			} else {
				Utils.showDialog(getActivity(),
						getString(R.string.please_select_one_image));

			}

			break;
		case R.id.uploadImageBtn:

			MyItemsGallary fragment1 = new MyItemsGallary();
			bundle = new Bundle();
			bundle.putString("itemid", itemId);
			fragment1.setArguments(bundle);
			((MyItemsContainerFragment) getParentFragment()).replaceFragment(
					fragment1, true, "My Items");
			break;
		case R.id.messageBtn:

			Utils.sendMessage(getActivity(), businessUser.getContact_phone());

			break;

		case R.id.btnTopLeft:

			((HomeActivity) getActivity()).onBackPressed();
			break;

		default:
			break;
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		getActivity().getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		
		Utils.hideKeyboard(getActivity(), ((EditText) v.findViewById(R.id.itemNameEdt)));

		
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
