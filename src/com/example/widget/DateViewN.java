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

//����
public class DateViewN extends LinearLayout {
	public static final int PAER_NUM = 500;// ����ViewPagerҳ��
	public static final String ARG_PAGE = "page";// keyֵ
	private DateEntity mShowDate;// ��ǰ����
	private ArrayList<DateEntity> entities;// ���������ݼ���
	private int width;// �п��
	private int height;// �����и߶�
	private boolean isExpansion;// �ж�˵�����Ƿ�չ��
	private LinearLayout mLinearLayoutContent = null;// ���������ؼ��ĸ�LinearLayout
	private View[] views;// ����˵����View����
	private int iRow;// �б�
	private Context context;
	private int cellWidth;// DateWidgetDayCellN���
	private int cellHeight;// DateWidgetDayCellN�߶�
	private ArrayList<DateWidgetDayCellN> mCalendarCells = new ArrayList<DateWidgetDayCellN>();// DateWidgetDayCellN����
	private ScrollView mScrollView = null;// ����mLinearLayoutContent��ʹ��ɻ���
	private ArrayList<DateEntity> currentEntities;// ��ǰ�µ����ݼ���
	private ArrayList<DateState> dateStates = null;// ˵��������չʾ�����ݼ���
	private MyGridView mygridview_item;// ˵�����е�GridView
	private DateGridViewAdapter viewAdapter;// GridView��������

	// ���캯��
	public DateViewN(Context context) {
		this(context, null);
	}

	// ���캯��
	public DateViewN(Context context, AttributeSet attrs) {

		this(context, attrs, 0);
	}

	// ���캯��
	public DateViewN(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;

		this.mShowDate = new DateEntity();// ��һ�ν������õ�ǰ����Ϊϵͳ����
		this.entities = new ArrayList<DateEntity>();
		// ������������������ViewΪLinearLayout���֣���Ϊ��ֱ���֣�����Ϊ��ɫ
		new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		// �п��Ϊ��Ļ���
		width = getResources().getDisplayMetrics().widthPixels;
		// �и�Ϊ��Ļ�߶�*27/60
		height = getResources().getDisplayMetrics().heightPixels * 27 / 60;
		// ��������Ϊ����һ��ScrollView����ӵ�View��
		mScrollView = new ScrollView(context);
		mScrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		mScrollView.setVerticalScrollBarEnabled(false);
		mScrollView.setVerticalFadingEdgeEnabled(false);
		mScrollView.setHorizontalFadingEdgeEnabled(false);
		addView(mScrollView);
		// ��������Ϊ����һ��LinearLayout����ӵ�mScrollView��
		mLinearLayoutContent = new LinearLayout(context);
		mLinearLayoutContent.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mLinearLayoutContent.setOrientation(LinearLayout.VERTICAL);
		mScrollView.addView(mLinearLayoutContent);
		cellWidth = width / 7;// ����DateWidgetDayCellN���
		cellHeight = height / 6;// ����DateWidgetDayCellN�߶�

		initRow();// ��ʼ���ּ�12X7�Ĳ���
		calculateDate();// ͨ�����ݶԳ�ʼ�����ֽ��У����ݵ�չ�֣�����ؼ������ػ��Ƴ�
	}

	// �����������
	public void setEntities(ArrayList<DateEntity> entities) {
		if (entities != null)
			this.entities = entities;
		else
			this.entities = new ArrayList<DateEntity>();
		update();
	}

	// ����ViewPagerҳ���������õ�ǰ����
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
			// ���󻬶�
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

	// ����UI
	public void update() {
		initRow();
		calculateDate();
	}

