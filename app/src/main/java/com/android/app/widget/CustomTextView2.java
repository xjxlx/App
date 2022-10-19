package com.android.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.helper.utils.ConvertUtil;

/**
 * @author : 流星
 * @CreateDate: 2022/10/18-22:29
 * @Description:
 */
public class CustomTextView2 extends View {

    private Paint mPaintCenterRect = new Paint();
    private final RectF mCenterRect = new RectF();
    private int mMaxWidth;// 屏幕最大的宽度
    private final int roundRectWidth = (int) ConvertUtil.toDp(125f); // 矩形的宽度
    private final int roundRectHeight = (int) ConvertUtil.toDp(125f); // 矩形的高度


    public CustomTextView2(Context context) {
        super(context);
    }

    public CustomTextView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaintCenterRect.setColor(Color.parseColor("#4d000000"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMaxWidth = MeasureSpec.getSize(widthMeasureSpec);

        mCenterRect.left = (mMaxWidth - roundRectWidth) / 2;
        mCenterRect.right = mCenterRect.left + roundRectWidth;
        mCenterRect.top = 0;
        mCenterRect.bottom = mCenterRect.top + roundRectHeight;

        setMeasuredDimension(widthMeasureSpec, 500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        // 绘制指定大小的透明背景
        canvas.drawRoundRect(mCenterRect, 20, 20, mPaintCenterRect);


    }
}
