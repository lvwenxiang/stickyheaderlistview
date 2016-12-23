package com.example.util;

import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {

	
	
	//view.setAnimation(shakeAnimation());
	private Animation shakeAnimation() {
		Animation animation = new TranslateAnimation(0, 30, 0, 0);
		animation.setInterpolator(new CycleInterpolator(1));
		animation.setDuration(300);
		return animation;
	}
}
