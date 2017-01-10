package com.example.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public class BaseActivity extends SwipeBackActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= 19) {
			// 当手机系统版本大于Android4.4时修改状态栏颜色
			setTranslucentStatus(true);
		}
		// tintManager = new SystemBarTintManager(this);
		// tintManager.setStatusBarTintEnabled(true);
		// tintManager.setStatusBarTintResource(R.color.statusbar_bg);
	}
	
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
//		final int bits = SystemBarTintManager.bits;
		//final int bits = TRANSLUCENT_STATUS_FLAG;
		final int bits =WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
}
