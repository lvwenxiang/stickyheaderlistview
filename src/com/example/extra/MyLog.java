package com.example.extra;

import android.util.Log;

public class MyLog {
private static boolean isDeBug = true;
	
	
	
	/**
	 * 
	 * @author 作者 兰少锋:
	 * @version 创建时间：2016年1月11日 上午9:37:57
	 * @param tag
	 * @param msg
	 *            说明 :红色LOG
	 */
	public static void e(String tag, String msg) {
		if (isDeBug) {
			Log.e(tag, "" + msg);
		}

	}

	/**
	 * 
	 * @author 作者 兰少锋:
	 * @version 创建时间：2016年1月11日 上午10:09:50
	 * @param tag
	 * @param msg
	 *            说明 :绿色LOG
	 */
	public static void i(String tag, String msg) {
		if (isDeBug) {
			Log.i(tag, "" + msg);
		}
	}

	public static void println(Object msg) {
		if (isDeBug) {
			System.out.println(msg);
		}
	}
}
