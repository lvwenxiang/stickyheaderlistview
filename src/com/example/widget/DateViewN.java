package com.example.widget;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.adapter.DateGridViewAdapter;
import com.example.bean.DateEntity;
import com.example.bean.DateState;
import com.example.stickyheaderlsitview.R;
import com.example.widget.DateWidgetDayCellN.OnItemClick;

//日历
public class DateViewN extends LinearLayout {
	public static final int PAER_NUM = 500;// 设置ViewPager页数
	public static final String ARG_PAGE = "page";// key值
	private DateEntity mShowDate;// 当前年月
	private ArrayList<DateEntity> entities;// 所传入数据集合
	private int width;// 行宽度
	private int height;// 日历行高度
	private boolean isExpansion;// 判断说明行是否展开
	private LinearLayout mLinearLayoutContent = null;// 整个日历控件的父LinearLayout
	private View[] views;// 考勤说明行View数组
	private int iRow;// 行标
	private Context context;
	private int cellWidth;// DateWidgetDayCellN宽度
	private int cellHeight;// DateWidgetDayCellN高度
	private ArrayList<DateWidgetDayCellN> mCalendarCells = new ArrayList<DateWidgetDayCellN>();// DateWidgetDayCellN集合
	private ScrollView mScrollView = null;// 包含mLinearLayoutContent，使其可滑动
	private ArrayList<DateEntity> currentEntities;// 当前月的数据集合
	private ArrayList<DateState> dateStates = null;// 说明行中所展示的数据集合
	private MyGridView mygridview_item;// 说明行中的GridView
	private DateGridViewAdapter viewAdapter;// GridView的适配器

	// 构造函数
	public DateViewN(Context context) {
		this(context, null);
	}

	// 构造函数
	public DateViewN(Context context, AttributeSet attrs) {

		this(context, attrs, 0);
	}

	// 构造函数
	public DateViewN(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		this.mShowDate = new DateEntity();// 第一次进入设置当前日期为系统日期
		this.entities = new ArrayList<DateEntity>();
		// 以下三行是设置整个View为LinearLayout布局，且为垂直布局，背景为白色
		new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		// 行宽度为屏幕宽度
		width = getResources().getDisplayMetrics().widthPixels;
		// 行高为屏幕高度*27/60
		height = getResources().getDisplayMetrics().heightPixels * 27 / 60;
		// 以下六行为创建一个ScrollView并添加到View中
		mScrollView = new ScrollView(context);
		mScrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mScrollView.setVerticalScrollBarEnabled(false);
		mScrollView.setVerticalFadingEdgeEnabled(false);
		mScrollView.setHorizontalFadingEdgeEnabled(false);
		addView(mScrollView);
		// 以下四行为创建一个LinearLayout并添加到mScrollView中
		mLinearLayoutContent = new LinearLayout(context);
		mLinearLayoutContent.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mLinearLayoutContent.setOrientation(LinearLayout.VERTICAL);
		mScrollView.addView(mLinearLayoutContent);
		cellWidth = width / 7;// 单个DateWidgetDayCellN宽度
		cellHeight = height / 6;// 单个DateWidgetDayCellN高度

		initRow();// 初始布局即12X7的布局
		calculateDate();// 通过数据对初始化布局进行，数据的展现，多余控件的隐藏或移除
	}

	// 设置数据入口
	public void setEntities(ArrayList<DateEntity> entities) {
		if (entities != null)
			this.entities = entities;
		else
			this.entities = new ArrayList<DateEntity>();
		update();
	}

	// 根据ViewPager页面编号来设置当前日期
	public void setShowDate(int i) {
		i = i - PAER_NUM;
		if (i > 0) {

			for (int j = 0; j < i; j++) {
				if (mShowDate.month == 12) {
					mShowDate.month = 1;
					mShowDate.year += 1;
				} else {
					mShowDate.month += 1;
				}
			}

		}
		if (i < 0) {
			// 向左滑动
			for (int j = 0; j < Math.abs(i); i++) {
				if (mShowDate.month == 1) {
					mShowDate.month = 12;
					mShowDate.year -= 1;
				} else {
					mShowDate.month -= 1;
				}
			}
		}
		update();
	}

	// 更新UI
	public void update() {
		initRow();
		calculateDate();
	}

