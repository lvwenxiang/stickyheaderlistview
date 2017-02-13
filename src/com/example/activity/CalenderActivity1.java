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
	private TextView date_title_calendar;// ��ʾ���±���
	private ViewPager viewpager_calender;// ���һ����ؼ�
	private CalendarAdapter calendarAdapter;// ViewPager��������
	private DateEntity mShowDate;// ��ǰ����
	private int startPageNum = 500;// ����ǰҳ�����óɵ�500��ҳ�棬��ǰ���Ի���499�Σ�499���£���Ӧ���ڶ����Ѿ��㹻
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_record);
		initView();
	}
//	�����ǰ�ȫ�� ��@SuppressWarnings����
	@SuppressWarnings("deprecation")
	private void initView() {
		date_title_calendar = (TextView) findViewById(R.id.date_title_calendar);
		viewpager_calender = (ViewPager) findViewById(R.id.viewpager_calender);
		calendarAdapter = new CalendarAdapter(getSupportFragmentManager());
		viewpager_calender.setAdapter(calendarAdapter);// ����ViewPager������
		viewpager_calender.setCurrentItem(startPageNum);
		mShowDate = new DateEntity();// ���ڶ���
		date_title_calendar.setText(mShowDate.year + "��" + mShowDate.month
				+ "��");// ��ʾ���±���
		viewpager_calender.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				System.out.println("arg0=" + arg0);
				setShowDate(arg0);
				date_title_calendar.setText(mShowDate.year + "��"
						+ mShowDate.month + "��");// �������±���
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
	// ���ݻ���������������
	public void setShowDate(int i) {
		mShowDate = new DateEntity();
		i = i - startPageNum;
		// ���һ�
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
		// ���󻬶�
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
