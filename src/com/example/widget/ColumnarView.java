package com.example.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.example.bean.WeekRankingbean;
import com.example.extra.MyLog;
import com.example.util.DensityUtil;

//类似光钥匙考勤图表
public class ColumnarView extends View {
	private Paint xLinePaint;// 坐标轴 轴线 画笔：
	private Paint hLinePaint;// 坐标轴水平内部 虚线画笔
	private Paint titlePaint;// 绘制文本的画笔
	private Paint paint;// 矩形画笔 柱状图的样式信息
	private int[] progress;// 7 条，显示各个柱状的数据
	private int[] aniProgress;// 实现动画的值
	private String[] colors;
	private final int TRUE = 1;// 在柱状图上显示数字
	private int[] text;// 设置点击事件，显示哪一条柱状的信息
	// private Bitmap bitmap;

	private String[] ySteps;// 坐标轴左侧的数标
	private String[] xWeeks;// 坐标轴底部的星期数
	private boolean flag;// 是否使用动画
	private Context mContext;
	private HistogramAnimation ani;
	private float max;
	private int startX;
	private int paddingRightX;
	private int columnWidth;

	public ColumnarView(Context context) {
		super(context);
		init(context);
	}

	public ColumnarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		mContext = context;
		ySteps = new String[] { "10k", "7.5k", "5k", "2.5k", "0" };// Y轴坐标值
		xWeeks = new String[] { "0周", "0周", "0周", "0周" };// X轴坐标
		colors = new String[] { "#FF911B", "#FF6B5B", "#1FB9ED", "#0CDC72" };
		progress = new int[] { 2000, 5000, 6000, 8000 };
		text = new int[] { 0, 0, 0, 0 };// 点击坐标值
		aniProgress = new int[] { 20, 20, 20, 20 };// 动画
		max = 10000f;
		startX = DensityUtil.dip2px(mContext, 35);
		paddingRightX = DensityUtil.dip2px(mContext, 30);
		columnWidth = DensityUtil.dip2px(mContext, 35);

		ani = new HistogramAnimation();
		ani.setDuration(1500);// 动画时间

		xLinePaint = new Paint();
		hLinePaint = new Paint();
		titlePaint = new Paint();
		paint = new Paint();

		// 给画笔设置颜色
		xLinePaint.setColor(Color.DKGRAY);
		hLinePaint.setColor(Color.LTGRAY);
		titlePaint.setColor(Color.BLACK);

