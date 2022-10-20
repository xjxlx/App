package com.android.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.android.helper.utils.ConvertUtil;
import com.android.helper.utils.CustomViewUtil;
import com.android.helper.utils.LogUtil;

import java.util.HashMap;

/**
 * @author : 流星
 * @CreateDate: 2022/10/19-22:05
 * @Description:
 */
public class Number6 extends View {

    private final String[] mNumbers = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"
    };

    private final RectF mCenterRect = new RectF();
    private int mMaxWidth;// 屏幕最大的宽度
    private final int roundRectWidth = (int) ConvertUtil.toDp(125f); // 矩形的宽度
    private final int roundRectHeight = (int) ConvertUtil.toDp(125f); // 矩形的高度

    /**
     * 每个文字的间距
     */
    private final float mNumberInterval = ConvertUtil.toDp(60);

    private final Paint mPaintCenterRect = new Paint();
    // 阴影线
    private final Paint mPaintCenterRectLine = new Paint();

    private final Paint mPaintNumber = new Paint();// 数字的paint
    private final int mNumberMinSize = 48;// 文字的最小值
    private final int mNumberMaxSize = 100;// 文字的最大值
    public int mSelectorIndex = 6; // 选中的数字的角标

    private int mScreenWidth;
    private int mScreenHeight;

    private HashMap<String, Integer> mMapX = new HashMap<>();
    private HashMap<String, Float> mMapWidth = new HashMap<>();
    private Scroller scroller;

    public Number6(Context context) {
        super(context);
    }

    public Number6(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mPaintCenterRect.setColor(Color.parseColor("#4d000000"));
        // 数字
        mPaintNumber.setColor(Color.WHITE);
        mPaintNumber.setAntiAlias(true);
        mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);

        mPaintCenterRectLine.setColor(Color.WHITE);

        scroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取view的宽度
