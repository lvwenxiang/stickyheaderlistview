package com.example.bean;

import java.io.Serializable;

public class WeekRankingbean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1376679290150490799L;
	private String WEEKS;
	private int WEEK_ORDER;
	private String YEAR;
	private String color;

	public WeekRankingbean(String WEEKS,int WEEK_ORDER,String YEAR,String color){
		this.WEEKS=WEEKS;
		this.WEEK_ORDER=WEEK_ORDER;
		this.YEAR=YEAR;
		this.color=color;
	}
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getWEEKS() {
		return WEEKS;
	}

	public void setWEEKS(String wEEKS) {
		WEEKS = wEEKS;
	}

	public int getWEEK_ORDER() {
		return WEEK_ORDER;
	}

	public void setWEEK_ORDER(int wEEK_ORDER) {
		WEEK_ORDER = wEEK_ORDER;
	}



	public String getYEAR() {
		return YEAR;
	}

	public void setYEAR(String yEAR) {
		YEAR = yEAR;
	}


	@Override
	public String toString() {
		return "WeekRankingOA [WEEKS=" + WEEKS + ", WEEK_ORDER=" + WEEK_ORDER
				+ ", YEAR=" + YEAR + ", color="
				+ color + "]";
	}

}
