package com.phonegap.astroapp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import org.apache.cordova.DroidGap;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.plugin.gcm.GCMIntentService;

public class phonegap {
	private WebView mAppView;
	  private DroidGap mGap;
	  
	  public phonegap(DroidGap gap, WebView view)
	  {
	    mAppView = view;
	    mGap = gap;
	   
	  }

	  public String getVibrationOn(){
	   /* TelephonyManager tm = 
	      (TelephonyManager) mGap.getSystemService(Context.TELEPHONY_SERVICE);
	    String imeiId = tm.getDeviceSoftwareVersion();      
        return imeiId;*/
         
        AudioManager audio = (AudioManager)
        		mGap.getSystemService(Context.AUDIO_SERVICE);
         audio.setRingerMode(audio.RINGER_MODE_VIBRATE);      
         String imeiId ="s";
         return imeiId;
	  }
	  public String getVibrationOff(){
		   /* TelephonyManager tm = 
		      (TelephonyManager) mGap.getSystemService(Context.TELEPHONY_SERVICE);
		    String imeiId = tm.getDeviceSoftwareVersion();      
	        return imeiId;*/
	         
	        AudioManager audio = (AudioManager)
	        		mGap.getSystemService(Context.AUDIO_SERVICE);
	         audio.setRingerMode(audio.RINGER_MODE_NORMAL);      
	         String imeiId ="s";
	         return imeiId;
		  }
	  public String getRingOn(){
		   /* TelephonyManager tm = 
		      (TelephonyManager) mGap.getSystemService(Context.TELEPHONY_SERVICE);
		    String imeiId = tm.getDeviceSoftwareVersion();      
	        return imeiId;*/
	         
	        AudioManager audio = (AudioManager)
	        		mGap.getSystemService(Context.AUDIO_SERVICE);
	         audio.setRingerMode(audio.RINGER_MODE_NORMAL);      
	         String imeiId ="sa";
	         return imeiId;
		  }
	  public String getRingOff(){
		   /* TelephonyManager tm = 
		      (TelephonyManager) mGap.getSystemService(Context.TELEPHONY_SERVICE);
		    String imeiId = tm.getDeviceSoftwareVersion();      
	        return imeiId;*/
	         
	        AudioManager audio = (AudioManager)
	        		mGap.getSystemService(Context.AUDIO_SERVICE);
	         audio.setRingerMode(audio.RINGER_MODE_SILENT);      
	         String imeiId ="sa";
	         return imeiId;
		  }
	 
	  public String setX(String html){
		  ContentValues values = new ContentValues();
           values.put(MediaStore.Video.Media.DATA, html);
           String s="s";
          return "";
	  }
	  public String NotificationBarOff(){
		
		  
          return "";
	  }
	  public String getNotify(){
		  NotificationManager notificationManager = (NotificationManager) mGap.getSystemService(Context.NOTIFICATION_SERVICE);
		 // notificationManager.notify(id, notification);
		  String r;
		  return "";
	  }
	  
	  public String sendToAndroid(String text) {
	      // this is called from JS with passed value
		  //  Toast t = Toast.makeText(mGap.getApplicationContext(), text, 2000);
	      // t.show();
		  Cursor c = mGap.getContentResolver().query(
				    Uri.parse(text),null,null,null,null);
				c.moveToNext();
				String path = c.getString(c.getColumnIndex(MediaStore.MediaColumns.DATA));
				Log.i("path:===",path);
				c.close();
		  // mAppView.loadUrl("javascript:window.androidResponse(text);"); 
		  Bitmap thumb = ThumbnailUtils.createVideoThumbnail(path,
				    MediaStore.Images.Thumbnails.MINI_KIND);
		  
		  
		  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		  thumb.compress(Bitmap.CompressFormat.PNG, 100, bytes);
		  String path1 = Images.Media.insertImage(mGap.getContentResolver(), thumb, "Title", null);
		 
		  
		  Cursor c1 = mGap.getContentResolver().query(
				    Uri.parse(path1),null,null,null,null);
				c1.moveToNext();
				String path2 = c1.getString(c1.getColumnIndex(MediaStore.MediaColumns.DATA));
				Log.i("path:===",path2);
				c1.close();
				
				Bitmap bm = BitmapFactory.decodeFile(path2);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				bm.compress(Bitmap.CompressFormat.PNG, 20, baos); //bm is the bitmap object   
				byte[] b = baos.toByteArray();
				String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
				Log.i("path:===",encodedImage);
					return (encodedImage);
		  
		  
		  
		  //LOG.i("sdf", text);
		 // return "";
		}
	  public static String GCMSenderId = "260194964778";
		private String registerId = "";
		
