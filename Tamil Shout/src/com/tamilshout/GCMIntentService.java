/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tamilshout;

import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.tamilshout.R;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.tamilshout.gsm.CommonUtilities;
import com.tamilshout.gsm.GCM;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	String alertType = null;
	String sound = null;
	String title = null;
	String message = null;
	String newspayload = null;
	String thedate = null;
	String pgLink = null;
	String picFile = null;
	String picFlv = null;
	String pgDesc = null;
	String pgMeta = null;

	public GCMIntentService() {
		super(CommonUtilities.SENDER_ID);
	}

	protected void onRegistered(Context context, String registrationId) {
		Log.e("GCM", "REGISTERD");
		Log.i(TAG, "Device registered: regId = " + registrationId);
		GCM.sendRegistrationIdToServer(context, registrationId);
	}

	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			// ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}

	protected void onMessage(Context context, Intent intent) {

		/*Constant.appContext = this;
		Constant.shf = getSharedPreferences("forexShf", MODE_PRIVATE);*/

		Log.i(TAG, "Received message");
		
	//	System.out.println("----------intent-------------"+intent.getDataString());
		//System.out.println("----------intent2-------------"+intent.toString());
		System.out.println("----------intent2-------------"+intent.getExtras().toString());
		
		
		// Log.i(TAG, "Received message"+intent.getData().toString());

		/*alertType = intent.getStringExtra(CommonUtilities.ALERT_TYPE);
		sound = intent.getStringExtra(CommonUtilities.SOUND);
		title = intent.getStringExtra("pgName");
		thedate = intent.getStringExtra("thedate");
		pgLink = intent.getStringExtra("pgLink");
		picFile = intent.getStringExtra("picFile");
		picFlv = intent.getStringExtra("picFlv");
		pgDesc = intent.getStringExtra("pgDesc");
		pgMeta = intent.getStringExtra("pgMeta");*/

		message = intent.getStringExtra("message");

		//
		// System.out.println("--------title-----------"+title);
		// System.out.println("--------thedate-----------"+thedate);
		// System.out.println("--------pgLink-----------"+pgLink);
		// System.out.println("--------picFile-----------"+picFile);
		// System.out.println("--------picFlv-----------"+picFlv);
		// System.out.println("--------pgDesc-----------"+pgDesc);
		//
		// System.out.println("---------------------------------------------------------------");

		Log.d(TAG, "Received alertType " + alertType);
		Log.d(TAG, "Received sound=" + sound);

		createNotification(context, message, sound, alertType);
	}

	protected void onDeletedMessages(Context context, int total) {

	}

	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	public void createNotification(Context context, String payload,
			String sound, String alertType) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
		Notification notification = new Notification(R.drawable.appicon,
				payload, System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		/*if (sound.equalsIgnoreCase("true")) {
			notification.defaults |= Notification.DEFAULT_SOUND;
			// notification.defaults|=Notification.DEFAULT_VIBRATE;
		} else {
			notification.defaults |= 0;
		}*/

		Intent intent = null;
		
		
		intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		/*	if (alertType.equalsIgnoreCase("NEWS")) {

			if (Constant.shf.getBoolean("isLogin", false)) {

				intent = new Intent(context, News.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("isComesFromPush", true);

			} else {
				intent = new Intent(this, Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("isComesFromPush", true);
				intent.putExtra("isFromNews", true);
			}

		} else {

			if (Constant.shf.getBoolean("isLogin", false)) {

				intent = new Intent(context, VideoAlert_New.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("isComesFromPush", true);
				intent.putExtra("webUrl", alertType);
				intent.putExtra("videoTitle", title);

				intent.putExtra("imageUrl", picFile);
				intent.putExtra("videoDate", thedate);
				intent.putExtra("videDesc", pgDesc);
				intent.putExtra("videoUrl", picFlv);

			} else {

				intent = new Intent(this, Login.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("isComesFromPush", true);
				intent.putExtra("webUrl", alertType);
				intent.putExtra("videoTitle", title);

				intent.putExtra("imageUrl", picFile);
				intent.putExtra("videoDate", thedate);
				intent.putExtra("videDesc", pgDesc);
				intent.putExtra("videoUrl", picFlv);

			}
		}
	*/
		Random rand = new Random();
		int n = rand.nextInt(5000000) + 1;
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.setLatestEventInfo(context, ""
				+ getString(R.string.app_name), payload, pendingIntent);
		notificationManager.notify(n, notification);
	}

}