//        mMaxWidth = MeasureSpec.getSize(widthMeasureSpec);

        mMaxWidth = getMeasuredWidth();

        mCenterRect.left = (mMaxWidth - roundRectWidth) / 2;
        mCenterRect.right = mCenterRect.left + roundRectWidth;
        mCenterRect.top = 0;
        mCenterRect.bottom = mCenterRect.top + roundRectHeight;
        // 计算出总体的高度后，重新去设置高度

        setMeasuredDimension(mMaxWidth, 500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制指定大小的透明背景
        canvas.drawRoundRect(mCenterRect, 20, 20, mPaintCenterRect);

        // 绘制阴影线
        int lineWidth = roundRectWidth / 2;
        int lineHeight = roundRectHeight / 2;
        canvas.drawLine(mCenterRect.left + lineWidth, mCenterRect.top, mCenterRect.left + lineWidth, mCenterRect.bottom, mPaintCenterRectLine);
        canvas.drawLine(mCenterRect.left, mCenterRect.top + lineHeight, mCenterRect.right, mCenterRect.top + lineHeight, mPaintCenterRectLine);

        // 绘制中心文字
        drawMiddle(canvas);

        // 绘制左侧文字
        drawLeftText(canvas);

        // 绘制右侧文字
        drawRightText(canvas);
    }


    // 中间文字的X轴
    private int mMiddleX;
    private float mMiddleWidth = 0;
    private int mLeftX;
    private int mRightX;

    private void drawMiddle(Canvas canvas) {
        String mNumber = mNumbers[mSelectorIndex];

        // 设置文字大小
        mPaintNumber.setTextSize(mNumberMaxSize);

        mMiddleWidth = CustomViewUtil.getTextViewWidth(mPaintNumber, mNumber);
        mMapWidth.put(mNumber, mMiddleWidth);

        mMiddleX = (int) (getWidth() - mMiddleWidth) / 2;
        // 存入x
        mMapX.put(mNumber, mMiddleX);

        float height = CustomViewUtil.getTextSize(mPaintNumber, mNumber)[1];
        float baseLine = CustomViewUtil.getBaseLine(mPaintNumber, mNumber);
        int y = (int) (mCenterRect.top + (roundRectHeight - height) / 2 + baseLine);

        canvas.drawText(mNumber, mMiddleX + dx, y, mPaintNumber);
    }

    private void drawLeftText(Canvas canvas) {
        // 重置数据
        mLeftX = 0;
        int numberWidth = 0;

        // 获取右侧的X值
        int tempIndex = 1;
        for (int i = mSelectorIndex - 1; i >= 0; i--) {
            String numberLeft = mNumbers[i];

            int size = 0;
            if (mNumberMaxSize - tempIndex * 6 > mNumberMinSize) {
                size = mNumberMaxSize - tempIndex * 6;
            } else {
                size = mNumberMinSize;
            }

            mPaintNumber.setTextSize(size);
            float textViewWidth = CustomViewUtil.getTextViewWidth(mPaintNumber, numberLeft);
            mMapWidth.put(numberLeft, textViewWidth);

            // 叠加宽度
            numberWidth += textViewWidth + mNumberInterval;

            // x轴 = 中间的x  - 间距 - 宽度
            mLeftX = mMiddleX - numberWidth;
            mMapX.put(numberLeft, mLeftX);

            LogUtil.e("i:" + i + "  numberLeft:" + numberLeft + " size:" + size + "  mLeftX:" + mLeftX);

            float textHeight = CustomViewUtil.getTextHeight(mPaintNumber, numberLeft);
            float baseLine = CustomViewUtil.getBaseLine(mPaintNumber, numberLeft);

            int y = (int) (mCenterRect.top + (roundRectHeight - textHeight) / 2 + baseLine);

            canvas.drawText(numberLeft, mLeftX + dx, y, mPaintNumber);
            tempIndex++;
        }
    }


    private void drawRightText(Canvas canvas) {
        // 重置数据
        mRightX = 0;
        int numberWidth = 0;

        // 获取右侧的X值
        int tempIndex = 1;
        for (int i = mSelectorIndex + 1; i < mNumbers.length; i++) {
            String numberRight = mNumbers[i];

            int size = 0;
            if (mNumberMaxSize - tempIndex * 6 > mNumberMinSize) {
                size = mNumberMaxSize - tempIndex * 6;
            } else {
                size = mNumberMinSize;
            }
            LogUtil.e("i:" + i + "  numberRight:" + numberRight + "  size:" + size);

            // 设置文字的大小
            mPaintNumber.setTextSize(size);
            // 获取宽度
            float textViewWidth = CustomViewUtil.getTextViewWidth(mPaintNumber, numberRight);
            mMapWidth.put(numberRight, textViewWidth);

            float baseLine = CustomViewUtil.getBaseLine(mPaintNumber, numberRight);
            float textHeight = CustomViewUtil.getTextHeight(mPaintNumber, numberRight);
            // 叠加宽度

            // x轴 = 中间文字 x + 中间文字的宽度 + 间距
            numberWidth += mNumberInterval;
            mRightX = (int) (mMiddleX + mMiddleWidth + numberWidth);

            int y = (int) (mCenterRect.top + (roundRectHeight - textHeight) / 2 + baseLine);

            canvas.drawText(numberRight, mRightX + dx, y, mPaintNumber);

            numberWidth += textViewWidth;
            tempIndex++;
        }
    }

    private int mStartX;
    private int dx;
    GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            scroller.startScroll((int) e1.getX(),
                    0,
                    (int) distanceX,
                    0);
            invalidate();

            return true;
//            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    });

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mStartX = (int) event.getX();
//
//                return true;
//
//            case MotionEvent.ACTION_MOVE:
//                int endX = (int) event.getX();
//                dx += endX - mStartX;
//                invalidate();
//
//                break;
//
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
//        return super.onTouchEvent(event);

        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            int currX = scroller.getCurrX();
            LogUtil.e("currX:" + currX);

            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            invalidate();
        }
    }
}
