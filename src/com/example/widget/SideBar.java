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
	// 触摸事件
	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	// 26个字母
	public static String[] INDEX_STRING = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };
	
    //是否需要根据实际的数据来生成索引数据源（例如 只有 A B C 三种tag，那么索引栏就 A B C 三项）
    private boolean isNeedRealIndex;
    private List<String> mIndexDatas;
	private int choose = -1;// 选中
	private Paint paint = new Paint();
	private List<? extends ISuspensionInterface> mSourceDatas;
    //View的宽高
    private int mWidth, mHeight;
    //每个index区域的高度
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
	        //取出宽高的MeasureSpec  Mode 和Size
	        int wMode = MeasureSpec.getMode(widthMeasureSpec);
	        int wSize = MeasureSpec.getSize(widthMeasureSpec);
	        int hMode = MeasureSpec.getMode(heightMeasureSpec);
	        int hSize = MeasureSpec.getSize(heightMeasureSpec);
	        int measureWidth = 0, measureHeight = 0;//最终测量出来的宽高
	        //得到合适宽度：
	        Rect indexBounds = new Rect();//存放每个绘制的index的Rect区域
	        String index;//每个要绘制的index内容
	        for (int i = 0; i < mIndexDatas.size(); i++) {
	            index = mIndexDatas.get(i);
	            paint.getTextBounds(index, 0, index.length(), indexBounds);//测量计算文字所在矩形，可以得到宽高
	            measureWidth = Math.max(indexBounds.width(), measureWidth);//循环结束后，得到index的最大宽度
	            measureHeight = Math.max(indexBounds.height(), measureHeight);//循环结束后，得到index的最大高度，然后*size
	        }
	        measureHeight *= mIndexDatas.size();
	        switch (wMode) {
	            case MeasureSpec.EXACTLY:
	                measureWidth = wSize;
	                break;
	            case MeasureSpec.AT_MOST:
	                measureWidth = Math.min(measureWidth, wSize);//wSize此时是父控件能给子View分配的最大空间
	                break;
	            case MeasureSpec.UNSPECIFIED:
	                break;
	        }
	        //得到合适的高度：
	        switch (hMode) {
	            case MeasureSpec.EXACTLY:
	                measureHeight = hSize;
	                break;
	            case MeasureSpec.AT_MOST:
	                measureHeight = Math.min(measureHeight, hSize);//wSize此时是父控件能给子View分配的最大空间
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
	        //add by zhangxutong 2016 09 08 :解决源数据为空 或者size为0的情况,
	        if (null == mIndexDatas || mIndexDatas.isEmpty()) {
	            return;
	        }
	        computeGapHeight();
	    }
	  
	  
	  
	  private void computeGapHeight() {
	        mGapHeight = (mHeight - getPaddingTop() - getPaddingBottom()) / mIndexDatas.size();
	    }
	  
	/**
	 * 重写这个方法
	 */
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	      int t = getPaddingTop();//top的基准点(支持padding)
	      //  String index;//每个要绘制的index内容
	    	for (int i = 0; i < mIndexDatas.size(); i++) {
				paint.setColor(Color.BLACK);
				paint.setTypeface(Typeface.DEFAULT);
				paint.setAntiAlias(true);
				paint.setTextSize(DensityUtil.sp2px(getContext(), 16));
				// 选中的状态
				if (i == choose) {
					paint.setColor(Color.parseColor("#3399ff"));
					paint.setFakeBoldText(true);
				}
				Paint.FontMetrics fontMetrics = paint.getFontMetrics();
				 int baseline = (int) ((mGapHeight - fontMetrics.bottom - fontMetrics.top) / 2);
				float xPos = mWidth / 2 - paint.measureText(mIndexDatas.get(i)) / 2;
				float yPos = t + mGapHeight * i + baseline;
				canvas.drawText(mIndexDatas.get(i), xPos, yPos, paint);
				paint.reset();// 重置画笔
	    	}
		
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();// 点击y坐标
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * mIndexDatas.size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

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
	 * 向外公开的方法
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}
	/**
	 * 接口
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
        initSourceDatas(mSourceDatas,mIndexDatas);//对数据源进行初始化
        computeGapHeight();
        return this;
    }

	private SideBar initSourceDatas(List<? extends ISuspensionInterface> sourceDatas, List<String> indexDatas) {
		 if (null == sourceDatas || sourceDatas.isEmpty()) {
	               return null;
	        }
	        //按数据源来 此时sourceDatas 已经有序
	        int size = sourceDatas.size();
	        String baseIndexTag;
	        for (int i = 0; i < size; i++) {
	        	if(sourceDatas.get(i).isShowSuspension()){
		            baseIndexTag = sourceDatas.get(i).getSuspensionTag();
		            if (!indexDatas.contains(baseIndexTag)) {//则判断是否已经将这个索引添加进去，若没有则添加
		            	indexDatas.add(baseIndexTag);
		            }
	        	}

	        }
			return this;
	}
    
    

}