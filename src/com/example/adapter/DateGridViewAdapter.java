package com.example.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bean.DateState;
import com.example.stickyheaderlsitview.R;

public class DateGridViewAdapter extends BaseAdapter {

	private ArrayList<DateState> dateStates;
	private Context mContext;

	private void setData(ArrayList<DateState> dateStates) {
		if (dateStates != null) {
			this.dateStates = dateStates;
		} else {
			dateStates = new ArrayList<DateState>();
		}
	}

	public DateGridViewAdapter(Context context, ArrayList<DateState> dateStates) {
		mContext = context;
		setData(dateStates);
	}

	@Override
	public int getCount() {
		return dateStates.size();
	}

	@Override
	public Object getItem(int position) {
		return dateStates.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mContext,
					R.layout.calender_item_new_item, null);

			holder.tv_1_1 = (TextView) convertView.findViewById(R.id.tv_1_1);
			holder.tv_1_2 = (TextView) convertView.findViewById(R.id.tv_1_2);
			holder.iv1 = (ImageView) convertView.findViewById(R.id.iv1);
			holder.row_1 = (View) convertView.findViewById(R.id.row_1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		DateState d = (DateState) getItem(position);
		if ("work".equals(d.getState())) {
			holder.tv_1_1.setText("ÉÏ°à/ÏÂ°à");
			holder.iv1.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.work_goback));
		} else if ("late".equals(d.getState())) {
			holder.tv_1_1.setText("³Ùµ½/ÔçÍË");
			holder.iv1.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.work_goback));
		} else if ("out".equals(d.getState())) {
			holder.tv_1_1.setText("³ö²î");
			holder.iv1.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.work_goback));
		} else if ("over".equals(d.getState())) {
			holder.tv_1_1.setText("¼Ó°à");
			holder.iv1.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.work_overtime));
		} else if ("vacation".equals(d.getState())) {
			holder.tv_1_1.setText("ÐÝ¼Ù");
			holder.iv1.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.work_goback));
		} else if ("remark".equals(d.getState())) {
			holder.tv_1_1.setText("±¸×¢");
			holder.iv1.setImageDrawable(mContext.getResources().getDrawable(
					R.drawable.work_remark));
		}
		if (dateStates.size() > 0) {
			if (position == dateStates.size() - 1) {
				holder.row_1.setVisibility(View.GONE);
			} else {
				holder.row_1.setVisibility(View.VISIBLE);
			}
		}
		holder.tv_1_2.setText(d.getContent());
		return convertView;
	}

	private class ViewHolder {
		ImageView iv1;
		TextView tv_1_1;
		TextView tv_1_2;
		View row_1;
	}
}
