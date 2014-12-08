package com.tamilshout.fragments;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.model.ItemPictureBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class MyItemsGallary extends BaseFragment {

	ImageLoader imageLoader = new ImageLoader();
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;

	EditText search_edit_text;
	String itemId = "", imageFilePath = "";
	ImageView imageView1, imageView2, imageView3, imageView4, imageView5;
	View v;
	
	Intent intent = null;

	int RESULT_OK = 1, REQUEST_CAMERA = 2, SELECT_FILE = 3;
	Uri outputFileUri = null;
	private final int RESULT_LOAD_IMAGE = 1111;
	ImageView imageView;

	String itemIdForUpload = "";
	Thread t1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Constant.newAddedImagePath = new TreeMap<Integer, String>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		v = inflater.inflate(R.layout.my_items_gallary, null);

		imageView1 = (ImageView) v.findViewById(R.id.imageView1);
		imageView2 = (ImageView) v.findViewById(R.id.imageView2);
		imageView3 = (ImageView) v.findViewById(R.id.imageView3);
		imageView4 = (ImageView) v.findViewById(R.id.imageView4);
		imageView5 = (ImageView) v.findViewById(R.id.imageView5);

		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView1.setTag(0);
				selectImage(imageView1);

			}
		});
		imageView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView2.setTag(1);
				selectImage(imageView2);
			}
		});
		imageView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView3.setTag(2);
				selectImage(imageView3);

			}
		});
		imageView4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView4.setTag(3);
				selectImage(imageView4);

			}
		});
		imageView5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imageView5.setTag(4);
				selectImage(imageView5);

			}
		});

		Log.d("drink", "Drink On Create View");

		Bundle args = getArguments();
		if (args != null && args.containsKey("itemid"))
			itemId = args.getString("itemid");

		setHeader(v, "Item Picture");

		setLeftButton(v, R.drawable.btn_back);
		setTouchNClick(v, R.id.addNewImageBtn);

		if (itemId != null) {
			itemIdForUpload = itemId + "@";
			getItemPicture();

		}

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
		case R.id.addNewImageBtn:

			((HomeActivity) getActivity()).onBackPressed();

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

				t1=new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "SelectItemForSaleImages",
								jsonObject.toString());

						if(v!=null){
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObject(response);

								if (pd!=null && pd.isShowing())
									pd.dismiss();
							}

						});
						}

					}
				});t1.start();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));

	}

	private void parsJsonObject(String response) {
		if (Constant.imagePathHashMap != null) {
			Constant.imagePathHashMap.clear();
			Constant.imagePathHashMap = null;
		}
		if (Constant.itemImageList != null) {
			Constant.itemImageList.clear();
			Constant.itemImageList = null;
		}

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {
			Constant.imagePathHashMap = new TreeMap<Integer, String>();

			Type listType = new TypeToken<ArrayList<ItemPictureBean>>() {
			}.getType();

			try {
				Constant.itemImageList = new Gson().fromJson(
						new JSONObject(response).getJSONArray(
								"responsepacketList").toString(), listType);

				if (Constant.itemImageList != null
						&& Constant.itemImageList.size() > 0) {
					ImageView imageView;
					imageView1.setImageBitmap(null);
					imageView2.setImageBitmap(null);
					imageView3.setImageBitmap(null);
					imageView4.setImageBitmap(null);
					imageView5.setImageBitmap(null);
					for (int i = 0; i < Constant.itemImageList.size(); i++) {
						String timeStamp = Constant.itemImageList.get(i)
								.getTime();

						String imgUrl = getString(R.string.image_url)
								+ "ItemForSale/"
								+ Constant.itemImageList.get(i).getItemid()
								+ "/"
								+ Constant.itemImageList.get(i).getItemimag()
								+ "/medium.jpg?+id=" + timeStamp;

						switch (i) {
						case 0:
							Constant.imagePathHashMap.put(0, imgUrl);

							setImageForView(imageView1, imgUrl);
							break;
						case 1:

							Constant.imagePathHashMap.put(1, imgUrl);
							setImageForView(imageView2, imgUrl);
							break;
						case 2:
							Constant.imagePathHashMap.put(2, imgUrl);

							setImageForView(imageView3, imgUrl);
							break;
						case 3:

							Constant.imagePathHashMap.put(3, imgUrl);
							setImageForView(imageView4, imgUrl);
							break;
						case 4:
							Constant.imagePathHashMap.put(4, imgUrl);

							setImageForView(imageView5, imgUrl);
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

	

	private void selectImage(ImageView imageView12) {

		imageView = imageView12;

		System.out.println("----------getTag-------" + imageView.getTag());

		final CharSequence[] items = { "Take Photo", "Choose from Gallery",
				"Remove Image", "Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {

					intent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

					File file = new File(Environment
							.getExternalStorageDirectory(), Calendar
							.getInstance().getTimeInMillis() + ".jpg");

					outputFileUri = Uri.fromFile(file);
					imageFilePath = file.getPath();

					intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
					getActivity()
							.startActivityForResult(intent, REQUEST_CAMERA);

				} else if (items[item].equals("Choose from Gallery")) {

					if (Build.VERSION.SDK_INT < 19) {
						Intent intent = new Intent();
						intent.setType("image/*");
						intent.setAction(Intent.ACTION_GET_CONTENT);
						getActivity().startActivityForResult(intent,
								SELECT_FILE);
					} else {
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						getActivity().startActivityForResult(intent,
								SELECT_FILE);
					}

					/*
					 * Intent intent = new Intent( Intent.ACTION_PICK,
					 * android.provider
					 * .MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					 * intent.setType("image/*");
					 * getActivity().startActivityForResult(
					 * Intent.createChooser(intent, "Select File"),
					 * SELECT_FILE);
					 */
				} else if (items[item].equals("Remove Image")) {

					if (Constant.itemImageList != null
							&& Integer.parseInt(imageView.getTag().toString()) < Constant.itemImageList
									.size()) {

						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setCancelable(true);
						builder.setMessage("Are you sure to remove this image from server?");
						builder.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										dialog.dismiss();
										deleteImageFromServer(Integer
												.parseInt(imageView.getTag()
														.toString()));

									}
								});

						builder.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();

									}
								});
						builder.create().show();
					} else {
						Constant.imagePathHashMap.remove(Integer
								.parseInt(imageView.getTag().toString()));
						imageView.setImageBitmap(null);
					}

				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	private void deleteImageFromServer(final int imageId) {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			try {

				jsonObject = new JSONObject();

				if (Constant.itemImageList != null
						&& imageId < Constant.itemImageList.size()) {

					jsonObject.put("id", Constant.itemImageList.get(imageId)
							.getItemimag());

					new Thread(new Runnable() {

						public void run() {

							response = GetDataFromWebService.readJsonFeed(
									getString(R.string.service_url)
											+ "DeleteItemForSaleImage",
									jsonObject.toString());

							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									if (response != null
											&& response.length() > 0
											&& response.startsWith("{")) {
										try {
											JSONObject jsonObject = new JSONObject(
													response);

											if (jsonObject != null
													&& jsonObject.getString(
															"responsecode")
															.equals("100")) {

												Utils.showDialog(
														getActivity(),
														jsonObject
																.getString("ResponseMessage"));

											} else {

												Utils.showDialog(
														getActivity(),
														jsonObject
																.getString("ResponseMessage"),
														"",
														new DialogInterface.OnClickListener() {

															@Override
															public void onClick(
																	DialogInterface dialog,
																	int which) {
																Constant.imagePathHashMap
																		.remove(imageId);
																getItemPicture();

															}
														}, false).show();
											}

										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}

									} else {
										Utils.showDialog(getActivity(),getString(R.string.server_error));
									}

									// parsJsonObject(response);

								}

							});

						}
					}).start();
				} else {

				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));

	}

	public String getPath(Uri uri, Activity activity) {
		String[] projection = { MediaColumns.DATA };
		Cursor cursor = activity
				.managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private String getRealPathFromURI(Uri contentURI) {
		try {
			Cursor cursor = getActivity().getContentResolver().query(
					contentURI, null, null, null, null);
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaColumns.DATA);
			return cursor.getString(idx);
		} catch (Exception e) {
			Log.d("error", e.toString());
		}
		return "";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("=============data=============" + data);
		// super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == SELECT_FILE && data != null) {

			Uri selectedImage = data.getData();
			String path = null;
			if (Build.VERSION.SDK_INT < 19) {
				path = Utils.getPath(getActivity(), selectedImage);
			} else {
				path = Utils.getPath(getActivity(),selectedImage
						);
			}

			System.out.println("path" + path);
			Constant.imagePathHashMap.put(Integer.parseInt(imageView.getTag().toString()), path);

			BitmapFactory.Options config = new BitmapFactory.Options();
			config.inPreferredConfig = Bitmap.Config.RGB_565;
			config.inSampleSize = 4;
			Bitmap bmImg = BitmapFactory.decodeFile(path, config);
			imageView.setImageBitmap(Utils.getOrientationFixedImage(path, bmImg));

			// customGridAdapter.notifyDataSetChanged();

			// Utils.putObjectIntoPref(ImageGallery.this, images,
			// Constant.IMAGES);
		} else if (requestCode == REQUEST_CAMERA) {

			Constant.imagePathHashMap.put(
					Integer.parseInt(imageView.getTag().toString()),
					imageFilePath);

			BitmapFactory.Options config = new BitmapFactory.Options();
			config.inPreferredConfig = Bitmap.Config.RGB_565;
			config.inSampleSize = 4;
			Bitmap bmImg = BitmapFactory.decodeFile(imageFilePath, config);

			imageView.setImageBitmap(Utils.getOrientationFixedImage(imageFilePath, bmImg));

			/*
			 * images.add(Utils.getRealPathFromURI(ImageGallery.this,
			 * selectedImage));
			 */
			// customGridAdapter.notifyDataSetChanged();

			// Utils.putObjectIntoPref(ImageGallery.this, images,
			// Constant.IMAGES);
		} else {
			System.out.println("requestCode" + requestCode);
			System.out.println("resultCode" + resultCode);
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
