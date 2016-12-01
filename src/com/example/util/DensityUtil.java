package com.example.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class DensityUtil {
	// �����ֻ��ķֱ��ʽ�dp�ĵ�λת��px(����)
		public static int dip2px(Context context, float dpValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		}

	    // �����ֻ��ķֱ��ʽ�px(����)�ĵ�λת��dp
		public static int px2dip(Context context, float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}

	    // ��pxֵת��Ϊspֵ
		public static int px2sp(Context context, float pxValue) {
			final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
			return (int) (pxValue / fontScale + 0.5f);
		}

	    // ��spֵת��Ϊpxֵ
		public static int sp2px(Context context, float spValue) {
			final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
			return (int) (spValue * fontScale + 0.5f);
		}

	    // ��Ļ��ȣ����أ�
	    public static  int getWindowWidth(Activity context){
	        DisplayMetrics metric = new DisplayMetrics();
	        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
	        return metric.widthPixels;
	    }

	    // ��Ļ�߶ȣ����أ�
	    public static int getWindowHeight(Activity activity){
	        DisplayMetrics metric = new DisplayMetrics();
	        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	        return metric.heightPixels;
	    }
}
