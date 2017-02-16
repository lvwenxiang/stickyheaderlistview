package com.example.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.stickyheaderlsitview.R;
import com.example.util.PrefShare;

public class GestureActivity extends Activity {
	private Button set_gesture_pwd, verify_gesture_pwd;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gesture);
		mContext = this;
		initView();

	}
	private void initView() {
		// TODO Auto-generated method stub
		set_gesture_pwd = (Button) findViewById(R.id.set_gesture_pwd);
		verify_gesture_pwd = (Button) findViewById(R.id.verify_gesture_pwd);

		set_gesture_pwd.setOnClickListener(onClickListener);
		verify_gesture_pwd.setOnClickListener(onClickListener);

	}
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.set_gesture_pwd:// 设置手势密码
				PrefShare.getInstance(mContext).saveGesturePwd(null);
				startActivity(new Intent(mContext, GestureSetAndVerifyActivity.class)
						.putExtra("set", "set").addFlags(
								Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				break;
			case R.id.verify_gesture_pwd:// 验证手势密码
				if (PrefShare.getInstance(mContext).getGesturePwd() == null) {
					startActivity(new Intent(mContext, GestureSetAndVerifyActivity.class)
							.putExtra("set", "set").addFlags(
									Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				} else {
					startActivity(new Intent(mContext, GestureSetAndVerifyActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				}
				break;

			}
		}
	};
}