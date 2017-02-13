package com.example.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout.LayoutParams;

import com.example.extra.MyLog;
import com.example.stickyheaderlsitview.R;



//������Ԫ��
public class DateWidgetDayCellN extends View {
	// �����С
	private int fTextSize;
	private final int margin = 6;// ѡ��ʱ�������ߵı߾�
	// ����Ԫ��
	private OnItemClick itemClick = null;

	private Paint pt = new Paint();// �������廭��
	private Paint linePaint = new Paint();// ����
	private Paint circlePaint = new Paint();// ��Բ
	private int radius;// Բ�뾶
	private RectF rect = new RectF();// ��Ԫ��
	private String sDate = "";// ��ǰ��
	private int iDateYear = 0;
	private int iDateMonth = 0;
	private int iDateDay = 0;
	private String weekString;// ���ڼ�
	private int row;// ��һҳ�����У���ǰ���ڵڼ���
	// ��������
	private boolean bSelected = false;// �Ƿ�Ϊ�����/ѡ��״̬
	private boolean bCurrentMonth = false;// �Ƿ���
	private boolean bToday = false;// �Ƿ����

	private boolean bHoliday = false;// �Ƿ����
	private boolean bLeave = false;// �Ƿ��ݼ�
	private boolean bOut = false;// �Ƿ��ݼ�
	private boolean bOverTime = false;// �Ƿ�Ӱ�
	private boolean bLate = false;// �Ƿ�ٵ�

	private String startTime;// �ϰ�ʱ��
	private String endTime;// �°�ʱ��
	private String overWorkDate;// �Ӱ�ʱ��
	private String outWorkDate;// ����ʱ��
	private String leaveDate;// ���ʱ��
	private String mark;// ����˵��
	private float mDownX;// ����λ�õ�Xֵ
	private float mDownY;// ����λ�õ�Yֵ
	private int touchSlop;// ������С����

	public boolean isbSelected() {
		return bSelected;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	// px��dipת��������,�˷����ɸ�����Ļ�ߴ粻ͬ����ͬ��dipֵת���ɲ�ͬ��pxֵ���Ѵﵽ���䲻ͬ��Ļ��Ч��
	public int dip2px(float dipValue) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	// ���캯��
	public DateWidgetDayCellN(Context context, int iWidth, int iHeight, int row) {
		super(context);
		this.row = row;
		// �����������ã�����view�ɵ������ռ�ø����ֽ���
		setClickable(true);
		setFocusable(false);
		setFocusableInTouchMode(false);

		setLayoutParams(new LayoutParams(iWidth, iHeight));// ���ÿռ��С
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();// ������С��������
		fTextSize = dip2px(15);
		radius = (Math.min(iHeight, iWidth) - dip2px(20)) / 2;// ����Բ�뾶
	}

	// ȡ����ֵ
	public String getDate() {

		StringBuffer buffer = new StringBuffer();

		buffer.append(iDateYear).append("-").append(iDateMonth).append("-")
				.append(iDateDay).append("/").append(weekString).append("/")
				.append(startTime).append("/").append(endTime).append("/")
				.append(overWorkDate).append("/").append(outWorkDate)
				.append("/").append(leaveDate).append("/").append(mark)
				.append("/").append(getValue(bHoliday)).append("/")
				.append(getValue(bLate));

		return buffer.toString();
	}

	private int getValue(boolean f) {
		if (f)
			return 1;
		return 0;
	}

	// ���ñ���ֵ
	public void setData(int iYear, int iMonth, int iDay, Boolean bToday,
			boolean bCurrentMonth, boolean bLeave, boolean bOut,
			boolean bHoliday, boolean bOverTime, boolean bLate, int week,
			String startTime, String endTime, String overWorkDate,
			String outWorkDate, String leaveDate, String mark) {
		iDateYear = iYear;
		iDateMonth = iMonth;
		iDateDay = iDay;
		this.startTime = startTime;
		this.endTime = endTime;
		this.overWorkDate = overWorkDate;
		this.outWorkDate = outWorkDate;
		this.leaveDate = leaveDate;
		this.mark = mark;

		this.sDate = Integer.toString(iDateDay);
		this.bCurrentMonth = bCurrentMonth;
		this.bToday = bToday;
		this.bLate = bLate;
		this.bLeave = bLeave;
		this.bOut = bOut;
		this.bOverTime = bOverTime;

		this.bHoliday = bHoliday;

		if (week == 0) {
			weekString = "������";
		} else if (week == 1) {
			weekString = "����һ";
		} else if (week == 2) {
			weekString = "���ڶ�";
		} else if (week == 3) {
			weekString = "������";
		} else if (week == 4) {
			weekString = "������";
		} else if (week == 5) {
			weekString = "������";
		} else if (week == 6) {
			weekString = "������";
		}
		invalidate();// ����ͼ��
	}

	// ���ػ��Ʒ���
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		rect.set(0, 0, this.getWidth(), this.getHeight());// ���õ�Ԫ���С
		// ���û�������
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(dip2px(1));
		linePaint.setColor(getResources().getColor(R.color.gray_calender_line));
		// ���Ƶ���
		if (!bSelected) {
			canvas.drawLine(0, this.getHeight(), this.getWidth(),
					this.getHeight(), linePaint);
		}

		drawDayView(canvas);// �����������񣬰�����������״̬
		drawDayNumber(canvas);// ���������е�����
	}

