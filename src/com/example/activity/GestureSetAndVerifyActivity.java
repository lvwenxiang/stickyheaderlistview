/**
 * 
 */
package com.example.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stickyheaderlsitview.R;
import com.example.util.PrefShare;
import com.example.widget.GesturePwdView;
import com.example.widget.GesturePwdView.OnLockScreenPinListener;

/**
 * @author wenxiang.lv
 *
 */
public class GestureSetAndVerifyActivity extends BaseActivity {
	private Button forget_gesture_pwd;
	private Context mContext;
	private GesturePwdView gesture_view;
	private TextView tv_set;
	private LinearLayout ll_set;
	private String set;
	private int setCount = 0;
	private String setGesturePwd;
	private int errorCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture_set);
		mContext = this;
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		set = getIntent().getStringExtra("set");
		forget_gesture_pwd = (Button) findViewById(R.id.forget_gesture_pwd);
		gesture_view = (GesturePwdView) findViewById(R.id.gesture_view);
		tv_set = (TextView) findViewById(R.id.tv_set);
		ll_set = (LinearLayout) findViewById(R.id.ll_set);
		tv_set.setText("");
		if (set != null && "set".equals(set)) {// �ж�����֤�������뻹��������������
			forget_gesture_pwd.setVisibility(View.GONE);
			ll_set.setVisibility(View.VISIBLE);
			gesture_view.setPassword("");
		} else {
			forget_gesture_pwd.setVisibility(View.VISIBLE);
			ll_set.setVisibility(View.GONE);
			gesture_view.setPassword(PrefShare.getInstance(mContext)
					.getGesturePwd());
			Toast.makeText(mContext,"���룺" + PrefShare.getInstance(mContext).getGesturePwd(),2).show();
		}
		forget_gesture_pwd.setOnClickListener(onClickListener);
		errorCount = PrefShare.getInstance(mContext).getGestureErrorCount();
		if (errorCount == -1) {
			errorCount = 4;
		}
		gesture_view.setOnLockScreenPinListener(new OnLockScreenPinListener() {

			@Override
			public void onComparePinSuccess(GesturePwdView gesturePwdView,// ��������������ȷ
					String pwd) {
				// TODO Auto-generated method stub
				if (set == null) {
					
					Toast.makeText(mContext,"���룺" + "����������ȷ�����룺" + pwd,2).show();
					PrefShare.getInstance(mContext).saveGestureErrorCount(-1);
					finish();
				}
			}

			@Override
			public void onComparePinFail(GesturePwdView gesturePwdView,// �������������������
					String pwd) {
				// TODO Auto-generated method stub
				if (set == null) {
					if (pwd != null && !"".equals(pwd)) {
						if (errorCount == 0) {
							PrefShare.getInstance(mContext)
									.saveGesturePwd(null);
							PrefShare.getInstance(mContext)
									.saveGestureErrorCount(-1);
							errorCount = PrefShare.getInstance(mContext)
									.getGestureErrorCount();

							finish();
						} else {
							Toast.makeText(mContext,"���룺" + "��������������룺"
									+ pwd + "������" + errorCount + "�λ���",2).show();
							errorCount--;
						}
						PrefShare.getInstance(mContext).saveGestureErrorCount(
								errorCount);
					}
				}
			}

			@Override
			public void onChangePin(GesturePwdView gesturePwdView, String pwd) {// ������������
				// TODO Auto-generated method stub
				if (set != null && "set".equals(set)) {
					if (pwd != null && !"".equals(pwd)) {
						if (setCount == 0 && pwd.length() < 4) {
							tv_set.setText("���ӵ�Ҫ�����ĸ�");
							return;
						}
						if (setCount == 0 && pwd.length() >= 4) {
							setCount++;
							setGesturePwd = pwd;
							tv_set.setText("���ٴ�ȷ�ϻ�������");
							return;
						}
						if (setCount == 1) {
							if (setGesturePwd.equals(pwd)) {
								PrefShare.getInstance(mContext).saveGesturePwd(
										setGesturePwd);
								
								Toast.makeText(mContext,"���룺" + "��������ɹ������룺" + setGesturePwd,2).show();
				
								finish();
							} else {
								setCount = 0;
								tv_set.setText("���λ������벻һ��,�����»���");
							}
						}
					}
				}
			}
		});

	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.forget_gesture_pwd:// ������������
				PrefShare.getInstance(mContext).saveGesturePwd(null);
				PrefShare.getInstance(mContext).saveGestureErrorCount(-1);
				finish();
				break;

			}
		}
	};
}
