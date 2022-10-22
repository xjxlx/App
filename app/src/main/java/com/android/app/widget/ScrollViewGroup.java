package com.android.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.android.helper.utils.LogUtil;

/**
 * @author : 流星
 * @CreateDate: 2022/10/22-16:42
 * @Description:
 */
public class ScrollViewGroup extends LinearLayout {
    private Scroller mScroller;
    private final String[] mNumbers = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"
    };

    private int mSelectorIndex = 7; // 默认选中的角标
    private int mFontSizeMax = 100;
    private int mFontSizeMin = 40;
    private int mRectLeft;
    private int mRectRight;
    private int mCurrX;

    private final GestureDetector mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int scrollX = getScrollX();
            LogUtil.e("scrollX:" + scrollX + " distanceX:" + distanceX);

            // 参数1：当前view的x轴位置，
            // 参数2：当前view的Y轴位置
            // 参数3：X轴偏移的距离
            // 参数4：y轴偏移的距离
            // 参数5：在多少时间内做完这些事，毫秒 距离 = 时间 * 速度   // 时间 = 距离 / 速度

            // mScroller.getCurrX(); = 当前view开始的位置 + 偏移的位置
            mScroller.startScroll(scrollX, 0, (int) distanceX, 0, 0);

            calculatePosition(distanceX);

            invalidate();
            return true;
        }

    });

    public ScrollViewGroup(Context context) {
        super(context);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void initView(Context context) {
        mScroller = new Scroller(context);

        setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < mNumbers.length; i++) {
            String number = mNumbers[i];
            TextView textView = new TextView(context);
            textView.setTextColor(Color.WHITE);

            textView.setText(number);
            addView(textView);
        }
    }

    public int getRectLeft() {
        return mRectLeft;
    }

    public void setRectLeft(int rectLeft) {
        mRectLeft = rectLeft;
        requestLayout();
    }

    public int getRectRight() {
        return mRectRight;
    }

    public void setRectRight(int rectRight) {
        mRectRight = rectRight;
        requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        for (int i = 0; i < getChildCount(); i++) {
            TextView textView = (TextView) getChildAt(i);

            // 设置间距
            LinearLayout.LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
            // 居中显示
            layoutParams.gravity = Gravity.CENTER;
            // 设置边距
            if (i > 0) {
                ((MarginLayoutParams) layoutParams).leftMargin = 65;
                textView.setLayoutParams((MarginLayoutParams) layoutParams);
            }
            int size = 0;
            float alpha = 0f;
            // 设置粗体
            TextPaint paint = textView.getPaint();
            if (i == mSelectorIndex) {
                paint.setFakeBoldText(true);
                textView.setTextSize(mFontSizeMax);
                size = mFontSizeMax;

                textView.setAlpha(1);
            } else {
                // 不加粗
                paint.setFakeBoldText(false);

                // 处理左侧
                if (i < mSelectorIndex) {
                    // 设置字体,左侧逐渐减小
                    size = mFontSizeMax - (mSelectorIndex - i) * 10;
                    if (size < mFontSizeMin) {
                        size = mFontSizeMin;
                    }

                    // 一共是1：逐个递减的标记是index ,index 会越来越小 ，透明度也会越来越小，z = index /
                    alpha = 1 - ((mSelectorIndex - i) * 0.25f);
                    if (alpha < 0) {
                        alpha = 0;
                    }
                }

                // 处理右侧
                if (i > mSelectorIndex) {
                    size = mFontSizeMax - (i - mSelectorIndex) * 10;
                    // 设置字体,左侧逐渐减小
                    if (size < mFontSizeMin) {
                        size = mFontSizeMin;
                    }

                    alpha = 1 - ((i - mSelectorIndex) * 0.25f);
                    if (alpha < 0) {
                        alpha = 0;
                    }
                }
                textView.setTextSize(size);
//                textView.setAlpha(alpha);
            }
        }

        int lll = 0;
        for (int i = 0; i < getChildCount(); i++) {
            TextView childAt = (TextView) getChildAt(i);
            if (i == mSelectorIndex) {
                int left = childAt.getLeft();
                int targetLeft = (getWidth() - childAt.getMeasuredWidth()) / 2;
                LogUtil.e("number: -->" + childAt.getText() + " left:" + left + "  interval:" + targetLeft + " offset:" + (targetLeft - left));
                lll = targetLeft - left;
                break;
            }
        }

        if (lll != 0) {
            for (int i = 0; i < getChildCount(); i++) {
                TextView childAt = (TextView) getChildAt(i);

                int left = childAt.getLeft();
                int top = childAt.getTop();
                int right = childAt.getRight();
                int bottom = childAt.getBottom();
                childAt.layout(left + lll + mCurrX, top, right + mCurrX + lll, bottom);
//                LogUtil.e("number * :" + childAt.getText() + " l:" + left + " t:" + top + " r:" + right + " b:" + bottom);
            }
        }
    }

    private void calculatePosition(float distanceX) {

        for (int i = 0; i < getChildCount(); i++) {
            TextView childAt = (TextView) getChildAt(i);
            int left = (int) childAt.getLeft();
            int right = (int) childAt.getRight();
//
//            LogUtil.e("number:" + childAt.getText()
//                    + " rectLeft:" + mRectLeft
//                    + "   currentLeft:" + (mCurrX + left)
//                    + " rectRight:" + mRectRight
//                    + "   currentRight:" + (mCurrX + right)
//                    + "   mCurrX:" + mCurrX
//                    + "   left:" + left
//                    + "   right:" + right
//            );

            if (distanceX > 0) {
//                LogUtil.e("向左：");
                // 文字右侧位置 > 矩形左侧边缘  && 文字左侧位置 < 矩形右侧边缘
                if (mRectLeft < right - mCurrX && mRectRight >= left - mCurrX) {
                    mSelectorIndex = i;
                    break;
                }
            } else {
//                LogUtil.e("向右：" + distanceX);
                if (mRectRight > left - mCurrX && mRectLeft < right - mCurrX) {
                    mSelectorIndex = i;
                    break;
                }
            }
            LogUtil.e("当前选中的是：" + mSelectorIndex);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            requestLayout();
        }
//        return super.onTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            mCurrX = mScroller.getCurrX();
            scrollTo(mCurrX, 0);
            invalidate();
        }
    }
}
