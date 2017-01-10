package com.example.activity;


import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.stickyheaderlsitview.R;
import com.example.widget.ClipImageLayout;
import com.example.widget.MatrixImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class ClipHeadportraitActivity extends  Activity implements MatrixImageView.OnMovingListener,MatrixImageView.OnSingleTapListener{
	private boolean mChildIsBeingDragged=false;
	private MatrixImageView.OnSingleTapListener onSingleTapListener;
	private DisplayImageOptions options;
	private ClipImageLayout clipimagelayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip);
		
	//	MatrixImageView imageView = (MatrixImageView) findViewById(R.id.id_clipImageLayout);
		 clipimagelayout = (ClipImageLayout) findViewById(R.id.id_clipImageLayout);
//		imageView.setOnMovingListener(this);
//		imageView.setOnSingleTapListener(this);
//		initoption();
//		ImageLoader.getInstance().displayImage("http://img2.3lian.com/2014/f2/37/d/39.jpg", imageView);
//	imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.a));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.id_action_clip:
			Bitmap bitmap = clipimagelayout.clip();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
			byte[] datas = baos.toByteArray();
			
			Intent intent = new Intent(this, ShowClipHeadActivity.class);
			intent.putExtra("bitmap", datas);
			startActivity(intent);

			break;
		}
		return super.onOptionsItemSelected(item);
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
	public void startDrag() {
		// TODO Auto-generated method stub
		mChildIsBeingDragged=true;
	}

	@Override
	public void stopDrag() {
		// TODO Auto-generated method stub
		mChildIsBeingDragged=false;
	}


	@Override
	public void onSingleTap() {
		// TODO Auto-generated method stub
		
	}
}
