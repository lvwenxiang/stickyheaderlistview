package com.example.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class TApplication extends Application {
	public static ArrayList<Activity> activities = new ArrayList<Activity>();
	public static Context applicationContext;
	private static TApplication instance;
	private static ConnectivityManager manager;
	
	public static TApplication getInstance() {
		return instance;
	}
	public static void exit() {
		for (Activity activity : activities) {
			activity.finish();
		}
		// System.exit(0);
	}
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		applicationContext = this;
		instance = this;
		manager = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);	
	}
	public static boolean isNetWorkAvailable() {// 检测是否连接网络
		if (null != manager) {
			NetworkInfo networkInfos[] = manager.getAllNetworkInfo();
			if (networkInfos != null) {
				for (NetworkInfo n : networkInfos) {
					if (n.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	public static boolean isInternetAvailable() {// 检测是否可以上网（连接百度）
		try {
			Process p = Runtime.getRuntime().exec(
					"ping -c 1 " + "www.baidu.com");
			if (p.waitFor() == 0) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
