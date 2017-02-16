package com.example.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PrefShare {
	private static PrefShare prefShare;
	private Context mContext;
	private SharedPreferences preferences;
	
	
	public PrefShare(Context context) {
		mContext = context;
		preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
	}
	
	
	public static PrefShare getInstance(Context context) {
		if (prefShare == null) {
			synchronized (PrefShare.class) {
				if (prefShare == null) {
					prefShare = new PrefShare(context);
				}
			}
		}
		return prefShare;
	}
	

	/**
	 * 保存int类型
	 * 
	 * @param key
	 * @param value
	 */
	public synchronized void saveInt(String key, int value) {
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	/**
	 * 获取保存的int值
	 * 
	 * @param key
	 * @return
	 */
	public synchronized int getInt(String key) {
		return preferences.getInt(key, -1);
	}

	
	/**
	 * 保存String类型
	 * 
	 * @param key
	 * @param value
	 */
	public synchronized void saveString(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	/**
	 * 获取保存的String值
	 * 
	 * @param key
	 * @return
	 */
	public synchronized String getString(String key) {
		return preferences.getString(key, null);
	}
	/**
	 * 保存手势密码
	 * 
	 * @param bean
	 */
	public void saveGesturePwd(String bean) {
		saveString("GesturePwd", bean);
	}
	/**
	 * 获取手势密码
	 * 
	 * @return
	 */
	public String getGesturePwd() {
		return getString("GesturePwd");
	}
	
	
	/**
	 * 保存输错次数
	 * 
	 * @param bean
	 */
	public void saveGestureErrorCount(int bean) {
		saveInt("ErrorCount", bean);
	}
	/**
	 * 获取手势密码输错次数
	 * 
	 * @return
	 */
	public int getGestureErrorCount() {
		return getInt("ErrorCount");
	}

}
