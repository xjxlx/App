package com.android.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.helper.utils.ConvertUtil;
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
    private final float mNumberInterval = ConvertUtil.toDp(60);

    private final RectF mCenterRect = new RectF();
    private final int roundRectWidth = (int) ConvertUtil.toDp(125f); // 矩形的宽度
    private final int roundRectHeight = (int) ConvertUtil.toDp(125f); // 矩形的高度

    private final Paint mPaintCenterRect = new Paint();
    private final Paint mPaintCenterRectLine = new Paint();// 阴影线

    private int mMaxWidth;
    private int mMaxHeight;
    private float mDx;

    // 字体颜色
    private final int[] mColors = new int[]{Color.TRANSPARENT, Color.TRANSPARENT, Color.WHITE, Color.TRANSPARENT, Color.TRANSPARENT};
    float[] positions = new float[]{0.1f, 0.28f, 0.5f, 0.73f, 1f};

    private final GestureDetector mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mDx += distanceX;
            requestLayout();
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    });

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
        mPaintCenterRect.setColor(Color.parseColor("#4d000000"));
//
//        // 数字
//        mPaintNumber.setTextSize(ConvertUtil.toDp(mNumberMaxSize));
//        mPaintNumber.setColor(Color.WHITE);
//        mPaintNumber.setAntiAlias(true);
//        mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);

        mPaintCenterRectLine.setColor(Color.WHITE);

        for (int i = 0; i < mNumbers.length; i++) {
            String number = mNumbers[i];
            TextView textView = new TextView(context);
            textView.setText(number);
            textView.setTextSize(80);
            textView.setTextColor(Color.WHITE);
            textView.setTag(number);

            addView(textView);
        }

        setWillNotDraw(false);

        setLayerType(LAYER_TYPE_SOFTWARE,null);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 重置
        mMaxWidth = 0;
        mMaxHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            measureChild(childAt, widthMeasureSpec, heightMeasureSpec);

            int measuredWidth = childAt.getMeasuredWidth();
            int measuredHeight = childAt.getMeasuredHeight();
            if (mMaxWidth < measuredHeight) {
                mMaxHeight = measuredHeight;
            }

            mMaxWidth += measuredWidth;
            if (i != childCount - 1) {
                mMaxWidth += mNumberInterval;
            }
        }

        mCenterRect.left = (mMaxWidth - roundRectWidth) / 2;
        mCenterRect.right = mCenterRect.left + roundRectWidth;
        mCenterRect.top = 0;
        mCenterRect.bottom = mCenterRect.top + roundRectHeight;

        if (mMaxHeight < mCenterRect.bottom) {
            mMaxHeight = (int) mCenterRect.bottom;
        }

        setMeasuredDimension(mMaxWidth, mMaxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        LogUtil.e("onLayout");
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);

            int measuredWidth = childAt.getMeasuredWidth();

            right += (measuredWidth + mNumberInterval);
            bottom = childAt.getMeasuredHeight();

            childAt.layout((int) (left + mDx), top, (int) (right + mDx), bottom);

            LogUtil.e("left:" + left + " t:" + top + "  r:" + right + " b:" + bottom + " dx:" + mDx);
            // 左侧 = 上一个view的宽度 + 间隔
            left += (measuredWidth + mNumberInterval);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return true;
//        return super.onTouchEvent(event);
    }

    private Paint mPaint = new Paint();
    private Rect mRect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制透明
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mMaxWidth;
        mRect.bottom = mMaxHeight;

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(mMaxHeight);

        @SuppressLint("DrawAllocation")
        LinearGradient linearGradient = new LinearGradient(
                0,
                mMaxHeight,
                mMaxWidth,
                mMaxHeight,
                mColors,
                positions,
                Shader.TileMode.MIRROR);

        mPaint.setShader(linearGradient);
//        canvas.drawRect(mRect, mPaint);
//
        canvas.drawLine(0, mMaxHeight, mMaxWidth, mMaxHeight, mPaint);


        LogUtil.e("onDraw");
        // 绘制指定大小的透明背景

        int save = canvas.save();

        canvas.clipRect(mCenterRect);

        canvas.drawRoundRect(mCenterRect, 20, 20, mPaintCenterRect);

        // 恢复画布状态，即裁剪之前
        canvas.restoreToCount(save);
    }
}
