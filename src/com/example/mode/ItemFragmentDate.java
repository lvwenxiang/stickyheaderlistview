package com.example.mode;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bean.DateEntity;
import com.example.stickyheaderlsitview.R;
import com.example.widget.DateViewN;


//考勤日历页面
public class ItemFragmentDate  extends Fragment {
	private View rootView;
	public static final String ARG_PAGE = "page";// key值
	private int mPageNumber;// 当前页码
	private DateViewN dateViewN;// 日历控件
	// 创建ItemFragmentDate通过ViewPager的页面编号
	private String currentMonth;// 当前月
	public MyHandler mHandler;// handler,用于数据更新
	private ArrayList<DateEntity> dateEntities;// 考勤数据集合
	private LinearLayout ll_progress;// 展示刷新进度LinearLayout
	private Button btn_dateview;// 刷新数据按钮
	private ProgressBar progressBar1;// 展示刷新进度圆圈，在ll_progress中
	private TextView tv_refresh_now;// 展示刷新进度文字，在ll_progress中
	
	
	// 自定义MyHandler类继承Handler，防止内存泄漏
	private static class MyHandler extends Handler {
		private final WeakReference<Context> mActivity;
		private onTaskListener listener;

		public MyHandler(Context activity, onTaskListener listener) {
			mActivity = new WeakReference<Context>(activity);
			this.listener = listener;
		}

		public interface onTaskListener {
			public void task(android.os.Message msg);
		}

		@Override
		public void handleMessage(android.os.Message msg) {
			System.out.println(msg);
			if (mActivity.get() == null) {
				System.out.println("6666666666=======================");
				return;
			}
			if (listener != null) {
				listener.task(msg);
			}
		}
	}
	public static Fragment create(int pageNumber) {
		ItemFragmentDate fragment = new ItemFragmentDate();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new MyHandler(getActivity(), new MyHandler.onTaskListener() {
			@Override
			public void task(android.os.Message msg) {
				if (getActivity() != null) {
					
					NameValuePair pair = (NameValuePair) msg.obj;
					if ("2017-02".equals(pair.getName())) {
						setData();
						if (dateEntities != null && dateEntities.size() > 0) {
							dateViewN.setEntities(dateEntities);// 将数据集合放入自定义日历控件中
							btn_dateview.setVisibility(View.GONE);
							ll_progress.setVisibility(View.GONE);
						} else {
							btn_dateview.setVisibility(View.GONE);
							ll_progress.setVisibility(View.VISIBLE);
							progressBar1.setVisibility(View.GONE);
							tv_refresh_now.setText("本月无考勤数据");
						}

					} else {
						btn_dateview.setVisibility(View.VISIBLE);
						ll_progress.setVisibility(View.GONE);
					}
				}
			}
			
		});
		mPageNumber = getArguments().getInt(ARG_PAGE);// 获取当前页面编号
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = View.inflate(getActivity(),
				R.layout.fragment_viewpager_date, null);
		dateViewN = (DateViewN) rootView.findViewById(R.id.dateview);// 初始日历控件
		dateViewN.setShowDate(mPageNumber);//设置当前页面对应的自定义日历控件中的日期
		
		currentMonth = dateViewN.getDateEntity().year + "-"
				+ getAddZero(dateViewN.getDateEntity().month + "");//获取当前页面对应的年月
		getDateEntityToNet(currentMonth);//通过当前月向网络获取数据
		
		btn_dateview = (Button) rootView.findViewById(R.id.btn_dateview);

		progressBar1 = (ProgressBar) rootView
				.findViewById(R.id.progressBar1);
		tv_refresh_now = (TextView) rootView
				.findViewById(R.id.tv_refresh_now);
		ll_progress = (LinearLayout) rootView
				.findViewById(R.id.ll_progress);

		btn_dateview.setVisibility(View.GONE);
		ll_progress.setVisibility(View.VISIBLE);

		btn_dateview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_dateview.setVisibility(View.GONE);
				ll_progress.setVisibility(View.VISIBLE);
				getDateEntityToNet(currentMonth);
			}
		});
		
		
		
		return rootView;	
	}
	
	
	
	/**
	 * s<10时返回"0s" 否则返回 "s"
	 * 
	 * @param s
	 * @return
	 */
	public static String getAddZero(String s) {
		if (Integer.parseInt(s) < 10) {
			return "0" + s;
		}
		return s;
	}
	
	// 模拟通过网络，按月获取考勤记录
	private void getDateEntityToNet(final String monthYear) {

		new Thread() {
			public void run() {
				String result = null;

				NameValuePair pair = new BasicNameValuePair(monthYear, result);

				if (getActivity() != null) {
					mHandler.obtainMessage(0, pair).sendToTarget();
				}
			};
		}.start();

	}
	
	// 设置数据集
		private void setData() {
			dateEntities = new ArrayList<DateEntity>();
			DateEntity d = new DateEntity(2017, 02, 01);
			d.setEarly(true);
			d.setDate("2017-02-01");
			d.setToday(false);
			d.setWeek(getDateWeek("2017-02-01"));
			d.setMark("考勤正常");
			d.setStartTime("08:00");
			d.setEndTime("20:00");
			d.setLeaveDate("12:00-15:00");
			d.setOutWorkDate("16:00-17:00");
			d.setOverWorkDate("18:00-20:00");
			d.setWork(true);
			d.setLate(false);
			d.setEarly(false);
			d.setNormal(true);
			dateEntities.add(d);

			d = new DateEntity(2017, 02, 02);
			d.setEarly(true);
			d.setDate("2017-02-02");
			d.setToday(false);
			d.setWeek(getDateWeek("2017-02-02"));
			d.setMark("迟到");
			d.setStartTime("10:00");
			d.setEndTime("20:00");
			d.setLeaveDate("12:00-15:00");
			d.setOutWorkDate("16:00-17:00");
			d.setOverWorkDate("18:00-21:00");
			d.setWork(true);
			d.setLate(true);
			d.setEarly(false);
			d.setNormal(true);
			dateEntities.add(d);

			d = new DateEntity(2017, 02, 05);
			d.setEarly(true);
			d.setDate("2017-02-05");
			d.setToday(false);
			d.setWeek(getDateWeek("2017-02-05"));
			d.setMark("考勤正常");
			d.setStartTime("09:00");
			d.setEndTime("20:00");
			d.setOutWorkDate("16:00-17:00");
			d.setOverWorkDate("18:00-21:00");
			d.setWork(true);
			d.setLate(false);
			d.setEarly(false);
			d.setNormal(true);
			dateEntities.add(d);
		}
		/**
		 * 返回日期向对应的星期数 0 = "星期日"; 1= "星期一";2 = "星期二";3 = "星期三"; 4 = "星期四"; 5= "星期五";
		 * 6= "星期六";
		 * 
		 * @param time
		 * @return
		 */
		public int getDateWeek(String time) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
					Locale.getDefault());
			Date date = null;
			try {
				date = format.parse(time);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int d = c.get(Calendar.DAY_OF_WEEK);

			return d - 1;
		}
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			rootView = null;
			dateViewN = null;
			currentMonth = null;
			dateEntities = null;
			ll_progress = null;
			btn_dateview = null;
			progressBar1 = null;
			tv_refresh_now = null;
			mHandler.removeCallbacksAndMessages(null);
		}
}
