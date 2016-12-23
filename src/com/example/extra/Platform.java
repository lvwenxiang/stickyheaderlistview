package com.example.extra;

import android.os.Build;

public class Platform {
	
	
	/**
	 * 
	 * 说明 :判断当前运行的环境
	 * 
	 * @author 作者 兰少锋:
	 * @version 创建时间：2016年12月13日 下午5:37:19
	 */
	
	
	
	//private static final Platform PLATFORM = findPlatform();

	/*  static Platform get() {
	    return PLATFORM;
	  }*/

/*	  private static Platform findPlatform() {
	    try {
	      Class.forName("android.os.Build");
	      if (Build.VERSION.SDK_INT != 0) {
	        return new Android();
	      }
	    } catch (ClassNotFoundException ignored) {
	    }
	    try {
	      Class.forName("java.util.Optional");
	      return new Java8();
	    } catch (ClassNotFoundException ignored) {
	    }
	    try {
	      Class.forName("org.robovm.apple.foundation.NSObject");
	      return new IOS();
	    } catch (ClassNotFoundException ignored) {
	    }
	    return new Platform();
	  }*/
}