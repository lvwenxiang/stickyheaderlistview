package com.example.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.adapter.HeaderOperationAdapter;
import com.example.extra.ImageManager;
import com.example.stickyheaderlsitview.R;

public class HeaderOperationView {
	private List<ImageView> ivList;
	private ImageManager mImageManager;
	private LayoutInflater mInflate;
	Context context;

	public HeaderOperationView(Activity context) {
		this.context = context;
		mInflate = LayoutInflater.from(context);
		ivList = new ArrayList<>();
		mImageManager = new ImageManager(context);
	}

	public void addView(List list, ListView listView) {
		View view = mInflate.inflate(R.layout.header_operation_layout,
				listView, false);
		GridView gvOperation = (GridView) view.findViewById(R.id.gv_operation);
		HeaderOperationAdapter adapter = new HeaderOperationAdapter(context,
				list);
		gvOperation.setAdapter(adapter);
		listView.addHeaderView(view);
	}
}
