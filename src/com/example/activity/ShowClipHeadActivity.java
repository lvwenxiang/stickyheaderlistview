package com.example.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.stickyheaderlsitview.R;
import com.example.widget.RoundImageView;

public class ShowClipHeadActivity extends Activity {
	private ImageView mImageView;
	private RoundImageView mImageView2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showcliphead_activity);

		mImageView = (ImageView) findViewById(R.id.id_showImage);
//		mImageView2 = (RoundImageView) findViewById(R.id.id_showImage);
		byte[] b = getIntent().getByteArrayExtra("bitmap");
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		if (bitmap != null) {
			mImageView.setImageBitmap(bitmap);
		}
	}
}