	// ��ʼ���ּ�12X7�Ĳ���
	public void initRow() {
		isExpansion = false;// Ĭ��˵����Ϊ��չ��
		mLinearLayoutContent.removeAllViews();// �Ƴ�������������Ԫ��
		mCalendarCells.clear();// ���mCalendarCells����
		views = new View[6];// ����˵����views����
		for (iRow = 0; iRow < 6; iRow++) {
			LinearLayout mLinearLayoutRow = new LinearLayout(context);
			mLinearLayoutRow.setLayoutParams(new LayoutParams(width,
					LayoutParams.WRAP_CONTENT));
			mLinearLayoutRow.setOrientation(LinearLayout.HORIZONTAL);
			for (int iDay = 0; iDay < 7; iDay++) {
				DateWidgetDayCellN dayCell = new DateWidgetDayCellN(context,
						cellWidth, cellHeight, iRow);// �½�DateWidgetDayCellN
				mCalendarCells.add(dayCell);// ���½���DateWidgetDayCellN����mCalendarCells������
				dayCell.setItemClick(new OnItemClick() {//
					// ����DateWidgetDayCellN����¼�����
					@Override
					public void OnClick(DateWidgetDayCellN item) {
						if (item.isbSelected()) {//
							// ����DateWidgetDayCellN�Ѿ���ѡ�У��������Ϊδѡ��״̬����˵���в�չʾ
							for (DateWidgetDayCellN cell : mCalendarCells) {
								cell.setSelected(false);
							}
							isExpansion = false;
						} else {//
							// ����DateWidgetDayCellNδ��ѡ�У��������Ϊѡ��״̬����˵����չʾ���������item��mCalendarCells���еĶ�Ϊδѡ��̬��
							for (DateWidgetDayCellN cell : mCalendarCells) {
								cell.setSelected(false);
							}
							item.setSelected(true);
							isExpansion = true;
						}

						String s = item.getDate();
						if (isExpansion) {// ����isExpansion״̬����ʾ������˵����
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
				mLinearLayoutRow.addView(dayCell);// ��dayCell������������
			}
			// ���ݺ���������˵����
			views[iRow] = View.inflate(context, R.layout.calender_item_new,
					null);
			views[iRow].setLayoutParams(new LayoutParams(width,
					LayoutParams.WRAP_CONTENT));
			views[iRow].setBackgroundColor(Color.GRAY);
			views[iRow].setVisibility(View.GONE);

			mLinearLayoutContent.addView(mLinearLayoutRow);// ����������ӵ���������
			mLinearLayoutContent.addView(views[iRow]);// ��˵������ӵ���������

		}
	}

	// ͨ�����ݶԳ�ʼ�����ֽ��У����ݵ�չ�֣�����ؼ������ػ��Ƴ�
	private void calculateDate() {
		if (currentEntities != null)
			currentEntities.clear();
		currentEntities = getCurrentMonth(entities);// ��ǰ������
		int currentMonthDays = getMonthDays(mShowDate.year, mShowDate.month);// ��ǰ������
		int firstDayWeek = getWeekDayFromDate(mShowDate.year, mShowDate.month);// ��ȡ��������µĵ�һ�������ڼ�0�գ�1һ��..6��
		int monthDay = getCurrentMonthDay();// �ֻ�ϵͳ��Ӧ�Ľ��������
		boolean isCurrentMonth = false;
		if (isCurrentMonth(mShowDate)) {// �Ƿ�Ϊ�ֻ�ϵͳ��Ӧ������
			isCurrentMonth = true;
		}
		int day = 0;
		boolean flag = false;
		for (int i = 0; i < mCalendarCells.size(); i++) {// ��mCalendarCells�е�DateWidgetDayCellN�������ݴ������
			flag = false;
			if (i >= firstDayWeek && i < firstDayWeek + currentMonthDays) {
				day++;// day��1-��ǰ���������
				for (DateEntity d : currentEntities) {
					if (d.day == day) {// ��currentEntities��¼�д��ڵ��������������ݴ������
						mCalendarCells.get(i).setData(d.year, d.month, d.day,
								d.isToday, true, isLeave(d), isOut(d),
								!d.isWork(), isOverWork(d), isLateOrEarly(d),
								d.week, d.getStartTime(), d.getEndTime(),
								d.getOverWorkDate(), d.getOutWorkDate(),
								d.getLeaveDate(), d.getMark());
						flag = true;
						if (d.isToday) {// �ж������Ƿ�Ϊ�ֻ�ϵͳ�еĽ��죬�����Ĭ��Ϊѡ��״̬��˵������ʾ
							mCalendarCells.get(i).setSelected(true);// Ĭ��Ϊѡ��״̬
							isExpansion = true;
							String s = mCalendarCells.get(i).getDate();
							if (isExpansion) {// ˵������ʾ
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
								closeTextVisibility();// ��������˵����
							}
						}
						break;
					}
				}
				if (!flag) {
					DateEntity date = DateEntity.modifiDayForObject(mShowDate,
							day); // ����������
					if (i == 0) {
						date.week = i;
					} else {
						date.week = i % 7;
					}// �������ڼ�
					if (isCurrentMonth && (day == monthDay)) {// �ж������Ƿ�Ϊ�ֻ�ϵͳ�еĽ��죬�����Ĭ��Ϊѡ��״̬��˵������ʾ

						mCalendarCells.get(i).setData(date.year, date.month,
								date.day, true, true, false, false, false,
								false, false, date.week, null, null, null,
								null, null, null);
						mCalendarCells.get(i).setSelected(false);// Ĭ��Ϊѡ��״̬
						isExpansion = false;
						String s = mCalendarCells.get(i).getDate();
						if (isExpansion) {// ˵������ʾ
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
							closeTextVisibility();// ��������˵����
						}
					} else {// �ж����������currentEntities��¼��û���ҵ���Ӧ������ʱ,�������ݴ���
						mCalendarCells.get(i).setData(date.year, date.month,
								date.day, false, true, false, false, false,
								false, false, date.week, null, null, null,
								null, null, null);
					}
				}
			} else if (i < firstDayWeek) {
				// ���iС��firstDayWeek����˵����һ�д�0�������գ���firstDayWeek-1λ��ǰ�µ��ϸ��£����ϸ��µ�DateWidgetDayCellN����
				// mCalendarCells.get(i).setVisibility(View.INVISIBLE);
				// �������������� ��ʾһ���µ�ͬʱ ����ʾ��һ���µ�����
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.YEAR, mShowDate.year);
				calendar.set(Calendar.MONTH, mShowDate.month - 1);
				calendar.set(Calendar.DAY_OF_MONTH, mShowDate.day);
				//���õ��µ�һ��      
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				//Ȼ���ȡ���¶�һ������ݼ�
				int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
				//����Ϊ�ϸ���ʱ��
				calendar.set(Calendar.DAY_OF_MONTH, 0);
				//��ȡ�ϸ��µ�����
				int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
				mCalendarCells.get(i).setData(calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH) + 1,
						dayInmonth - dayInWeek + 2 + i, false, false, false,
						false, false, false, false, dayInWeek - 1, null, null,
						null, null, null, null);
			} else if (i >= firstDayWeek + currentMonthDays) {// ���i >=
																// firstDayWeek
																// +
																// currentMonthDays����˵������Ŀؼ�Ϊ��ǰ�µ��¸��£������Ƴ�
				// mCalendarCells.get(i).setVisibility(View.GONE);
				//��������������  ��ʾһ���µ�ͬʱ  ����ʾ��һ���µ�����
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

	public static int getWeekDayFromDate(int year, int month) {// ��ȡ��������µĵ�һ�������ڼ�0�գ�1һ��..6��
		Calendar cal = Calendar.getInstance();
		cal.setTime(getDateFromString(year, month));
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}

	public static Date getDateFromString(int year, int month) {// ͨ�����·��ظ��µ�һ���DATE
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
	 * ͨ�����»����һ�����һ���ж�����
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
			arr[1] = 29; // ����2��29��
		}

		try {
			days = arr[month - 1];
		} catch (Exception e) {
			e.getStackTrace();
		}

		return days;
	}

	public static int getCurrentMonthDay() {// ��ǰ��
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getYear() {// ��ǰ��
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getMonth() {// ��ǰ��
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public DateEntity getDateEntity() {
		return mShowDate;
	}

	public static boolean isToday(DateEntity date) {// �Ƿ�Ϊ����
		return (date.year == getYear() && date.month == getMonth() && date.day == getCurrentMonthDay());
	}

	public static boolean isCurrentMonth(DateEntity date) {// �Ƿ�Ϊ��ǰ��
		return (date.year == getYear() && date.month == getMonth());
	}

	// ����˵��������չ��
	private void setTextVisibilityNew(int i, String date) {
		dateStates = new ArrayList<DateState>();
		for (int b = 0; b < views.length; b++) {
			if (b == i) {
				views[b].setVisibility(View.VISIBLE);
				mygridview_item = (MyGridView) views[b]
						.findViewById(R.id.mygridview_item);
				System.out.println("date=" + date);
				// 0����iDateDay:1���ڼ�weekString:2startTime:3endTime:4�Ӱ�overWorkDate:5����outWorkDate
				// :6���leaveDate:7mark:8�Ƿ����0����1��getValue(bHoliday):9�Ƿ�ٵ�����0����1��getValue(bLate)
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
					remark = "���޼�¼";
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

	// �Ƿ�ٵ�����
	private boolean isLateOrEarly(DateEntity d) {
		if (d.isEarly() || d.isLate()) {
			return true;
		}
		return false;
	}

	// �Ƿ����
	private boolean isOut(DateEntity d) {
		if (d.getOutWorkDate() != null) {
			return true;
		}
		return false;
	}

	// �Ƿ�Ӱ�
	private boolean isOverWork(DateEntity d) {
		if (d.getOverWorkDate() != null) {
			return true;
		}
		if (!d.isWork() && d.getStartTime() != null) {
			return true;
		}
		return false;
	}

	// �Ƿ����
	private boolean isLeave(DateEntity d) {
		if (d.getLeaveDate() != null) {
			return true;
		}
		return false;
	}
}
