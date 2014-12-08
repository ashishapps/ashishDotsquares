package com.tamilshout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.tamilshout.R;
import com.tamilshout.gsm.GCM;

public class SplashScreen extends Activity {

	private boolean isRunning;
	private boolean fromPush;
	public static String versionMsg;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		isRunning = true;
		
		

		startSplash();
		
		new Thread(new Runnable() {

			@Override
			public void run() {

				GCM.doRegister(SplashScreen.this);

			}
		}).start();

	}

	private void startSplash() {

		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					long start = System.currentTimeMillis();

					long sleep = 3000 - (System.currentTimeMillis() - start);
					if (sleep > 100)
						Thread.sleep(sleep);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {

							doFinish();
						}
					});
				}
			}
		}).start();
	}

	private synchronized void doFinish() {

		if (isRunning) {
			isRunning = false;
			Intent i = new Intent(SplashScreen.this, HomeActivity.class);
			startActivity(i);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isRunning = false;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}