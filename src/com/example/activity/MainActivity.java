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
	   private List<TravelingEntity> travelingList = new ArrayList<TravelingEntity>(); // ListView����
	   private boolean isStickyTop = false; // �Ƿ������ڶ���
	   private int adViewHeight = 180; // �����ͼ�ĸ߶�
	   private int titleViewHeight = 50; // �������ĸ߶�
	   private TravelingAdapter mAdapter; // ��ҳ����
	   SmoothListView  smoothListView;
	   HeaderAdViewView adHeaderView;
	   HeaderFilterView headerfilterview;
	   private View itemHeaderAdView; // ��ListView��ȡ�Ĺ����View
	   private boolean isScrollIdle = true; // ListView�Ƿ��ڻ���
	   private int adViewTopSpace; // �����ͼ���붥���ľ���
	   private int filterViewPosition = 3; // ɸѡ��ͼ��λ��
	   private int filterViewTopSpace; // ɸѡ��ͼ���붥���ľ���
	   private View itemHeaderFilterView; // ��ListView��ȡ��ɸѡ��View
	   TextView fv_top_filter;
	    private HeaderOperationView headerOperationViewView; // ��Ӫ��ͼ
	   RelativeLayout rlBar;
	   View  viewTitleBg,viewActionMoreBg;
	   private List<OperationEntity> operationList = new ArrayList<>(); // ��Ӫ����
	   private List<String> adList = new ArrayList<>(); // �������
	   
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
		  // ListView����
        travelingList = ModelUtil.getTravelingData();
        // ��Ӫ����
        operationList = ModelUtil.getOperationData();

        // �������
        adList = ModelUtil.getAdData();
		//�������
		adHeaderView = new HeaderAdViewView(this);
		adHeaderView.addView(adList,smoothListView);
		
		

        // ������Ӫ����
        headerOperationViewView = new HeaderOperationView(this);
        headerOperationViewView.addView(operationList, smoothListView);
		
		
		headerfilterview= new HeaderFilterView(this);
		headerfilterview.addView(smoothListView);
		
		
		
		
		
        // ����ListView����
        mAdapter = new TravelingAdapter(this, travelingList);
       smoothListView.setAdapter(mAdapter);
	}

    private void initImageLoader(){
  /*      //����ͼƬ����,��ϳ��õ�ͼƬ�����UIL,����Ը����Լ������Լ�����������ͼƬ��
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
        //�޸����ӳ�ʱʱ��5�룬���س�ʱʱ��5��
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
					      // ��ȡ���ͷ��View������ĸ߶ȡ����붥���ĸ߶�
		                if (itemHeaderAdView == null) {
		                    itemHeaderAdView = smoothListView.getChildAt(1-firstVisibleItem);
		                }
		                if (itemHeaderAdView != null) {
		                	
		                    adViewTopSpace = DensityUtil.px2dip(MainActivity.this, itemHeaderAdView.getTop());
		                    adViewHeight = DensityUtil.px2dip(MainActivity.this, itemHeaderAdView.getHeight());
		  
		                }
					   
		                // ��ȡɸѡView�����붥���ĸ߶�
		                if (itemHeaderFilterView == null) {
		                    itemHeaderFilterView = smoothListView.getChildAt(filterViewPosition - firstVisibleItem);
		                } 
		                if (itemHeaderFilterView != null) {
		                    filterViewTopSpace = DensityUtil.px2dip(MainActivity.this, itemHeaderFilterView.getTop());
		                }
		                
		                
		                // ����ɸѡ�Ƿ������ڶ���
		                if (filterViewTopSpace > titleViewHeight) {
		                	 isStickyTop = false; // û�������ڶ���
		                	fv_top_filter.setVisibility(View.INVISIBLE);
		                } else {
		                	 isStickyTop = true; // û�������ڶ���
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
