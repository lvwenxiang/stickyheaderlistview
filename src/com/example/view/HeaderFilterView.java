package com.example.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.extra.ImageManager;
import com.example.stickyheaderlsitview.R;

public class HeaderFilterView extends LinearLayout{
    private List<ImageView> ivList;
    private ImageManager mImageManager;
    private LayoutInflater mInflate;
    
    
    
	public HeaderFilterView(Context context) {
		super(context);
	     mInflate = LayoutInflater.from(context);
	        ivList = new ArrayList<>();
	        mImageManager = new ImageManager(context);
	        initView(context);
		initView(context);
	}
	

	public HeaderFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
	     mInflate = LayoutInflater.from(context);
	        ivList = new ArrayList<>();
	        mImageManager = new ImageManager(context);
	        initView(context);
		initView(context);
	}
	
	
 
	  private void initView(Context context) {
		  View view = mInflate.inflate(R.layout.header_filter_layout,null);
		  addView(view);
		
	}

	  public void addView(ListView listView) {
	        View view = mInflate.inflate(R.layout.header_filter_layout, listView, false);
	        listView.addHeaderView(view);
	    }
}
