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
	 * ����int����
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
	 * ��ȡ�����intֵ
	 * 
	 * @param key
	 * @return
	 */
	public synchronized int getInt(String key) {
		return preferences.getInt(key, -1);
	}

	
	/**
	 * ����String����
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
	 * ��ȡ�����Stringֵ
	 * 
	 * @param key
	 * @return
	 */
	public synchronized String getString(String key) {
		return preferences.getString(key, null);
	}
	/**
	 * ������������
	 * 
	 * @param bean
	 */
	public void saveGesturePwd(String bean) {
		saveString("GesturePwd", bean);
	}
	/**
	 * ��ȡ��������
	 * 
	 * @return
	 */
	public String getGesturePwd() {
		return getString("GesturePwd");
	}
	
	
	/**
	 * ����������
	 * 
	 * @param bean
	 */
	public void saveGestureErrorCount(int bean) {
		saveInt("ErrorCount", bean);
	}
	/**
	 * ��ȡ��������������
	 * 
	 * @return
	 */
	public int getGestureErrorCount() {
		return getInt("ErrorCount");
	}

}
