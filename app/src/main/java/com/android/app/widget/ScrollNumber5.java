package com.android.app.widget;

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

import androidx.annotation.Nullable;

import com.android.helper.utils.ConvertUtil;
import com.android.helper.utils.CustomViewUtil;
import com.android.helper.utils.LogUtil;

import java.util.HashMap;

/**
 * @author : 流星
 * @CreateDate: 2022/10/19-09:59
 * @Description:
 */
public class ScrollNumber5 extends View {

    private final String[] mNumbers = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"
    };
    private final RectF mCenterRect = new RectF();
    private int mMaxWidth;// 屏幕最大的宽度
    private final int roundRectWidth = (int) ConvertUtil.toDp(125f); // 矩形的宽度
    private final int roundRectHeight = (int) ConvertUtil.toDp(125f); // 矩形的高度
    private final int minuteInterval = (int) ConvertUtil.toDp(26);

    /**
     * 每个文字的间距
     */
    private final float mNumberInterval = ConvertUtil.toDp(125);

    private final Paint mPaintCenterRect = new Paint();
    // 阴影线
    private final Paint mPaintCenterRectLine = new Paint();

    private final Paint mPaintNumber = new Paint();// 数字的paint
    private final float mNumberMinSize = 48;// 文字的最小值
    private final float mNumberMaxSize = 100;// 文字的最大值
    private int mSelectorIndex = 9; // 选中的数字的角标
    private HashMap<String, Point> mMapPoint = new HashMap<>();

    private GestureDetector mDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    });
    private int mScreenWidth;
    private int mScreenHeight;

    public ScrollNumber5(Context context) {
        super(context);
    }

    public ScrollNumber5(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mPaintCenterRect.setColor(Color.parseColor("#4d000000"));
        // 数字
        mPaintNumber.setTextSize(ConvertUtil.toDp(mNumberMaxSize));
        mPaintNumber.setColor(Color.WHITE);
        mPaintNumber.setAntiAlias(true);
        mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);

        mPaintCenterRectLine.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 获取view的宽度
        mMaxWidth = MeasureSpec.getSize(widthMeasureSpec);

//        if (mMMaxWidth > 0) {
//            centerLines = mMMaxWidth / 2;
//        }
//
        mCenterRect.left = (mMaxWidth - roundRectWidth) / 2;
        mCenterRect.right = mCenterRect.left + roundRectWidth;
        mCenterRect.top = 0;
        mCenterRect.bottom = mCenterRect.top + roundRectHeight;
//        // 计算出总体的高度后，重新去设置高度
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

        calculatePosition();
        // 绘制中间的数字

