package com.example.util;

import java.util.Comparator;

import com.example.bean.SortContactbean;

public class PinyinComparator implements Comparator<SortContactbean> {

	public int compare(SortContactbean o1, SortContactbean o2) {
	if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getPinyin().compareTo(o2.getPinyin());
		}		
	}

}
