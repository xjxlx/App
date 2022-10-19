package com.android.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
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

    /**
     * 每个文字的间距
     */
    private final float mNumberInterval = ConvertUtil.toDp(100);

    private final Paint mPaintCenterRect = new Paint();
    // 阴影线
    private final Paint mPaintCenterRectLine = new Paint();

    private final Paint mPaintNumber = new Paint();// 数字的paint
    private final float mNumberMinSize = 48;// 文字的最小值
    private final float mNumberMaxSize = 100;// 文字的最大值
    public int mSelectorIndex = 9; // 选中的数字的角标
    private HashMap<String, Point> mMapPoint = new HashMap<>();
    private float mDx;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mStartX;
    // 存储一份原始的数据
    private HashMap<String, Float> mMapFontSize = new HashMap<>();

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

        if (mMapPoint.size() == 0) {
        }

        // 绘制阴影线
        int lineWidth = roundRectWidth / 2;
        int lineHeight = roundRectHeight / 2;
        canvas.drawLine(mCenterRect.left + lineWidth, mCenterRect.top, mCenterRect.left + lineWidth, mCenterRect.bottom, mPaintCenterRectLine);
        canvas.drawLine(mCenterRect.left, mCenterRect.top + lineHeight, mCenterRect.right, mCenterRect.top + lineHeight, mPaintCenterRectLine);

        // 绘制中间的数字

        for (int i = 0; i < mNumbers.length; i++) {
            String number = mNumbers[i];
            Point point = mMapPoint.get(number);
            if (point != null) {
                mPaintNumber.setTextSize(point.textSize);
                canvas.drawText(number, point.x + mDx, point.y, mPaintNumber);
            }
        }
    }

    private synchronized void calculatePosition() {
        // 1:计算中间的位置
        if (mSelectorIndex < mNumbers.length) {
            String number = mNumbers[mSelectorIndex];
            // 设置文字的大小
            mPaintNumber.setTextSize(mNumberMaxSize);

            // 存储文字的大小
            mMapFontSize.put(number, mNumberMaxSize);

            saveParameter(1, number, mSelectorIndex);
        }

        // 计算左侧的数据
        for (int i = mSelectorIndex - 1; i >= 0; i--) {
            String numberLeft = mNumbers[i];

            saveParameter(2, numberLeft, i);
        }

        // 计算右侧的值
        for (int i = mSelectorIndex + 1; i < mNumbers.length; i++) {
            String numberRight = mNumbers[i];
            saveParameter(3, numberRight, i);
        }
    }

    private synchronized void saveParameter(int type, String number, int index) {

        Point point = getPoint(number);

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
            if (index + 1 < mNumbers.length) {
                // 上一个数字
                String numberRight = mNumbers[index + 1];
                Point pointRight = mMapPoint.get(numberRight);
                if (pointRight != null) {
                    // 重新设置文字的大小
                    float textSize = pointRight.textSize;
                    point.textSize = Math.max(textSize - 8, mNumberMinSize);

                    // 设置文字大小，重新测量位置
                    mPaintNumber.setTextSize(point.textSize);

                    // 获取文字宽度
                    float leftTextViewWidth = CustomViewUtil.getTextViewWidth(mPaintNumber, number);

                    // 文字高度
                    float leftNumberHeight = CustomViewUtil.getTextSize(mPaintNumber, number)[1];
                    // 文字基线
                    float leftBaseLine = CustomViewUtil.getBaseLine(mPaintNumber, number);

                    // 左侧x = 上一个的x + 间距 + 本身的宽度
                    if (index > 0) {
                        point.x = pointRight.x - mNumberInterval - leftTextViewWidth;
                    } else if (index == 0) {
                        point.x = pointRight.x - mNumberInterval;
                    }
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

    private Point getPoint(String number) {
        Point point = mMapPoint.get(number);
        if (point == null) {
            point = new Point();
        }
        return point;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = (int) event.getRawX();
                return true;

            case MotionEvent.ACTION_MOVE:
                int endX = (int) event.getRawX();

                mDx += endX - mStartX;

                calculatePosition();
                mStartX = endX;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                sss();

                break;
        }
        return super.onTouchEvent(event);
    }

    public synchronized void sss() {
        for (int i = 0; i < mNumbers.length; i++) {
            // 当前数字
            String number = mNumbers[i];

            // 原始的位置
            Point point = getPoint(number);
            float x = point.x;
            float textWidth = point.textWidth;

            /**
             * 文字的位置逻辑：
             * 1：中心线的位置
             *
             * 1：文字原来位置 + 文字的宽度一半 =  文字最中心的位置
             * 2：目前位置 >= 文字原来位置 - 间距的一半
             * 3：目前位置 <= 文字原来位置 + 文字的宽度 + 间距的一半
             */

//            LogUtil.e("number:" + number
//                    + " 文字的中心位置" + getWidth() / 2
//                    + " 文字现在的位置：" + position
//                    + " 文字左侧的边界：" + (point.x - mNumberInterval / 2)
//                    + "  文字右侧的边距：" + (point.x + mNumberInterval / 2));

            LogUtil.e(
                    "number: " + number
                            + " left:" + mCenterRect.left
                            + " x-l:" + (point.x + mDx)
                            + " right:" + mCenterRect.right
                            + " x-r:" + (point.x + point.textWidth + mDx)
                            + " mDx:" + (mDx)
            );

            if (point.x + mDx > mCenterRect.left && point.x + mDx + point.textWidth > mCenterRect.right) {
                LogUtil.e("选中饿了！" + number);
                mSelectorIndex = i;
                break;
            }
        }
        invalidate();
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
