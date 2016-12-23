package com.example.bean;

import java.io.Serializable;

import com.example.tools.ISuspensionInterface;

public class SortContactbean implements Serializable,ISuspensionInterface  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2836307560657343667L;
	private String city;// 城市名字
	private String sortLetters; // 显示数据拼音的首字母
	private String pinyin;
	private boolean isTop;// 是否是最上面的 不需要被转化成拼音的
	

	public SortContactbean() {
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
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
	@Override
	public boolean isShowSuspension() {
		  return !isTop;
	}

	@Override
	public String getSuspensionTag() {
		return sortLetters;
	}

}
