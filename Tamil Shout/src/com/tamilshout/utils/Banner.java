package com.tamilshout.utils;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tamilshout.R;
import com.tamilshout.model.AddvertiseBean;

public class Banner extends LinearLayout {

	private Context ctx;
	private ImageView imgBanner;
	private String website = "";
	String imgUrl = "";
	ImageLoader imgLoader;
	WebView addevertiseWebView;

	JSONObject jsonObject = null;
	String response = "";
	static ArrayList<AddvertiseBean> addvertiseBeans = null;
	ProgressDialog pd;
	ImageLoader imageLoader = new ImageLoader();

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public Banner(Context context, AttributeSet atb) {

		super(context, atb);
		// TODO Auto-generated constructor stub\

		int width = 0;
		int height = 0;

		ctx = context;
		final LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.bottom, this);

		addevertiseWebView = (WebView) view
				.findViewById(R.id.addevertiseImageView);
		addevertiseWebView.getSettings().setJavaScriptEnabled(true);
		addevertiseWebView.setVerticalScrollBarEnabled(false);
		addevertiseWebView.setHorizontalScrollBarEnabled(false);

		Constant.appContext = context;

		if (android.os.Build.VERSION.SDK_INT >= 13) {
			Display display = ((Activity) context).getWindowManager()
					.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);

			width = size.x;
			height = size.y;
		} else {
			width = ((Activity) context).getWindowManager().getDefaultDisplay()
					.getWidth();
			height = ((Activity) context).getWindowManager()
					.getDefaultDisplay().getHeight();
		}

		height = (height / 100) * 10;

		addevertiseWebView.getLayoutParams().height = height;
		height = (int) Utils.convertPixelsToDp(height, context);
		width = (int) Utils.convertPixelsToDp(width, context);

		System.out.println("--------width---------" + width);
		System.out.println("--------height---------" + height);

		String location = "";
		if (Constant.SELECTED_COUNTRY_NAME != null
				&& Constant.SELECTED_COUNTRY_NAME.length() > 0) {
			location = Constant.SELECTED_COUNTRY_NAME;

		} else {
			location = Constant.CURRENT_COUNTRY;
		}

		String webUrl = ctx.getString(R.string.add_url) + "type=other&height="
				+ height + "&width=" + width + "&device=android&location="
				+ location;

		addevertiseWebView.loadUrl(webUrl);

	}

}
