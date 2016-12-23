package com.example.bean;

import java.io.Serializable;

import com.example.tools.ISuspensionInterface;

public class SortContactbean implements Serializable,ISuspensionInterface  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2836307560657343667L;
	private String city;// ��������
	private String sortLetters; // ��ʾ����ƴ��������ĸ
	private String pinyin;
	private boolean isTop;// �Ƿ���������� ����Ҫ��ת����ƴ����
	

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
