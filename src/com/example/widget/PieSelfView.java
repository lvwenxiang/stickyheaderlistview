package com.example.widget;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.example.bean.MyRatebean;
import com.example.util.DensityUtil;

//光钥匙饼图
public class PieSelfView extends View {
	private int viewWidthHeight;// 饼状图的长宽
	private int width;// 整个View的宽
	private int height;// 整个View的高
	private double total;// 所有状态值的总和

	private Double[] itemValuesTemp;// 各个状态对应的值

	private Double[] itemsValues;// 实际中各个状态对应的值
	private String[] tags;// 各个状态对应的说明

	private String[] itemColors;// 各个状态对应的颜色值
	private float[] itemsAngle;// 各个状态对应的角度
	private float[] itemsStartAngle;// 各个状态对应的角度对应的开始角度
	private float[] itemsPercent;// 各个状态对应的百分比
	private float radius;// 饼图半径
	private int titleSize = 15;
	// private int markSize = 10;
	private int spacing = 2;
	private String title;// 中间小圆形中写标题
	private float startA = 0;
	private int aph = 0;
	private float[] mSweep;// 展开动画，设置各个角度，随动画展开递增
	private mAnimation animation;// 展开动画
	private Paint paint;
	private RectF oval;

	public PieSelfView(Context context, AttributeSet attrs) {
		super(context, attrs);
		invalidate();
		animation = new mAnimation();// 初始化动画对象，设置动画时间1.5秒
		animation.setDuration(1500);
		paint = new Paint();
		paint.setAntiAlias(true);
	}

