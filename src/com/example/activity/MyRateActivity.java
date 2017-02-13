package com.example.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.example.bean.MyRatebean;
import com.example.mode.ModelUtil;
import com.example.stickyheaderlsitview.R;
import com.example.widget.PieSelfView;

public class MyRateActivity extends Activity {
	
	private ArrayList<MyRatebean> myRatebeanlist;
	private PieSelfView pieview;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rate);
		pieview=(PieSelfView) findViewById(R.id.pie_month);
		myRatebeanlist= ModelUtil.getMyRatebean();
		pieview.setRates(myRatebeanlist);
		pieview.startAnim();
	}
}
