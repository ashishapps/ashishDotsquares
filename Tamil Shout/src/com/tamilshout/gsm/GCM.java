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
package com.tamilshout.gsm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;

/**
 * Main UI for the demo app.
 */
public class GCM {

	public static final String REG_ID = "c2dmReg";
	public static final String REG_SUCCESS_SERVER = "c2dmRegServer";
	public static final String ALLOW_PUSH = "c2dmAllowPush";
	public static final String IS_FIRST = "c2dmIsFirst";

	public static void doRegister(Activity ctx) {

		GCMRegistrar.checkDevice(ctx);
		GCMRegistrar.checkManifest(ctx);

		final String regId = GCMRegistrar.getRegistrationId(ctx);

		Log.d("GCM Registration ID", regId);

		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(ctx, CommonUtilities.SENDER_ID);
		} else {
			// sendRegistrationIdToServer(ctx, regId);
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(ctx)) {
				// Skips registration.

			} else {
				sendRegistrationIdToServer(ctx, regId);
			}
		}
	}

	public static void destroy(Activity ctx) {
		GCMRegistrar.onDestroy(ctx);
	}

	public static final void sendRegistrationIdToServer(final Context ctx,
			final String id) {
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		Editor edit = prefs.edit();
		edit.putString("deviceTokenId", id);
		edit.commit();
		new Thread(new Runnable() {
			public void run() {
				Log.d("C2DM",
						"Sending registration ID to my application server");

				Editor edit = prefs.edit();
				edit.putBoolean(REG_SUCCESS_SERVER, true);
				edit.commit();

				// DAO.setFirstTimeAlertSettings(Secure.getString(ctx.getContentResolver(),Secure.ANDROID_ID),id,ctx);
			}

		}).start();
	}

	public static void showPushNoticeDialog(final Activity ctx) {
		Log.e("GCM", "POPUP");
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(ctx);
		if (prefs.getBoolean(IS_FIRST, true)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
			builder.setMessage("Allow push notifications?")
					.setCancelable(false)
					.setPositiveButton("ALLOW", new OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							Editor edit = prefs.edit();
							edit.putBoolean(ALLOW_PUSH, true);
							edit.commit();
							Log.e("GCM", "ALLOWPOPUP");
							doRegister(ctx);
						}
					});
			builder.setNeutralButton("DON'T ALLOW", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					Log.e("GCM", "NOALLOWPOPUP");
					dialog.cancel();
					Editor edit = prefs.edit();
					edit.putBoolean(ALLOW_PUSH, false);
					edit.commit();
				}
			});

			AlertDialog alert = builder.create();
			alert.show();

			Editor edit = prefs.edit();
			edit.putBoolean(IS_FIRST, false);
			edit.commit();
		} else if (prefs.getBoolean(ALLOW_PUSH, false)) {
			Log.e("GCM", "SECONDALLOWPOPUP");
			String id = prefs.getString(REG_ID, null);
			if (id == null)
				doRegister(ctx);
			else if (!prefs.getBoolean(REG_SUCCESS_SERVER, false))
				sendRegistrationIdToServer(ctx, id);
		}

	}

	public static String convertStreamToString(InputStream in) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder result = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null)
				result.append(line);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}

}