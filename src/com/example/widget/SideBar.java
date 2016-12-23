package com.example.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.extra.MyLog;
import com.example.stickyheaderlsitview.R;
import com.example.tools.ISuspensionInterface;
import com.example.util.DensityUtil;

public class SideBar extends View {
	// �����¼�
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26����ĸ
	public static String[] INDEX_STRING = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	
    //�Ƿ���Ҫ����ʵ�ʵ�������������������Դ������ ֻ�� A B C ����tag����ô�������� A B C ���
    private boolean isNeedRealIndex;
    private List<String> mIndexDatas;
	private int choose = -1;// ѡ��
	private Paint paint = new Paint();
	private List<? extends ISuspensionInterface> mSourceDatas;
    //View�Ŀ��
    private int mWidth, mHeight;
    //ÿ��index����ĸ߶�
    private int mGapHeight;
	private TextView mTextDialog;

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		 initIndexDatas();
	}

	public SideBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SideBar(Context context) {
		 this(context, null);
	}

	
	
	
	
	 @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 MyLog.i("TAG", "onMeasure====");
	        //ȡ����ߵ�MeasureSpec  Mode ��Size
	        int wMode = MeasureSpec.getMode(widthMeasureSpec);
	        int wSize = MeasureSpec.getSize(widthMeasureSpec);
	        int hMode = MeasureSpec.getMode(heightMeasureSpec);
	        int hSize = MeasureSpec.getSize(heightMeasureSpec);
	        int measureWidth = 0, measureHeight = 0;//���ղ��������Ŀ��
	        //�õ����ʿ�ȣ�
	        Rect indexBounds = new Rect();//���ÿ�����Ƶ�index��Rect����
	        String index;//ÿ��Ҫ���Ƶ�index����
	        for (int i = 0; i < mIndexDatas.size(); i++) {
	            index = mIndexDatas.get(i);
	            paint.getTextBounds(index, 0, index.length(), indexBounds);//���������������ھ��Σ����Եõ����
	            measureWidth = Math.max(indexBounds.width(), measureWidth);//ѭ�������󣬵õ�index�������
	            measureHeight = Math.max(indexBounds.height(), measureHeight);//ѭ�������󣬵õ�index�����߶ȣ�Ȼ��*size
	        }
	        measureHeight *= mIndexDatas.size();
	        switch (wMode) {
	            case MeasureSpec.EXACTLY:
	                measureWidth = wSize;
	                break;
	            case MeasureSpec.AT_MOST:
	                measureWidth = Math.min(measureWidth, wSize);//wSize��ʱ�Ǹ��ؼ��ܸ���View��������ռ�
	                break;
	            case MeasureSpec.UNSPECIFIED:
	                break;
	        }
	        //�õ����ʵĸ߶ȣ�
	        switch (hMode) {
	            case MeasureSpec.EXACTLY:
	                measureHeight = hSize;
	                break;
	            case MeasureSpec.AT_MOST:
	                measureHeight = Math.min(measureHeight, hSize);//wSize��ʱ�Ǹ��ؼ��ܸ���View��������ռ�
	                break;
	            case MeasureSpec.UNSPECIFIED:
	                break;
	        }

	        setMeasuredDimension(measureWidth, measureHeight);
	    }
	
	 
	 
	  @Override
	    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
	        super.onSizeChanged(w, h, oldw, oldh);
	        MyLog.i("TAG", "onSizeChanged====");
	        mWidth = w;
	        mHeight = h;
	        //add by zhangxutong 2016 09 08 :���Դ����Ϊ�� ����sizeΪ0�����,
	        if (null == mIndexDatas || mIndexDatas.isEmpty()) {
	            return;
	        }
	        computeGapHeight();
	    }
	  
	  
	  
	  private void computeGapHeight() {
	        mGapHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / mIndexDatas.size();
	    }
	  
	/**
	 * ��д�������
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	      int t = getPaddingTop();//top�Ļ�׼��(֧��padding)
	      //  String index;//ÿ��Ҫ���Ƶ�index����
	    	for (int i = 0; i < mIndexDatas.size(); i++) {
				paint.setColor(Color.BLACK);
				paint.setTypeface(Typeface.DEFAULT);
				paint.setAntiAlias(true);
				paint.setTextSize(DensityUtil.sp2px(getContext(), 16));
				// ѡ�е�״̬
				if (i == choose) {
					paint.setColor(Color.parseColor("#3399ff"));
					paint.setFakeBoldText(true);
				}
				Paint.FontMetrics fontMetrics = paint.getFontMetrics();
				 int baseline = (int) ((mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2);
				float xPos = mWidth / 2 - paint.measureText(mIndexDatas.get(i)) / 2;
				float yPos = t + mGapHeight * i + baseline;
				canvas.drawText(mIndexDatas.get(i), xPos, yPos, paint);
				paint.reset();// ���û���
	    	}
		
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// ���y����
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * mIndexDatas.size());// ���y������ռ�ܸ߶ȵı���*b����ĳ��Ⱦ͵��ڵ��b�еĸ���.

		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			setBackgroundResource(R.drawable.sidebar_background);
			//setBackgroundColor(Color.rgb(57, 0, 0));
			if (oldChoose != c) {
				if (c >= 0 && c < mIndexDatas.size()) {
					if (listener != null) {
						listener.onTouchingLetterChanged(mIndexDatas.get(c));
					}
					if (mTextDialog != null) {
						mTextDialog.setText(mIndexDatas.get(c));
						mTextDialog.setVisibility(View.VISIBLE);
					}
					choose = c;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		default:
			setBackground(new ColorDrawable(0x00000000));
			choose = -1;//
			invalidate();
			if (mTextDialog != null) {
				mTextDialog.setVisibility(View.INVISIBLE);
			}
			break;
		}
		return true;
	}

	/**
	 * ���⹫���ķ���
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}
	/**
	 * �ӿ�
	 * 
	 * @author coder
	 * 
	 */
	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

	
	
	
    public SideBar setNeedRealIndex(boolean needRealIndex) {
        isNeedRealIndex = needRealIndex;
        initIndexDatas();
        return this;
    }

    private void initIndexDatas() {
        if (isNeedRealIndex) {
            mIndexDatas = new ArrayList<>();
        } else {
            mIndexDatas = Arrays.asList(INDEX_STRING);
        }
    }
    
    public SideBar setDatas( List<? extends ISuspensionInterface> mSourceDatas) {
        this.mSourceDatas = mSourceDatas;
        initSourceDatas(mSourceDatas,mIndexDatas);//������Դ���г�ʼ��
        computeGapHeight();
        return this;
    }

	private SideBar initSourceDatas(List<? extends ISuspensionInterface> sourceDatas, List<String> indexDatas) {
		 if (null == sourceDatas || sourceDatas.isEmpty()) {
	               return null;
	        }
	        //������Դ�� ��ʱsourceDatas �Ѿ�����
	        int size = sourceDatas.size();
	        String baseIndexTag;
	        for (int i = 0; i < size; i++) {
	        	if(sourceDatas.get(i).isShowSuspension()){
		            baseIndexTag = sourceDatas.get(i).getSuspensionTag();
		            if (!indexDatas.contains(baseIndexTag)) {//���ж��Ƿ��Ѿ������������ӽ�ȥ����û�������
		            	indexDatas.add(baseIndexTag);
		            }
	        	}

	        }
			return this;
	}
    
    

}