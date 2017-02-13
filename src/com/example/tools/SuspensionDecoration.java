package com.example.tools;

import java.util.List;

import com.example.extra.MyLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

public class SuspensionDecoration extends RecyclerView.ItemDecoration {
    private List<? extends ISuspensionInterface> mDatas;
    private Paint mPaint;
    private Rect mBounds;//���ڴ�Ų�������Rect
    private LayoutInflater mInflater;
    private int mTitleHeight;//title�ĸ�
    private static int COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF");
    private static int COLOR_TITLE_FONT = Color.parseColor("#FF999999");
    private static int mTitleFontSize;//title�����С
    private int mHeaderViewCount = 0;

    public SuspensionDecoration(Context context, List<? extends ISuspensionInterface> datas) {
        super();
        mDatas = datas;
        mPaint = new Paint();
        mBounds = new Rect();
        mTitleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, context.getResources().getDisplayMetrics());
        mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, context.getResources().getDisplayMetrics());
        mPaint.setTextSize(mTitleFontSize);
        mPaint.setAntiAlias(true);
        mInflater = LayoutInflater.from(context);
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //super���������0 0 0 0
        super.getItemOffsets(outRect, view, parent, state);
        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewPosition();
       // MyLog.i("TAG", "getItemOffsets"+position);
        position -= getHeaderViewCount();
        if (mDatas == null || mDatas.isEmpty() || position > mDatas.size() - 1) {//posΪ1��sizeΪ1��1>0? true
            return;//Խ��
        }
        //�Ҽǵ�Rv��item position������ʱ����Ϊ-1.���յ��ж�һ�°�
        if (position > -1) {
            ISuspensionInterface titleCategoryInterface = mDatas.get(position);
            //����0�϶�Ҫ��title��,
            // 2016 11 07 add ���ǵ�headerView ����0 Ҳ��Ӧ����title
            // 2016 11 10 add ͨ���ӿ����isShowSuspension() �������ȹ��˵�������ʾ��ͣ��item
            if (titleCategoryInterface.isShowSuspension()) {
                if (position == 0) {
                    outRect.set(0, mTitleHeight, 0, 0);
                } else {//������ͨ���ж�
                    if (null != titleCategoryInterface.getSuspensionTag() && !titleCategoryInterface.getSuspensionTag().equals(mDatas.get(position - 1).getSuspensionTag())) {
                        //��Ϊ�� �Ҹ�ǰһ��tag��һ���ˣ�˵�����µķ��࣬ҲҪtitle
                        outRect.set(0, mTitleHeight, 0, 0);
                    }
                }
            }
        }
    }
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int position = params.getViewPosition();
            position -=  getHeaderViewCount();
            //posΪ1��sizeΪ1��1>0? true
             MyLog.i("TAG", "onDraw=-=-=-="+position);
            if (mDatas == null || mDatas.isEmpty() || position > mDatas.size() - 1 || position < 0 || !mDatas.get(position).isShowSuspension()) {
            	 MyLog.i("TAG", "onDraw=-="+position);
                continue;//Խ��
            }
            //�Ҽǵ�Rv��item position������ʱ����Ϊ-1.���յ��ж�һ�°�
            if (position > -1) {
                if (position == 0) {//����0�϶�Ҫ��title��
                    drawTitleArea(c, left, right, child, params, position);
                } else {//������ͨ���ж�
                    if (null != mDatas.get(position).getSuspensionTag() && !mDatas.get(position).getSuspensionTag().equals(mDatas.get(position - 1).getSuspensionTag())) {
                        //��Ϊ�� �Ҹ�ǰһ��tag��һ���ˣ�˵�����µķ��࣬ҲҪtitle
                        drawTitleArea(c, left, right, child, params, position);
                    } else {
                        //none
                    }
                }
            }
        }
    } 
    
    private void drawTitleArea(Canvas c, int left, int right, View child, RecyclerView.LayoutParams params, int position) {//���ȵ��ã����������²�
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(left, child.getTop() - params.topMargin - mTitleHeight, right, child.getTop() - params.topMargin, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
/*        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;*/
        mPaint.getTextBounds(mDatas.get(position).getSuspensionTag(), 0, mDatas.get(position).getSuspensionTag().length(), mBounds);
        c.drawText(mDatas.get(position).getSuspensionTag(), child.getPaddingLeft(), child.getTop() - params.topMargin - (mTitleHeight / 2 - mBounds.height() / 2), mPaint);
    }
    
    @Override
    public void onDrawOver(Canvas c, final RecyclerView parent, RecyclerView.State state) {
        //������ ���������ϲ�
        int pos = ((LinearLayoutManager) (parent.getLayoutManager())).findFirstVisibleItemPosition();
        pos -= getHeaderViewCount();
        //posΪ1��sizeΪ1��1>0? true
        if (mDatas == null || mDatas.isEmpty() || pos > mDatas.size() - 1 || pos < 0 || !mDatas.get(pos).isShowSuspension()) {
            return;//Խ��
        }
        String tag = mDatas.get(pos).getSuspensionTag();
        //View child = parent.getChildAt(pos);
        View child = parent.findViewHolderForPosition(pos + getHeaderViewCount()).itemView;//����һ����ֵ�bug����ʱ��childΪ�գ����Խ� child = parent.getChildAt(i)��-�� parent.findViewHolderForLayoutPosition(pos).itemView
        boolean flag = false;//����һ��flag��Canvas�Ƿ�λ�ƹ��ı�־
        if ((pos + 1) < mDatas.size()) {//��ֹ����Խ�磨һ�����������֣�
            if (null != tag && !tag.equals(mDatas.get(pos + 1).getSuspensionTag())) {//��ǰ��һ���ɼ���Item��tag�����������һ��item��tag��˵��������ViewҪ�л���
                Log.d("zxt", "onDrawOver() called with: c = [" + child.getTop());//��getTop��ʼ�为�����ľ���ֵ���ǵ�һ���ɼ���Item�Ƴ���Ļ�ľ��룬
                if (child.getHeight() + child.getTop() < mTitleHeight) {//����һ���ɼ���item����Ļ�л�ʣ�ĸ߶�С��title����ĸ߶�ʱ������Ҳ�ÿ�ʼ������Title�ġ�����������
                    c.save();//ÿ�λ���ǰ ���浱ǰCanvas״̬��
                    flag = true;
                    //һ��ͷ���۵���������Ч�����˾���Ҳ������~
                    //����123�� c.drawRect �Ƚϣ�ֻ��bottom������һ�������� child.getHeight() + child.getTop() < mTitleHeight�����Ի����������ڲ��ϵļ�С�������۵������ĸо�
                   c.clipRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + child.getHeight() + child.getTop());
                    //���ƶ���ô���ʱ,��Ʒ�б����ͣͷ���л�������Ч����
                    //�ϻ�ʱ����canvas���� ��yΪ������ ,���Ժ���canvas ��������Rect��Text�������ˣ������л��ġ��������о�
                //    c.translate(0, child.getHeight() + child.getTop() - mTitleHeight);
                  //  MyLog.i("TAG", "onDrawOver===translate");
                   
                }
            }
        }
        mPaint.setColor(COLOR_TITLE_BG);
        c.drawRect(parent.getPaddingLeft(), parent.getPaddingTop(), parent.getRight() - parent.getPaddingRight(), parent.getPaddingTop() + mTitleHeight, mPaint);
        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(tag, 0, tag.length(), mBounds);
        c.drawText(tag, child.getPaddingLeft(),
                parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2),
                mPaint);
        if (flag)
            c.restore();//�ָ�������֮ǰ�����״̬

    } 
    
    public int getHeaderViewCount() {
        return mHeaderViewCount;
    }
}
