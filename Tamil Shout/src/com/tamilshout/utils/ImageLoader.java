package com.tamilshout.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.Thread.State;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ImageLoader {
	private static File cacheDir = new File(Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/cache");
	private static final long MAX_BM_SIZE = 1024 * 50;
	private final HashMap<String, SoftReference<Bitmap>> Cache = new HashMap<String, SoftReference<Bitmap>>();

	public final class QueueItem {
		public String url;
		public ImageLoadedListener listener;
	}

	private final HashSet<String> urlSet = new HashSet<String>();
	private final ArrayList<QueueItem> Queue = new ArrayList<QueueItem>();
	private final Handler handler = new Handler();
	private Thread thread;
	private QueueRunner runner = new QueueRunner();

	public ImageLoader() {

		thread = new Thread(runner);

		try {
			if (!cacheDir.exists())
				cacheDir.mkdirs();
			File f = new File(cacheDir, ".nomedia");
			if (!f.exists())
				f.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public interface ImageLoadedListener {
		public void imageLoaded(Bitmap imageBitmap);
	}

	public class QueueRunner implements Runnable {
		@Override
		public void run() {

			synchronized (this) {
				while (Queue.size() > 0) {
					final QueueItem item = Queue.remove(0);
					urlSet.remove(item.url.toString());

					if (Cache.get(item.url) != null
							&& Cache.get(item.url).get() != null
							&& !Cache.get(item.url).get().isRecycled()) {
						handler.post(new Runnable() {
							@Override
							public void run() {

								if (item.listener != null) {
									SoftReference<Bitmap> ref = Cache
											.get(item.url.toString());
									item.listener.imageLoaded(ref.get());
								}
							}
						});
					} else {
						Bitmap bmp = readBitmapFromCache(item.url.toString());
						if (bmp == null && Utils.isOnline())
							bmp = readBitmapFromNetwork(item.url);
						if (bmp != null)
							Cache.put(item.url.toString(),
									new SoftReference<Bitmap>(bmp));
						final Bitmap bm = bmp;
						handler.post(new Runnable() {
							@Override
							public void run() {

								if (item.listener != null) {
									item.listener.imageLoaded(bm);
								}
							}
						});
					}

				}
			}
		}
	}

	public Bitmap loadImage(final String uri, final ImageLoadedListener listener)

	{

		if (Cache.containsKey(uri)) {
			SoftReference<Bitmap> ref = Cache.get(uri);
			if (ref != null && ref.get() != null && !ref.get().isRecycled()) {
				return ref.get();
			}
		}

		if (urlSet.add(uri)) {
			QueueItem item = new QueueItem();
			item.url = uri;
			item.listener = listener;
			Queue.add(item);
		}

		if (thread.getState() == State.NEW) {
			thread.start();
		} else if (thread.getState() == State.TERMINATED) {
			thread = new Thread(runner);
			thread.start();
		}
		return null;
	}

	public Bitmap readBitmapFromCache(String file) {

		Bitmap bmp = null;
		try {
			File f = new File(cacheDir, Uri.encode(file));
			if (!f.exists())
				return bmp;

			bmp = BitmapFactory.decodeFile(f.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public void writeBitmapToCache(final Bitmap bm, String url) {

		final File f = new File(cacheDir, Uri.encode(url));
		new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					bm.compress(CompressFormat.PNG, 100,
							new FileOutputStream(f));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public Bitmap readBitmapFromNetwork(final String urlStr) {

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
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte buf[] = new byte[1024];
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];

			int len = 0;
			while ((len = bis.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}

			buf = out.toByteArray();
			Log.e("AVAIL", buf.length + "--" + MAX_BM_SIZE);
			if (buf.length > MAX_BM_SIZE) {
				Options opt = new Options();
				opt.inScaled = true;
				opt.inDither = true;
				opt.inSampleSize = 2;
				opt.inTempStorage = new byte[16000];
				bmp = BitmapFactory.decodeByteArray(buf, 0, buf.length, opt);
			} else
				bmp = BitmapFactory.decodeByteArray(buf, 0, buf.length);

			if (bmp != null)
				writeBitmapToCache(bmp, url.toString());

			out.close();
			if (is != null)
				is.close();
			if (bis != null)
				bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bmp;
	}

	public Bitmap loadSingleImage(final String url, final BaseAdapter adapter) {

		if (Cache.get(url) != null && Cache.get(url).get() != null
				&& !Cache.get(url).get().isRecycled())
			return Cache.get(url).get();
		else {
			new Thread(new Runnable() {
				@Override
				public void run() {

					Bitmap bmp = readBitmapFromCache(url);
					if (bmp == null)
						bmp = readBitmapFromNetwork(url);
					if (bmp != null)
						Cache.put(url, new SoftReference<Bitmap>(bmp));
					handler.post(new Runnable() {

						@Override
						public void run() {
							adapter.notifyDataSetChanged();
						}
					});
				}
			}).start();
			return null;
		}
	}

	public void loadSingleImageBm(final String path, final ImageView img) {

		if (Cache.get(path) != null && Cache.get(path).get() != null
				&& !Cache.get(path).get().isRecycled())

		{
			Bitmap bmp = readBitmapFromCache(path);
			img.setImageBitmap(bmp);
		} else {
			new Thread(new Runnable() {
				@Override
				public void run() {

					Bitmap bmp = null;
					if (Utils.isOnline())
						bmp = readBitmapFromNetwork(path);
					if (bmp != null)
						Cache.put(path, new SoftReference<Bitmap>(bmp));

					final Bitmap bm = bmp;
					handler.post(new Runnable() {

						@Override
						public void run() {
							if (bm != null)
								img.setImageBitmap(bm);

						}
					});
				}
			}).start();
		}
	}

	public File getImageFile(String file) {

		File f = new File(cacheDir, Uri.encode(file));
		return f;
	}

}
