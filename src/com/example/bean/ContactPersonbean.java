package com.example.bean;

import java.io.Serializable;

public class ContactPersonbean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2836307560657343667L;

	private String city;// ��������
	private boolean isTop;// �Ƿ���������� ����Ҫ��ת����ƴ����

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
