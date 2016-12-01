package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.adapter.TravelingAdapter;
import com.example.mode.OperationEntity;
import com.example.mode.TravelingEntity;
import com.example.stickyheaderlsitview.R;
import com.example.stickyheaderlsitview.SmoothListView;
import com.example.stickyheaderlsitview.R.color;
import com.example.stickyheaderlsitview.R.id;
import com.example.stickyheaderlsitview.R.layout;
import com.example.stickyheaderlsitview.SmoothListView.ISmoothListViewListener;
import com.example.stickyheaderlsitview.SmoothListView.OnSmoothScrollListener;
import com.example.util.ColorUtil;
import com.example.util.DensityUtil;
import com.example.util.ModelUtil;
import com.example.view.HeaderAdViewView;
import com.example.view.HeaderFilterView;
import com.example.view.HeaderOperationView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

public class MainActivity extends Activity implements SmoothListView.ISmoothListViewListener {
	   private List<TravelingEntity> travelingList = new ArrayList<TravelingEntity>(); // ListView数据
	   private boolean isStickyTop = false; // 是否吸附在顶部
	   private int adViewHeight = 180; // 广告视图的高度
	   private int titleViewHeight = 50; // 标题栏的高度
	   private TravelingAdapter mAdapter; // 主页数据
	   SmoothListView  smoothListView;
	   HeaderAdViewView adHeaderView;
	   HeaderFilterView headerfilterview;
	   private View itemHeaderAdView; // 从ListView获取的广告子View
	   private boolean isScrollIdle = true; // ListView是否在滑动
	   private int adViewTopSpace; // 广告视图距离顶部的距离
	   private int filterViewPosition = 3; // 筛选视图的位置
	   private int filterViewTopSpace; // 筛选视图距离顶部的距离
	   private View itemHeaderFilterView; // 从ListView获取的筛选子View
	   TextView fv_top_filter;
	    private HeaderOperationView headerOperationViewView; // 运营视图
	   RelativeLayout rlBar;
	   View  viewTitleBg,viewActionMoreBg;
	   private List<OperationEntity> operationList = new ArrayList<>(); // 运营数据
	   private List<String> adList = new ArrayList<>(); // 广告数据
	   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		  initImageLoader();
		initView();
		initdata();
		initListiner();
	}

	private void initView() {
		smoothListView=(SmoothListView) findViewById(R.id.listView);
		fv_top_filter=(TextView) findViewById(R.id.fv_top_filter);
		rlBar =(RelativeLayout) findViewById(R.id.rl_bar);
		
		viewTitleBg=findViewById(R.id.view_title_bg);
	
		viewActionMoreBg=findViewById(R.id.view_action_more_bg);
		
		
        smoothListView.setRefreshEnable(true);
        smoothListView.setLoadMoreEnable(true);
	}

	private void initdata() {
		  // ListView数据
        travelingList = ModelUtil.getTravelingData();
        // 运营数据
        operationList = ModelUtil.getOperationData();

        // 广告数据
        adList = ModelUtil.getAdData();
		//广告数据
		adHeaderView = new HeaderAdViewView(this);
		adHeaderView.addView(adList,smoothListView);
		
		

        // 设置运营数据
        headerOperationViewView = new HeaderOperationView(this);
        headerOperationViewView.addView(operationList, smoothListView);
		
		
		headerfilterview= new HeaderFilterView(this);
		headerfilterview.addView(smoothListView);
		
		
		
		
		
        // 设置ListView数据
        mAdapter = new TravelingAdapter(this, travelingList);
       smoothListView.setAdapter(mAdapter);
	}

    private void initImageLoader(){
  /*      //网络图片例子,结合常用的图片缓存库UIL,你可以根据自己需求自己换其他网络图片库
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                showImageForEmptyUri(R.drawable.ic_launcher)
                .cacheInMemory(true).cacheOnDisk(true).build();*/

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(getApplicationContext());
        config.threadPriority(Thread.NORM_PRIORITY);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(getApplicationContext(), 5 * 1000, 5 * 1000));
        //		config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());

   
    }
	
	public void initListiner(){
		filterViewPosition = smoothListView.getHeaderViewsCount() - 1;
	       smoothListView.setRefreshEnable(true);
	        smoothListView.setLoadMoreEnable(true);
	        smoothListView.setSmoothListViewListener(this);
	        smoothListView.setOnScrollListener(new SmoothListView.OnSmoothScrollListener(){

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
				    isScrollIdle = (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE);
				  
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
				
					
					   if (isScrollIdle && adViewTopSpace < 0) return;
					      // 获取广告头部View、自身的高度、距离顶部的高度
		                if (itemHeaderAdView == null) {
		                    itemHeaderAdView = smoothListView.getChildAt(1-firstVisibleItem);
		                }
		                if (itemHeaderAdView != null) {
		                	
		                    adViewTopSpace = DensityUtil.px2dip(MainActivity.this, itemHeaderAdView.getTop());
		                    adViewHeight = DensityUtil.px2dip(MainActivity.this, itemHeaderAdView.getHeight());
		  
		                }
					   
		                // 获取筛选View、距离顶部的高度
		                if (itemHeaderFilterView == null) {
		                    itemHeaderFilterView = smoothListView.getChildAt(filterViewPosition - firstVisibleItem);
		                } 
		                if (itemHeaderFilterView != null) {
		                    filterViewTopSpace = DensityUtil.px2dip(MainActivity.this, itemHeaderFilterView.getTop());
		                }
		                
		                
		                // 处理筛选是否吸附在顶部
		                if (filterViewTopSpace > titleViewHeight) {
		                	 isStickyTop = false; // 没有吸附在顶部
		                	fv_top_filter.setVisibility(View.INVISIBLE);
		                } else {
		                	 isStickyTop = true; // 没有吸附在顶部
		                    fv_top_filter.setVisibility(View.VISIBLE);
		                }
		                
		                if (firstVisibleItem > filterViewPosition) {
		                   // isStickyTop = true;
		                	fv_top_filter.setVisibility(View.VISIBLE);
		                }
		                handleTitleBarColorEvaluate();
		                
				}


	

				@Override
				public void onSmoothScrolling(View view) {
					// TODO Auto-generated method stub
					
				}
	        	
	        });
	        
	        
	        
	        
	}


	
	@SuppressLint("NewApi")
	private void handleTitleBarColorEvaluate() {
		float fraction;
        if (adViewTopSpace > 0) {
            fraction = 1f - adViewTopSpace * 1f / 60;
            if (fraction < 0f) fraction = 0f;
            rlBar.setAlpha(fraction);
            return ;
        }

        float space = Math.abs(adViewTopSpace) * 1f;
        fraction = space / (adViewHeight - titleViewHeight);
        if (fraction < 0f) fraction = 0f;
        if (fraction > 1f) fraction = 1f;
        rlBar.setAlpha(1f);

        if (fraction >= 1f || isStickyTop) {
            isStickyTop = true;
            viewTitleBg.setAlpha(0f);
            viewActionMoreBg.setAlpha(0f);
            rlBar.setBackgroundColor(MainActivity.this.getResources().getColor(R.color.orange));
        } else {
            viewTitleBg.setAlpha(1f - fraction);
            viewActionMoreBg.setAlpha(1f - fraction);
            rlBar.setBackgroundColor(ColorUtil.getNewColorByStartEndColor(MainActivity.this, fraction, R.color.transparent, R.color.orange));
        }
		
	}
	@Override
	public void onRefresh() {
	    smoothListView.stopRefresh();
		
	}

	@Override
	public void onLoadMore() {
		
		 smoothListView.stopLoadMore();
	}


}