	// 初始布局即12X7的布局
	public void initRow() {
		isExpansion = false;// 默认说明行为不展开
		mLinearLayoutContent.removeAllViews();// 移除父布局中所有元素
		mCalendarCells.clear();// 清除mCalendarCells集合
		views = new View[6];// 设置说明行views数组
		for (iRow = 0; iRow < 6; iRow++) {
			LinearLayout mLinearLayoutRow = new LinearLayout(context);
			mLinearLayoutRow.setLayoutParams(new LayoutParams(width,
					LayoutParams.WRAP_CONTENT));
			mLinearLayoutRow.setOrientation(LinearLayout.HORIZONTAL);
			for (int iDay = 0; iDay < 7; iDay++) {
				DateWidgetDayCellN dayCell = new DateWidgetDayCellN(context,
						cellWidth, cellHeight, iRow);// 新建DateWidgetDayCellN
				mCalendarCells.add(dayCell);// 将新建的DateWidgetDayCellN放入mCalendarCells集合中
				dayCell.setItemClick(new OnItemClick() {//
					// 设置DateWidgetDayCellN点击事件监听
					@Override
					public void OnClick(DateWidgetDayCellN item) {
						if (item.isbSelected()) {//
							// 若被DateWidgetDayCellN已经被选中，点击后则为未选中状态，且说明行不展示
							for (DateWidgetDayCellN cell : mCalendarCells) {
								cell.setSelected(false);
							}
							isExpansion = false;
						} else {//
							// 若被DateWidgetDayCellN未被选中，点击后则为选中状态，且说明行展示，除了这个item，mCalendarCells所有的都为未选中态，
							for (DateWidgetDayCellN cell : mCalendarCells) {
								cell.setSelected(false);
							}
							item.setSelected(true);
							isExpansion = true;
						}

						String s = item.getDate();
						if (isExpansion) {// 根据isExpansion状态来显示或隐藏说明行
							if (item.isbSelected()) {
								switch (item.getRow()) {
								case 0:
									setTextVisibilityNew(0, s);
									break;
								case 1:
									setTextVisibilityNew(1, s);
									break;
								case 2:
									setTextVisibilityNew(2, s);
									break;
								case 3:
									setTextVisibilityNew(3, s);
									break;
								case 4:
									setTextVisibilityNew(4, s);
									break;
								case 5:
									setTextVisibilityNew(5, s);
									break;
								}
							}
						} else {
							closeTextVisibility();
						}
					}
				});
				mLinearLayoutRow.addView(dayCell);// 将dayCell加入日历行中
			}
			// 根据航标来设置说明行
			views[iRow] = View.inflate(context, R.layout.calender_item_new,
					null);
			views[iRow].setLayoutParams(new LayoutParams(width,
					LayoutParams.WRAP_CONTENT));
			views[iRow].setBackgroundColor(Color.GRAY);
			views[iRow].setVisibility(View.GONE);

			mLinearLayoutContent.addView(mLinearLayoutRow);// 将日历行添加到父布局中
			mLinearLayoutContent.addView(views[iRow]);// 将说明行添加到父布局中

		}
	}

