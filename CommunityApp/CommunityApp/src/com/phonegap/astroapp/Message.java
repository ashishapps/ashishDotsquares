package com.phonegap.astroapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.plugin.gcm.PushPlugin;

public class Message {
	private static final String TAG = "GCMIntentService";
	public void onMessage(Context context, Intent intent) {
		Log.d(TAG, "onMessage - context: " + context);
		// Extract the payload from the message
		Bundle extras = intent.getExtras();
		if (extras != null)
		{
			// if we are in the foreground, just surface the payload, else post it to the statusbar
            if (PushPlugin.isInForeground()) {
				extras.putBoolean("foreground", true);
                PushPlugin.sendExtras(extras);
			}
			else {
				extras.putBoolean("foreground", false);
				// Send a notification if there is a message
                /*if (extras.getString("message") != null && extras.getString("message").length() != 0) {
                    createNotification(context, extras);
                }*/
            }
        }
	}
}
