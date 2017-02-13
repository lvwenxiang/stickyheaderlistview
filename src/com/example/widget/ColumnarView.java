package com.example.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.example.bean.WeekRankingbean;
import com.example.extra.MyLog;
import com.example.util.DensityUtil;

//���ƹ�Կ�׿���ͼ��
public class ColumnarView extends View {
	private Paint xLinePaint;// ������ ���� ���ʣ�
	private Paint hLinePaint;// ������ˮƽ�ڲ� ���߻���
	private Paint titlePaint;// �����ı��Ļ���
	private Paint paint;// ���λ��� ��״ͼ����ʽ��Ϣ
	private int[] progress;// 7 ������ʾ������״������
	private int[] aniProgress;// ʵ�ֶ�����ֵ
	private String[] colors;
	private final int TRUE = 1;// ����״ͼ����ʾ����
	private int[] text;// ���õ���¼�����ʾ��һ����״����Ϣ
	// private Bitmap bitmap;

	private String[] ySteps;// ��������������
	private String[] xWeeks;// ������ײ���������
	private boolean flag;// �Ƿ�ʹ�ö���
	private Context mContext;
	private HistogramAnimation ani;
	private float max;
	private int startX;
	private int paddingRightX;
	private int columnWidth;

	public ColumnarView(Context context) {
		super(context);
		init(context);
	}

