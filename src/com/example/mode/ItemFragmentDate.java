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


//��������ҳ��
public class ItemFragmentDate  extends Fragment {
	private View rootView;
	public static final String ARG_PAGE = "page";// keyֵ
	private int mPageNumber;// ��ǰҳ��
	private DateViewN dateViewN;// �����ؼ�
	// ����ItemFragmentDateͨ��ViewPager��ҳ����
	private String currentMonth;// ��ǰ��
	public MyHandler mHandler;// handler,�������ݸ���
	private ArrayList<DateEntity> dateEntities;// �������ݼ���
	private LinearLayout ll_progress;// չʾˢ�½���LinearLayout
	private Button btn_dateview;// ˢ�����ݰ�ť
	private ProgressBar progressBar1;// չʾˢ�½���ԲȦ����ll_progress��
	private TextView tv_refresh_now;// չʾˢ�½������֣���ll_progress��
	
	
	// �Զ���MyHandler��̳�Handler����ֹ�ڴ�й©
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
							dateViewN.setEntities(dateEntities);// �����ݼ��Ϸ����Զ��������ؼ���
							btn_dateview.setVisibility(View.GONE);
							ll_progress.setVisibility(View.GONE);
						} else {
							btn_dateview.setVisibility(View.GONE);
							ll_progress.setVisibility(View.VISIBLE);
							progressBar1.setVisibility(View.GONE);
							tv_refresh_now.setText("�����޿�������");
						}

					} else {
						btn_dateview.setVisibility(View.VISIBLE);
						ll_progress.setVisibility(View.GONE);
					}
				}
			}
			
		});
		mPageNumber = getArguments().getInt(ARG_PAGE);// ��ȡ��ǰҳ����
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = View.inflate(getActivity(),
				R.layout.fragment_viewpager_date, null);
		dateViewN = (DateViewN) rootView.findViewById(R.id.dateview);// ��ʼ�����ؼ�
		dateViewN.setShowDate(mPageNumber);//���õ�ǰҳ���Ӧ���Զ��������ؼ��е�����
		
		currentMonth = dateViewN.getDateEntity().year + "-"
				+ getAddZero(dateViewN.getDateEntity().month + "");//��ȡ��ǰҳ���Ӧ������
		getDateEntityToNet(currentMonth);//ͨ����ǰ���������ȡ����
		
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
	 * s<10ʱ����"0s" ���򷵻� "s"
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
	
	// ģ��ͨ�����磬���»�ȡ���ڼ�¼
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
	
	// �������ݼ�
		private void setData() {
			dateEntities = new ArrayList<DateEntity>();
			DateEntity d = new DateEntity(2017, 02, 01);
			d.setEarly(true);
			d.setDate("2017-02-01");
			d.setToday(false);
			d.setWeek(getDateWeek("2017-02-01"));
			d.setMark("��������");
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
			d.setMark("�ٵ�");
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
			d.setMark("��������");
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
		 * �����������Ӧ�������� 0 = "������"; 1= "����һ";2 = "���ڶ�";3 = "������"; 4 = "������"; 5= "������";
		 * 6= "������";
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
