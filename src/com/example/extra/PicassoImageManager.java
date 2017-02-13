package com.example.extra;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import android.content.Context;
import android.os.Environment;
import android.widget.ImageView;

import com.example.stickyheaderlsitview.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class PicassoImageManager {
	private Context mContext;
	private Picasso picasso;
	private static PicassoImageManager picassoimagemanager;
	private File TempFile;

	public PicassoImageManager(Context context) {
		this.mContext = context;
		if (hasSdcard() && TempFile == null) {
			TempFile = new File(Environment.getExternalStorageDirectory(),
					"kuang-chi" + "/" + "picasso-cache");
		}
		OkHttpClient client = new OkHttpClient.Builder()
				.cache(new Cache(TempFile, 1024 * 1024 * 100))
				.addInterceptor(new PicassoCacheInterceptor())
				.addNetworkInterceptor(new PicassoCacheInterceptor()).build();
		Picasso.Builder picassoBuilder = new Picasso.Builder(mContext);
		picassoBuilder.downloader(new OkHttp3Downloader(client)).build();
		picasso = picassoBuilder.build();
		Picasso.setSingletonInstance(picasso);
	}

	public static PicassoImageManager getInstance(Context context) {
		if (picassoimagemanager == null) {
			synchronized (PicassoImageManager.class) {
				if (picassoimagemanager == null) {
					picassoimagemanager = new PicassoImageManager(context);
				}
			}
		}
		return picassoimagemanager;
	}

	public void loadImage(String url, ImageView imageView) {
		picasso.load(url).placeholder(R.drawable.a).error(R.drawable.a)
				.fit().into(imageView);
	}

	public void changeImage(String url, ImageView imageView) {
		picasso.load(url).placeholder(R.drawable.a).error(R.drawable.a)
				.memoryPolicy(MemoryPolicy.NO_CACHE)
				.networkPolicy(NetworkPolicy.NO_CACHE).fit().into(imageView);
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
