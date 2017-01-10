package com.example.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ClipImageBorderView extends View {
	/**
	 * 水平方向与View的边距
	 */
	private int mHorizontalPadding;
	/**
	 * 垂直方向与View的边距
	 */
	private int mVerticalPadding;
	/**
	 * 绘制的矩形的宽度
	 */
	private int mWidth;
	/**
	 * 边框的颜色，默认为白色
	 */
	private int mBorderColor = Color.parseColor("#FFFFFF");
	/**
	 * 边框的宽度 单位dp
	 */
	private int mBorderWidth = 1;

	private Paint mPaint;

	public ClipImageBorderView(Context context) {
		this(context, null);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ClipImageBorderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mBorderWidth = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mBorderWidth, getResources()
						.getDisplayMetrics());
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		 super.onDraw(canvas);
		// 方形剪切		
		 // 计算矩形区域的宽度
		 mWidth = getWidth() - 2 * mHorizontalPadding;
		 // 计算距离屏幕垂直边界 的边距
		 mVerticalPadding = (getHeight() - mWidth) / 2;
		 mPaint.setColor(Color.parseColor("#aa000000"));
		 mPaint.setStyle(Style.FILL);
		 // 绘制左边1
		 canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
		 // 绘制右边2
		 canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
		 getHeight(), mPaint);
		 // 绘制上边3
		 canvas.drawRect(mHorizontalPadding, 0, getWidth() -
		 mHorizontalPadding,
		 mVerticalPadding, mPaint);
		 // 绘制下边4
		 canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
		 getWidth() - mHorizontalPadding, getHeight(), mPaint);
		 // 绘制外边框
		 mPaint.setColor(mBorderColor);
		 mPaint.setStrokeWidth(mBorderWidth);
		 mPaint.setStyle(Style.STROKE);
		  canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
		 - mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);

		
//仿微博 圆形剪切		
//			super.onDraw(canvas);
//		Paint mPaintCirle = new Paint();
//		Paint mPaintRect = new Paint();
//		Bitmap mBgBitmap = null;
//		Canvas mCanvas = null;
//		RectF mRect = null;
//		Paint mPaintCirle2 = new Paint();	
//		mPaintCirle.setXfermode(new PorterDuffXfermode(Mode.XOR));
//		mPaintRect.setColor(Color.parseColor("#aa000000"));
//		mPaintRect.setStyle(Style.FILL);
//		// 绘制外边框
//		mPaint.setColor(mBorderColor);
//		mPaint.setStrokeWidth(mBorderWidth);
//		mPaint.setStyle(Style.STROKE);
//		if (mBgBitmap == null) {
//			mBgBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
//					Bitmap.Config.ARGB_8888);
//			mCanvas = new Canvas(mBgBitmap);
//			mRect = new RectF(0, 0, getWidth(), getHeight());
//		}
//		// 绘制阴影层
//		mCanvas.drawRect(mRect, mPaintRect);
//
////		 绘制实心圆 ，绘制完后，在mCanvas画布中，mPaintRect和mPaintCirle相交部分即被掏空
//		mCanvas.drawCircle(getWidth() / 2, getHeight() / 2,
//				getWidth() / 2 - mHorizontalPadding, mPaintCirle);
//		// 将阴影层画进本View的画布中
//		canvas.drawBitmap(mBgBitmap, null, mRect, new Paint());
////		// 绘制圆环
//		canvas.drawCircle(getWidth() / 2, getHeight() / 2,
//				getWidth() / 2 - mHorizontalPadding, mPaint);
//		
		
		

		
	}

	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

}
