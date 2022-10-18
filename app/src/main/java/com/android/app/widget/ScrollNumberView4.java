package com.android.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.helper.utils.LogUtil;

/**
 * @author : 流星
 * @CreateDate: 2022/10/18-13:34
 * @Description:
 */
public class ScrollNumberView4 extends LinearLayout {

    private final String[] mNumbers = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"
    };

    public ScrollNumberView4(Context context) {
        super(context);
        initView(context);
    }

    public ScrollNumberView4(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < mNumbers.length; i++) {
            String number = mNumbers[i];
            TextView textView = new TextView(context);
            textView.setText(number);
            textView.setTextSize(40);
            textView.setTextColor(Color.WHITE);
            textView.setTag(number);

            addView(textView);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
//            childAt.measure(widthMeasureSpec, heightMeasureSpec);

//            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);
        }

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        LogUtil.e("measuredWidth: ---> " + measuredWidth + " measuredHeight: ---> " + measuredHeight);
    }

    private int left = 0;
    private int top = 0;
    private int right = 0;
    private int bottom = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);

            LogUtil.e("text:" + childAt.getTag());
            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            LogUtil.e("measuredWidth:" + measuredWidth + "  measuredHeight:" + measuredHeight);

            right += measuredWidth;
            bottom = measuredHeight;

            childAt.layout(left, top, right, bottom);

            left += measuredWidth;
        }
    }

}
