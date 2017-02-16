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
 * �Զ�����������
 * 
 * @author wenxiang.lv
 * 
 */
public class GesturePwdView extends View {
	private Paint linePaint;// ���߱�
	private Paint circlePaint;// ��Բ��
	private int[][] data;// �������ݾ���
	private PointF[][] centerCxCy;// Բ�ľ���
	private boolean[][] selected;// ��ѡ�㣬֮������ѡ
	private final int flag = 3;// �����С
	private int viewWidth;// �ؼ���
	private int viewHeight;// �ؼ���
	private int radius;// �뾶
	private int red;
	private int green;
	private int blue;
	private boolean isPressedDown = false;// �Ƿ�����ָ����
	private List<PointF> selPointList; // ѡ�е�Բ�е㼯��
	private boolean isError = false;
	private int startX;//
	private int startY;
	private int endX;
	private int endY;
	private Thread errorThread;// ��������������ʱ�߳�
	private boolean isGestureOpen = true;
	private String lockPwd = "";// ���������ַ���
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
	
	// ��ԭΪ��ʼ״̬
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

	// ����ͼ��
	private void setDraw(int red, int green, int blue, Canvas canvas) {
		for (int i = 0; i < selected[0].length; i++) {
			for (int j = 0; j < selected[0].length; j++) {
				PointF center = centerCxCy[i][j];
				if (selected[i][j]) { // �Ƿ�ѡ��
					circlePaint.setColor(Color.rgb(red, green, blue)); // ������Բ��ʱ���û�ɫ����
					linePaint.setColor(Color.argb(255, red, green, blue));
					canvas.drawCircle(center.x, center.y, radius / 3, linePaint); // ѡ��ʱ��ʵ����Բ
				} else {
					circlePaint.setColor(Color.argb(180, 255, 255, 255)); // �������ûҰ�ɫ����
				}
				canvas.drawCircle(center.x, center.y, radius, circlePaint); // ����Բ
			}
		}
		linePaint.setColor(Color.argb(96, red, green, blue));
		if (isPressedDown) {
			for (int i = 0; i < selPointList.size() - 1; i++) { // ����ѡ��Բ֮���·��
				PointF preCenter = selPointList.get(i); // ǰһ��Բ�е�
				PointF curCenter = selPointList.get(i + 1); // ����Բ�е�
				canvas.drawLine(preCenter.x, preCenter.y, curCenter.x,
						curCenter.y, linePaint);
			}
			if (!isError) {
				if (selPointList.size() > 0) {
					PointF center = selPointList.get(selPointList.size() - 1); // ���һ��ѡ��Բ�е�
					canvas.drawLine(center.x, center.y, endX, endY, linePaint);
				}
			}
		}

	}

	// ���ð뾶��Բ��
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
		linePaint.setStrokeWidth(5);// ���߱ʿ��
		linePaint.setStyle(Style.FILL);// ������ʽ����
		linePaint.setAntiAlias(true);// �����

		circlePaint = new Paint();
		circlePaint.setStrokeWidth(DensityUtil.dip2px(getContext(), 1));
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.STROKE); // ������ʽ����

		centerCxCy = new PointF[flag][flag];
		data = new int[flag][flag];
		selected = new boolean[flag][flag];
		selPointList = new ArrayList<PointF>();

		initData();
	}

	// ��ֵdata����
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

	// ����ԭ��������
	public void setPassword(String password) {
		this.password = password;
	}	
	// ���ѡ�м�¼
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
				if (isInCircle(center, x, y)) { // �ж��Ƿ���Բ��
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
	
	// �ж��Ƿ���ĳ��Բ��
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
		// ������ı�
		public void onChangePin(GesturePwdView gesturePwdView, String pwd);

		// �Ƚ�����ɹ�
		public void onComparePinSuccess(GesturePwdView gesturePwdView,
				String pwd);

		// �Ƚ�����ʧ��
		public void onComparePinFail(GesturePwdView gesturePwdView, String pwd);
	}
}
