package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HeaderAdAdapter extends PagerAdapter {

	
	   private Context mContext;
	    private List<ImageView> ivList; // ImageView�ļ���
	    private int count = 1; // ��������
	    
	    
	    public HeaderAdAdapter(Context context, List<ImageView> ivList) {
	        super();
	        this.mContext = context;
	        this.ivList = ivList;
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
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int newPosition = position % count;
        // ���Ƴ�����ӣ�����ͼƬ��container�е�λ�ã���iv����containerĩβ��
        ImageView iv = ivList.get(newPosition);
        container.removeView(iv);
        container.addView(iv);
        return iv;
    }
}
