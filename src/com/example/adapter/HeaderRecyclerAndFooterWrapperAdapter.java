package com.example.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.example.extra.MyLog;
import com.example.tools.ViewHolder;

public abstract class HeaderRecyclerAndFooterWrapperAdapter extends
		RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int BASE_ITEM_TYPE_HEADER = 1000000;// headerview��viewtype��׼ֵ
	private static final int BASE_ITEM_TYPE_FOOTER = 2000000;// footerView��ViewType��׼ֵ
	@SuppressWarnings("rawtypes")
	private SparseArrayCompat<SparseArrayCompat> mHeaderDatas = new SparseArrayCompat<SparseArrayCompat>();
	private SparseArrayCompat<View> mFooterViews = new SparseArrayCompat<>();// ���FooterViews,key��viewType
	@SuppressWarnings("rawtypes")
	protected RecyclerView.Adapter mInnerAdapter;// �ڲ��ĵ���ͨAdapter

	public HeaderRecyclerAndFooterWrapperAdapter(
			RecyclerView.Adapter mInnerAdapter) {
		this.mInnerAdapter = mInnerAdapter;
	}

	@Override
	public int getItemCount() {
		return getInnerItemCount() + getHeaderViewCount()
				+ getFooterViewCount();
	}

	/**
	 * ����position �ж��Ƿ���headerview
	 * 
	 * @param position
	 * @return
	 */
	public boolean isHeaderViewPos(int position) {// ������ 2 ��ͷ��pos 0 1��true�� 2+
													// false
		return getHeaderViewCount() > position;
	}

	public int getHeaderViewCount() {
		return mHeaderDatas.size();
	}

	public int getFooterViewCount() {
		return mFooterViews.size();
	}

	public boolean isFooterViewPos(int position) {// ������ 2��ͷ��2��inner��pos 0 1 2 3
													// ,false,4+true
		return position >= getHeaderViewCount() + getInnerItemCount();
	}

	private int getInnerItemCount() {
		return mInnerAdapter != null ? mInnerAdapter.getItemCount() : 0;
	}

	protected abstract void onBindHeaderHolder(ViewHolder holder,
			int headerPos, int layoutId, Object o);// ��ش�һ��layoutId��ȥ�������ж��ǵڼ���headerview

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (isHeaderViewPos(position)) {
			int layoutId = mHeaderDatas.get(getItemViewType(position)).keyAt(0);
			onBindHeaderHolder((ViewHolder) holder, position, layoutId,
					mHeaderDatas.get(getItemViewType(position)).get(layoutId));
			return;
		} else if (isFooterViewPos(position)) {
			return;
		}
		// �����ӣ�2��header��0 1��ͷ��2�ǿ�ʼ��2-2 = 0
		mInnerAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
	}

	@Override
	public int getItemViewType(int position) {
		MyLog.i("TAG", "getItemViewType(position)====" + position);
		if (isHeaderViewPos(position)) {
			return mHeaderDatas.keyAt(position);
		} else if (isFooterViewPos(position)) {// ������header 2�� innter 2��
												// 0123�����ǣ�4���ǣ�4-2-2 = 0��ok��
			return mFooterViews.keyAt(position - getHeaderViewCount()
					- getInnerItemCount());
		}
		return super.getItemViewType(position - getHeaderViewCount());
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
			int viewType) {

		MyLog.i("TAG", "onCreateViewHolder====" + viewType);
		if (mHeaderDatas.get(viewType) != null) {// ��Ϊ�գ�˵����headerview

			return ViewHolder.get(parent.getContext(), null, parent,
					mHeaderDatas.get(viewType).keyAt(0), -1);
		} else if (mFooterViews.get(viewType) != null) {// ��Ϊ�գ�˵����footerview
			return new ViewHolder(parent.getContext(),
					mFooterViews.get(viewType));
		}
		return mInnerAdapter.onCreateViewHolder(parent, viewType);
	}

	/**
	 * ���HeaderView
	 * 
	 * @param layoutId
	 *            headerView ��LayoutId
	 * @param data
	 *            headerView ��data(���ܶ��ֲ�ͬ���͵�header ֻ����Object��)
	 */
	public void addHeaderView(int layoutId, Object data) {
		// mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, v);
		SparseArrayCompat headerContainer = new SparseArrayCompat();
		headerContainer.put(layoutId, data);

		// MyLog.i("TAG", "mHeaderDatas.size()1====" + mHeaderDatas.size());
		mHeaderDatas.put(mHeaderDatas.size() + BASE_ITEM_TYPE_HEADER,
				headerContainer);
	}

	public void addHeaderView(int headerPos, int layoutId, Object data) {
		if (mHeaderDatas.size() > headerPos) {
			SparseArrayCompat headerContainer = new SparseArrayCompat();
			headerContainer.put(layoutId, data);
			mHeaderDatas.setValueAt(headerPos, headerContainer);
		} else if (mHeaderDatas.size() == headerPos) {// ����addHeaderView
			addHeaderView(layoutId, data);
		} else {
			//
			addHeaderView(layoutId, data);
		}
	}

//	
//	  @Override public void onAttachedToRecyclerView(RecyclerView recyclerView)
//	  { mInnerAdapter.onAttachedToRecyclerView(recyclerView); //Ϊ�˼���GridLayout
//	  RecyclerView.LayoutManager layoutManager =
//	  recyclerView.getLayoutManager(); if (layoutManager instanceof
//	  GridLayoutManager) { final GridLayoutManager gridLayoutManager =
//	  (GridLayoutManager) layoutManager; final GridLayoutManager.SpanSizeLookup
//	  spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
//	  
//	  gridLayoutManager.setSpanSizeLookup(new
//	  GridLayoutManager.SpanSizeLookup() {
//	  
//	  @Override public int getSpanSize(int position) { int viewType =
//	  getItemViewType(position); if (mHeaderDatas.get(viewType) != null) {
//	  return gridLayoutManager.getSpanCount(); } else if
//	  (mFooterViews.get(viewType) != null) { return
//	  gridLayoutManager.getSpanCount(); } if (spanSizeLookup != null) return
//	  spanSizeLookup.getSpanSize(position); return 1; } });
//	  gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount()); }
//	  
//	  }
	 
	@Override
	public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
		mInnerAdapter.onViewAttachedToWindow(holder);
		int position = holder.getPosition();
		if (isHeaderViewPos(position) || isFooterViewPos(position)) {
			ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

			if (lp != null
					&& lp instanceof StaggeredGridLayoutManager.LayoutParams) {

				StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

				p.setFullSpan(true);
			}
		}
	}
}
