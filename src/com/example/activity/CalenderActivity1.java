/**
 * 
 */
package com.example.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.TextView;

import com.example.adapter.CalendarAdapter;
import com.example.bean.DateEntity;
import com.example.stickyheaderlsitview.R;

/**
 * @author wenxiang.lv
 *
 */
public class CalenderActivity1 extends BaseActivity {
	private TextView date_title_calendar;// 显示年月标题
	private ViewPager viewpager_calender;// 左右滑动控件
	private CalendarAdapter calendarAdapter;// ViewPager的适配器
	private DateEntity mShowDate;// 当前年月
	private int startPageNum = 500;// 将当前页面设置成第500个页面，往前可以滑动499次，499个月，对应考勤而言已经足够
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_record);
		initView();
	}
//	警告是安全的 用@SuppressWarnings声明
	@SuppressWarnings("deprecation")
	private void initView() {
		date_title_calendar = (TextView) findViewById(R.id.date_title_calendar);
		viewpager_calender = (ViewPager) findViewById(R.id.viewpager_calender);
		calendarAdapter = new CalendarAdapter(getSupportFragmentManager());
		viewpager_calender.setAdapter(calendarAdapter);// 设置ViewPager适配器
		viewpager_calender.setCurrentItem(startPageNum);
		mShowDate = new DateEntity();// 日期对象
		date_title_calendar.setText(mShowDate.year + "年" + mShowDate.month
				+ "月");// 显示年月标题
		viewpager_calender.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				System.out.println("arg0=" + arg0);
				setShowDate(arg0);
				date_title_calendar.setText(mShowDate.year + "年"
						+ mShowDate.month + "月");// 更新年月标题
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}
	// 根据滑动方向配置年月
	public void setShowDate(int i) {
		mShowDate = new DateEntity();
		i = i - startPageNum;
		// 向右滑
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
		// 向左滑动
		if (i < 0) {
			for (int j = 0; j < Math.abs(i); i++) {
				if (mShowDate.month == 1) {
					mShowDate.month = 12;
					mShowDate.year -= 1;
				} else {
					mShowDate.month -= 1;
				}
			}
		}
	}
}
