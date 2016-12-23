package com.example.bean;

import java.io.Serializable;

public class ContactPersonbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2836307560657343667L;

	private String city;// 城市名字
	private boolean isTop;// 是否是最上面的 不需要被转化成拼音的

	public ContactPersonbean() {
	}

	public ContactPersonbean(String city) {
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean getTop() {
		return isTop;
	}

	public void setTop(boolean isTop) {
		this.isTop = isTop;
	}
	 public boolean isNeedToPinyin() {
	        return !isTop;
	    }
}
