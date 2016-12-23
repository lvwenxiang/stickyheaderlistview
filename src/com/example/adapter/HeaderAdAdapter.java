package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HeaderAdAdapter extends PagerAdapter {

	
	   private Context mContext;
	    private List<ImageView> ivList; // ImageView的集合
	    private int count = 1; // 广告的数量
	    private ViewPager vp;
	    
	    
	    public HeaderAdAdapter(Context context, List<ImageView> ivList,ViewPager vp) {
	        super();
	        this.mContext = context;
	        this.ivList = ivList;
	        this.vp = vp;
	        if(ivList != null && ivList.size() > 0){
	            count = ivList.size();
	        }
	    }
	    
	
	@Override
	public int getCount() {
		  if (count == 1) {
	            return 1;
	        } else {
	            return Integer.MAX_VALUE;
	        }
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		 
		        return arg0 == arg1;
		
	}
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	  View view = (View) object;
          container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int newPosition = position % count;
        // 先移除在添加，更新图片在container中的位置（把iv放至container末尾）
        ImageView iv = ivList.get(newPosition);
     //   container.removeView(iv);
        container.addView(iv);
        return iv;
    }
    
/*    @Override
    public void finishUpdate(View container) {
    	   int position = vp.getCurrentItem();
           if (position == 0) {
               position = getFristItem();
           } else if (position == getCount() - 1) {
               position = getLastItem();
           }
           try {
           	vp.setCurrentItem(position, false);
           }catch (IllegalStateException e){}
    }*/
 
    

    public int getFristItem() {
    	  return  ivList.size();
    }
    
    public int getLastItem() {
    	        return ivList.size() - 1;
    }
    
}
