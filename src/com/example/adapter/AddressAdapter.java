package com.example.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.SortContactbean;
import com.example.extra.MyLog;
import com.example.stickyheaderlsitview.R;
import com.example.widget.SwipeMenuLayout;

public class AddressAdapter extends
		RecyclerView.Adapter<AddressAdapter.ViewHolder> {
	protected Context mContext;
	protected List<SortContactbean> mDatas;
	protected LayoutInflater mInflater;

	public List<SortContactbean> getDatas() {
		return mDatas;
	}

	public AddressAdapter setDatas(List<SortContactbean> datas) {
		mDatas = datas;
		return this;
	}

	public AddressAdapter(Context mContext, List<SortContactbean> mDatas) {
		this.mContext = mContext;
		this.mDatas = mDatas;
		mInflater = LayoutInflater.from(mContext);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView tvCity;
		ImageView avatar;
		View content;

		public ViewHolder(View itemView) {
			super(itemView);
			tvCity = (TextView) itemView.findViewById(R.id.tvCity);
			avatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
			content = itemView.findViewById(R.id.content);
		}
	}

	@Override
	public int getItemCount() {
		return mDatas != null ? mDatas.size() : 0;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, final int position) {
		final SortContactbean sortcontactbean = mDatas.get(position);
		holder.tvCity.setText(sortcontactbean.getCity());
		holder.content.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(mContext, "pos:" + position, Toast.LENGTH_SHORT)
						.show();
			}
		});
		holder.avatar.setImageResource(R.drawable.friend);
		holder.itemView.findViewById(R.id.btnDel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						((SwipeMenuLayout) holder.itemView).quickClose();
						mDatas.remove(holder.getPosition());
						notifyDataSetChanged();
						// notifyItemRemoved(holder.getPosition());
					}
				});
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
		// return new ViewHolder(mInflater.inflate(R.layout.item_address, arg0,
		// false));
		return new ViewHolder(mInflater.inflate(R.layout.item_city_swipe, arg0,
				false));
	}
}