//        String number = mNumbers[mSelectorIndex];
//        Point point = mMapPoint.get(number);
//        mPaintNumber.setTextSize(point.textSize);
//        canvas.drawText(number, point.x, point.y, mPaintNumber);

        for (int i = 0; i < mNumbers.length; i++) {
            String number1 = mNumbers[i];
            Point point1 = mMapPoint.get(number1);
            if (point1 != null) {
                mPaintNumber.setTextSize(point1.textSize);
                canvas.drawText(number1, point1.x, point1.y, mPaintNumber);
            }
        }
    }

    private void calculatePosition() {
        // 1:计算中间的位置

        String number = mNumbers[mSelectorIndex];
        // 设置文字的大小
        mPaintNumber.setTextSize(mNumberMaxSize);
        saveParameter(1, number, mSelectorIndex);

        // 计算左侧的数据
        for (int i = mSelectorIndex - 1; i >= 0; i--) {
            String numberLeft = mNumbers[i];
            LogUtil.e("i:" + i + " number:" + numberLeft);

            saveParameter(2, numberLeft, i);
        }

        // 计算右侧的值
        for (int i = mSelectorIndex + 1; i < mNumbers.length; i++) {
            String numberRight = mNumbers[i];
            LogUtil.e("i:" + i + " numberRight:" + numberRight);

            saveParameter(3, numberRight, i);
        }
    }

    private void saveParameter(int type, String number, int index) {
        Point point = mMapPoint.get(number);
        if (point == null) {
            point = new Point();
        }

        // 计算中间的数字
        if (type == 1) {
            // 设置文字大小
            mPaintNumber.setTextSize(mNumberMaxSize);

            if (mScreenWidth == 0) {
                mScreenWidth = getWidth();
            }
            if (mScreenHeight == 0) {
                mScreenHeight = getHeight();
            }
            LogUtil.e("screenWidth:" + mScreenWidth + "  screenHeight:" + mScreenHeight);

            // 获取文字的高度
            float middleNumberHeight = CustomViewUtil.getTextSize(mPaintNumber, number)[1];
            // 获取文字的宽度
            float middleTextWidth = CustomViewUtil.getTextViewWidth(mPaintNumber, number);
            // 获取基线
            float middleBaseLine = CustomViewUtil.getBaseLine(mPaintNumber, number);

            point.x = (mScreenWidth - middleTextWidth) / 2;
            point.y = mCenterRect.top + (roundRectHeight - middleNumberHeight) / 2 + middleBaseLine;
            point.textSize = mNumberMaxSize;
            point.textWidth = middleTextWidth;

        } else if (type == 2) {
            // 计算左侧的数字

            // 获取右侧的X值
            if (index + 1 <= mNumbers.length) {
                // 上一个数字
                String numberRight = mNumbers[index + 1];
                Point pointRight = mMapPoint.get(numberRight);
                if (pointRight != null) {
                    // 重新设置文字的大小
                    float textSize = pointRight.textSize;
                    if (textSize - 8 >= mNumberMinSize) {
                        point.textSize = textSize - 8;
                    } else {
                        point.textSize = mNumberMinSize;
                    }

                    // 设置文字大小，重新测量位置
                    mPaintNumber.setTextSize(point.textSize);

                    // 获取文字宽度
                    float leftTextViewWidth = CustomViewUtil.getTextViewWidth(mPaintNumber, number);

                    // 文字高度
                    float leftNumberHeight = CustomViewUtil.getTextSize(mPaintNumber, number)[1];
                    // 文字基线
                    float leftBaseLine = CustomViewUtil.getBaseLine(mPaintNumber, number);

                    // 左侧x = 上一个的x + 间距 + 本身的宽度
                    point.x = pointRight.x - mNumberInterval - leftTextViewWidth;
                    point.y = mCenterRect.top + (roundRectHeight - leftNumberHeight) / 2 + leftBaseLine;
                    point.textWidth = leftTextViewWidth;
                }
            }
        } else if (type == 3) {
            // 获取左侧的值
            if (index - 1 >= 0) {
                String numberLeft = mNumbers[index - 1];
                Point pointLeft = mMapPoint.get(numberLeft);
                if (pointLeft != null) {
                    // 重新设置文字大小
                    if (pointLeft.textSize - 8 >= mNumberMinSize) {
                        point.textSize = pointLeft.textSize - 8;
                    } else {
                        point.textSize = mNumberMinSize;
                    }
                    // 设置文字大小
                    mPaintNumber.setTextSize(point.textSize);

                    // 测量文字宽度
                    float rightTextViewWidth = CustomViewUtil.getTextViewWidth(mPaintNumber, number);
                    // 获取基准线
                    float rightBaseLine = CustomViewUtil.getBaseLine(mPaintNumber, number);
                    // 获取高度
                    float rightHeight = CustomViewUtil.getTextSize(mPaintNumber, number)[1];

                    // x  = 左侧x + 左侧的宽度 + 间距
                    point.x = pointLeft.x + pointLeft.textWidth + mNumberInterval;
                    point.y = mCenterRect.top + (roundRectHeight - rightHeight) / 2 + rightBaseLine;
                    point.textWidth = rightTextViewWidth;
                }
            }
        }

        mMapPoint.put(number, point);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
//        return super.onTouchEvent(event);
    }

    private static class Point {
        private float x;
        private float y;
        private float textSize;
        private float textWidth;

        public Point() {
        }

        public float getTextWidth() {
            return textWidth;
        }

        public void setTextWidth(float textWidth) {
            this.textWidth = textWidth;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getTextSize() {
            return textSize;
        }

        public void setTextSize(float textSize) {
            this.textSize = textSize;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "x=" + x +
                    ", y=" + y +
                    ", textSize=" + textSize +
                    '}';
        }
    }
}
