package com.example.extra;

import android.util.Log;

public class MyLog {
private static boolean isDeBug = true;
	
	
	
	/**
	 * 
	 * @author ���� ���ٷ�:
	 * @version ����ʱ�䣺2016��1��11�� ����9:37:57
	 * @param tag
	 * @param msg
	 *            ˵�� :��ɫLOG
	 */
	public static void e(String tag, String msg) {
		if (isDeBug) {
			Log.e(tag, "" + msg);
		}

	}

	/**
	 * 
	 * @author ���� ���ٷ�:
	 * @version ����ʱ�䣺2016��1��11�� ����10:09:50
	 * @param tag
	 * @param msg
	 *            ˵�� :��ɫLOG
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
