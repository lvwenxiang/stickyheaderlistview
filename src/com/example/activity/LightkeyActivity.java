package com.example.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.stickyheaderlsitview.R;

public class LightkeyActivity extends Activity {
	private Button btn_k;
	private Button btn_k2;
	private Button btn_k3;
	private Button btn_k4;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lightkey);
		btn_k = (Button) findViewById(R.id.kaoqin);
		btn_k2 = (Button) findViewById(R.id.kaoqin2);
		btn_k3 = (Button) findViewById(R.id.kaoqinrili1);
		btn_k4 = (Button) findViewById(R.id.kaoqinrili2);
		btn_k.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LightkeyActivity.this,
						MyRankingActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
		btn_k2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(LightkeyActivity.this,
						MyRateActivity.class);
				startActivity(intent);
				overridePendingTransition(0, 0);
			}
		});
		btn_k3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(LightkeyActivity.this, CalenderActivity1.class)
				.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				overridePendingTransition(0, 0);
			}
		});
		btn_k4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startActivity(new Intent(LightkeyActivity.this, CalenderActivity2.class)
				.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
				overridePendingTransition(0, 0);
			}
		});
	}
}