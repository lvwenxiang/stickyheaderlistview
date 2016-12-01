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

	
/*	Universal Image Loader��һ��ǿ���ͼƬ���ؿ⣬�������ָ��������ã������ƣ�ʹ��Ҳ��㷺��

	Picasso: Square��Ʒ��������Ʒ����OKHttp�������������ϣ�

	Volley ImageLoader��Google�ٷ���Ʒ����ϧ���ܼ��ر���ͼƬ~

	Fresco��Facebook���ģ���������������һ���ǿ��

	Glide��Google�Ƽ���ͼƬ���ؿ⣬רע�������Ĺ�����*/
	
	// ����ԴIDתΪUri
	public Uri resourceIdToUri(int resourceId) {
		return Uri.parse(ANDROID_RESOURCE + mContext.getPackageName()
				+ FOREWARD_SLASH + resourceId);
	}

	// ��������ͼƬ
	public void loadUrlImage(String url, ImageView imageView) {	
		Glide.with(mContext).load(url).placeholder(R.color.font_black_6)
				.error(R.color.font_black_6).crossFade().into(imageView);
	}

	// ����drawableͼƬ
	public void loadResImage(int resId, ImageView imageView) {
		Glide.with(mContext).load(resourceIdToUri(resId))
				.placeholder(R.color.font_black_6).error(R.color.font_black_6)
				.crossFade().into(imageView);
	}

	// ���ر���ͼƬ
	public void loadLocalImage(String path, ImageView imageView) {
		Glide.with(mContext).load("file://" + path)
				.placeholder(R.color.font_black_6).error(R.color.font_black_6)
				.crossFade().into(imageView);
	}

	// ��������Բ��ͼƬ
	public void loadCircleImage(String url, ImageView imageView) {
		Glide.with(mContext).load(url).placeholder(R.drawable.ic_launcher)
				.error(R.drawable.ic_launcher).crossFade();
			//	.transform(new GlideCircleTransform(mContext)).into(imageView);
	}

	// ����drawableԲ��ͼƬ
	public void loadCircleResImage(int resId, ImageView imageView) {
		Glide.with(mContext).load(resourceIdToUri(resId))
				.placeholder(R.color.font_black_6).error(R.color.font_black_6)
			//	.crossFade().transform(new GlideCircleTransform(mContext))
				.into(imageView);
	}

	// ���ر���Բ��ͼƬ
	public void loadCircleLocalImage(String path, ImageView imageView) {
		Glide.with(mContext).load("file://" + path)
				.placeholder(R.color.font_black_6).error(R.color.font_black_6)
			//	.crossFade().transform(new GlideCircleTransform(mContext))
				.into(imageView);
	}

}
