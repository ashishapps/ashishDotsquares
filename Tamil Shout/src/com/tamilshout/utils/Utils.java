package com.tamilshout.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.Toast;

import com.tamilshout.HomeActivity;
import com.tamilshout.R;
import com.tamilshout.web.GetDataFromWebService;

public class Utils {

	public static double MULTIPLIER = 0.033814;
	public static double POUND_MULTIPLIER = 2.20462;
	public static String POUNDS = "Pounds";
	public static String KGS = "Kgs";
	public Context _context;
	static boolean flag = false;

	static private JSONObject jsonObject = new JSONObject();
	static private String response = null;

	private static File cacheDir = new File(""
			+ Environment.getDataDirectory().getAbsolutePath() + "/image/");

	public static void callDialog(final Context ctx, final String tel) {

		final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage("Call " + tel + " ?")
				.setCancelable(false)
				.setPositiveButton("Call",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

								Intent callIntent = new Intent(
										Intent.ACTION_DIAL);
								callIntent.setData(Uri.parse("tel:" + tel));
								callIntent
										.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								ctx.startActivity(callIntent);

							}
						});
		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
		final AlertDialog alert = builder.create();
		alert.show();

	}

	public Utils(Context context) {
		this._context = context;
	}

	public static boolean isPasswordMatch(EditText password,
			EditText confirmPassword) {
		if (!password.getText().toString()
				.equals(confirmPassword.getText().toString())) {
			return false;
		}
		return true;
	}

	private boolean IsSupportedFile(String filePath) {
		String ext = filePath.substring((filePath.lastIndexOf(".") + 1),
				filePath.length());

		if (Constant.FILE_EXTN.contains(ext.toLowerCase(Locale.getDefault())))
			return true;
		else
			return false;

	}

	public static boolean isPasswordMatch(String password,
			String confirmPassword) {
		if (!password.equals(confirmPassword)) {
			return false;
		}
		return true;
	}

	public static boolean isEmailMatch(EditText email, EditText confirmEmail) {
		if (!email.getText().toString()
				.equals(confirmEmail.getText().toString())) {
			return false;
		}
		return true;
	}

	public static boolean isEmailMatch(String email, String confirmEmail) {
		if (!email.toString().equals(confirmEmail.toString())) {
			return false;
		}
		return true;
	}

	public static void Log(String msg) {
		Log.d("Drink", msg);
	}

	public static void showMap(Context ctx) {

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
				+ ","));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ctx.startActivity(intent);

	}

	public static String readFileFromAsset(Context context) {
		String res = null;
		InputStream inputStream;

		try {
			inputStream = context.getAssets().open("json.txt");
			int size = inputStream.available();
			byte[] buffer = new byte[size];
			inputStream.read(buffer);
			inputStream.close();
			res = new String(buffer);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return res;
		/*
		 * try { json = new JSONObject(res);
		 * 
		 * } catch (Exception exception) { exception.printStackTrace(); }
		 */
	}

	public static AlertDialog showDialog(final Context ctx, final String msg,
			final String ttl, final DialogInterface.OnClickListener listener,
			boolean cancellable) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg).setCancelable(true)
				.setPositiveButton(android.R.string.ok, listener);

		builder.setTitle("" + ttl);
		builder.setIcon(ctx.getResources().getDrawable(R.drawable.appicon));
		builder.setCancelable(cancellable);

		// final AlertDialog alert = builder.create();
		// alert.show();

		return builder.create();
	}

	public static void showDialog(final Context ctx, final String msg,
			final String ttl) {
		showDialog(ctx, msg, ttl, new DialogInterface.OnClickListener() {
			public void onClick(final DialogInterface dialog, final int id) {
				dialog.dismiss();
			}
		}, true).show();

	}

	public static void showDialog(final Context ctx, final String msg) {
		showDialog(ctx, msg, "Tamil Shout",
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.dismiss();
					}
				}, true).show();

	}

	public static Bitmap getOrientationFixedImage(String f, Bitmap bmp) {

		try {
			ExifInterface exif = new ExifInterface(f);
			int orientation = exif.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);

			int angle = 0;

			if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
				angle = 90;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
				angle = 180;
			} else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
				angle = 270;
			}

			Matrix mat = new Matrix();
			mat.postRotate(angle);

			Bitmap correctBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
					bmp.getHeight(), mat, true);
			return correctBmp;
		} catch (OutOfMemoryError oom) {
			Log.w("TAG", "-- OOM Error in setting image");
		} catch (Exception e) {
			Log.w("TAG", "-- Error in setting image");
		}
		return bmp;
	}

	public static void showDisclaimerDialog(final Context ctx,
			DialogInterface.OnClickListener lis) {

		String msg = "The BAC level is only an estimated BAC and we take no responsibility legally";

		showDialog(ctx, "Disclaimer", msg, "Agree", "Disagree", lis, null);
	}

	public static void showAutoHideDialog(final Context ctx, final String msg) {
		final AlertDialog ad = showDialog(ctx, msg, "Drink Tracker",
				new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.dismiss();
					}
				}, true);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (ad != null && ad.isShowing()) {
					ad.dismiss();
				}
			}
		}, 10000);

		ad.show();
	}

	public static void showListDialog(final Context ctx, final String ttl,
			ArrayList<String> itemList, DialogInterface.OnClickListener listener) {

		AlertDialog.Builder adb = new AlertDialog.Builder(ctx);
		adb.setTitle(ttl);
		CharSequence[] items = null;
		try {
			items = new CharSequence[itemList.size()];
			for (int i = 0; i < itemList.size(); i++) {
				items[i] = itemList.get(i);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		adb.setItems(items, listener);
		adb.create().show();

	}

	public static void showDialog(Context ctx, String title, String msg,
			String btn1, String btn2,
			DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setIcon(ctx.getResources().getDrawable(R.drawable.appicon));
		builder.setMessage(msg).setCancelable(true)
				.setPositiveButton(btn1, listener1);
		if (btn2 != null)
			builder.setNegativeButton(btn2, listener2);

		builder.create().show();

	}

	public static final boolean isOnline() {

		try {
			ConnectivityManager conMgr = (ConnectivityManager) Constant.appContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			if (conMgr.getActiveNetworkInfo() != null

			&& conMgr.getActiveNetworkInfo().isAvailable()

			&& conMgr.getActiveNetworkInfo().isConnected())
				return true;
			return false;
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return false;

	}

	public static void showNoNetworkDialog(Context ctx) {

		showDialog(
				ctx,
				"Internet is not available. Please check your network connection.",
				"No Network Connection");
	}

	private static void writeBitmapToCache(final Bitmap bm, String name) {
		final File f = new File(cacheDir, name);// +".png"
		new Thread(new Runnable() {
			public void run() {
				try {
					bm.compress(CompressFormat.PNG, 60, new FileOutputStream(f));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static Bitmap readBitmapFromNetwork(final String urlStr) {
		if (!URLUtil.isValidUrl(urlStr))
			return null;
		InputStream is = null;
		BufferedInputStream bis = null;
		Bitmap bmp = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.connect();
			is = conn.getInputStream();
			bis = new BufferedInputStream(is);
			Options opt = new Options();
			opt.outHeight = 100;
			opt.outWidth = 100;

			bmp = BitmapFactory.decodeStream(bis, null, opt);

			if (bmp != null)
				writeBitmapToCache(bmp, urlStr);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (is != null)
					is.close();
				if (bis != null)
					bis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return bmp;
	}

	public static boolean isNetworkAvailableNew(Context ctx) {
		ConnectivityManager connectivity = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {

		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static Bitmap getCompresedBitmap(String path, int h, int w) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 2;

		if (height > h || width > w) {
			final int heightRatio = Math.round((float) height / (float) h);
			final int widthRatio = Math.round((float) width / (float) w);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		Bitmap bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}

	public static boolean isValidEmail(String email) {

		String emailExp = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,10}$";
		Pattern pattern = Pattern.compile(emailExp, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static void createNoMediaFile(File dir) {

		try {
			File f = new File(dir, ".nomedia");
			if (!f.exists())
				f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final Bitmap getCompressedBm(byte b[], int w, int h) {

		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(b, 0, b.length, options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > h || width > w) {
				final int heightRatio = Math.round((float) height / (float) h);
				final int widthRatio = Math.round((float) width / (float) w);

				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;

			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(b, 0, b.length, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static final Bitmap getCompressedBm(File file, int w, int h) {

		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file.getAbsolutePath(), options);
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > h || width > w) {
				final int heightRatio = Math.round((float) height / (float) h);
				final int widthRatio = Math.round((float) width / (float) w);

				inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;
			}
			options.inSampleSize = inSampleSize;
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Typeface getTypeFace(Context ctx) {
		return Typeface.createFromAsset(ctx.getAssets(), "AGENCYB.TTF");
	}

	public static final void saveCompressedBm(String source, String dest) {

		try {
			Options opt = new Options();
			opt.inSampleSize = 4;
			Bitmap bm = BitmapFactory.decodeFile(source, opt);
			bm.compress(CompressFormat.PNG, 100, new FileOutputStream(dest));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static final void hideKeyboard(Activity ctx, View v) {

		try {
			InputMethodManager imm = (InputMethodManager) ctx
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String JSONTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}

	public static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public static String convertMinutesToHours(long minutes) {

		return pad((int) (minutes / 60)) + ":"
				+ Utils.pad((int) (minutes % 60)) + "";
	}

	public static String getRemainingTime(String goHomeDate) {

		long goHome = getMillisFromDate(goHomeDate);
		long current = System.currentTimeMillis();

		long remTime = goHome - current;

		if (remTime < 0)
			return "00:00";
		else {
			long minutes = remTime / 60000L;
			String time = pad((int) (minutes / 60)) + ":"
					+ Utils.pad((int) (minutes % 60)) + "";
			return time;
		}

	}

	public static long getMillisFromDate(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a",
				Locale.getDefault());

		Calendar cal = Calendar.getInstance();
		;

		try {
			cal.setTime(sdf.parse(date + ""));
		} catch (Exception e) {
			cal = Calendar.getInstance();
		}

		return cal.getTimeInMillis();
	}

	public static String getRealPathFromURI(Context con, Uri contentURI) {
		try {
			Cursor cursor = con.getContentResolver().query(contentURI, null,
					null, null, null);
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaColumns.DATA);
			return cursor.getString(idx);
		} catch (Exception e) {
			Log.d("error", e.toString());
		}
		return "";
	}

	/**
	 * Method use to get file path from uri for kitkat and upper version.
	 * 
	 * @param contentUri
	 *            url of file.
	 * @param act
	 *            activity instance of activity.
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getRealPathForKitkatFromURI(Uri contentUri,
			Activity act) {
		String wholeID = DocumentsContract.getDocumentId(contentUri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Images.Media.DATA };

		// where id is equal to
		String sel = MediaStore.Images.Media._ID + "=?";

		Cursor cursor = act.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
				new String[] { id }, null);

		int columnIndex = cursor.getColumnIndex(column[0]);
		String path = "";
		if (cursor.moveToFirst()) {

			Log.d("cursor gallery", cursor.getString(columnIndex) + "");
			path = cursor.getString(columnIndex);
		}
		return path;
	}

	/**
	 * Method use to get file path from uri for kitkat and upper version.
	 * 
	 * @param contentUri
	 *            url of file.
	 * @param act
	 *            activity instance of activity.
	 * @return
	 */
	@SuppressLint("NewApi")
	public static String getRealPathForKitkatFromURI(Uri contentUri,
			FragmentActivity act) {
		String wholeID = DocumentsContract.getDocumentId(contentUri);

		// Split at colon, use second item in the array
		String id = wholeID.split(":")[1];

		String[] column = { MediaStore.Images.Media.DATA };

		// where id is equal to
		String sel = MediaStore.Images.Media._ID + "=?";

		Cursor cursor = act.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
				new String[] { id }, null);

		int columnIndex = cursor.getColumnIndex(column[0]);
		String path = "";
		if (cursor.moveToFirst()) {

			Log.d("cursor gallery", cursor.getString(columnIndex) + "");
			path = cursor.getString(columnIndex);
		}
		return path;
	}
	 /**
	   * @param uri The Uri to check.
	   * @return Whether the Uri authority is ExternalStorageProvider.
	   */
	  public static boolean isExternalStorageDocument(Uri uri) {
	      return "com.android.externalstorage.documents".equals(uri.getAuthority());
	  }

	  /**
	   * @param uri The Uri to check.
	   * @return Whether the Uri authority is DownloadsProvider.
	   */
	  public static boolean isDownloadsDocument(Uri uri) {
	      return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	  }

	  /**
	   * @param uri The Uri to check.
	   * @return Whether the Uri authority is MediaProvider.
	   */
	  public static boolean isMediaDocument(Uri uri) {
	      return "com.android.providers.media.documents".equals(uri.getAuthority());
	  }

	/**
	   * Get a file path from a Uri. This will get the the path for Storage Access
	   * Framework Documents, as well as the _data field for the MediaStore and
	   * other file-based ContentProviders.
	   *
	   * @param context The context.
	   * @param uri The Uri to query.
	   * @author paulburke
	   */
	  @SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {

	      final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	      // DocumentProvider
	      if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
	          // ExternalStorageProvider
	          if (isExternalStorageDocument(uri)) {
	              final String docId = DocumentsContract.getDocumentId(uri);
	              final String[] split = docId.split(":");
	              final String type = split[0];

	              if ("primary".equalsIgnoreCase(type)) {
	                  return Environment.getExternalStorageDirectory() + "/" + split[1];
	              }

	              // TODO handle non-primary volumes
	          }
	          // DownloadsProvider
	          else if (isDownloadsDocument(uri)) {

	              final String id = DocumentsContract.getDocumentId(uri);
	              final Uri contentUri = ContentUris.withAppendedId(
	                      Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

	              return getDataColumn(context, contentUri, null, null);
	          }
	          // MediaProvider
	          else if (isMediaDocument(uri)) {
	              final String docId = DocumentsContract.getDocumentId(uri);
	              final String[] split = docId.split(":");
	              final String type = split[0];

	              Uri contentUri = null;
	              if ("image".equals(type)) {
	                  contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	              } else if ("video".equals(type)) {
	                  contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	              } else if ("audio".equals(type)) {
	                  contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	              }

	              final String selection = "_id=?";
	              final String[] selectionArgs = new String[] {
	                      split[1]
	              };

	              return getDataColumn(context, contentUri, selection, selectionArgs);
	          }
	      }
	      // MediaStore (and general)
	      else if ("content".equalsIgnoreCase(uri.getScheme())) {
	          return getDataColumn(context, uri, null, null);
	      }
	      // File
	      else if ("file".equalsIgnoreCase(uri.getScheme())) {
	          return uri.getPath();
	      }

	      return null;
	  }
	  /**
	   * Get the value of the data column for this Uri. This is useful for
	   * MediaStore Uris, and other file-based ContentProviders.
	   *
	   * @param context The context.
	   * @param uri The Uri to query.
	   * @param selection (Optional) Filter used in the query.
	   * @param selectionArgs (Optional) Selection arguments used in the query.
	   * @return The value of the _data column, which is typically a file path.
	   */
	  public static String getDataColumn(Context context, Uri uri, String selection,
	          String[] selectionArgs) {

	      Cursor cursor = null;
	      final String column = "_data";
	      final String[] projection = {
	              column
	      };

	      try {
	          cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                  null);
	          if (cursor != null && cursor.moveToFirst()) {
	              final int column_index = cursor.getColumnIndexOrThrow(column);
	              return cursor.getString(column_index);
	          }
	      } finally {
	          if (cursor != null)
	              cursor.close();
	      }
	      return null;
	  }
	
	public static Spannable spanText(String text, int start, int end, int color) {

		Spannable wordtoSpan = new SpannableString(text);
		wordtoSpan.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		return wordtoSpan;
	}

	public static String getFormattedDate(long millis) {
		return (String) DateFormat.format("dd-MMM-yyyy", millis);
	}

	public static ArrayList<String> getDrinkAwareMsgList() {

		ArrayList<String> drinkAwareList = new ArrayList<String>();
		drinkAwareList.add("Drunkenness is temporary suicide");
		drinkAwareList
				.add("Alcoholism isn't a spectator sport. Eventually the whole family gets to play.");
		drinkAwareList
				.add("First you take a drink, then the drink takes a drink, then the drink takes you.");
		drinkAwareList
				.add("Alcohol is the anesthesia by which we endure the operation of life.");
		drinkAwareList
				.add("Herb is the healing of a nation, alcohol is the destruction.");
		drinkAwareList
				.add("The intermediate stage between socialism and capitalism is alcoholism");
		drinkAwareList
				.add("Alcohol can ruin your life and the lives of others, so why even take one drink?");
		drinkAwareList.add("Wine hath drowned more men than the sea.");
		drinkAwareList
				.add("Alcohol doesn't console, it doesn't fill up anyone's psychological gaps, all it replaces is the lack of God.");
		drinkAwareList
				.add("The driver is safer when the roads are dry; the roads are safer when the driver is dry.");

		return drinkAwareList;
	}

	public static double calculateBAC(double weight, double volume,
			double alcohol, double consumeMillis) {

		Log.d("weight", weight + " Kg");

		double consumeHours = consumeMillis / 3600000D;

		if (weight == 0) {
			return 0;
		} else {
			double PA = (volume * MULTIPLIER * alcohol * 0.075) / weight;
			double PB = consumeHours * 0.015;

			return (PA - PB);
		}
	}

	public static String padBAC(double c) {
		if (c == 0)
			return "0.0000";
		else {
			return c + "";
		}
	}

	public static void createNotification(Context context, String payload) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Resources res = context.getResources();

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setSmallIcon(R.drawable.appicon)
				.setLargeIcon(
						BitmapFactory.decodeResource(res, R.drawable.appicon))
				.setTicker(payload).setDefaults(Notification.DEFAULT_SOUND)
				.setWhen(System.currentTimeMillis()).setAutoCancel(true)
				.setContentTitle("Alco App").setContentText(payload);

		notificationManager.notify(0, builder.build());
	}

	public static final void makeCall(final Activity act, final String number) {

		if (number != null && number.toString().trim().length() <= 0) {
			Toast.makeText(act, "Phone number is not available!",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Utils.showDialog(act, "Call " + number.replace(" ", ""), "Ok",
				"Cancel", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent call = new Intent(Intent.ACTION_CALL);
						call.setData(Uri.parse("tel:" + number.trim()));
						act.startActivity(call);
					}
				}).show();
	}

	public static final void sendMail(final Activity act, final String mailId) {

		if (mailId != null && mailId.toString().trim().length() <= 0) {
			Toast.makeText(act, "Email address not available!",
					Toast.LENGTH_SHORT).show();
			return;
		} else if (!isValidEmail(mailId)) {
			Toast.makeText(act, "Invalid email address found",
					Toast.LENGTH_SHORT).show();
			return;
		}

		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

		/* Fill it with Data */
		emailIntent.setType("plain/text");
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
				new String[] { "" + mailId });
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

		/* Send it off to the Activity-Chooser */
		act.startActivity(Intent.createChooser(emailIntent, "Send mail..."));

	}

	public static final void openWebsite(final Activity act, String url) {

		if (url != null && url.toString().trim().length() <= 0) {
			Toast.makeText(act, "Website link not available!",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (!url.startsWith("http") && !url.startsWith("https")) {
			url = "https://" + url;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		act.startActivity(i);

	}

	@SuppressLint("NewApi")
	public static final void sendMessage(final Activity act, String mobileNo) {

		if (mobileNo != null && mobileNo.toString().trim().length() <= 0) {
			Toast.makeText(act, "Phone number is not available!",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least
																	// KitKat
		{
			String defaultSmsPackageName = Telephony.Sms
					.getDefaultSmsPackage(act); // Need to change the build to
												// API 19

			Intent intent = new Intent(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse("smsto:" + Uri.encode(mobileNo)));
			act.startActivity(intent);

		} else // For early versions, do what worked for you before.
		{
			Intent sendIntent = new Intent(Intent.ACTION_VIEW);
			sendIntent.setData(Uri.parse("sms:"));
			sendIntent.putExtra("sms_body", "");
			sendIntent.putExtra("address", mobileNo);
			act.startActivity(sendIntent);
		}

		/*
		 * Intent sendIntent = new Intent(Intent.ACTION_VIEW);
		 * sendIntent.putExtra("sms_body", "default content");
		 * sendIntent.putExtra("address", mobileNo);
		 * sendIntent.setType("vnd.android-dir/mms-sms");
		 * act.startActivity(sendIntent);
		 */

	}

	public static AlertDialog showDialog(Context ctx, String msg, String btn1,
			String btn2, DialogInterface.OnClickListener listener) {

		return showDialog(ctx, msg, btn1, btn2, listener,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
					}
				});

	}

	public static AlertDialog showDialog(Context ctx, String msg, String btn1,
			String btn2, DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton(btn1, listener1);
		if (btn2 != null && listener2 != null)
			builder.setNegativeButton(btn2, listener2);

		AlertDialog alert = builder.create();
		alert.show();
		return alert;

	}

	/**
	 * 
	 * This genric method use to put object into preference<br>
	 * How to use<br>
	 * Bean bean = new Bean();<br>
	 * putObjectIntoPref(context,bean,key)
	 * 
	 * @param context
	 *            Context of an application
	 * @param e
	 *            your genric object
	 * @param key
	 *            String key which is associate with object
	 */
	public static <E> void putObjectIntoPref(Context context, E e, String key) {
		Editor editor = context.getSharedPreferences("appPref",
				Context.MODE_PRIVATE).edit();
		try {
			editor.putString(key, ObjectSerializer.serialize(e));
		} catch (IOException exc) {
			exc.printStackTrace();
		}

		editor.commit();

	}

	/**
	 * This method is use to get your object from preference.<br>
	 * How to use<br>
	 * Bean bean = getObjectFromPref(context,key);
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E> E getObjectFromPref(Context context, String key) {
		try {
			SharedPreferences pref = context.getSharedPreferences("appPref",
					Context.MODE_PRIVATE);
			return (E) ObjectSerializer.deserialize(pref.getString(key, null));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void showDialog(Context ctx, String title, String msg,
			String btn1, String btn2,
			DialogInterface.OnClickListener listener1,
			DialogInterface.OnClickListener listener2, boolean cancellable) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title);
		builder.setIcon(ctx.getResources().getDrawable(R.drawable.appicon));
		builder.setMessage(msg).setCancelable(true)
				.setPositiveButton(btn1, listener1);
		if (btn2 != null)
			builder.setNegativeButton(btn2, listener2);

		builder.setCancelable(cancellable);
		builder.create().show();

	}

	// Get Two Decimal value with Round value
	public static double roundMyData(double Rval, int numberOfDigitsAfterDecimal) {
		double p = (float) Math.pow(10, numberOfDigitsAfterDecimal);
		Rval = Rval * p;
		double tmp = Math.floor(Rval);

		return (double) tmp / p;
	}

	/**
	 * This method converts dp unit to equivalent pixels, depending on device
	 * density.
	 * 
	 * @param dp
	 *            A value in dp (density independent pixels) unit. Which we need
	 *            to convert into pixels
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent px equivalent to dp depending on
	 *         device density
	 */
	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return px;
	}

	/**
	 * This method converts device specific pixels to density independent
	 * pixels.
	 * 
	 * @param px
	 *            A value in px (pixels) unit. Which we need to convert into db
	 * @param context
	 *            Context to get resources and device specific display metrics
	 * @return A float value to represent dp equivalent to px value
	 */
	public static float convertPixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}

	public static boolean checkImageIsAvailable(final String fileUrl) {

		try {

			URL url = new URL(fileUrl);
			GetDataFromWebService.executeReq(url);

			return true;

		} catch (Exception e) {
			return false;

		}

	}

	public static String getCountry(Activity activity, double lat, double lng) {

		String countryName = activity.getResources().getConfiguration().locale
				.getDisplayCountry();

		Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			Address obj = addresses.get(0);

			countryName = obj.getCountryCode();

			return countryName;

		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static File getFileFromBitmap(Bitmap bmp, String name) {

		OutputStream outStream = null;

		File file = new File(Constant.CACHE_DIR, "" + name);
		if (file.exists()) {
			file.delete();
			file = new File(Constant.CACHE_DIR, "" + name);

		}

		try {
			// make a new bitmap from your file
			Bitmap bitmap = bmp;

			outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.e("file", "" + file);
		return file;

	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	static public boolean isUserExistsOnServer(final Context context) {

		flag = true;

		if (Utils.isNetworkAvailableNew(context)) {

			try {

				SharedPreferences preferences = PreferenceManager
						.getDefaultSharedPreferences(context);

				jsonObject.put("email", AppPrefrence.getProfile(context)
						.getEmail());
				jsonObject.put("password", AppPrefrence.getProfile(context)
						.getPassword());
				jsonObject.put("devicetoken",
						preferences.getString("deviceTokenId", ""));
				jsonObject.put("devicetype", "android");

				new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								context.getString(R.string.service_url)
										+ "UserLogin", jsonObject.toString());

						if (response != null && response.length() > 0
								&& response.startsWith("{")) {

							try {
								jsonObject = new JSONObject(response);
								if (jsonObject.getString("responsecode")
										.equals("101")) {
									Utils.showDialog(context, jsonObject
											.getString("ResponseMessage"));

								} else if (jsonObject.getString("responsecode")
										.equals("100")) {

									AppPrefrence.setLogin(context, false);

									flag = false;

								} else if (jsonObject.getString("responsecode")
										.equals("200")) {

								} else {

									Utils.showDialog(context, context
											.getString(R.string.server_error));
								}

							} catch (JSONException e) {

								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}
					}

				}).start();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(context,
					context.getString(R.string.no_network_available));

		return flag;
	}

}