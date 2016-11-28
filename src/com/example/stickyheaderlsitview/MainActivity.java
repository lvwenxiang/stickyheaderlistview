package com.example.stickyheaderlsitview;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity implements SmoothListView.ISmoothListViewListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}


}
