package com.example.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mode.ItemFragmentDate;

public class CalendarAdapter extends FragmentStatePagerAdapter{
	private final int ALL_PAGE_NUM = 500 * 2;// 设置页面总数
	public CalendarAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return ItemFragmentDate.create(arg0);// 根据传入的页面当前页面编号生成ItemFragmentDate对象碎片
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ALL_PAGE_NUM;
	}

}
