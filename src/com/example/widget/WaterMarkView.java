package com.example.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.stickyheaderlsitview.R;
import com.example.util.DensityUtil;

public class WaterMarkView extends View {
	private int mDegrees ;
	// 水印文字
	
	public int viewHeight;
	public int viewwith;
	public int mTextHeight;
	public int mTextWidth;
	public int Ynum;
	public int Xnum;
	public Context mcontext;
	private float alpha ;
	private String waterMarkmText;
	private int mTextSize;
	private Paint mPaint;
	private Rect mTextBound = new Rect();
	private PointF[][] startCxCy;
	private int textColor;
	private int Xinterval; 
	private int Yinterval; 
	
	
	
	public WaterMarkView(Context context) {
		this(context, null);
	}

	public WaterMarkView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WaterMarkView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mcontext=context;
		  TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WaterMarkView);
		  textColor = ta.getColor(R.styleable.WaterMarkView_textColor, Color.DKGRAY);
		  mTextSize= ta.getDimensionPixelSize(R.styleable.WaterMarkView_waterTextsize, DensityUtil.sp2px(context, 20));
		  Xinterval= ta.getDimensionPixelSize(R.styleable.WaterMarkView_Xinterval, DensityUtil.sp2px(context, 20));
		  Yinterval= ta.getDimensionPixelSize(R.styleable.WaterMarkView_Yinterval, DensityUtil.sp2px(context, 20));
		  
		  
		  waterMarkmText= ta.getString(R.styleable.WaterMarkView_waterText);
			if (waterMarkmText == null) {
				waterMarkmText="手机光钥匙";
			}
		  
		  mDegrees= ta.getDimensionPixelSize(R.styleable.WaterMarkView_degree, 350);
		  alpha= ta.getFloat(R.styleable.WaterMarkView_viewalpha, 0.5f);
		  
		  this.setAlpha(alpha);
		  ta.recycle();
		  init();

	}
	private void init(){

		mPaint=new Paint();
		mPaint.setStrokeWidth(4);// 画线笔宽度
		mPaint.setStyle(Style.FILL);// 画笔样式铺满
		mPaint.setAntiAlias(true);// 抗锯齿
		mPaint.setColor(textColor);
		mPaint.setTextSize(mTextSize);	
		measureText();
	}
	
	private void measureText() {
		mTextWidth = (int) mPaint.measureText(waterMarkmText)+Xinterval;
		FontMetrics fm = mPaint.getFontMetrics();
		mTextHeight = (int) Math.ceil(fm.descent - fm.top);
		mPaint.getTextBounds(waterMarkmText, 0, waterMarkmText.length(), mTextBound);
		mTextHeight = mTextBound.height()+Yinterval;
	}
	
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed) {
			viewwith = getWidth();
			viewHeight = getHeight();
			Xnum=viewwith/mTextWidth+5;
			Ynum=viewHeight/mTextHeight+5;
		}
		startCxCy = new PointF[Xnum][Ynum];
		setWaterMarkPoint();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
		drawWaterMark(canvas);
	}	
		
	public void drawWaterMark(Canvas canvas) {
		for (int i = 0; i < Xnum; i++) {
			for (int j = 0; j < Ynum; j++) {
				PointF center = startCxCy[i][j];
				canvas.save();
			    canvas.rotate(mDegrees, i*(mTextWidth/2f) ,j*(mTextHeight));
				canvas.drawText(waterMarkmText, center.x, center.y, mPaint);
				  canvas.restore(); 		
			}
		}
		   
	}
	
	public void setWaterMarkPoint(){
		for (int i = 0; i < Xnum; i++) {
			for (int j = 0; j < Ynum; j++) {
				PointF cPointF = new PointF();
				cPointF.x = (i * mTextWidth);
				cPointF.y = (j * mTextHeight);
				startCxCy[i][j] = cPointF;
			}
		}
	}
	
	
	public int getTextSize() {
		return mTextSize;
	}

	public void setTextSize(int mTextSize) {
		this.mTextSize = mTextSize;
		  init();
		requestLayout();
		invalidate();
	}
	
	public void setText(String text) {
		this.waterMarkmText = text;
		 init();
		requestLayout();
		invalidate();
	}
}
