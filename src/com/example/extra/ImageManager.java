package com.example.extra;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.stickyheaderlsitview.R;


public class ImageManager {
	private Context mContext;
	public static final String ANDROID_RESOURCE = "android.resource://";
	public static final String FOREWARD_SLASH = "/";

	public ImageManager(Context context) {
		this.mContext = context;
	}

	
/*	Universal Image Loader：一个强大的图片加载库，包含各种各样的配置，最老牌，使用也最广泛。

	Picasso: Square出品，必属精品。和OKHttp搭配起来更配呦！

	Volley ImageLoader：Google官方出品，可惜不能加载本地图片~

	Fresco：Facebook出的，天生骄傲！不是一般的强大。

	Glide：Google推荐的图片加载库，专注于流畅的滚动。*/
	
	// 将资源ID转为Uri
	public Uri resourceIdToUri(int resourceId) {
		return Uri.parse(ANDROID_RESOURCE + mContext.getPackageName()
				+ FOREWARD_SLASH + resourceId);
	}

	// 加载网络图片
	public void loadUrlImage(String url, ImageView imageView) {	
		Glide.with(mContext).load(url).placeholder(R.color.font_black_6)
				.error(R.color.font_black_6).crossFade().into(imageView);
	}

	// 加载drawable图片
	public void loadResImage(int resId, ImageView imageView) {
		Glide.with(mContext).load(resourceIdToUri(resId))
				.placeholder(R.color.font_black_6).error(R.color.font_black_6)
				.crossFade().into(imageView);
	}

	// 加载本地图片
	public void loadLocalImage(String path, ImageView imageView) {
		Glide.with(mContext).load("file://" + path)
				.placeholder(R.color.font_black_6).error(R.color.font_black_6)
				.crossFade().into(imageView);
	}

	// 加载网络圆型图片
	public void loadCircleImage(String url, ImageView imageView) {
		Glide.with(mContext).load(url).placeholder(R.drawable.ic_launcher)
				.error(R.drawable.ic_launcher).crossFade();
			//	.transform(new GlideCircleTransform(mContext)).into(imageView);
	}

	// 加载drawable圆型图片
	public void loadCircleResImage(int resId, ImageView imageView) {
		Glide.with(mContext).load(resourceIdToUri(resId))
				.placeholder(R.color.font_black_6).error(R.color.font_black_6)
			//	.crossFade().transform(new GlideCircleTransform(mContext))
				.into(imageView);
	}

	// 加载本地圆型图片
	public void loadCircleLocalImage(String path, ImageView imageView) {
		Glide.with(mContext).load("file://" + path)
				.placeholder(R.color.font_black_6).error(R.color.font_black_6)
			//	.crossFade().transform(new GlideCircleTransform(mContext))
				.into(imageView);
	}

}
