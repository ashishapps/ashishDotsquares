package com.tamilshout;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.model.ItemPictureBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageGallery extends BaseClass {
	ImageLoader imageLoader = new ImageLoader();
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;
	ArrayList<ItemPictureBean> itemForSaleList;
	String businessId = "";
	private GridView gvGallery;
	private CustomGridViewAdapter customGridAdapter;
	private ArrayList<String> images = new ArrayList<String>();
	private static int RESULT_LOAD_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_gallery);

		setHeader("GALLERY");

		setLeftButton(R.drawable.btn_back);

		initialize();
		
		
		businessId=getIntent().getStringExtra("businessId");

		customGridAdapter = new CustomGridViewAdapter(this,
				R.layout.gallery_item, images);
		gvGallery.setAdapter(customGridAdapter);
		
		getBusinessGalleryPicture();

	}

	private void getBusinessGalleryPicture() {

		if (Utils.isNetworkAvailableNew(this)) {

			pd = ProgressDialog.show(this, "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", businessId);
				jsonObject.put("isorderbyasc", true);
				jsonObject.put("orderbyprm", "addedon");
				jsonObject.put("pageno", 0);
				jsonObject.put("pagesize", 10);

				new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "GalleryListByBusiness",
								jsonObject.toString());

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObject(response);

								if (pd.isShowing())
									pd.dismiss();
							}

						});

					}
				}).start();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(this, getString(R.string.no_network_available));

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

					}

				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private void initialize() {
		gvGallery = (GridView) findViewById(R.id.gv_gallery);
		images = Utils.getObjectFromPref(ImageGallery.this, Constant.IMAGES);
		if (images == null)
			images = new ArrayList<String>();
	}

	public class CustomGridViewAdapter extends ArrayAdapter<String> {
		Context context;
		int layoutResourceId;
		ArrayList<String> data;

		public CustomGridViewAdapter(Context context, int layoutResourceId,
				ArrayList<String> data) {
			super(context, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.data = data;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = null;
			// RecordHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context)
						.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
				ImageView iv = (ImageView) row.findViewById(R.id.iv);

				row.setTag(position);
				BitmapFactory.Options config = new BitmapFactory.Options();
				config.inPreferredConfig = Bitmap.Config.RGB_565;
				config.inSampleSize = 4;
				Bitmap bmImg = BitmapFactory.decodeFile(data.get(position),
						config);
				iv.setImageBitmap(bmImg);

				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(final View v) {
						// TODO Auto-generated method stub
						Utils.showDialog(ImageGallery.this, "Delete",
								"Would you like to delete image from gallery",
								"Yes", "No",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										int id = (Integer) v.getTag();
										data.remove(id);
										notifyDataSetChanged();
										dialog.dismiss();
									}
								}, new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								}, true);

					}
				});
			}
			return row;

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*
		 * case R.id.btn: Intent i = new Intent( Intent.ACTION_PICK,
		 * android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		 * 
		 * startActivityForResult(i, RESULT_LOAD_IMAGE);
		 * 
		 * break;
		 */

		case R.id.btnTopLeft:
			finish();
			break;

		default:
			break;
		}

		super.onClick(v);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			Utils.getRealPathFromURI(ImageGallery.this, selectedImage);
			images.add(Utils.getRealPathFromURI(ImageGallery.this,
					selectedImage));
			customGridAdapter.notifyDataSetChanged();

			Utils.putObjectIntoPref(ImageGallery.this, images, Constant.IMAGES);
		}

	}
}
