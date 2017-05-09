package com.example.util;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.example.extra.MyLog;

public class SyncImageLoader {
	private Context mContext;
	private Object lock = new Object();
	private boolean mAllowLoad = true;
	private boolean firstLoad = true;
	private int mStartLoadLimit = 0;
	private int mStopLoadLimit = 0;
	private final Handler handler = new Handler();
	private HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();

	public SyncImageLoader(Context context) {
		this.mContext = context;
	}

	public interface OnImageLoadListener {
		public void onImageLoad(Integer t, Bitmap bitmap);

		public void onError(Integer t);
	}

	public void setLoadLimit(int startLoadLimit, int stopLoadLimit) {
		if (startLoadLimit > stopLoadLimit) {
			return;
		}
		mStartLoadLimit = startLoadLimit;
		mStopLoadLimit = stopLoadLimit;
	}

	public void restore() {
		mAllowLoad = true;
		firstLoad = true;
	}

	public void lock() {
		mAllowLoad = false;
		firstLoad = false;
	}

	public void unlock() {
		mAllowLoad = true;
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public void loadImage(Integer t, String imageUrl,
			OnImageLoadListener listener) {// throws Exception
		final OnImageLoadListener mListener = listener;
		final String mImageUrl = imageUrl;
		final Integer mt = t;

		new Thread(new Runnable() {

			@Override
			public void run() {
				if (!mAllowLoad) {
					synchronized (lock) {
						try {
							lock.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

				if (mAllowLoad && firstLoad) {
					loadImage(mImageUrl, mt, mListener);
				}

				if (mAllowLoad && mt <= mStopLoadLimit && mt >= mStartLoadLimit) {
					loadImage(mImageUrl, mt, mListener);
				}
			}

		}).start();
	}

	private void loadImage(final String mImageUrl, final Integer mt,
			final OnImageLoadListener mListener) {

		if (imageCache.containsKey(mImageUrl)) {
			SoftReference<Bitmap> softReference = imageCache.get(mImageUrl);
			final Bitmap d = softReference.get();
			if (d != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if (mAllowLoad) {
							mListener.onImageLoad(mt, d);
						}
					}
				});
				return;
			} else {
				// imageCache.remove(imageCache);
				imageCache.remove(mImageUrl);

			}
		}
		final Bitmap dBitmap = BitmapUtil.getBitmapFromFile(
				BitmapUtil.createFile(mImageUrl, mContext), 80, 80);
		// BitmapFactory.decodeFile(createFile(mImageUrl)
		// .getAbsolutePath());
		if (dBitmap != null) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (mAllowLoad) {
						mListener.onImageLoad(mt, dBitmap);
					}
				}
			});
			return;
		}
		try {
			final Bitmap d = loadImageFromUrl(mImageUrl);
			if (d != null) {
				imageCache.put(mImageUrl, new SoftReference<Bitmap>(d));
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (mAllowLoad) {
						mListener.onImageLoad(mt, d);
					}
				}
			});
		} catch (Exception e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mListener.onError(mt);
				}
			});
			e.printStackTrace();
		}
	}

	private Bitmap loadImageFromUrl(String url) throws Exception {
		MyLog.println("sy_loadImageFromUrl=" + url);
		Bitmap bitmap = null;
		try {
//			HttpEntity entity = HttpConnectUtil.getEntity(url, null,
//					HttpConnectUtil.METHOD_GET);
//			byte[] data = EntityUtils.toByteArray(entity);
//
//			bitmap = BitmapUtil.getBitmap(data, 100, 100);
//			BitmapUtil.save(bitmap, BitmapUtil.createFile(url, mContext));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
}