	// ������������
	private void drawDayView(Canvas canvas) {
		int realMargin = dip2px(margin);
		float h = this.getHeight();
		if (bSelected) {

			linePaint.setColor(getResources().getColor(R.color.grey_backgrond));// ���û���Ϊ��ɫ
			RectF oval = new RectF(realMargin, realMargin, this.getWidth()
					- realMargin, h);// ��cell������һ��ѡ�з�Χ�ڵľ���
			canvas.drawArc(oval, 180, 180, true, linePaint);// ���ƶ���Բ��
															// ���������180�ȿ�ʼ����180��,˳ʱ�����
			canvas.drawRect(dip2px(margin), (h - dip2px(margin)) / 2
					+ realMargin, this.getWidth() - realMargin, h, linePaint);// ���ײ�����
			canvas.drawRect(0, h - realMargin, realMargin, h, linePaint);// ������ߵײ�С���Σ�������ɫ
			canvas.drawRect(this.getWidth() - realMargin, h - realMargin,
					this.getWidth(), h, linePaint);// �����ұ߱ߵײ�С���Σ�������ɫ

			linePaint.setColor(Color.WHITE);// ���û���Ϊ��ɫ
			oval = new RectF(0, h - realMargin, realMargin, h);
			canvas.drawArc(oval, 0, 90, true, linePaint);// ��ߵײ�С���� ����Բ��

			canvas.drawRect(0, h - realMargin, realMargin / 2, h, linePaint);
			canvas.drawRect(0, h - realMargin, realMargin, h - realMargin / 2,
					linePaint);// ����ߵײ�С����ʣ���ɫ���ֻ��Ƴɰ�ɫ

			oval = new RectF(this.getWidth() - realMargin, h - realMargin,
					this.getWidth(), h);
			canvas.drawArc(oval, 90, 90, true, linePaint);// �ұߵײ�С���� ����Բ��

			canvas.drawRect(this.getWidth() - realMargin, h - realMargin,
					this.getWidth(), h - realMargin / 2, linePaint);
			canvas.drawRect(this.getWidth() - realMargin / 2, h - realMargin,
					this.getWidth(), h, linePaint);// ���ұߵײ�С����ʣ���ɫ���ֻ��Ƴɰ�ɫ
			linePaint.setColor(getResources().getColor(R.color.grey_backgrond));// ��ѡ��ʱ���ײ�����ɫ����Ϊѡ�б�����ɫ
			canvas.drawLine(0, this.getHeight(), this.getWidth(),
					this.getHeight(), linePaint);
		}
		circlePaint.setStyle(Paint.Style.FILL);
		circlePaint.setAntiAlias(true);
		RectF oval = new RectF(this.getWidth() / 2 - radius, this.getHeight()
				/ 2 - radius, this.getWidth() / 2 + radius, this.getHeight()
				/ 2 + radius);// ����״̬����Բ
		for (int i = 0; i < getColorBkg().size(); i++) {
			circlePaint.setColor(getColorBkg().get(i));// ����״̬�Ķ��٣����Ʋ�ͬ��ɫ�ͽǶȵ�����
			canvas.drawArc(oval, (i * 360) / getColorBkg().size(),
					360 / getColorBkg().size(), true, circlePaint);
		}

	}

	// ���������е�����
	public void drawDayNumber(Canvas canvas) {
		// draw day number

		pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		pt.setTextSize(fTextSize);
		pt.setColor(getColorText());
		pt.setUnderlineText(false);

		if (!bCurrentMonth)
			pt.setColor(Color.GRAY);

		if (bToday)
			pt.setUnderlineText(true);

		final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
				- ((int) pt.measureText(sDate) >> 1);

		final int iPosY = (int) (this.getHeight()
				- (this.getHeight() - getTextHeight()) / 2 - pt
				.getFontMetrics().bottom);

		canvas.drawText(sDate, iPosX, iPosY, pt);

		pt.setUnderlineText(false);

	}

	// �õ�����߶�
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// IInputConnectionWrapper(13298): showStatusIcon on inactive
	// InputConnection

	// �����������ز�ͬ��ɫֵ
	private ArrayList<Integer> getColorBkg() {
		ArrayList<Integer> integers = new ArrayList<Integer>();
		if (bSelected) {
			integers.add(Color.RED);
			return integers;
		}

		if (bLate)
			integers.add(Color.parseColor("#FF9595"));
		if (bOverTime) {
			integers.add(Color.parseColor("#F7AC1A"));

		}
		if (bOut)
			integers.add(Color.parseColor("#2CCCBB"));
		if (bLeave)
			integers.add(Color.parseColor("#1FB9ED"));
		if (!bLate && !bOverTime && !bOut && !bLeave)
			integers.add(Color.WHITE);
		return integers;
	}

	// �����������ز�ͬ��ɫֵ
	private int getColorText() {

		if (bLate || bOverTime || bLeave || bOut || bSelected)
			return Color.WHITE;
		if (bToday)
			return Color.RED;
		if (bHoliday)
			return Color.parseColor("#ACACAC");
		return Color.parseColor("#596569");
	}

	// �����Ƿ�ѡ��
	@Override
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
			this.invalidate();
		}
	}

	public boolean getIsCurrentMonth() {
		return bCurrentMonth;
	}

	public interface OnItemClick {// ���ü����ӿ�
		public void OnClick(DateWidgetDayCellN item);
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	public void doItemClick() {
		if (itemClick != null)
			itemClick.OnClick(this);
	}

	// ����¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = event.getX();
			mDownY = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			float disX = event.getX() - mDownX;
			float disY = event.getY() - mDownY;
			if (Math.abs(disX) < touchSlop && Math.abs(disY) < touchSlop) {
				// setSelected(true);
				doItemClick();
			}
			break;
		}
		return true;

	}
}
