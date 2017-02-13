package com.example.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;

import com.example.bean.WeekRankingbean;
import com.example.mode.ModelUtil;
import com.example.stickyheaderlsitview.R;
import com.example.widget.ColumnarView;

public class MyRankingActivity extends Activity {
	
	private ColumnarView columnarview;
	private ArrayList<WeekRankingbean> weekrankingbeanlist;
	private ArrayList<WeekRankingbean> showweekrankingbeanlist;
	private int max;
	private boolean start;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);
		columnarview=(ColumnarView) findViewById(R.id.columnar);
		
		weekrankingbeanlist=(ArrayList<WeekRankingbean>) ModelUtil.getWeekRanking();
		if (weekrankingbeanlist != null || weekrankingbeanlist.size() > 0) {
			if (weekrankingbeanlist.size() <= 4) {
				showweekrankingbeanlist = weekrankingbeanlist;
			} else {
				showweekrankingbeanlist.addAll(weekrankingbeanlist);
//				showweekrankingbeanlist = new ArrayList<WeekRankingbean>();
//				showweekrankingbeanlist.add(weekrankingbeanlist.get(0));
//				showweekrankingbeanlist.add(weekrankingbeanlist.get(1));
//				showweekrankingbeanlist.add(weekrankingbeanlist.get(2));
//				showweekrankingbeanlist.add(weekrankingbeanlist.get(3));
			}
			for (int i = 0; i < showweekrankingbeanlist.size(); i++) {

				if (max < showweekrankingbeanlist.get(i).getWEEK_ORDER())
					max = showweekrankingbeanlist.get(i).getWEEK_ORDER();
			}
			columnarview.setDate(showweekrankingbeanlist,
					Float.parseFloat(getMax(max + "")));
			if (!start) {
				columnarview.start(true);
				start = true;
			} else {
				columnarview.start(false);
			}
		}
	}
	private String getMax(String m) {
		String result = null;
		if (m.length() == 1) {
			result = 10 + "";
		} else {
			result = m.substring(0, 1);
			result = getM(Integer.parseInt(result)) + "";
			for (int i = 0; i < m.length() - 1; i++) {
				result = result + "0";
			}
		}
		return result;
	}

	private int getM(int i) {
		int result = 0;
		if (i == 1) {
			result = 2;
		} else if (i == 2) {
			result = 3;
		} else if (i == 3) {
			result = 4;
		} else if (i == 4) {
			result = 5;
		} else if (i == 5) {
			result = 6;
		} else if (i == 6) {
			result = 7;
		} else if (i == 7) {
			result = 8;
		} else if (i == 8) {
			result = 9;
		} else if (i == 9) {
			result = 10;
		}
		return result;
	}
}
