/**
 * 
 */
package com.example.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.extra.MyLog;
import com.example.stickyheaderlsitview.R;

/**
 * @author wenxiang.lv
 *
 */
public class EleSearchActivity extends BaseActivity {
	 private EditText mSearchBGTxt;
	    private TextView mHintTxt;
	    private TextView mSearchTxt;
	    private FrameLayout mContentFrame;
	    private ImageView mArrowImg;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_ele_search);

	        mSearchBGTxt = (EditText) findViewById(R.id.tv_search_bg);
	        mHintTxt = (TextView) findViewById(R.id.tv_hint);
	        mContentFrame = (FrameLayout) findViewById(R.id.frame_content_bg);
	        mSearchTxt = (TextView) findViewById(R.id.tv_search);
	        mArrowImg = (ImageView) findViewById(R.id.iv_arrow);

	        mSearchBGTxt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
	            @SuppressLint("NewApi")
				@Override
	            public void onGlobalLayout() {
	                mSearchBGTxt.getViewTreeObserver().removeOnGlobalLayoutListener(this);

	                performEnterAnimation();
	            }
	        });


	    }

		private void performEnterAnimation() {
			 float originY = getIntent().getIntExtra("y", 0);
		        int location[] = new int[2];
		        mSearchBGTxt.getLocationOnScreen(location);

		        final float translateY = originY - (float) location[1];
		        
		        
		        MyLog.i("TAG", "translateY==="+translateY);
		      //�ŵ�ǰһ��ҳ���λ��
		        mSearchBGTxt.setY(mSearchBGTxt.getY() + translateY);
		        mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
		        mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
		        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), mSearchBGTxt.getY() - 100);
		        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		            @Override
		            public void onAnimationUpdate(ValueAnimator valueAnimator) {
		                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());

		                mArrowImg.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
		                mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
		                mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
		            }
		        });
		        ValueAnimator scaleVa = ValueAnimator.ofFloat(1, 0.8f);
		        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		            @Override
		            public void onAnimationUpdate(ValueAnimator valueAnimator) {
		                mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
		            }
		        });
		        ValueAnimator alphaVa = ValueAnimator.ofFloat(0, 1f);
		        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		            @Override
		            public void onAnimationUpdate(ValueAnimator valueAnimator) {
		                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());
		                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
		                mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
		            }
		        });
		        alphaVa.setDuration(500);
		        translateVa.setDuration(500);
		        scaleVa.setDuration(500);

		        alphaVa.start();
		        translateVa.start();
		        scaleVa.start();
		}
		
		
		 @Override
		    public void onBackPressed() {
		        performExitAnimation();
		    }

		    private void performExitAnimation() {
		        float originY = getIntent().getIntExtra("y", 0);

		        int location[] = new int[2];
		        mSearchBGTxt.getLocationOnScreen(location);

		        final float translateY = originY - (float) location[1];


		        final ValueAnimator translateVa = ValueAnimator.ofFloat(mSearchBGTxt.getY(), mSearchBGTxt.getY()+translateY);
		        translateVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		            @Override
		            public void onAnimationUpdate(ValueAnimator valueAnimator) {
		                mSearchBGTxt.setY((Float) valueAnimator.getAnimatedValue());
		                mArrowImg.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mArrowImg.getHeight()) / 2);
		                mHintTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mHintTxt.getHeight()) / 2);
		                mSearchTxt.setY(mSearchBGTxt.getY() + (mSearchBGTxt.getHeight() - mSearchTxt.getHeight()) / 2);
		            }
		        });
		        translateVa.addListener(new Animator.AnimatorListener() {
		            @Override
		            public void onAnimationStart(Animator animator) {

		            }

		            @Override
		            public void onAnimationEnd(Animator animator) {
		                finish();
		                overridePendingTransition(0, 0);
		            }

		            @Override
		            public void onAnimationCancel(Animator animator) {

		            }

		            @Override
		            public void onAnimationRepeat(Animator animator) {

		            }
		        });

		        ValueAnimator scaleVa = ValueAnimator.ofFloat(0.8f, 1f);
		        scaleVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		            @Override
		            public void onAnimationUpdate(ValueAnimator valueAnimator) {
		                mSearchBGTxt.setScaleX((Float) valueAnimator.getAnimatedValue());
		            }
		        });

		        ValueAnimator alphaVa = ValueAnimator.ofFloat(1, 0f);
		        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		            @Override
		            public void onAnimationUpdate(ValueAnimator valueAnimator) {
		                mContentFrame.setAlpha((Float) valueAnimator.getAnimatedValue());

		                mArrowImg.setAlpha((Float) valueAnimator.getAnimatedValue());
		                mSearchTxt.setAlpha((Float) valueAnimator.getAnimatedValue());
		            }
		        });


		        alphaVa.setDuration(500);
		        translateVa.setDuration(500);
		        scaleVa.setDuration(500);

		        alphaVa.start();
		        translateVa.start();
		        scaleVa.start();

		    }
}
