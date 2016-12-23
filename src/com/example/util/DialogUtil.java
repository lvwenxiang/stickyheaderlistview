package com.example.util;


import com.example.stickyheaderlsitview.R;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class DialogUtil {
	private AlertDialog builder;
	private Animation animation;
	private TextView progress_shape, progress_content;
	private View quitview;

	private AlertDialog promtDialog;

	/**
	 * 
	 *            弹出框提示语
	 * @param mContext
	 * @param okListener
	 *            确定按钮监听
	 * @param cancelListener
	 *            取消按钮监听 说明 :
	 */
	public void showPromtDialog(String msg, final Context mContext,
			OnClickListener okListener, OnClickListener cancelListener,
			String confirm, String cancel) {
		if (mContext == null) {
			return;
		}
		if (promtDialog == null) {
			promtDialog = new AlertDialog.Builder(mContext).create();
		}

		if (promtDialog.isShowing()) {
			return;
		}
		promtDialog.setCancelable(false);
		promtDialog.show();
		LayoutInflater mLi = LayoutInflater.from(mContext);
		View quitview = mLi.inflate(R.layout.double_btn_dialog, null);

		promtDialog.getWindow().setContentView(quitview);
		TextView content = (TextView) quitview
				.findViewById(R.id.dialog_content);
		content.setText(msg);
		Button b_cancel = (Button) quitview
				.findViewById(R.id.btn_dialog_cancel);
		Button b_ok = (Button) quitview.findViewById(R.id.btn_dialog_confirm);

		if (confirm != null && !"".equals(confirm)) {
			b_ok.setText(confirm);
		}
		if (cancel != null && !"".equals(cancel)) {
			b_cancel.setText(cancel);
		}

		b_ok.setOnClickListener(okListener);
		if (cancelListener != null) {
			b_cancel.setOnClickListener(cancelListener);
		} else {
			b_cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					promtDialog.dismiss();
				}
			});
		}
	}

	private void showDialogMessage(Context context ) {
		final AlertDialog  builder = new AlertDialog.Builder(context).create();
		builder.setCanceledOnTouchOutside(false);
		builder.show();
		LayoutInflater mLi = LayoutInflater.from(context);
		View quitview = mLi.inflate(R.layout.photo_dialog, null);

		Animation anim = AnimationUtils.loadAnimation(context,
				R.anim.in_from_bottom);
		anim.setDuration(200);
		quitview.setAnimation(anim);
		builder.getWindow().setContentView(quitview);

		builder.getWindow().setGravity(Gravity.BOTTOM);
		LayoutParams lparams = builder.getWindow().getAttributes();
		//应用在activity中得getWindowManager()
		//lparams.width = getWindowManager().getDefaultDisplay().getWidth();
		lparams.width = builder.getWindow().getWindowManager().getDefaultDisplay().getWidth();
		builder.getWindow().setAttributes(lparams);

		Button photo_camera_btn = (Button) quitview
				.findViewById(R.id.photo_camera_btn);
		Button photo_store_btn = (Button) quitview
				.findViewById(R.id.photo_store_btn);
		Button photo_cancel = (Button) quitview.findViewById(R.id.photo_cancel);
		photo_cancel.setText("");
		photo_camera_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
			}
		});
		photo_store_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			
			}
		});
		photo_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
			}
		});
	}
	
	
	//进度 旋转 请稍后
	public void showDialogProgressNew(String msg, Context context) {
		if (context == null) {
			return;
		}
	//	Config.hideInput(context);// 隐藏键盘
		builder = new AlertDialog.Builder(context).create();
		builder.show();
		quitview = View.inflate(context, R.layout.dialog_progress, null);
		builder.getWindow().setContentView(quitview);
		progress_shape = (TextView) quitview.findViewById(R.id.progress_shape);
		progress_content = (TextView) quitview
				.findViewById(R.id.progress_content);
		progress_content.setText(msg);

		LinearInterpolator interpolator = new LinearInterpolator();
		animation = AnimationUtils
				.loadAnimation(context, R.anim.progress_small);
		animation.setInterpolator(interpolator);
		progress_shape.startAnimation(animation);
		builder.setCanceledOnTouchOutside(false);

		return;
	}
}