	// 通过数据对初始化布局进行，数据的展现，多余控件的隐藏或移除
	private void calculateDate() {
		if (currentEntities != null)
			currentEntities.clear();
		currentEntities = getCurrentMonth(entities);// 当前月数据
		int currentMonthDays = getMonthDays(mShowDate.year, mShowDate.month);// 当前月天数
		int firstDayWeek = getWeekDayFromDate(mShowDate.year, mShowDate.month);// 获取随意的年月的第一天是星期几0日，1一，..6六
		int monthDay = getCurrentMonthDay();// 手机系统对应的今天的日期
		boolean isCurrentMonth = false;
		if (isCurrentMonth(mShowDate)) {// 是否为手机系统对应的年月
			isCurrentMonth = true;
		}
		int day = 0;
		boolean flag = false;
		for (int i = 0; i < mCalendarCells.size(); i++) {// 对mCalendarCells中的DateWidgetDayCellN进行数据传入操作
			flag = false;
			if (i >= firstDayWeek && i < firstDayWeek + currentMonthDays) {
				day++;// day从1-当前月天结束；
				for (DateEntity d : currentEntities) {
					if (d.day == day) {// 对currentEntities记录中存在的天数，进行数据传入操作
						mCalendarCells.get(i).setData(d.year, d.month, d.day,
								d.isToday, true, isLeave(d), isOut(d),
								!d.isWork(), isOverWork(d), isLateOrEarly(d),
								d.week, d.getStartTime(), d.getEndTime(),
								d.getOverWorkDate(), d.getOutWorkDate(),
								d.getLeaveDate(), d.getMark());
						flag = true;
						if (d.isToday) {// 判断日期是否为手机系统中的今天，如果是默认为选中状态，说明行显示
							mCalendarCells.get(i).setSelected(true);// 默认为选中状态
							isExpansion = true;
							String s = mCalendarCells.get(i).getDate();
							if (isExpansion) {// 说明行显示
								if (mCalendarCells.get(i).isbSelected()) {
									switch (mCalendarCells.get(i).getRow()) {
									case 0:
										setTextVisibilityNew(0, s);
										break;
									case 1:
										setTextVisibilityNew(1, s);
										break;
									case 2:
										setTextVisibilityNew(2, s);
										break;
									case 3:
										setTextVisibilityNew(3, s);
										break;
									case 4:
										setTextVisibilityNew(4, s);
										break;
									case 5:
										setTextVisibilityNew(5, s);
										break;
									}
								}
							} else {
								closeTextVisibility();// 屏蔽所有说明行
							}
						}
						break;
					}
				}
				if (!flag) {
					DateEntity date = DateEntity.modifiDayForObject(mShowDate,
							day); // 设置年月日
					if (i == 0) {
						date.week = i;
					} else {
						date.week = i % 7;
					}// 设置星期几
					if (isCurrentMonth && (day == monthDay)) {// 判断日期是否为手机系统中的今天，如果是默认为选中状态，说明行显示

						mCalendarCells.get(i).setData(date.year, date.month,
								date.day, true, true, false, false, false,
								false, false, date.week, null, null, null,
								null, null, null);
						mCalendarCells.get(i).setSelected(false);// 默认为选中状态
						isExpansion = false;
						String s = mCalendarCells.get(i).getDate();
						if (isExpansion) {// 说明行显示
							if (mCalendarCells.get(i).isbSelected()) {
								switch (mCalendarCells.get(i).getRow()) {
								case 0:
									setTextVisibilityNew(0, s);
									break;
								case 1:
									setTextVisibilityNew(1, s);
									break;
								case 2:
									setTextVisibilityNew(2, s);
									break;
								case 3:
									setTextVisibilityNew(3, s);
									break;
								case 4:
									setTextVisibilityNew(4, s);
									break;
								case 5:
									setTextVisibilityNew(5, s);
									break;
								}
							}
						} else {
							closeTextVisibility();// 屏蔽所有说明行
						}
					} else {// 判断如果日期在currentEntities记录中没有找到对应的数据时,进行数据传入
						mCalendarCells.get(i).setData(date.year, date.month,
								date.day, false, true, false, false, false,
								false, false, date.week, null, null, null,
								null, null, null);
					}
				}
			} else if (i < firstDayWeek) {
				// 如果i小于firstDayWeek，则说明第一行从0（星期日）到firstDayWeek-1位当前月的上个月，将上个月的DateWidgetDayCellN隐藏
				// mCalendarCells.get(i).setVisibility(View.INVISIBLE);
				// 类似日历弹出框 显示一个月的同时 会显示上一个月的日子
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, mShowDate.year);
				calendar.set(Calendar.MONTH, mShowDate.month - 1);
				calendar.set(Calendar.DAY_OF_MONTH, mShowDate.day);
				//设置当月第一天      
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				//然后获取当月读一天是礼拜几
				int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
				//设置为上个月时间
				calendar.set(Calendar.DAY_OF_MONTH, 0);
				//获取上个月的天数
				int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
				mCalendarCells.get(i).setData(calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH) + 1,
						dayInmonth - dayInWeek + 2 + i, false, false, false,
						false, false, false, false, dayInWeek - 1, null, null,
						null, null, null, null);
			} else if (i >= firstDayWeek + currentMonthDays) {// 如果i >=
																// firstDayWeek
																// +
																// currentMonthDays，这说明后面的控件为当前月的下个月，可以移除
				// mCalendarCells.get(i).setVisibility(View.GONE);
				//类似日历弹出框  显示一个月的同时  会显示上一个月的日子
				int week;
				if ((firstDayWeek + (i - firstDayWeek - currentMonthDays + 1)) == 0) {
					week = (firstDayWeek + (i - firstDayWeek - currentMonthDays + 1));
				} else {
					week = (firstDayWeek + (i - firstDayWeek - currentMonthDays + 1)) % 7;
				}
				mCalendarCells.get(i).setData(mShowDate.year,
						mShowDate.month + 1,
						i - firstDayWeek - currentMonthDays + 1, false, false,
						false, false, false, false, false, week, null, null,
						null, null, null, null);
			}
		}
	}

	private ArrayList<DateEntity> getCurrentMonth(ArrayList<DateEntity> list) {
		ArrayList<DateEntity> exists = new ArrayList<DateEntity>();
		for (DateEntity d : list) {
			if (d.month == mShowDate.month && d.year == mShowDate.year) {
				exists.add(d);
			}
		}
		return exists;
	}

	public static int getWeekDayFromDate(int year, int month) {// 获取随意的年月的第一天是星期几0日，1一，..6六
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(year, month));
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	public static Date getDateFromString(int year, int month) {// 通过年月返回该月第一天的DATE
		String dateString = year + "-" + (month > 9 ? month : ("0" + month))
				+ "-01";
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 通过年月获得这一年的这一月有多少天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
		if (month > 12) {
			month = 1;
			year += 1;
		} else if (month < 1) {
			month = 12;
			year -= 1;
		}
		int[] arr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int days = 0;

		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
			arr[1] = 29; // 闰年2月29天
		}

		try {
			days = arr[month - 1];
		} catch (Exception e) {
			e.getStackTrace();
		}

		return days;
	}

	public static int getCurrentMonthDay() {// 当前日
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getYear() {// 当前年
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {// 当前月
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public DateEntity getDateEntity() {
		return mShowDate;
	}

	public static boolean isToday(DateEntity date) {// 是否为今天
		return (date.year == getYear() && date.month == getMonth() && date.day == getCurrentMonthDay());
	}

	public static boolean isCurrentMonth(DateEntity date) {// 是否为当前月
		return (date.year == getYear() && date.month == getMonth());
	}

	// 设置说明行数据展现
	private void setTextVisibilityNew(int i, String date) {
		dateStates = new ArrayList<DateState>();
		for (int b = 0; b < views.length; b++) {
			if (b == i) {
				views[b].setVisibility(View.VISIBLE);
				mygridview_item = (MyGridView) views[b]
						.findViewById(R.id.mygridview_item);
				System.out.println("date=" + date);
				// 0日期iDateDay:1星期几weekString:2startTime:3endTime:4加班overWorkDate:5出差outWorkDate
				// :6请假leaveDate:7mark:8是否假期0不是1是getValue(bHoliday):9是否迟到早退0不是1是getValue(bLate)
				String s[] = date.split("/");
				boolean isWork = false;
				boolean isOverWork = false;
				boolean isOutWork = false;
				boolean isLeaveDate = false;
				boolean isLate = false;
				String work = null;
				String OverWork = null;
				String OutWork = null;
				String LeaveDate = null;
				String Late = null;
				String remark = null;

				if ("".equals(s[7]) || "null".equals(s[7])) {
					remark = "暂无记录";
				} else {
					remark = s[7];
				}
				if ("1".equals(s[8])) {
					if ("null".equals(s[2]) && "null".equals(s[5])) {

					} else if ((!"null".equals(s[2])) && "null".equals(s[5])) {
						isOverWork = true;
					} else if (("null".equals(s[2])) && (!"null".equals(s[5]))) {
						isOutWork = true;
					} else {
						isOverWork = true;
						isOutWork = true;
					}
					OverWork = s[2] + "-" + s[3];
					OutWork = s[5];

				} else {
					if (!"null".equals(s[2])) {
						if ("1".equals(s[9])) {
							isLate = true;
							Late = s[2] + "-" + s[3];
						} else {
							isWork = true;
							work = s[2] + "-" + s[3];
						}
					}
					if (!"null".equals(s[4])) {
						isOverWork = true;
						OverWork = s[4];
					}
					if (!"null".equals(s[5])) {
						isOutWork = true;
						OutWork = s[5];
					}
					if (!"null".equals(s[6])) {
						isLeaveDate = true;
						LeaveDate = s[6];
					}
				}
				DateState d = null;
				if (isLate) {
					d = new DateState(Late, "late");
					dateStates.add(d);
				}
				if (isWork) {
					d = new DateState(work, "work");
					dateStates.add(d);
				}
				if (isLeaveDate) {
					d = new DateState(LeaveDate, "vacation");
					dateStates.add(d);
				}
				if (isOutWork) {
					d = new DateState(OutWork, "out");
					dateStates.add(d);
				}
				if (isOverWork) {
					d = new DateState(OverWork, "over");
					dateStates.add(d);
				}
				d = new DateState(remark, "remark");
				dateStates.add(d);
				viewAdapter = new DateGridViewAdapter(context, dateStates);
				mygridview_item.setAdapter(viewAdapter);
			} else {
				views[b].setVisibility(View.GONE);
			}
		}
	}

	public void closeTextVisibility() {
		for (int b = 0; b < views.length; b++) {
			views[b].setVisibility(View.GONE);
		}
	}

	// 是否迟到早退
	private boolean isLateOrEarly(DateEntity d) {
		if (d.isEarly() || d.isLate()) {
			return true;
		}
		return false;
	}

	// 是否出差
	private boolean isOut(DateEntity d) {
		if (d.getOutWorkDate() != null) {
			return true;
		}
		return false;
	}

	// 是否加班
	private boolean isOverWork(DateEntity d) {
		if (d.getOverWorkDate() != null) {
			return true;
		}
		if (!d.isWork() && d.getStartTime() != null) {
			return true;
		}
		return false;
	}

	// 是否请假
	private boolean isLeave(DateEntity d) {
		if (d.getLeaveDate() != null) {
			return true;
		}
		return false;
	}
}
