package com.example.tools;

import android.annotation.SuppressLint;
import android.view.View;

public class FlipVerticalTransformer extends ABaseTransformer {

	@SuppressLint("NewApi")
	@Override
	protected void onTransform(View view, float position) {
		final float rotation = -180f * position;

		view.setAlpha(rotation > 90f || rotation < -90f ? 0f : 1f);
		view.setPivotX(view.getWidth() * 0.5f);
		view.setPivotY(view.getHeight() * 0.5f);
		view.setRotationX(rotation);
	}

}
