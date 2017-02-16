package com.example.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.util.DensityUtil;

/**
 * 自定义手势密码
 * 
 * @author wenxiang.lv
 * 
 */
public class GesturePwdView extends View {
	private Paint linePaint;// 画线笔
	private Paint circlePaint;// 画圆笔
	private int[][] data;// 密码数据矩阵
	private PointF[][] centerCxCy;// 圆心矩阵
	private boolean[][] selected;// 已选点，之后不能重选
	private final int flag = 3;// 矩阵大小
	private int viewWidth;// 控件宽
	private int viewHeight;// 控件高
	private int radius;// 半径
	private int red;
	private int green;
	private int blue;
	private boolean isPressedDown = false;// 是否有手指按下
	private List<PointF> selPointList; // 选中的圆中点集合
	private boolean isError = false;
	private int startX;//
	private int startY;
	private int endX;
	private int endY;
	private Thread errorThread;// 输错手势密码后延时线程
	private boolean isGestureOpen = true;
	private String lockPwd = "";// 锁屏密码字符串
	private String password = null;
	private OnLockScreenPinListener lockListener;
	
	private Handler mHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			setIsErrorDelayOver();
			return false;
		}
	});
	
	// 还原为初始状态
	private void setIsErrorDelayOver() {
		lockPwd = "";
		selPointList.clear();
		clearSelected();
		invalidate();
	}
	
	public GesturePwdView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public GesturePwdView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public GesturePwdView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		if (changed) {
			viewWidth = getWidth();
			viewHeight = getHeight();
			setRadius();
		}
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		linePaint.setStrokeWidth(2 * radius / 3);
		setDraw(red, green, blue, canvas);
	}

	// 绘制图形
	private void setDraw(int red, int green, int blue, Canvas canvas) {
		for (int i = 0; i < selected[0].length; i++) {
			for (int j = 0; j < selected[0].length; j++) {
				PointF center = centerCxCy[i][j];
				if (selected[i][j]) { // 是否选中
					circlePaint.setColor(Color.rgb(red, green, blue)); // 滑动到圆内时设置黄色画笔
					linePaint.setColor(Color.argb(255, red, green, blue));
					canvas.drawCircle(center.x, center.y, radius / 3, linePaint); // 选中时的实心内圆
				} else {
					circlePaint.setColor(Color.argb(180, 255, 255, 255)); // 否则设置灰白色画笔
				}
				canvas.drawCircle(center.x, center.y, radius, circlePaint); // 画外圆
			}
		}
		linePaint.setColor(Color.argb(96, red, green, blue));
		if (isPressedDown) {
			for (int i = 0; i < selPointList.size() - 1; i++) { // 画已选中圆之间的路径
				PointF preCenter = selPointList.get(i); // 前一个圆中点
				PointF curCenter = selPointList.get(i + 1); // 现在圆中点
				canvas.drawLine(preCenter.x, preCenter.y, curCenter.x,
						curCenter.y, linePaint);
			}
			if (!isError) {
				if (selPointList.size() > 0) {
					PointF center = selPointList.get(selPointList.size() - 1); // 最后一个选中圆中点
					canvas.drawLine(center.x, center.y, endX, endY, linePaint);
				}
			}
		}

	}

	// 设置半径和圆心
	private void setRadius() {
		int w = viewWidth / 3;
		int h = viewHeight / 3;
		if (w <= h) {
			radius = w / 3;
		} else {
			radius = h / 3;
		}

		for (int i = 0; i < flag; i++) {
			for (int j = 0; j < flag; j++) {
				PointF cPointF = new PointF();
				cPointF.x = (i * w) + w / 2;
				cPointF.y = (j * h) + h / 2;
				centerCxCy[i][j] = cPointF;
			}
		}
	}

	private void init() {
		linePaint = new Paint();
		linePaint.setStrokeWidth(5);// 画线笔宽度
		linePaint.setStyle(Style.FILL);// 画笔样式铺满
		linePaint.setAntiAlias(true);// 抗锯齿

		circlePaint = new Paint();
		circlePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.STROKE); // 画笔样式空心

		centerCxCy = new PointF[flag][flag];
		data = new int[flag][flag];
		selected = new boolean[flag][flag];
		selPointList = new ArrayList<PointF>();

		initData();
	}

	// 赋值data数组
	private void initData() {
		// TODO Auto-generated method stub
		int num = 1;
		for (int i = 0; i < flag; i++) {
			for (int j = 0; j < flag; j++) {
				data[j][i] = num;
				num++;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!isGestureOpen) {
			selPointList.clear();
			return true;
		}
		int pin = 0;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isError) {
				errorThread.interrupt();
			}
			endX = (int) event.getX();
			endY = (int) event.getY();
			lockPwd = "";
			selPointList.clear();
			isPressedDown = true;
			startX = (int) event.getX();
			startY = (int) event.getY();
			pin = getLockPinData(startX, startY);
			red = 167;
			green = 169;
			blue = 175;
			if (pin > 0) {
				isError = false;
				lockPwd += pin;
				invalidate();
			}

			break;

		case MotionEvent.ACTION_MOVE:
			endX = (int) event.getX();
			endY = (int) event.getY();
			red = 167;
			green = 169;
			blue = 175;
			pin = getLockPinData(endX, endY);
			if (pin > 0) {
				isError = false;
				lockPwd += pin;
			}
			lockPwd = setReplenish(lockPwd);

			invalidate();
			break;

		case MotionEvent.ACTION_UP:
			endX = (int) event.getX();
			endY = (int) event.getY();
			isPressedDown = false;

			if (null == password || "".equals(password)) {
				red = 167;
				green = 169;
				blue = 175;
				lockListener.onChangePin(this, lockPwd);
				clearSelected();
				invalidate();
			} else if (password.equals(lockPwd)) {
				red = 167;
				green = 169;
				blue = 175;
				lockListener.onComparePinSuccess(this, lockPwd);
				clearSelected();
				invalidate();
			} else if (!password.equals(lockPwd)) {
				isPressedDown = true;
				isError = true;
				red = 255;
				green = 0;
				blue = 0;
				invalidate();
				lockListener.onComparePinFail(this, lockPwd);
				inputError();
				// clearSelected();
				// invalidate();
			}
			
		}
		return true;

	}
	public String getPassword() {
		return password;
	}

	// 设置原手势密码
	public void setPassword(String password) {
		this.password = password;
	}	
	// 清除选中记录
		private void clearSelected() {
			for (int i = 0; i < selected.length; i++) {
				for (int j = 0; j < selected.length; j++) {
					selected[i][j] = false;
				}
			}
		}
	private String setReplenish(String s) {
		if (!s.contains("5")) {
			if (s.contains("46")) {
				selected[1][1] = true;
				return addString(s, '4', "5");
			} else if (s.contains("64")) {
				selected[1][1] = true;
				return addString(s, '6', "5");
			} else if (s.contains("28")) {
				selected[1][1] = true;
				return addString(s, '2', "5");
			} else if (s.contains("82")) {
				selected[1][1] = true;
				return addString(s, '8', "5");
			} else if (s.contains("19")) {
				selected[1][1] = true;
				return addString(s, '1', "5");
			} else if (s.contains("91")) {
				selected[1][1] = true;
				return addString(s, '9', "5");
			} else if (s.contains("37")) {
				selected[1][1] = true;
				return addString(s, '3', "5");
			} else if (s.contains("73")) {
				selected[1][1] = true;
				return addString(s, '7', "5");
			}
		}

		if (!s.contains("2")) {
			if (s.contains("13")) {
				selected[1][0] = true;
				return addString(s, '1', "2");
			} else if (s.contains("31")) {
				selected[1][0] = true;
				return addString(s, '3', "2");
			}
		}
		if (!s.contains("8")) {
			if (s.contains("79")) {
				selected[1][2] = true;
				return addString(s, '7', "8");
			} else if (s.contains("97")) {
				selected[1][2] = true;
				return addString(s, '9', "8");
			}
		}
		if (!s.contains("4")) {
			if (s.contains("17")) {
				selected[0][1] = true;
				return addString(s, '1', "4");
			} else if (s.contains("71")) {
				selected[0][1] = true;
				return addString(s, '7', "4");
			}
		}
		if (!s.contains("6")) {
			if (s.contains("39")) {
				selected[2][1] = true;
				return addString(s, '3', "6");
			} else if (s.contains("93")) {
				selected[2][1] = true;
				return addString(s, '9', "6");
			}
		}
		return s;
	}

	private String addString(String s, char f, String d) {
		char[] a = s.toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			sb.append(a[i]);
			if (a[i] == f) {
				sb.append(d);
			}
		}
		return sb.toString();
	}
	
	private int getLockPinData(int x, int y) {
		// TODO Auto-generated method stub
		for (int i = 0; i < data[0].length; i++) {
			for (int j = 0; j < data[0].length; j++) {
				PointF center = centerCxCy[i][j];
				if (isInCircle(center, x, y)) { // 判断是否在圆内
					if (!selected[i][j]) {
						selected[i][j] = true;
						selPointList.add(center);
						return data[i][j];
					}
				}
			}
		}
		return 0;
	}
	
	// 判断是否在某个圆内
	private boolean isInCircle(PointF p, int x, int y) {
		int distance = (int) Math.sqrt((p.x - x) * (p.x - x) + (p.y - y)
				* (p.y - y));
		if (distance <= radius) {
			return true;
		} else {
			return false;
		}
	}
	
	private void inputError() {
		errorThread = new Thread() {
			public void run() {
				try {
					sleep(3000);
					mHandler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
					mHandler.sendEmptyMessage(0);
					e.printStackTrace();
				}
			};
		};
		errorThread.start();
	}
	public void setOnLockScreenPinListener(OnLockScreenPinListener listener) {
		this.lockListener = listener;
	}
	public interface OnLockScreenPinListener {
		// 当密码改变
		public void onChangePin(GesturePwdView gesturePwdView, String pwd);

		// 比较密码成功
		public void onComparePinSuccess(GesturePwdView gesturePwdView,
				String pwd);

		// 比较密码失败
		public void onComparePinFail(GesturePwdView gesturePwdView, String pwd);
	}
}
