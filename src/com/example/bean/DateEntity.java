package com.example.bean;

import java.io.Serializable;
import java.util.Calendar;

public class DateEntity implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 7437013172373144599L;
	private String Date;// 日期 2016-11-25
	private String startTime;// 上班时间 08:00
	private String endTime;// 下班时间 20:00
	private String overWorkDate;// 加班时段 18:00-20:00
	private String outWorkDate;// 出差 16:00-17:00
	private String leaveDate;// 请假时段 12:00-15:00
	private String mark;// 考勤说明
	public int year;// 日期分解年 2016
	public int month;// 日期分解月 11
	public int day;// 日期分解日 25
	public int week;// 日期分解星期几
	public boolean isToday;// 当前日期是否为今天
	private boolean isLate;// 是否迟到
	private boolean isWork;// 是否工作日
	private boolean isEarly;// 是否早退
	private boolean isNormal;// 状态是否正常

	public boolean isLate() {
		return isLate;
	}

	public void setLate(boolean isLate) {
		this.isLate = isLate;
	}

	public boolean isWork() {
		return isWork;
	}

	public void setWork(boolean isWork) {
		this.isWork = isWork;
	}

	public boolean isEarly() {
		return isEarly;
	}

	public void setEarly(boolean isEarly) {
		this.isEarly = isEarly;
	}

	public boolean isNormal() {
		return isNormal;
	}

	public void setNormal(boolean isNormal) {
		this.isNormal = isNormal;
	}

	public String getOverWorkDate() {
		return overWorkDate;
	}

	public void setOverWorkDate(String overWorkDate) {
		this.overWorkDate = overWorkDate;
	}

	public String getOutWorkDate() {
		return outWorkDate;
	}

	public void setOutWorkDate(String outWorkDate) {
		this.outWorkDate = outWorkDate;
	}

	public String getLeaveDate() {
		return leaveDate;
	}

	public void setLeaveDate(String leaveDate) {
		this.leaveDate = leaveDate;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public boolean isToday() {
		return isToday;
	}

	public void setToday(boolean isToday) {
		this.isToday = isToday;
	}

	public DateEntity(int year, int month, int day) {
		if (month > 12) {
			month = 1;
			year++;
		} else if (month < 1) {
			month = 12;
			year--;
		}
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public DateEntity() {
		this.year = Calendar.getInstance().get(Calendar.YEAR);
		this.month = Calendar.getInstance().get(Calendar.MONTH) + 1; //Calendar得到的默认是0表示一月
		this.day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static DateEntity modifiDayForObject(DateEntity date, int day) {
		DateEntity modifiDate = new DateEntity(date.year, date.month, day);
		return modifiDate;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	@Override
	public String toString() {
		return "DateEntity [Date=" + Date + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", overWorkDate=" + overWorkDate
				+ ", outWorkDate=" + outWorkDate + ", leaveDate=" + leaveDate
				+ ", mark=" + mark + ", year=" + year + ", month=" + month
				+ ", day=" + day + ", week=" + week + ", isToday=" + isToday
				+ ", isLate=" + isLate + ", isWork=" + isWork + ", isEarly="
				+ isEarly + ", isNormal=" + isNormal + "]";
	}
}