	public ColumnarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context) {
		mContext = context;
		ySteps = new String[] { "10k", "7.5k", "5k", "2.5k", "0" };// Y������ֵ
		xWeeks = new String[] { "0��", "0��", "0��", "0��" };// X������
		colors = new String[] { "#FF911B", "#FF6B5B", "#1FB9ED", "#0CDC72" };
		progress = new int[] { 2000, 5000, 6000, 8000 };
		text = new int[] { 0, 0, 0, 0 };// �������ֵ
		aniProgress = new int[] { 20, 20, 20, 20 };// ����
		max = 10000f;
		startX = DensityUtil.dip2px(mContext, 35);
		paddingRightX = DensityUtil.dip2px(mContext, 30);
		columnWidth = DensityUtil.dip2px(mContext, 35);

		ani = new HistogramAnimation();
		ani.setDuration(1500);// ����ʱ��

		xLinePaint = new Paint();
		hLinePaint = new Paint();
		titlePaint = new Paint();
		paint = new Paint();

		// ������������ɫ
		xLinePaint.setColor(Color.DKGRAY);
		hLinePaint.setColor(Color.LTGRAY);
		titlePaint.setColor(Color.BLACK);

		// ���ػ�ͼ
		// bitmap = BitmapFactory
		// .decodeResource(getResources(), R.drawable.hudong);
	}
	
	public void setDate(ArrayList<WeekRankingbean> rankingOAs, float max) {
		if (rankingOAs != null && rankingOAs.size() > 0) {
			xWeeks = new String[rankingOAs.size()];
			colors = new String[rankingOAs.size()];
			text = new int[rankingOAs.size()];
			aniProgress = new int[rankingOAs.size()];
			progress = new int[rankingOAs.size()];
			for (int i = 0; i < rankingOAs.size(); i++) {
				xWeeks[i] = rankingOAs.get(i).getWEEKS() + "��";
				colors[i] = rankingOAs.get(i).getColor();
				progress[i] = rankingOAs.get(i).getWEEK_ORDER();
				text[i] = 0;
				aniProgress[i] = 0;
			}
			ySteps = new String[] { (int) max + "", (int) (max - max / 4) + "",
					(int) (max - max / 2) + "", (int) (max - max * 3 / 4) + "",
					0 + "" };
			this.max = max;
			invalidate();
		}
	}	
	public void start(boolean flag) {
		this.flag = flag;
		this.startAnimation(ani);
	}	
	protected void onDraw(Canvas canvas) {
		int width = getWidth();// ���ÿռ���
		int height = getHeight() - DensityUtil.dip2px(mContext, 50);// ���ÿռ�߶ȣ��ײ�Ԥ��50dip
		
		// ����X����ߣ���㣨30dip,height+3dip���յ㣨width-30dip,height+3dip��
				canvas.drawLine(startX, height + DensityUtil.dip2px(mContext, 3), width
						- paddingRightX, height + DensityUtil.dip2px(mContext, 3),
						xLinePaint);
				
				int leftHeight = height - DensityUtil.dip2px(mContext, 5);// �߶�
				int hPerHeight = leftHeight / 4;// �ֳ��Ĳ���

				hLinePaint.setTextAlign(Align.CENTER);// ����ˮƽ���У�Ȼ��drawText��X��������Ϊwidth/2���ɡ�
				for (int i = 0; i < 4; i++) {
					canvas.drawLine(startX, DensityUtil.dip2px(mContext, 10) + i
							* hPerHeight, width - paddingRightX,
							DensityUtil.dip2px(mContext, 10) + i * hPerHeight,
							hLinePaint);
				}
				
				// ���� Y ������
				titlePaint.setTextAlign(Align.RIGHT);// ����ˮƽ����
				titlePaint.setTextSize(DensityUtil.dip2px(mContext, 10));
				titlePaint.setAntiAlias(true);
				titlePaint.setStyle(Paint.Style.FILL);

				// ����Y�ܵ����� �㣨25dip,13dip��
				// ��25dip+hPerHeight,13dip��
				// ��25dip+2hPerHeight,13dip��
				// ��25dip+3hPerHeight,13dip��
				// ��25dip+4hPerHeight,13dip��
				for (int i = 0; i < ySteps.length; i++) {
					canvas.drawText(ySteps[i], startX - DensityUtil.dip2px(mContext, 3),
							DensityUtil.dip2px(mContext, 13) + i * hPerHeight,
							titlePaint);
				}
				
				// ���� X �� ������
				int xAxisLength = width - startX;// width-30dip
				int columCount = xWeeks.length + 1;// 8
				int step = xAxisLength / columCount;// (width-30dip)/8
				for (int i = 0; i < columCount - 1; i++) {
					canvas.drawText(xWeeks[i],
							columnWidth - (columnWidth - titlePaint.measureText("38��"))
									/ 2F + step * (i + 1),
							height + DensityUtil.dip2px(mContext, 20), titlePaint);
				}
				// ���ƾ���
				if (aniProgress != null && aniProgress.length > 0) {
					for (int i = 0; i < aniProgress.length; i++) {// ѭ��������7����״ͼ�λ�����
						int value = aniProgress[i];
						paint.setAntiAlias(true);// �����Ч��
						paint.setStyle(Paint.Style.FILL);
						paint.setTextSize(DensityUtil.sp2px(mContext, 12));// �����С
						paint.setColor(Color.parseColor(colors[i]));// ������ɫ
						Rect rect = new Rect();// ��״ͼ����״

						rect.left = step * (i + 1);
						rect.right = columnWidth + step * (i + 1);

						int rh = (int) (leftHeight - leftHeight * (value / max));// leftHeight*(1-value
																					// /
																					// 10000.0);
						rect.top = rh + DensityUtil.dip2px(mContext, 10);
						rect.bottom = height;

						canvas.drawRect(rect, paint);
						// �Ƿ���ʾ��״ͼ�Ϸ�������
						if (this.text[i] == TRUE) {
							canvas.drawText(value + "",
									(columnWidth - paint.measureText(value + "")) / 2F
											+ step * (i + 1),
									rh + DensityUtil.dip2px(mContext, 5), paint);
						}

					}
				}		
				
	}
	/**
	 * ���õ���¼����Ƿ���ʾ����
	 */
	public boolean onTouchEvent(MotionEvent event) {
		performClick();
		int step = (getWidth() - startX) / (xWeeks.length + 1);
		int x = (int) event.getX();
		for (int i = 0; i < xWeeks.length; i++) {
			if (x > (columnWidth / 2 + step * (i + 1) - columnWidth / 2)
					&& x < (columnWidth / 2 + step * (i + 1) + columnWidth / 2)) {
				text[i] = 1;
				for (int j = 0; j < xWeeks.length; j++) {
					if (i != j) {
						text[j] = 0;
					}
				}
				if (Looper.getMainLooper() == Looper.myLooper()) {
					invalidate();
				} else {
					postInvalidate();
				}
			}
		}
		return super.onTouchEvent(event);
	}
	/**
	 * ����animation��һ��������
	 * 
	 */
	private class HistogramAnimation extends Animation {
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			super.applyTransformation(interpolatedTime, t);
			MyLog.e("TAG", "interpolatedTime==="+interpolatedTime);
			if (interpolatedTime < 1.0f && flag) {
				for (int i = 0; i < aniProgress.length; i++) {
					aniProgress[i] = (int) (progress[i] * interpolatedTime);
				}
			} else {
				for (int i = 0; i < aniProgress.length; i++) {
					aniProgress[i] = progress[i];
				}
			}
			invalidate();
		}
	}
}
