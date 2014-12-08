package com.tamilshout;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.tamilshout.R;
import com.tamilshout.model.ImageList;
import com.tamilshout.utils.Utils;
import com.viewpagerindicator.CirclePageIndicator;

public class FullScreenViewActivity extends BaseClass {

	private Utils utils;
	private FullScreenImageAdapter adapter;
	private ViewPager viewPager;
	CirclePageIndicator circlePageIndicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		super.setContentView(R.layout.activity_fullscreen_view);
		viewPager = (ViewPager) findViewById(R.id.pager);
		
		setHeader("Image Gallery");
		setLeftButton(R.drawable.btn_back);

		utils = new Utils(this);

		circlePageIndicator = (CirclePageIndicator) findViewById(R.id.pagerIndicator);
		Intent i = getIntent();
		int position = i.getIntExtra("position", 0);
		ArrayList<ImageList> list = (ArrayList) getIntent().getExtras()
				.getSerializable("imagePathList");

		adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, list,
				viewPager);

		viewPager.setAdapter(adapter);

		// displaying selected image first
		viewPager.setCurrentItem(position);

		circlePageIndicator.setViewPager(viewPager);
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
