package com.example.uisoftware;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * �Զ���֧�ֺ��������ListView
 * @author 
 *
 */
public class HVListView extends ListView {

    /** ���� */
    private GestureDetector mGesture;
    /** ��ͷ */
    public LinearLayout mListHead;
    /** ƫ������ */
    private int mOffset = 0;
    /** ��Ļ��� */
    private int screenWidth;

    /** ���캯�� */
    public HVListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGesture = new GestureDetector(context, mOnGesture);
    }

    /** �ַ������¼� */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return mGesture.onTouchEvent(ev);
    }

    /** ���� */
    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            return false;
        }

        /** ���� */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            synchronized (HVListView.this) {
                int moveX = (int) distanceX;
                int curX = mListHead.getScrollX();
                int scrollWidth = getWidth();
                int dx = moveX;
                //����Խ������
                if (curX + moveX < 0)
                    dx = 0;
                if (curX + moveX + getScreenWidth() > scrollWidth)
                    dx = scrollWidth - getScreenWidth() - curX;

                mOffset += dx;
                //�������ƹ���Item��ͼ
                for (int i = 0, j = getChildCount(); i < j; i++) {
                    View child = ((ViewGroup) getChildAt(i)).getChildAt(1);
                    if (child.getScrollX() != mOffset)
                        child.scrollTo(mOffset, 0);
                }
                mListHead.scrollBy(dx, 0);
            }
            requestLayout();
            return true;
        }
    };

    
    /**
     * ��ȡ��Ļ�ɼ���Χ�������Ļ
     * @return
     */
    public int getScreenWidth() {
        if (screenWidth == 0) {
            screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
            if (getChildAt(0) != null) {
                screenWidth -= ((ViewGroup) getChildAt(0)).getChildAt(0)
                        .getMeasuredWidth();
            } else if (mListHead != null) {
                //��ȥ�̶���һ��
                screenWidth -= mListHead.getChildAt(0).getMeasuredWidth();
            }
        }
        return screenWidth;
    }
    
    /** ��ȡ��ͷƫ���� */
    public int getHeadScrollX() {
        return mListHead.getScrollX();
    }
}