package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import com.example.extra.ImageManager;
import com.example.stickyheaderlsitview.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public abstract class BaseListAdapter<E> extends BaseAdapter {

	 private List<E> mList = new ArrayList<E>();
	    protected Context mContext;
	    protected LayoutInflater mInflater;
	    protected ImageManager mImageManager;
	    protected DisplayImageOptions   options;
	    public BaseListAdapter(Context context) {
	    	initoption();
	        mContext = context;
	        mInflater = LayoutInflater.from(context);
	        mImageManager = new ImageManager(context);	   
	    }

	    public BaseListAdapter(Context context, List<E> list) {
	        this(context);
	        initoption();
	        mList = list;
	        mInflater = LayoutInflater.from(context);
	        mImageManager = new ImageManager(context);        
	    }

	    public void initoption(){
		      options=new DisplayImageOptions.Builder()
		        .cacheInMemory(true)
		        .cacheOnDisk(false)
		        .showImageForEmptyUri(R.drawable.ic_launcher)
		        .showImageOnFail(R.drawable.ic_launcher)
		        .showImageOnLoading(R.drawable.ic_launcher)
		        .bitmapConfig(Bitmap.Config.RGB_565)
		        .displayer(new SimpleBitmapDisplayer()).build();	       
	    }

	    
	    
	    @Override
	    public int getCount() {
	        return mList.size();
	    }

	    public void clearAll() {
	        mList.clear();
	    }

	    public List<E> getData() {
	        return mList;
	    }

	    public void addALL(List<E> lists){
	        if(lists==null||lists.size()==0){
	            return ;
	        }
	        mList.addAll(lists);
	    }
	    public void add(E item){
	        mList.add(item);
	    }

	    @Override
	    public E getItem(int position) {
	        return (E) mList.get(position);
	    }

	    @Override
	    public long getItemId(int position) {
	        return position;
	    }

	    public void removeEntity(E e){
	        mList.remove(e);
	    }


}
