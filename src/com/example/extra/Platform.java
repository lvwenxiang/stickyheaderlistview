package com.example.extra;

import android.os.Build;

public class Platform {
	
	
	/**
	 * 
	 * ˵�� :�жϵ�ǰ���еĻ���
	 * 
	 * @author ���� ���ٷ�:
	 * @version ����ʱ�䣺2016��12��13�� ����5:37:19
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