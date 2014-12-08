package com.tamilshout.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class GetDataFromWebService {

	public static String readJsonFeed(String Url, String data) {

		String result = "";
		try {
			Log.d("http request url", Url);
			DefaultHttpClient client = new DefaultHttpClient();

			HttpPost request = new HttpPost(Url);
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept", "application/json");
			try {
				StringEntity se = new StringEntity(data);
				// se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
				// "application/json"));

				request.setEntity(se);
			} catch (UnsupportedEncodingException e2) {
				e2.printStackTrace();
			}

			HttpResponse response = null;
			try {

				Log.d("http request", data.toString());
				response = client.execute(request);

			} catch (Exception e1) {
				e1.printStackTrace();
				Log.d("http resp error", e1.toString());
			}

			try {
				result = EntityUtils.toString(response.getEntity());
				Log.d("http resp ", result);
				result = JSONTokener(result);

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String JSONTokener(String in) {
		// consume an optional byte order mark (BOM) if it exists
		if (in != null && in.startsWith("\ufeff")) {
			in = in.substring(1);
		}
		return in;
	}

	public static void executeReq(URL urlObject) throws IOException {
		HttpURLConnection conn = null;
		conn = (HttpURLConnection) urlObject.openConnection();
		conn.setReadTimeout(30000);// milliseconds
		conn.setConnectTimeout(3500);// milliseconds
		conn.setRequestMethod("GET");
		conn.setDoInput(true);

		// Start connect
		conn.connect();
		InputStream response = conn.getInputStream();
		System.out.println("----------------response--------------------"
				+ response);
		Log.d("File Available Response:", response.toString());
		if (conn != null) {
			conn.disconnect();
		}
	}

	static public String uploadImagesOnServer(ArrayList<String> imagePathList,
			String url) {

		String res = "";

		Log.d("http request", url);
		System.out.println("---url---" + url);

		try {
			HttpParams params = new BasicHttpParams();
			params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
					HttpVersion.HTTP_1_1);
			DefaultHttpClient mHttpClient = new DefaultHttpClient(params);

			HttpPost httppost = new HttpPost(url);

			int timeoutConnection = 90000;
			HttpConnectionParams
					.setConnectionTimeout(params, timeoutConnection);
			// Set the default socket timeout (SO_TIMEOUT)
			// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 90000;
			HttpConnectionParams.setSoTimeout(params, timeoutSocket);

			MultipartEntity multipartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);

			for (int i = 0; i < imagePathList.size(); i++) {

				System.out.println("--------path-inner----"
						+ imagePathList.get(i));
				String pathImage = imagePathList.get(i);

				try {
					File f = new File(pathImage);
					/*FileBody filebody = new FileBody(f);
					multipartEntity.addPart("image", filebody);*/
					
					
			        ContentBody cbFile = new FileBody(f, "image/*");         

			        //Add the data to the multipart entity
			        multipartEntity.addPart("image", cbFile);
					
					
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			System.out.println("===========path List======" + imagePathList);
			/*
			 * Log.d("key", key); Log.d("val", val);
			 * 
			 * multipartEntity.addPart(key, new StringBody("" + val));
			 */
			// Log.d("response","path "+pathImage.largeImage+"  "+res);

			System.out.println("---------multipart---------"
					+ multipartEntity.getContentLength());

			httppost.setEntity(multipartEntity);

			String responseJSonObj = null;

			HttpResponse mHttpResponse;
			try {

				Log.d("bef Res-->", "Before Request");
				mHttpResponse = mHttpClient.execute(httppost);
				// System.out.println("HttpClientTest  ---> get response");
				Log.d("Response-->", "" + mHttpResponse.toString());
				// responseJSonObj
				// =EntityUtils.toString(mHttpResponse.getEntity());

				Log.d("HttpClientTest Response", mHttpResponse.getStatusLine()
						.getStatusCode() + "");
				if (mHttpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

					responseJSonObj = EntityUtils.toString(mHttpResponse
							.getEntity());

					Log.d("HttpClientTest Response", responseJSonObj.toString());

					res = responseJSonObj.toString();
					mHttpClient.getConnectionManager().shutdown();

				} else {
					httppost.abort();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {

		}
		return res;
	}

}
