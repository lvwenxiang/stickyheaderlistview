package com.example.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stickyheaderlsitview.R;
import com.example.widget.CalendarView;
import com.example.widget.CalendarView.OnItemCalendarClickListener;


public class CalenderActivity2 extends BaseActivity {
	

	private Button  date_dialog_cancel;
	private Context mContext;
	private View view;
	private ImageButton calendarLeft;
	private ImageButton calendarRight;
	private CalendarView calendarView;
	private TextView calendarCenter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViewCalendar();
		mContext = this;
	}
	
	
	@SuppressLint("SimpleDateFormat")
	private void initViewCalendar() {
		final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.show();
		view = View.inflate(this, R.layout.date_msg_dialog, null);
		alertDialog.getWindow().setContentView(view);
		calendarLeft = (ImageButton) view.findViewById(R.id.calendarLeft);
		calendarRight = (ImageButton) view.findViewById(R.id.calendarRight);
		calendarView = (CalendarView) view.findViewById(R.id.calendar);
		calendarCenter = (TextView) view.findViewById(R.id.calendarCenter);
		date_dialog_cancel = (Button) view
				.findViewById(R.id.date_dialog_cancel);

		// ��ȡ���������� ya[0]Ϊ�꣬ya[1]Ϊ�£���ʽ��ҿ��������������ؼ��иģ�
		String[] ya = calendarView.getYearAndmonth().split("-");
		calendarCenter.setText(ya[0] + "��" + ya[1]);
		date_dialog_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.dismiss();
				finish();
			}
		});
		calendarLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String leftYearAndmonth = calendarView.clickLeftMonth();
				String[] lya = leftYearAndmonth.split("-");
				calendarCenter.setText(lya[0] + "��" + lya[1]);
			}
		});
		calendarRight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String rightYearAndmonth = calendarView.clickRightMonth();
				String[] rya = rightYearAndmonth.split("-");
				calendarCenter.setText(rya[0] + "��" + rya[1]);
			}
		});

		// ���ÿؼ����������Լ����������ÿһ�죨���Ҳ�����ڿؼ��������趨��
		calendarView
				.setOnItemCalendarClickListener(new OnItemCalendarClickListener() {

					@Override
					public void OnItemClick(Date date) {
						// TODO Auto-generated method stub
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"yyyy-MM-dd");
						Toast.makeText(mContext, dateFormat.format(date), 2).show();
						alertDialog.dismiss();
					}
				});

	}
}
