package com.example.extra;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.stickyheaderlsitview.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

public class PicassoImageLoader {
	private String imgUrl;
	private OnImageLoadListener mListener;
	private File tempFile;
	private Context context;
	private ImageView imageview;
	
	
	private static Target target = new Target(){

		@Override
		public void onBitmapFailed(Drawable arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onBitmapLoaded(Bitmap arg0, LoadedFrom arg1) {
			//可以拿到位图
			
		}

		@Override
		public void onPrepareLoad(Drawable arg0) {
			// TODO Auto-generated method stub
			
		}
		 
	 };
	 
	
	public PicassoImageLoader(Context context,String url, OnImageLoadListener mListener,
			File tempFile) {
		// TODO Auto-generated constructor stub
		this.imgUrl = url;
		this.mListener = mListener;
		this.tempFile = tempFile;
		this.context=context;
	}


	public interface OnImageLoadListener {
		public void onImageLoad(Bitmap bitmap);

		public void onError(String error);
	}
	//如果想自己指定缓存目录，那么像我下面这样就ok啦：
	private void loadImageCache(Context context,String url,ImageView imageview) {
	        final String imageCacheDir = ""+ "imagew";
	        Picasso picasso = new Picasso.Builder(context).downloader(
	                new OkHttpDownloader(new File(imageCacheDir))).build();
	        Picasso.setSingletonInstance(picasso);
	        
	        picasso  
	        .load(url)
	        .into(imageview);
	    }

	
	public static void loadImageFromUrl2(Context context,String url,ImageView imageview) {
	
	Picasso.with(context)
	.load(url)
	.placeholder(R.drawable.btn_white).error(R.drawable.btn_white)
	.noFade() //没有淡入的效果
	.into(target);
	}
	public static void loadImageFromUrl(Context context,String url,ImageView imageview) {
		Picasso.with(context)
		.load(url)
		.placeholder(R.drawable.btn_white).error(R.drawable.btn_white)
		.noFade() //没有淡入的效果
		.into(imageview,new Callback(){

			@Override
			public void onError() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				
			}
			
		});			
	}
}