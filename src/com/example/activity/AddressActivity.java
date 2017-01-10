package com.example.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.adapter.AddressAdapter;
import com.example.adapter.HeaderRecyclerAndFooterWrapperAdapter;
import com.example.bean.ContactPersonbean;
import com.example.bean.SortContactbean;
import com.example.stickyheaderlsitview.R;
import com.example.tools.DividerItemDecoration;
import com.example.tools.SuspensionDecoration;
import com.example.tools.ViewHolder;
import com.example.util.CharacterParserUtil;
import com.example.util.PinyinComparator;
import com.example.widget.SideBar;
import com.example.widget.SideBar.OnTouchingLetterChangedListener;

public class AddressActivity extends BaseActivity {
	RecyclerView mRv;
	AddressAdapter mAdapter;
	private List<ContactPersonbean> mDatas = new ArrayList<ContactPersonbean>();;
	SideBar sidebar;
	private CharacterParserUtil characterParserutil;
	PinyinComparator pinyinComparator;
	private List<SortContactbean> SourceDateList;
	private SuspensionDecoration mDecoration;
	private TextView show_letter;
	 private LinearLayoutManager mManager;
	 private static final String INDEX_STRING_TOP = "↑";
	 private HeaderRecyclerAndFooterWrapperAdapter mHeaderAdapter;
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		initView();
		initData();
		addListener();
	}

	private void initView() {
		sidebar = (SideBar) findViewById(R.id.sidebar);
		mRv = (RecyclerView) findViewById(R.id.rv);
		show_letter = (TextView) findViewById(R.id.show_letter);
		show_letter.setVisibility(View.INVISIBLE);

	}

	private void initData() {
		mManager=new LinearLayoutManager(AddressActivity.this);
		mRv.setLayoutManager(mManager);
		characterParserutil = CharacterParserUtil.getInstance();
		pinyinComparator = new PinyinComparator();
		initDatas(getResources().getStringArray(R.array.provinces));
		if (mDatas != null && mDatas.size() > 0) {
			SourceDateList = filledData(mDatas);
			Collections.sort(SourceDateList, pinyinComparator);
		} else {
			SourceDateList = new ArrayList<SortContactbean>();
		}

		sidebar.setTextView(show_letter);
		sidebar.setNeedRealIndex(true);
		sidebar.setDatas(SourceDateList).invalidate();

		mAdapter = new AddressAdapter(this, SourceDateList);
		
	      mHeaderAdapter = new HeaderRecyclerAndFooterWrapperAdapter(mAdapter) {
	            @Override
	            protected void onBindHeaderHolder(ViewHolder holder, int headerPos, int layoutId, Object o) {
	                holder.setText(R.id.tvCity, (String) o);
	            }
	        };
	        mHeaderAdapter.addHeaderView(R.layout.item_address, "测试头部");
		
		mRv.setAdapter(mAdapter);
		mRv.setItemAnimator(new DefaultItemAnimator());
		mRv.addItemDecoration(mDecoration = new SuspensionDecoration(this,
				SourceDateList));
		// 添加分割线
		mRv.addItemDecoration(new DividerItemDecoration(AddressActivity.this,
				DividerItemDecoration.VERTICAL_LIST));
	}

	private void addListener() {
		sidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// TODO Auto-generated method stub
				int i = getLetterPosition(s);
				if (i != -1) {
					if (android.os.Build.VERSION.SDK_INT >= 8) {
						//mRv.smoothScrollToPosition(i);
						mManager.scrollToPositionWithOffset(i, 0);
					}
				}
			}
		});
	}

	private int getLetterPosition(String s) {
		if (SourceDateList != null && SourceDateList.size() > 0) {
			for (int i = 0; i < SourceDateList.size(); i++) {
				if (SourceDateList.get(i).getSortLetters().equals(s)) {
					return i;
				}
			}
		}
		return -1;
	}

	@SuppressLint("DefaultLocale")
	private List<SortContactbean> filledData(List<ContactPersonbean> users) {
		List<SortContactbean> mSortList = new ArrayList<SortContactbean>();

		for (int i = 0; i < users.size(); i++) {
			SortContactbean sortModel = new SortContactbean();
			sortModel.setCity(users.get(i).getCity());
			sortModel.setTop(users.get(i).getTop());	
			if(!users.get(i).getTop()){
				// 汉字转换成拼音
				String pinyin = characterParserutil.getSelling(users.get(i)
						.getCity());
				sortModel.setPinyin(pinyin);
				
				String sortString = pinyin.substring(0, 1).toUpperCase(
						Locale.getDefault());
				// 正则表达式，判断首字母是否是英文字母
				if (sortString.matches("[A-Z]")) {
					sortModel.setSortLetters(sortString.toUpperCase(Locale
							.getDefault()));
				} else {
					sortModel.setSortLetters("#");
				}
			}
			else{
				sortModel.setSortLetters("");	
				sortModel.setPinyin("");
			}
				
			mSortList.add(sortModel);
			
		}
		return mSortList;

	}

	private void initDatas(final String[] data) {
		// 延迟两秒 模拟加载数据中....
		// getWindow().getDecorView().postDelayed(new Runnable() {
		// @Override
		// public void run() {

		// 微信的头部 也是可以右侧IndexBar导航索引的，
		// 但是它不需要被ItemDecoration设一个标题titile
		ContactPersonbean CP1=new ContactPersonbean("新的朋友");
		CP1.setTop(true);
		ContactPersonbean CP2=new ContactPersonbean("群聊");
		CP2.setTop(true);
		ContactPersonbean CP3=new ContactPersonbean("标签");
		CP3.setTop(true);
		ContactPersonbean CP4=new ContactPersonbean("公众号");
		CP4.setTop(true);
		
		  mDatas.add(CP1); 
		  mDatas.add(CP2); 
		  mDatas.add(CP3);
		  mDatas.add(CP4);
		 

		for (int i = 0; i < data.length; i++) {
			ContactPersonbean contactpersonbean = new ContactPersonbean();
			contactpersonbean.setCity(data[i]);// 设置城市名称
			contactpersonbean.setTop(false);// 设置城市名称
			mDatas.add(contactpersonbean);
		}
		// mAdapter.setDatas(mDatas);
		// mAdapter.notifyDataSetChanged();
		// }
		// }, 500);
	}
}
