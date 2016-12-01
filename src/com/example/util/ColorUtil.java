package com.example.util;

import android.content.Context;

public class ColorUtil {

    // ���µ���ɫֵ
    public static int getNewColorByStartEndColor(Context context, float fraction, int startValue, int endValue) {
        return evaluate(fraction, context.getResources().getColor(startValue), context.getResources().getColor(endValue));
    }
    /**
     * ���µ���ɫֵ
     * @param fraction ��ɫȡֵ�ļ��� (0.0f ~ 1.0f)
     * @param startValue ��ʼ��ʾ����ɫ
     * @param endValue ������ʾ����ɫ
     * @return ���������µ���ɫֵ
     */
    public static int evaluate(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }

}
