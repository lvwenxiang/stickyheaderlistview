package com.example.bean;

import java.io.Serializable;

public class MyRatebean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1350653290370630639L;

	private String title;
	private double value;
	private String color;
	private String type;
	private String valueString;
	
	public MyRatebean(String title,double value,String color,String type,String valueString){
		this.title=title;
		this.value=value;
		this.color=color;
		this.type=type;
		this.valueString=valueString;
	}
	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getValueString() {
		return valueString;
	}
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
}