		protected IntentFilter gcmFilter;
		protected GCMReceiver gcmReceiver;
	    
	    private class GCMReceiver extends BroadcastReceiver {
			
	    	@Override
	    	public void onReceive(Context context, Intent intent) {
	    		
	    		registerId = intent.getStringExtra("registration_id");
	    	}
	    }
	    
	  public String getDevice(){
			try {
	    		gcmFilter = new IntentFilter();
	    		gcmReceiver = new GCMReceiver();
	    		gcmFilter.addAction("com.phonegap.ON_REGISTERED");
	    		
	    		mGap.registerReceiver(gcmReceiver, gcmFilter);
	    		
	    				
	    			GCMRegistrar.checkDevice(mGap);
	    			GCMRegistrar.checkManifest(mGap);
	    			
	    			registerId = GCMRegistrar.getRegistrationId(mGap);
	    			
	    			if(registerId.equals(""))					
	    				GCMRegistrar.register(mGap, GCMSenderId);
	    	
	    			
	    	} 
	    	catch(Exception e) {
	    		e.printStackTrace();
	    		return "";
	    	}
	    	catch(Error r) {
	    		r.printStackTrace();
	    		return "";
	    	}	
	    	return registerId;
	  }
	  public String showmsg(String text) {
		  Toast.makeText(mGap, text, Toast.LENGTH_LONG).show();
		  return "";
		}
	  
	  public static ArrayList<String> PushMsg() {
		  Log.v("sss", "cccsscccccc: " + GCMIntentService.MsgList);
		  return GCMIntentService.MsgList;
	}
	  public static ArrayList<String> PushMsgName() {
		  Log.v("sss", "cccsscccccc: " + GCMIntentService.NameList);
		  return GCMIntentService.NameList;
	}
	  public static ArrayList<String> PushMsgId() {
		  Log.v("sss", "cccsscccccc: " + GCMIntentService.userIdList);
		  return GCMIntentService.userIdList;
	}
	  public static ArrayList<String> PushMsgPic() {
		  Log.v("sss", "cccsscccccc: " + GCMIntentService.PicList);
		  return GCMIntentService.PicList;
	}
	  public void Playvid(String vid) {
		 // vid="http://plugthat.com/plug_that/uploads/uservideos/4124bc0a9335c27f086f24ba207a4912.mp4";
		  Log.v("sss", "cccsscccccc: " + vid);
		  Intent videoIntent = new Intent(mGap, VideoPlay.class);
		  videoIntent.putExtra("vid", vid);
		  mGap.startActivity(videoIntent);
		 // return "";
	}
	  public String ImgDecode(String text) {
		  Cursor c1 = mGap.getContentResolver().query(
				    Uri.parse(text),null,null,null,null);
				c1.moveToNext();
				String path2 = c1.getString(c1.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
				Log.i("path2",path2);
				c1.close();
				
				Bitmap bm = BitmapFactory.decodeFile(path2);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();  
				bm.compress(Bitmap.CompressFormat.PNG, 20, baos); //bm is the bitmap object   
				byte[] b = baos.toByteArray();
				String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
				 encodedImage="data:image/jpeg;base64,"+encodedImage;
				Log.i("path:===",encodedImage);
					return (encodedImage);
	}
}