		// 加载画图
		// bitmap = BitmapFactory
		// .decodeResource(getResources(), R.drawable.hudong);
	}
	
	public void setDate(ArrayList<WeekRankingbean> rankingOAs, float max) {
		if (rankingOAs != null && rankingOAs.size() > 0) {
			xWeeks = new String[rankingOAs.size()];
			colors = new String[rankingOAs.size()];
			text = new int[rankingOAs.size()];
			aniProgress = new int[rankingOAs.size()];
			progress = new int[rankingOAs.size()];
			for (int i = 0; i < rankingOAs.size(); i++) {
				xWeeks[i] = rankingOAs.get(i).getWEEKS() + "周";
				colors[i] = rankingOAs.get(i).getColor();
				progress[i] = rankingOAs.get(i).getWEEK_ORDER();
				text[i] = 0;
				aniProgress[i] = 0;
			}
			ySteps = new String[] { (int) max + "", (int) (max - max / 4) + "",
					(int) (max - max / 2) + "", (int) (max - max * 3 / 4) + "",
					0 + "" };
			this.max = max;
			invalidate();
		}
	}	
	public void start(boolean flag) {
		this.flag = flag;
		this.startAnimation(ani);
	}	
	protected void onDraw(Canvas canvas) {
		int width = getWidth();// 设置空间宽度
		int height = getHeight() - DensityUtil.dip2px(mContext, 50);// 设置空间高度，底部预留50dip
		
		// 绘制X轴底线，起点（30dip,height+3dip）终点（width-30dip,height+3dip）
				canvas.drawLine(startX, height + DensityUtil.dip2px(mContext, 3), width
						- paddingRightX, height + DensityUtil.dip2px(mContext, 3),
						xLinePaint);
				
				int leftHeight = height - DensityUtil.dip2px(mContext, 5);// 高度
				int hPerHeight = leftHeight / 4;// 分成四部分

				hLinePaint.setTextAlign(Align.CENTER);// 设置水平居中，然后drawText的X坐标设置为width/2即可。
				for (int i = 0; i < 4; i++) {
					canvas.drawLine(startX, DensityUtil.dip2px(mContext, 10) + i
							* hPerHeight, width - paddingRightX,
							DensityUtil.dip2px(mContext, 10) + i * hPerHeight,
							hLinePaint);
				}
				
				// 绘制 Y 周坐标
				titlePaint.setTextAlign(Align.RIGHT);// 设置水平居右
				titlePaint.setTextSize(DensityUtil.dip2px(mContext, 10));
				titlePaint.setAntiAlias(true);
				titlePaint.setStyle(Paint.Style.FILL);

				// 设置Y周的数字 点（25dip,13dip）
				// （25dip+hPerHeight,13dip）
				// （25dip+2hPerHeight,13dip）
				// （25dip+3hPerHeight,13dip）
				// （25dip+4hPerHeight,13dip）
				for (int i = 0; i < ySteps.length; i++) {
					canvas.drawText(ySteps[i], startX - DensityUtil.dip2px(mContext, 3),
							DensityUtil.dip2px(mContext, 13) + i * hPerHeight,
							titlePaint);
				}
				
				// 绘制 X 周 做坐标
				int xAxisLength = width - startX;// width-30dip
				int columCount = xWeeks.length + 1;// 8
				int step = xAxisLength / columCount;// (width-30dip)/8
				for (int i = 0; i < columCount - 1; i++) {
					canvas.drawText(xWeeks[i],
							columnWidth - (columnWidth - titlePaint.measureText("38周"))
									/ 2F + step * (i + 1),
							height + DensityUtil.dip2px(mContext, 20), titlePaint);
				}
				// 绘制矩形
				if (aniProgress != null && aniProgress.length > 0) {
					for (int i = 0; i < aniProgress.length; i++) {// 循环遍历将7条柱状图形画出来
						int value = aniProgress[i];
						paint.setAntiAlias(true);// 抗锯齿效果
						paint.setStyle(Paint.Style.FILL);
						paint.setTextSize(DensityUtil.sp2px(mContext, 12));// 字体大小
						paint.setColor(Color.parseColor(colors[i]));// 字体颜色
						Rect rect = new Rect();// 柱状图的形状

						rect.left = step * (i + 1);
						rect.right = columnWidth + step * (i + 1);

						int rh = (int) (leftHeight - leftHeight * (value / max));// leftHeight*(1-value
																					// /
																					// 10000.0);
						rect.top = rh + DensityUtil.dip2px(mContext, 10);
						rect.bottom = height;

						canvas.drawRect(rect, paint);
						// 是否显示柱状图上方的数字
						if (this.text[i] == TRUE) {
							canvas.drawText(value + "",
									(columnWidth - paint.measureText(value + "")) / 2F
											+ step * (i + 1),
									rh + DensityUtil.dip2px(mContext, 5), paint);
						}

					}
				}		
				
	}
	/**
	 * 设置点击事件，是否显示数字
	 */
	public boolean onTouchEvent(MotionEvent event) {
		performClick();
		int step = (getWidth() - startX) / (xWeeks.length + 1);
		int x = (int) event.getX();
		for (int i = 0; i < xWeeks.length; i++) {
			if (x > (columnWidth / 2 + step * (i + 1) - columnWidth / 2)
					&& x < (columnWidth / 2 + step * (i + 1) + columnWidth / 2)) {
				text[i] = 1;
				for (int j = 0; j < xWeeks.length; j++) {
					if (i != j) {
						text[j] = 0;
					}
				}
				if (Looper.getMainLooper() == Looper.myLooper()) {
					invalidate();
				} else {
					postInvalidate();
				}
			}
		}
		return super.onTouchEvent(event);
	}
	/**
	 * 集成animation的一个动画类
	 * 
	 */
	private class HistogramAnimation extends Animation {
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			MyLog.e("TAG", "interpolatedTime==="+interpolatedTime);
			if (interpolatedTime < 1.0f && flag) {
				for (int i = 0; i < aniProgress.length; i++) {
					aniProgress[i] = (int) (progress[i] * interpolatedTime);
				}
			} else {
				for (int i = 0; i < aniProgress.length; i++) {
					aniProgress[i] = progress[i];
				}
			}
			invalidate();
		}
	}
}
