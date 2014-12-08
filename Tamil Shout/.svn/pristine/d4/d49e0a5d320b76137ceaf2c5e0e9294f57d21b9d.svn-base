package com.tamilshout;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.tamilshout.R;
import com.tamilshout.model.BusinessPictureBean;
import com.tamilshout.model.ImageList;
import com.tamilshout.model.ItemPictureBean;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.TouchImageView;

public class FullScreenImageAdapter extends PagerAdapter {

	private Activity _activity;
	private ArrayList<ImageList> list;
	private LayoutInflater inflater;
	ViewPager viewPager;
	ImageLoader imageLoader=new ImageLoader();

	// constructor
	public FullScreenImageAdapter(Activity activity,
			ArrayList<ImageList> imagePaths, ViewPager viewPager) {
		this._activity = activity;
		this.list = imagePaths;
		this.viewPager = viewPager;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((RelativeLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final TouchImageView imgDisplay;
		// ImageView priviousImg,nextImg;
		Button btnClose;

		inflater = (LayoutInflater) _activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image,
				container, false);

		imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
		/*
		 * priviousImg = (ImageView) viewLayout.findViewById(R.id.previousBtn);
		 * nextImg = (ImageView) viewLayout.findViewById(R.id.nextBtn);
		 */
	//	btnClose = (Button) viewLayout.findViewById(R.id.btnClose);
		Object object=null;
		if(list.get(position)instanceof ItemPictureBean)
		{
			object=(ItemPictureBean)list.get(position);
			String timestamp = ((ItemPictureBean) object).getTime();
			String imgUrl = _activity.getString(R.string.image_url)
					+ "ItemForSale/" + ((ItemPictureBean) object).getItemid() + "/"	+ ((ItemPictureBean) object).getItemimag() + "/original.jpg?id=" + timestamp;
			

			if (imgUrl.length() > 0
					&& (imgUrl.contains(".jpg") || imgUrl.contains(".gif")
							|| imgUrl.contains(".png") || imgUrl
								.contains(".jpeg"))) {

				Bitmap bitmap = imageLoader.loadImage(imgUrl,
						new ImageLoader.ImageLoadedListener() {

							@Override
							public void imageLoaded(Bitmap imageBitmap) {
								if (imageBitmap != null) {
									imgDisplay.setImageBitmap(imageBitmap);

									notifyDataSetChanged();
								}

							}
						});
				if (bitmap != null)
					imgDisplay.setImageBitmap(bitmap);
				else
					imgDisplay.setImageDrawable(_activity.getResources().getDrawable(
							R.drawable.no_image));

			}
		}
		else
		{
			
			object=(BusinessPictureBean)list.get(position);
			String timestamp = ((BusinessPictureBean) object).getTime();
			String imgUrl = _activity.getString(R.string.image_url)
					+ "gallery/images/" + ((BusinessPictureBean) object).getId() + "/original.jpg?id=" + timestamp;
			

			if (imgUrl.length() > 0
					&& (imgUrl.contains(".jpg") || imgUrl.contains(".gif")
							|| imgUrl.contains(".png") || imgUrl
								.contains(".jpeg"))) {

				Bitmap bitmap = imageLoader.loadImage(imgUrl,
						new ImageLoader.ImageLoadedListener() {

							@Override
							public void imageLoaded(Bitmap imageBitmap) {
								if (imageBitmap != null) {
									imgDisplay.setImageBitmap(imageBitmap);

									notifyDataSetChanged();
								}

							}
						});
				if (bitmap != null)
					imgDisplay.setImageBitmap(bitmap);
				else
					imgDisplay.setImageDrawable(_activity.getResources().getDrawable(
							R.drawable.no_image));

			}
		}
		
		
	/*	btnClose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_activity.finish();
			}
		});*/

		/*
		 * priviousImg.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * if(Integer.parseInt(v.getTag().toString())>0)
		 * viewPager.setCurrentItem(Integer.parseInt(v.getTag().toString())-1,
		 * true); } }); nextImg.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * if(list.size()>Integer.parseInt(v.getTag().toString()))
		 * viewPager.setCurrentItem(Integer.parseInt(v.getTag().toString())+1,
		 * true);
		 * 
		 * } });
		 */
		((ViewPager) container).addView(viewLayout);

		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((RelativeLayout) object);

	}
}