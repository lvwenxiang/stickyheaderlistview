package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mode.TravelingEntity;
import com.example.stickyheaderlsitview.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TravelingAdapter extends BaseListAdapter<TravelingEntity> {

	private boolean isNoData;
	private int mHeight;
	public static final int ONE_SCREEN_COUNT = 7; // һ������ʾ�ĸ��������������Ļ�߶Ⱥ͸��Ե�����
	public static final int ONE_REQUEST_COUNT = 10; // һ������ĸ���
	
	
	public TravelingAdapter(Context context) {
		super(context);
	}

	public TravelingAdapter(Context context, List<TravelingEntity> list) {
		super(context, list);
	}

	// ��������
	public void setData(List<TravelingEntity> list) {
		clearAll();
		addALL(list);

		isNoData = false;
		if (list.size() == 1 && list.get(0).isNoData()) {
			// �������ݲ���
			isNoData = list.get(0).isNoData();
			mHeight = list.get(0).getHeight();
		} else {
			// ��ӿ�����
			if (list.size() < ONE_SCREEN_COUNT) {
				addALL(createEmptyList(ONE_SCREEN_COUNT - list.size()));
			}
		}
		notifyDataSetChanged();
	}

	// ��������һ���Ŀ�����
	public List<TravelingEntity> createEmptyList(int size) {
		List<TravelingEntity> emptyList = new ArrayList<>();
		if (size <= 0)
			return emptyList;
		for (int i = 0; i < size; i++) {
			emptyList.add(new TravelingEntity());
		}
		return emptyList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// ��������
		if (isNoData) {
			convertView = mInflater.inflate(R.layout.item_no_data_layout, null);
			AbsListView.LayoutParams params = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
			RelativeLayout rootView = (RelativeLayout) convertView
					.findViewById(R.id.rl_root_view);
			rootView.setLayoutParams(params);
			return convertView;
		}

		// ��������
		final ViewHolder holder;
		if (convertView != null && convertView instanceof LinearLayout) {
			holder = (ViewHolder) convertView.getTag();
		} else {
			convertView = mInflater.inflate(R.layout.item_travel, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}

		TravelingEntity entity = getItem(position);
		holder.llRootView=(LinearLayout) convertView.findViewById(R.id.ll_root_view);		
		holder.llRootView.setVisibility(View.VISIBLE);
		holder.ivImage=(ImageView) convertView.findViewById(R.id.iv_image);
		holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
		holder.tvRank=(TextView) convertView.findViewById(R.id.tv_rank);	
		
		if (TextUtils.isEmpty(entity.getType())) {
			holder.llRootView.setVisibility(View.INVISIBLE);
			return convertView;
		}
		
	
        ImageLoader.getInstance().displayImage(entity.getImage_url(),holder.ivImage,options);
	
		holder.tvTitle.setText(entity.getFrom() + entity.getTitle()
				+ entity.getType());
		holder.tvRank.setText("������" + entity.getRank());
	//	mImageManager.loadUrlImage(entity.getImage_url(), holder.ivImage);

		return convertView;
	}

	static class ViewHolder {
		LinearLayout llRootView;
		ImageView ivImage;
		TextView tvTitle;
		TextView tvRank;
		ViewHolder(View view) {
		}

	}
}
