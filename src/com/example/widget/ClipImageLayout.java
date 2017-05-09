package com.example.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import com.example.stickyheaderlsitview.R;

public class ClipImageLayout extends RelativeLayout{
	private ClipImageBorderView mClipImageView;
	private MatrixImageView matriximageview;
	private ClipZoomImageView mZoomImageView;
	/**
	 * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
	 */
	private int mHorizontalPadding = 20;
	
	
	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mZoomImageView = new ClipZoomImageView(context);
//		matriximageview = new MatrixImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		
		/**
		 * 这里测试，直接写死了图片，真正使用过程中，可以提取为自定义属性
		 */
//		matriximageview.setImageDrawable(getResources().getDrawable(
//				R.drawable.ba));
		mZoomImageView.setImageDrawable(getResources().getDrawable(
				R.drawable.ba));
		
		this.addView(mZoomImageView, lp);
//		this.addView(matriximageview, lp);
		this.addView(mClipImageView, lp);
		
		
		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}
	
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}
}
