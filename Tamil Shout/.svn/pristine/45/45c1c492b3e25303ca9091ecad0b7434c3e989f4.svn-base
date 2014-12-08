package com.tamilshout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.tamilshout.R;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.TouchImageView;
import com.tamilshout.utils.Utils;
import com.tamilshout.utils.ImageLoader.ImageLoadedListener;

public class ImageZoomInZoomOutActivity extends BaseClass {

	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;
	ImageView imgDisplay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.image_zoom_in_out);

		utils = new Utils(this);
		System.out.println("------------ImageZoomInZoomOutActivity---------------");

		setHeader("Image");
		setLeftButton(R.drawable.btn_back);
		Intent i = getIntent();

		imgDisplay = (TouchImageView) findViewById(R.id.imgDisplay);
		/*
		 * priviousImg = (ImageView) viewLayout.findViewById(R.id.previousBtn);
		 * nextImg = (ImageView) viewLayout.findViewById(R.id.nextBtn);
		 */

		Bitmap bitmap = new ImageLoader().loadImage(i.getStringExtra("imgUrl"),
				new ImageLoadedListener() {

					@Override
					public void imageLoaded(Bitmap imageBitmap) {

						if (imageBitmap != null) {
							imgDisplay.setImageBitmap(imageBitmap);
						}
						// TODO Auto-generated method stub

					}
				});

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopLeft:
			finish();
			
			break;

		default:
			break;
		}

	}
}
