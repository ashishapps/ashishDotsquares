package com.phonegap.astroapp;


import org.apache.cordova.DroidGap;

import android.os.Bundle;
import android.view.WindowManager;

public class AstroApp extends DroidGap {
	private phonegap mc;
	@Override
	  public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init();
		mc = new phonegap(this, appView);
        appView.addJavascriptInterface(mc, "phonegap");
		//setIntegerProperty("loadUrlTimeoutValue", 60000);
		// setIntegerProperty("splashscreen", R.drawable.splash);
        super.loadUrl("file:///android_asset/www/index.html");
        // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	
	}
}
