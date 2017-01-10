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
	 * ˮƽ������View�ı߾�
	 */
	private int mHorizontalPadding;
	/**
	 * ��ֱ������View�ı߾�
	 */
	private int mVerticalPadding;
	/**
	 * ���Ƶľ��εĿ��
	 */
	private int mWidth;
	/**
	 * �߿����ɫ��Ĭ��Ϊ��ɫ
	 */
	private int mBorderColor = Color.parseColor("#FFFFFF");
	/**
	 * �߿�Ŀ�� ��λdp
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
		// ���μ���		
		 // �����������Ŀ��
		 mWidth = getWidth() - 2 * mHorizontalPadding;
		 // ���������Ļ��ֱ�߽� �ı߾�
		 mVerticalPadding = (getHeight() - mWidth) / 2;
		 mPaint.setColor(Color.parseColor("#aa000000"));
		 mPaint.setStyle(Style.FILL);
		 // �������1
		 canvas.drawRect(0, 0, mHorizontalPadding, getHeight(), mPaint);
		 // �����ұ�2
		 canvas.drawRect(getWidth() - mHorizontalPadding, 0, getWidth(),
		 getHeight(), mPaint);
		 // �����ϱ�3
		 canvas.drawRect(mHorizontalPadding, 0, getWidth() -
		 mHorizontalPadding,
		 mVerticalPadding, mPaint);
		 // �����±�4
		 canvas.drawRect(mHorizontalPadding, getHeight() - mVerticalPadding,
		 getWidth() - mHorizontalPadding, getHeight(), mPaint);
		 // ������߿�
		 mPaint.setColor(mBorderColor);
		 mPaint.setStrokeWidth(mBorderWidth);
		 mPaint.setStyle(Style.STROKE);
		  canvas.drawRect(mHorizontalPadding, mVerticalPadding, getWidth()
		 - mHorizontalPadding, getHeight() - mVerticalPadding, mPaint);

		
//��΢�� Բ�μ���		
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
//		// ������߿�
//		mPaint.setColor(mBorderColor);
//		mPaint.setStrokeWidth(mBorderWidth);
//		mPaint.setStyle(Style.STROKE);
//		if (mBgBitmap == null) {
//			mBgBitmap = Bitmap.createBitmap(getWidth(), getHeight(),
//					Bitmap.Config.ARGB_8888);
//			mCanvas = new Canvas(mBgBitmap);
//			mRect = new RectF(0, 0, getWidth(), getHeight());
//		}
//		// ������Ӱ��
//		mCanvas.drawRect(mRect, mPaintRect);
//
////		 ����ʵ��Բ �����������mCanvas�����У�mPaintRect��mPaintCirle�ཻ���ּ����Ϳ�
//		mCanvas.drawCircle(getWidth() / 2, getHeight() / 2,
//				getWidth() / 2 - mHorizontalPadding, mPaintCirle);
//		// ����Ӱ�㻭����View�Ļ�����
//		canvas.drawBitmap(mBgBitmap, null, mRect, new Paint());
////		// ����Բ��
//		canvas.drawCircle(getWidth() / 2, getHeight() / 2,
//				getWidth() / 2 - mHorizontalPadding, mPaint);
//		
		
		

		
	}

	public void setHorizontalPadding(int mHorizontalPadding) {
		this.mHorizontalPadding = mHorizontalPadding;
	}

}
