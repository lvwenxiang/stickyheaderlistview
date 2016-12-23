package com.example.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.adapter.HeaderAdAdapter;
import com.example.extra.ImageManager;
import com.example.stickyheaderlsitview.R;
import com.example.tools.ViewPagerScroller;
import com.example.transformer.RotateDownPageTransformer;
import com.example.util.DensityUtil;

public class HeaderAdViewView {
	private List<ImageView> ivList;
	private ImageManager mImageManager;
	private LayoutInflater mInflate;
	private View view;
	Context mContext;
	ViewPager vpAd;
	private ViewPagerScroller scroller;
	private Thread mThread;
	LinearLayout llIndexContainer;
	private boolean isStopThread = false;

	private static final int TYPE_CHANGE_AD = 0;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == TYPE_CHANGE_AD) {
				vpAd.setCurrentItem(vpAd.getCurrentItem() + 1);
			}
		}
	};

	public HeaderAdViewView(Activity context) {
		mContext = context;
		mInflate = LayoutInflater.from(context);
		ivList = new ArrayList<>();
		mImageManager = new ImageManager(context);

	}

	public void addView(List<String> list, ListView listView) {
		view = mInflate.inflate(R.layout.header_ad_layout, listView, false);
		initView();
		dealWithTheView(list);
		listView.addHeaderView(view);
		initViewPagerScroll();
	}

	private void initView() {
		vpAd = (ViewPager) view.findViewById(R.id.vp_ad);
		llIndexContainer = (LinearLayout) view
				.findViewById(R.id.ll_index_container);
		// 设置Page间间距
		vpAd.setPageMargin(20);
		// 设置缓存的页面数量
		vpAd.setOffscreenPageLimit(3);
	}

	private void dealWithTheView(List<String> list) {
		ivList.clear();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			ivList.add(createImageView(list.get(i)));
		}

		HeaderAdAdapter photoAdapter = new HeaderAdAdapter(mContext, ivList,vpAd);
		vpAd.setAdapter(photoAdapter);
		vpAd.setCurrentItem(ivList.size()*100, false);
		// vpAd.setPageTransformer(true,new ZoomInTransformer());
		// vpAd.setPageTransformer(true,new FlipVerticalTransformer());
		vpAd.setPageTransformer(true,new RotateDownPageTransformer());
		addIndicatorImageViews(size);
		setViewPagerChangeListener(size);
		startADRotate();
	}

	// 创建要显示的ImageView
	private ImageView createImageView(String url) {
		ImageView imageView = new ImageView(mContext);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		imageView.setLayoutParams(params);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		mImageManager.loadUrlImage(url, imageView);
		return imageView;
	}

	// 添加指示图标
	private void addIndicatorImageViews(int size) {
		llIndexContainer.removeAllViews();
		for (int i = 0; i < size; i++) {
			ImageView iv = new ImageView(mContext);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(
							mContext, 5));
			if (i != 0) {
				lp.leftMargin = DensityUtil.dip2px(mContext, 7);
			}
			iv.setLayoutParams(lp);
			iv.setBackgroundResource(R.drawable.xml_round_orange_grey_sel);
			iv.setEnabled(false);
			if (i == 0) {
				iv.setEnabled(true);
			}
			llIndexContainer.addView(iv);
		}
	}

	// 为ViewPager设置监听器
	private void setViewPagerChangeListener(final int size) {
		vpAd.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (ivList != null && ivList.size() > 0) {
					int newPosition = position % size;
					for (int i = 0; i < size; i++) {
						llIndexContainer.getChildAt(i).setEnabled(false);
						if (i == newPosition) {
							llIndexContainer.getChildAt(i).setEnabled(true);
						}
					}
				}
			}

			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * 设置ViewPager的滚动速度
	 * 
	 * @param scrollDuration
	 */
	public void setScrollDuration(int scrollDuration) {
		scroller.setScrollDuration(scrollDuration);
	}

	/**
	 * 设置ViewPager的滑动速度
	 * */
	private void initViewPagerScroll() {
		try {
			Field mScroller = null;
			mScroller = ViewPager.class.getDeclaredField("mScroller");
			mScroller.setAccessible(true);
			scroller = new ViewPagerScroller(mContext);
			mScroller.set(vpAd, scroller);

		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// 启动循环广告的线程
	private void startADRotate() {
		// 一个广告的时候不用转
		if (ivList == null || ivList.size() <= 1) {
			return;
		}
		if (mThread == null) {
			mThread = new Thread(new Runnable() {

				@Override
				public void run() {
					// 当没离开该页面时一直转
					while (!isStopThread) {
						// 每隔5秒转一次
						SystemClock.sleep(5000);
						// 在主线程更新界面
						mHandler.sendEmptyMessage(TYPE_CHANGE_AD);
					}
				}
			});
			mThread.start();
		}
	}
}
