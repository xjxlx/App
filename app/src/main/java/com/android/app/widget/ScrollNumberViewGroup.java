package com.android.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.R;

/**
 * @author : 流星
 * @CreateDate: 2022/10/22-16:32
 * @Description:
 */
public class ScrollNumberViewGroup extends RelativeLayout {

    private ScrollViewGroup mScrollViewGroup;
    private int mRectWidth = 110;
    private int mRectHeight = 128;

    public ScrollNumberViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context) {

        View mBackgroundView = new View(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(mRectWidth, mRectHeight);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        mBackgroundView.setLayoutParams(layoutParams);

        mBackgroundView.setBackgroundResource(R.drawable.shape_scroll_number_bg);
        addView(mBackgroundView);

        mScrollViewGroup = new ScrollViewGroup(context);
        addView(mScrollViewGroup);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }

        int measuredWidth = getMeasuredWidth();
        int left = (measuredWidth - mRectWidth) / 2;
        mScrollViewGroup.setRectLeft(left);
        mScrollViewGroup.setRectRight(left + mRectWidth);
    }
}
