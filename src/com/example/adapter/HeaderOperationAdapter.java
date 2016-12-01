package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mode.OperationEntity;
import com.example.stickyheaderlsitview.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class HeaderOperationAdapter extends BaseListAdapter<OperationEntity> {
	public HeaderOperationAdapter(Context context) {
        super(context);
    }

    public HeaderOperationAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_operation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
    	holder.ivImage=(ImageView) convertView.findViewById(R.id.iv_image);
		holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
		holder.tvSubtitle=(TextView) convertView.findViewById(R.id.tv_subtitle);	
        OperationEntity entity = getItem(position);

        holder.tvTitle.setText(entity.getTitle());

        DisplayImageOptions   options=new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(false)
        .showImageForEmptyUri(R.drawable.ic_launcher)
        .showImageOnFail(R.drawable.ic_launcher)
        .showImageOnLoading(R.drawable.ic_launcher)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .displayer(new SimpleBitmapDisplayer()).build();
        ImageLoader.getInstance().displayImage(entity.getImage_url(),holder.ivImage,options);
        
       // mImageManager.loadUrlImage(entity.getImage_url(), holder.ivImage);
        if (TextUtils.isEmpty(entity.getSubtitle())) {
            holder.tvSubtitle.setVisibility(View.INVISIBLE);
        } else {
            holder.tvSubtitle.setVisibility(View.VISIBLE);
            holder.tvSubtitle.setText(entity.getSubtitle());
        }

        return convertView;
    }

    static class ViewHolder {
    	
		LinearLayout llRootView;
		ImageView ivImage;
		TextView tvTitle;
		TextView tvSubtitle;
		ViewHolder(View view) {
		}
    }
}
