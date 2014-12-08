package com.tamilshout;

import com.tamilshout.R;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TermsAndConditionsActivity extends BaseClass {
	private TextView txtHeading;

	private WebView web;
	private ProgressBar pbMain;
	private String path;
	private ImageView img;
	Boolean flag = true;

	private ProgressDialog pd;

	LinearLayout lay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.detail_image_view);
		
		setHeader("Terms and Conditions");
		setLeftButton(R.drawable.btn_back);

		

		pbMain = (ProgressBar) findViewById(R.id.progressBarWeb);
		web = (WebView) findViewById(R.id.web);
		web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

		WebSettings setting = web.getSettings();
		setting.setBuiltInZoomControls(true);
		// setting.enableSmoothTransition();
		// setting.setDisplayZoomControls(false);
		setting.setJavaScriptEnabled(true);
		web.setWebChromeClient(new WebChromeClient() {

			public void onProgressChanged(WebView view, int progress) {

				if (progress == 100)
					pbMain.setVisibility(View.GONE);
			}
		});

		web.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// do your handling codes here, which url is the requested url
				// probably you need to open that url rather than redirect:

				view.loadUrl(url);
				return false;

			}
		});


		web.loadUrl("file:///android_asset/TandC.html");
		

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
		// TODO Auto-generated method stub
		super.onClick(v);
	}

}
