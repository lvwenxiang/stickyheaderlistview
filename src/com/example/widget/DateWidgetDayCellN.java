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



//日历单元格
public class DateWidgetDayCellN extends View {
	// 字体大小
	private int fTextSize;
	private final int margin = 6;// 选中时左右两边的边距
	// 基本元素
	private OnItemClick itemClick = null;

	private Paint pt = new Paint();// 绘制字体画笔
	private Paint linePaint = new Paint();// 划线
	private Paint circlePaint = new Paint();// 画圆
	private int radius;// 圆半径
	private RectF rect = new RectF();// 单元格
	private String sDate = "";// 当前日
	private int iDateYear = 0;
	private int iDateMonth = 0;
	private int iDateDay = 0;
	private String weekString;// 星期几
	private int row;// 在一页日历中，当前日在第几行
	// 布尔变量
	private boolean bSelected = false;// 是否为被点击/选择状态
	private boolean bCurrentMonth = false;// 是否当月
	private boolean bToday = false;// 是否今天

	private boolean bHoliday = false;// 是否假期
	private boolean bLeave = false;// 是否休假
	private boolean bOut = false;// 是否休假
	private boolean bOverTime = false;// 是否加班
	private boolean bLate = false;// 是否迟到

	private String startTime;// 上班时间
	private String endTime;// 下班时间
	private String overWorkDate;// 加班时段
	private String outWorkDate;// 出差时段
	private String leaveDate;// 请假时段
	private String mark;// 考勤说明
	private float mDownX;// 按下位置的X值
	private float mDownY;// 按下位置的Y值
	private int touchSlop;// 滑动最小距离

	public boolean isbSelected() {
		return bSelected;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	// px与dip转换工具类,此方法可根据屏幕尺寸不同将相同的dip值转换成不同的px值，已达到适配不同屏幕的效果
	public int dip2px(float dipValue) {
		float scale = getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	// 构造函数
	public DateWidgetDayCellN(Context context, int iWidth, int iHeight, int row) {
		super(context);
		this.row = row;
		// 以下三行作用，设置view可点击不会占用父布局焦点
		setClickable(true);
		setFocusable(false);
		setFocusableInTouchMode(false);

		setLayoutParams(new LayoutParams(iWidth, iHeight));// 设置空间大小
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();// 设置最小滑动距离
		fTextSize = dip2px(15);
		radius = (Math.min(iHeight, iWidth) - dip2px(20)) / 2;// 设置圆半径
	}

	// 取变量值
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

	// 设置变量值
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
			weekString = "星期日";
		} else if (week == 1) {
			weekString = "星期一";
		} else if (week == 2) {
			weekString = "星期二";
		} else if (week == 3) {
			weekString = "星期三";
		} else if (week == 4) {
			weekString = "星期四";
		} else if (week == 5) {
			weekString = "星期五";
		} else if (week == 6) {
			weekString = "星期六";
		}
		invalidate();// 绘制图形
	}

	// 重载绘制方法
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		rect.set(0, 0, this.getWidth(), this.getHeight());// 设置单元格大小
		// 设置画笔属性
		linePaint.setAntiAlias(true);
		linePaint.setStrokeWidth(dip2px(1));
		linePaint.setColor(getResources().getColor(R.color.gray_calender_line));
		// 绘制底线
		if (!bSelected) {
			canvas.drawLine(0, this.getHeight(), this.getWidth(),
					this.getHeight(), linePaint);
		}

		drawDayView(canvas);// 绘制日历方格，包括各个属性状态
		drawDayNumber(canvas);// 绘制日历中的数字
	}

	// 绘制日历方格
	private void drawDayView(Canvas canvas) {
		int realMargin = dip2px(margin);
		float h = this.getHeight();
		if (bSelected) {

			linePaint.setColor(getResources().getColor(R.color.grey_backgrond));// 设置画笔为灰色
			RectF oval = new RectF(realMargin, realMargin, this.getWidth()
					- realMargin, h);// 在cell中设置一个选中范围内的矩形
			canvas.drawArc(oval, 180, 180, true, linePaint);// 绘制顶部圆弧
															// 参数意义从180度开始，画180度,顺时针绘制
			canvas.drawRect(dip2px(margin), (h - dip2px(margin)) / 2
					+ realMargin, this.getWidth() - realMargin, h, linePaint);// 画底部矩形
			canvas.drawRect(0, h - realMargin, realMargin, h, linePaint);// 绘制左边底部小矩形，背景灰色
			canvas.drawRect(this.getWidth() - realMargin, h - realMargin,
					this.getWidth(), h, linePaint);// 绘制右边边底部小矩形，背景灰色

			linePaint.setColor(Color.WHITE);// 设置画笔为白色
			oval = new RectF(0, h - realMargin, realMargin, h);
			canvas.drawArc(oval, 0, 90, true, linePaint);// 左边底部小矩形 绘制圆弧

			canvas.drawRect(0, h - realMargin, realMargin / 2, h, linePaint);
			canvas.drawRect(0, h - realMargin, realMargin, h - realMargin / 2,
					linePaint);// 将左边底部小矩形剩余灰色部分绘制成白色

			oval = new RectF(this.getWidth() - realMargin, h - realMargin,
					this.getWidth(), h);
			canvas.drawArc(oval, 90, 90, true, linePaint);// 右边底部小矩形 绘制圆弧

			canvas.drawRect(this.getWidth() - realMargin, h - realMargin,
					this.getWidth(), h - realMargin / 2, linePaint);
			canvas.drawRect(this.getWidth() - realMargin / 2, h - realMargin,
					this.getWidth(), h, linePaint);// 将右边底部小矩形剩余灰色部分绘制成白色
			linePaint.setColor(getResources().getColor(R.color.grey_backgrond));// 将选中时将底部线颜色绘制为选中背景颜色
			canvas.drawLine(0, this.getHeight(), this.getWidth(),
					this.getHeight(), linePaint);
		}
		circlePaint.setStyle(Paint.Style.FILL);
		circlePaint.setAntiAlias(true);
		RectF oval = new RectF(this.getWidth() / 2 - radius, this.getHeight()
				/ 2 - radius, this.getWidth() / 2 + radius, this.getHeight()
				/ 2 + radius);// 绘制状态背景圆
		for (int i = 0; i < getColorBkg().size(); i++) {
			circlePaint.setColor(getColorBkg().get(i));// 根据状态的多少，绘制不同颜色和角度的扇形
			canvas.drawArc(oval, (i * 360) / getColorBkg().size(),
					360 / getColorBkg().size(), true, circlePaint);
		}

	}

	// 绘制日历中的数字
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

	// 得到字体高度
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// IInputConnectionWrapper(13298): showStatusIcon on inactive
	// InputConnection

	// 根据条件返回不同颜色值
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

	// 根据条件返回不同颜色值
	private int getColorText() {

		if (bLate || bOverTime || bLeave || bOut || bSelected)
			return Color.WHITE;
		if (bToday)
			return Color.RED;
		if (bHoliday)
			return Color.parseColor("#ACACAC");
		return Color.parseColor("#596569");
	}

	// 设置是否被选中
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

	public interface OnItemClick {// 设置监听接口
		public void OnClick(DateWidgetDayCellN item);
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	public void doItemClick() {
		if (itemClick != null)
			itemClick.OnClick(this);
	}

	// 点击事件
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