	/**
	 * 重写动画类
	 * 
	 * @author yang.shen
	 * 
	 */
	private class mAnimation extends Animation {
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			// interpolatedTime 从0到1慢慢递增
			if (itemsAngle != null) {
				for (int i = 0; i < itemsAngle.length; i++) {
					mSweep[i] = itemsAngle[i] * interpolatedTime;
				}
				aph = (int) (255 * interpolatedTime);
			}
			invalidate();
		}
	}

	// 开始动画
	public void startAnim() {
		startAnimation(animation);
	}

	// 设置数据
	public void setRates(ArrayList<MyRatebean> rates) {
		if (rates != null && rates.size() > 0) {
			itemValuesTemp = new Double[rates.size()];
			tags = new String[rates.size()];
			itemColors = new String[rates.size()];
			for (int i = 0; i < rates.size(); i++) {
				itemValuesTemp[i] = rates.get(i).getValue();// 给itemValuesTemp赋值
				tags[i] = rates.get(i).getTitle();// 给tags赋值
				itemColors[i] = rates.get(i).getColor();// 给itemColors赋值
			}
			mSweep = new float[itemValuesTemp.length];// 初始mSweep数组
			reSetTotal();
			refreshItemsAngs();
		}
		invalidate();
	}

	// 获取itemValuesTemp中的Value总和
	private void reSetTotal() {
		double totalSizes = getAllSizes();
		if (total < totalSizes)
			total = totalSizes;
	}

	private double getAllSizes() {
		float tempAll = 0.0F;
		if ((itemValuesTemp != null) && (itemValuesTemp.length > 0)) {
			for (double itemsize : itemValuesTemp) {
				tempAll += itemsize;
			}
		}
		return tempAll;
	}

	// 对itemsValues,itemsPercent，itemsStartAngle，itemsAngle数组进行赋值
	private void refreshItemsAngs() {
		if ((itemValuesTemp != null) && (itemValuesTemp.length > 0)) {
			if (total > getAllSizes()) {
				itemsValues = new Double[itemValuesTemp.length + 1];
				for (int i = 0; i < itemValuesTemp.length; i++) {
					itemsValues[i] = itemValuesTemp[i];
				}
				itemsValues[(itemsValues.length - 1)] = (total - getAllSizes());
			} else {
				itemsValues = new Double[itemValuesTemp.length];
				itemsValues = itemValuesTemp;
			}

			itemsPercent = new float[itemsValues.length];
			itemsStartAngle = new float[itemsValues.length];
			itemsAngle = new float[itemsValues.length];
			float startAngle = 0.0F;

			for (int i = 0; i < itemsValues.length; i++) {
				itemsPercent[i] = ((float) (itemsValues[i] * 1.0D / total * 1.0D));// 获取各个状态所占的百分比

			}
			for (int i = 0; i < itemsPercent.length; i++) {
				itemsAngle[i] = (360.0F * itemsPercent[i]);// 获取各个状态所占的角度
				// 获取各个状态的开始角度
				if (i != 0) {
					itemsStartAngle[i] = startAngle + itemsAngle[i - 1];
					startAngle = 360.0F * itemsPercent[(i - 1)] + startAngle;
				} else {
					itemsStartAngle[i] = -itemsAngle[i] / 2;
					startAngle = itemsStartAngle[i];
				}
			}
		}
	}

	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);

		if ((itemsAngle != null) && (itemsStartAngle != null)) {
			startA = itemsStartAngle[0];
			for (int i = 0; i < itemsAngle.length; i++) {
				paint.setStyle(Paint.Style.FILL);
				paint.setColor(Color.parseColor(itemColors[i]));
				canvas.drawArc(oval, startA, mSweep[i], true, paint);// 画扇形

				paint.setStyle(Paint.Style.STROKE);
				paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
				paint.setColor(Color.WHITE);
				canvas.drawArc(oval, startA, mSweep[i], true, paint);// 画笔设置为镂空，在画扇外画白边

				startA = startA + mSweep[i];

				if (i != 0) {
					double ang = itemsAngle[0] / 2;
					for (int c = 1; c <= i; c++) {
						if (c == i) {
							ang += itemsAngle[c] / 2;
						} else {
							ang += itemsAngle[c];
						}
					}
					drawLine(ang, canvas, itemColors[i], paint, tags[i]);// 画线,写值
				}

				if (i == 0) {
					paint.setTextSize(DensityUtil.sp2px(getContext(), 10));
					paint.setStyle(Paint.Style.STROKE);
					paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
					paint.setColor(Color.parseColor(itemColors[i]));
					paint.setAlpha(aph);
					canvas.drawCircle(
							width / 2 + radius
									+ DensityUtil.dip2px(getContext(), 4),
							height / 2, DensityUtil.dip2px(getContext(), 2),
							paint);

					canvas.drawLine(
							width / 2 + radius
									+ DensityUtil.dip2px(getContext(), 6),
							height / 2,
							width
									/ 2
									+ radius
									+ DensityUtil.dip2px(getContext(),
											6 + spacing)
									+ paint.measureText("正常上班80.00%"),
							height / 2, paint);
					paint.setStyle(Paint.Style.FILL);
					canvas.drawCircle(
							width
									/ 2
									+ radius
									+ DensityUtil.dip2px(getContext(),
											7 + spacing)
									+ paint.measureText("正常上班80.00%"),
							height / 2, DensityUtil.dip2px(getContext(), 1),
							paint);
					String s[] = tags[i].split(" ");
					paint.setColor(Color.parseColor("#949da1"));
					paint.setAlpha(aph);
					canvas.drawText(
							s[0],
							width
									/ 2
									+ radius
									+ DensityUtil.dip2px(getContext(),
											6 + spacing),
							height / 2
									- DensityUtil.dip2px(getContext(), spacing),
							paint);
					paint.setColor(Color.parseColor(itemColors[i]));
					paint.setTypeface(Typeface.DEFAULT_BOLD);
					paint.setAlpha(aph);
					canvas.drawText(
							s[1],
							width
									/ 2
									+ radius
									+ DensityUtil.dip2px(getContext(),
											6 + spacing)
									+ paint.measureText(s[0]),
							height / 2
									- DensityUtil.dip2px(getContext(), spacing),
							paint);
				}
			}
		}
		// 画中心圆
		paint.setTextSize(DensityUtil.sp2px(getContext(), titleSize));
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		canvas.drawCircle(width / 2, height / 2, radius * 15 / 30, paint);

		// 画中间文本title
		// paint.setColor(Color.parseColor("#596569"));
		// paint.setAlpha(aph);
		// if (title != null) {// 写标题
		// if (title.length() <= 4) {
		// canvas.drawText(title,
		// width / 2 - paint.measureText(title) / 2,
		// height / 2 + DensityUtil.sp2px(getContext(), titleSize)
		// / 2, paint);
		// } else {
		// canvas.drawText(title.substring(0, 3),
		// width / 2 - paint.measureText(title.substring(0, 3))
		// / 2, height / 2, paint);
		// canvas.drawText(
		// title.substring(3, title.length()),
		// width
		// / 2
		// - paint.measureText(title.substring(3,
		// title.length())) / 2,
		// height
		// / 2
		// + DensityUtil.sp2px(getContext(), titleSize
		// + spacing), paint);
		// }
		// }
	}

	// 画线
	private void drawLine(double Angle, Canvas canvas, String color,
			Paint paint, String tag) {
		paint.setTextSize(DensityUtil.sp2px(getContext(), 10));
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
		paint.setColor(Color.parseColor(color));
		paint.setAlpha(aph);
		float x, y;
		if (Angle <= 90) {
			x = (float) (width / 2 + radius * Math.cos(Math.toRadians(Angle)));
			y = (float) (height / 2 + radius * Math.sin(Math.toRadians(Angle)));

			canvas.drawCircle(x + getCos(4, Angle), y + getSin(4, Angle),
					DensityUtil.dip2px(getContext(), 2), paint);
			
			canvas.drawLine(x + getCos(6, Angle), y + getSin(6, Angle), x
					+ getCos(16, Angle), y + getSin(16, Angle), paint);
			
			canvas.drawLine(x + getCos(16, Angle), y + getSin(16, Angle), x
					+ getCos(16, Angle) + paint.measureText("加班10.00%")
					+ DensityUtil.dip2px(getContext(), spacing),
					y + getSin(16, Angle), paint);
			
			paint.setStyle(Paint.Style.FILL);
			paint.setTypeface(Typeface.DEFAULT);
			canvas.drawCircle(
					x + getCos(16, Angle) + paint.measureText("加班10.00%")
							+ DensityUtil.dip2px(getContext(), 1 + spacing), y
							+ getSin(16, Angle),
							DensityUtil.dip2px(getContext(), 1), paint);
			String s[] = tag.split(" ");

			paint.setColor(Color.parseColor("#949da1"));
			paint.setAlpha(aph);
			canvas.drawText(
					s[0],
					x + getCos(16, Angle)
							+ DensityUtil.dip2px(getContext(), spacing),
					y + getSin(16, Angle)
							- DensityUtil.dip2px(getContext(), spacing), paint);

			paint.setColor(Color.parseColor(color));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAlpha(aph);
			canvas.drawText(
					s[1],
					x + getCos(16, Angle)
							+ DensityUtil.dip2px(getContext(), spacing)
							+ paint.measureText(s[0]), y + getSin(16, Angle)
							- DensityUtil.dip2px(getContext(), spacing), paint);
		}else if (Angle < 180) {
			x = (float) (width / 2 - radius
					* Math.cos(Math.toRadians(180 - Angle)));
			y = (float) (height / 2 + radius
					* Math.sin(Math.toRadians(180 - Angle)));

			canvas.drawCircle(x - getCos(4, 180 - Angle),
					y + getSin(4, 180 - Angle),
					DensityUtil.dip2px(getContext(), 2), paint);

			canvas.drawLine(x - getCos(6, 180 - Angle),
					y + getSin(6, 180 - Angle), x - getCos(16, 180 - Angle), y
							+ getSin(16, 180 - Angle), paint);

			canvas.drawLine(x - getCos(16, 180 - Angle),
					y + getSin(16, 180 - Angle),
					x - getCos(16, 180 - Angle) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), spacing), y
							+ getSin(16, 180 - Angle), paint);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(
					x - getCos(16, 180 - Angle) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), 1 + spacing), y
							+ getSin(16, 180 - Angle),
					DensityUtil.dip2px(getContext(), 1), paint);
			String s[] = tag.split(" ");
			paint.setTypeface(Typeface.DEFAULT);
			paint.setColor(Color.parseColor("#949da1"));
			paint.setAlpha(aph);
			canvas.drawText(
					s[0],
					x - getCos(16, 180 - Angle) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), spacing),
					y + getSin(16, 180 - Angle)
							- DensityUtil.dip2px(getContext(), spacing), paint);
			paint.setColor(Color.parseColor(color));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAlpha(aph);
			canvas.drawText(
					s[1],
					x - getCos(16, 180 - Angle) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), spacing)
							+ paint.measureText(s[0]),
					y + getSin(16, 180 - Angle)
							- DensityUtil.dip2px(getContext(), spacing), paint);
		} else if (Angle == 180) {
			x = (float) (width / 2 - radius
					* Math.cos(Math.toRadians(180 - Angle)));
			y = (float) (height / 2 + radius
					* Math.sin(Math.toRadians(180 - Angle)));

			canvas.drawCircle(x - getCos(4, 180 - Angle),
					y + getSin(4, 180 - Angle),
					DensityUtil.dip2px(getContext(), 2), paint);

			canvas.drawLine(x - getCos(6, 180 - Angle),
					y + getSin(6, 180 - Angle),
					x - getCos(6, 180 - Angle) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), spacing), y
							+ getSin(6, 180 - Angle), paint);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(
					x - getCos(6, 180 - Angle) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), 1 + spacing), y
							+ getSin(6, 180 - Angle),
					DensityUtil.dip2px(getContext(), 1), paint);
			String s[] = tag.split(" ");
			paint.setTypeface(Typeface.DEFAULT);
			paint.setColor(Color.parseColor("#949da1"));
			paint.setAlpha(aph);
			canvas.drawText(
					s[0],
					x - getCos(6, 180 - Angle) - paint.measureText("迟到/早退10%"),
					y + getSin(6, 180 - Angle)
							- DensityUtil.dip2px(getContext(), spacing), paint);
			paint.setColor(Color.parseColor(color));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAlpha(aph);
			canvas.drawText(
					s[1],
					x - getCos(6, 180 - Angle) - paint.measureText("迟到/早退10%")
							+ paint.measureText(s[0]),
					y + getSin(6, 180 - Angle)
							- DensityUtil.dip2px(getContext(), spacing), paint);
		} else if (Angle <= 270) {
			x = (float) (width / 2 - radius
					* Math.cos(Math.toRadians(Angle - 180)));
			y = (float) (height / 2 - radius
					* Math.sin(Math.toRadians(Angle - 180)));

			canvas.drawCircle(x - getCos(4, Angle - 180),
					y - getSin(4, Angle - 180),
					DensityUtil.dip2px(getContext(), 2), paint);

			canvas.drawLine(x - getCos(6, Angle - 180),
					y - getSin(6, Angle - 180), x - getCos(16, Angle - 180), y
							- getSin(16, Angle - 180), paint);

			canvas.drawLine(x - getCos(16, Angle - 180),
					y - getSin(16, Angle - 180),
					x - getCos(16, Angle - 180) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), spacing), y
							- getSin(16, Angle - 180), paint);

			paint.setStyle(Paint.Style.FILL);

			canvas.drawCircle(
					x - getCos(16, Angle - 180) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), 1 + spacing), y
							- getSin(16, Angle - 180),
					DensityUtil.dip2px(getContext(), 1), paint);

			String s[] = tag.split(" ");
			paint.setColor(Color.parseColor("#949da1"));
			paint.setAlpha(aph);
			paint.setTypeface(Typeface.DEFAULT);
			canvas.drawText(
					s[0],
					x - getCos(16, Angle - 180) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), spacing),
					y
							- getSin(16, Angle - 180)
							+ DensityUtil
									.sp2px(getContext(), 10 + spacing),
					paint);
			paint.setColor(Color.parseColor(color));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAlpha(aph);
			canvas.drawText(
					s[1],
					x - getCos(16, Angle - 180) - paint.measureText("迟到/早退10%")
							- DensityUtil.dip2px(getContext(), spacing)
							+ paint.measureText(s[0]),
					y
							- getSin(16, Angle - 180)
							+ DensityUtil
									.sp2px(getContext(), 10 + spacing),
					paint);
		} else {
			x = (float) (width / 2 + radius
					* Math.cos(Math.toRadians(360 - Angle)));
			y = (float) (height / 2 - radius
					* Math.sin(Math.toRadians(360 - Angle)));

			canvas.drawCircle(x + getCos(4, 360 - Angle),
					y - getSin(4, 360 - Angle),
					DensityUtil.dip2px(getContext(), 2), paint);

			canvas.drawLine(x + getCos(6, 360 - Angle),
					y - getSin(6, 360 - Angle), x + getCos(16, 360 - Angle), y
							- getSin(16, 360 - Angle), paint);

			canvas.drawLine(x + getCos(16, 360 - Angle),
					y - getSin(16, 360 - Angle),
					x + getCos(16, 360 - Angle) + paint.measureText("迟到/早退10%")
							+ DensityUtil.dip2px(getContext(), spacing), y
							- getSin(16, 360 - Angle), paint);
			paint.setStyle(Paint.Style.FILL);

			canvas.drawCircle(
					x + getCos(16, 360 - Angle) + paint.measureText("迟到/早退10%")
							+ DensityUtil.dip2px(getContext(), 1 + spacing), y
							- getSin(16, 360 - Angle),
					DensityUtil.dip2px(getContext(), 1), paint);

			String s[] = tag.split(" ");
			paint.setColor(Color.parseColor("#949da1"));
			paint.setAlpha(aph);
			paint.setTypeface(Typeface.DEFAULT);
			canvas.drawText(
					s[0],
					x + getCos(16, 360 - Angle)
							+ DensityUtil.dip2px(getContext(), spacing),
					y
							- getSin(16, 360 - Angle)
							+ DensityUtil
									.sp2px(getContext(), 10 + spacing),
					paint);
			paint.setColor(Color.parseColor(color));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setAlpha(aph);
			canvas.drawText(
					s[1],
					x + getCos(16, 360 - Angle)
							+ DensityUtil.dip2px(getContext(), spacing)
							+ paint.measureText(s[0]),
					y
							- getSin(16, 360 - Angle)
							+ DensityUtil
									.sp2px(getContext(), 10 + spacing),
					paint);
		}
	}

	private float getCos(int x, double Angle) {
		return DensityUtil.dip2px(getContext(),
				(float) (x * Math.cos(Math.toRadians(Angle))));
	}

	private float getSin(int x, double Angle) {
		return DensityUtil.dip2px(getContext(),
				(float) (x * Math.sin(Math.toRadians(Angle))));
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		if (changed) {
			width = getWidth();
			height = getHeight();
			viewWidthHeight = Math.min(getWidth(), getHeight());

			radius = viewWidthHeight / 3f;

			oval = new RectF(width / 2 - radius, height / 2 - radius, width / 2
					+ radius, height / 2 + radius);
		}

		super.onLayout(changed, left, top, right, bottom);
	}
}
