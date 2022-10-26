package com.android.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.helper.utils.ConvertUtil;

import java.util.HashMap;

/**
 * @author : 流星
 * @CreateDate: 2022/10/22-14:58
 * @Description:
 */
public class ScrollNumber extends View {

    private String TAG = "NumberTouch";
    private final String[] mNumbers = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"
    };

    private final RectF mCenterRect = new RectF();
    private float mMaxWidth;// 屏幕最大的宽度
    private float mMaxHeight; // 屏幕最大的高度
    private int mRoundRectWidth = (int) toDp(110); // 矩形的宽度
    private final int mRoundRectHeight = (int) toDp(128); // 矩形的高度
    private final float minuteInterval = ConvertUtil.toDp(35); // 分钟和矩形的间隔

    /**
     * 每个文字的间距
     */
    private final float mNumberInterval = toDp(65);
    // 间隙的一半
    private final float mIntervalHalf = mNumberInterval / 2;

    private final Paint mPaintCenterRect = new Paint();
    // 阴影线
    private final Paint mPaintCenterRectLine = new Paint();

    private final Paint mPaintNumber = new Paint();// 数字的paint
    private final int mNumberMinSize = 40;// 文字的最小值
    private final int mNumberMaxSize = 100;// 文字的最大值
    public int mSelectorIndex = 6; // 选中的数字的角标
    private float mDx;
    private float mCurrentDx;

    // 字体颜色
    private final int[] mColors = new int[]{Color.TRANSPARENT, Color.TRANSPARENT, Color.WHITE, Color.TRANSPARENT, Color.TRANSPARENT};
    float[] positions = new float[]{0.1f, 0.28f, 0.5f, 0.73f, 1f};
    /**
     * 底部的分钟
     */
    private Paint mPaintMinute;

    private final HashMap<String, Point> mMapPoint = new HashMap<>();

    private final GestureDetector mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mCurrentDx = distanceX;

            if (distanceX > 0) {
                log("-----> 左侧");
                String left = mNumbers[0];
                Point pointLeft = getPoint(left);
                float leftStartX = pointLeft.getStartX();
                if (leftStartX >= mCenterRect.left) {
                    return true;
                }
            } else {
                log("-----> 右侧");
                String rightNumber = mNumbers[mNumbers.length - 1];
                Point pointRight = getPoint(rightNumber);
                if (pointRight.endX <= mCenterRect.right) {
                    return true;
                }
            }
            mDx += distanceX;

            calculationPosition();
            invalidate();
            return true;
        }
    });

    public ScrollNumber(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mPaintCenterRect.setColor(Color.parseColor("#4d000000"));
        // 数字
        mPaintNumber.setColor(Color.WHITE);
        mPaintNumber.setAntiAlias(true);
        mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);

        // 线的颜色
        mPaintCenterRectLine.setColor(Color.WHITE);

        mPaintMinute = new Paint();
        mPaintMinute.setColor(Color.WHITE);
        mPaintMinute.setTextSize(ConvertUtil.toSp(36));
        mPaintMinute.setStyle(Paint.Style.FILL);
        mPaintMinute.setTypeface(Typeface.DEFAULT_BOLD);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取view的宽度
        mMaxWidth = MeasureSpec.getSize(widthMeasureSpec);

        // 先给他设置一个高度，然后再去计算，这个计算结果可能会有多次
        if (mMaxHeight <= 0) {
            mMaxHeight = mCenterRect.bottom + minuteInterval;
        }
        setMeasuredDimension((int) mMaxWidth, (int) mMaxHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //动态改变选中文字的宽度
        String number = mNumbers[mSelectorIndex];
        int selector = Integer.parseInt(number);
        if (selector >= 10) {
            mRoundRectWidth = 130;
        } else {
            mRoundRectWidth = 110;
        }

        // 阴影矩形
        mCenterRect.left = (mMaxWidth - mRoundRectWidth) / 2;
        mCenterRect.right = mCenterRect.left + mRoundRectWidth;
        mCenterRect.top = 0;
        mCenterRect.bottom = mCenterRect.top + mRoundRectHeight;

        // 绘制指定大小的透明背景
        canvas.drawRoundRect(mCenterRect, 20, 20, mPaintCenterRect);

        // 绘制阴影线
//        int lineWidth = mRoundRectWidth / 2;
//        int lineHeight = mRoundRectHeight / 2;
//        canvas.drawLine(mCenterRect.left + lineWidth, mCenterRect.top, mCenterRect.left + lineWidth, mCenterRect.bottom, mPaintCenterRectLine);
//        canvas.drawLine(mCenterRect.left, mCenterRect.top + lineHeight, mCenterRect.right, mCenterRect.top + lineHeight, mPaintCenterRectLine);

        // 绘制底部固定文字
        String minute = "分钟";
        float textViewWidth = getTextViewWidth(mPaintMinute, minute);
        float minuteX = mCenterRect.left + ((mRoundRectWidth - textViewWidth) / 2);
        float minuteHeight = getTextHeight(mPaintMinute, minute);
        float baseLine = getBaseLine(mPaintMinute, minute);
        float minuteY = mCenterRect.bottom + minuteInterval + minuteHeight / 2 + baseLine;
        canvas.drawText(minute, minuteX, minuteY, mPaintMinute);

        // 重新测量高度
        if (mMaxHeight < mRoundRectHeight + minuteInterval + minuteHeight + baseLine) {
            // 屏幕的高度 =  矩形高度 + 间距 +分钟的高度
            mMaxHeight = mCenterRect.bottom + minuteInterval + minuteHeight + baseLine;
            requestLayout();
        }

        drawTextNumber(canvas);
    }

    private void drawTextNumber(Canvas canvas) {
        // 绘制滑动的文字
        float mNumberStartX = 0;
        float mNumberStartY = mCenterRect.bottom;
        float mNumberEndX = mMaxWidth;
        float mNumberEndY = mCenterRect.bottom;

        @SuppressLint("DrawAllocation")
        LinearGradient linearGradient = new LinearGradient(
                mNumberStartX,
                mNumberStartY,
                mNumberEndX,
                mNumberEndY,
                mColors,
                positions,
                Shader.TileMode.MIRROR);

        mPaintNumber.setShader(linearGradient);

        // 找到中间的数据
        String middleNumber = mNumbers[mSelectorIndex];
        // 设置文字大小
        mPaintNumber.setTextSize(mNumberMaxSize);
        // 获取文字宽度
        float middleWidth = getTextViewWidth(mPaintNumber, middleNumber);
        float middleHeight = getTextHeight(mPaintNumber, middleNumber);
        float middleBaseLine = getBaseLine(mPaintNumber, middleNumber);

        Point pointMiddle = getPoint(middleNumber);
        pointMiddle.textSize = mNumberMaxSize;
        if (pointMiddle.startX == 0) {
            // 目标选中的位置，始终在最中心的位置
            pointMiddle.startX = (getWidth() - middleWidth) / 2 + mDx;
        } else {
            // 滑动的时候，则每次增加当前的偏移值
            pointMiddle.startX += mCurrentDx;
        }
        pointMiddle.y = mCenterRect.top + (mRoundRectHeight - middleHeight) / 2 + middleBaseLine;
        pointMiddle.numberWidth = middleWidth;
        pointMiddle.endX = pointMiddle.startX + middleWidth;
        canvas.drawText(middleNumber, pointMiddle.startX, pointMiddle.y, mPaintNumber);

        setPoint(middleNumber, pointMiddle);

        // 绘制左侧部分
        int tempIndex = 1;
        int leftTotalWidth = 0;
        int leftTotalInterval = 0;
        for (int i = mSelectorIndex - 1; i >= 0; i--) {
            String leftNumber = mNumbers[i];
            int size;
            if (mNumberMaxSize - tempIndex * 10 > mNumberMinSize) {
                size = mNumberMaxSize - tempIndex * 10;
            } else {
                size = mNumberMinSize;
            }

            // 设置字体大小
            mPaintNumber.setTextSize(size);
            float leftTextViewWidth = getTextViewWidth(mPaintNumber, leftNumber);
            float leftTextHeight = getTextHeight(mPaintNumber, leftNumber);
            float leftBaseLine = getBaseLine(mPaintNumber, leftNumber);

            leftTotalWidth += leftTextViewWidth;
            leftTotalInterval += mNumberInterval;
            float x = pointMiddle.startX - leftTotalInterval - leftTotalWidth;

            Point pointLeft = getPoint(leftNumber);
            pointLeft.textSize = size;
            pointLeft.numberWidth = leftTextViewWidth;
            pointLeft.startX = x;
            pointLeft.y = mCenterRect.top + (mRoundRectHeight - leftTextHeight) / 2 + leftBaseLine;
            pointLeft.endX = pointLeft.startX + leftTextViewWidth;

            canvas.drawText(leftNumber, x, pointLeft.y, mPaintNumber);
            tempIndex++;

            setPoint(leftNumber, pointLeft);
        }

        // 绘制右侧位置
        tempIndex = 1;
        int rightTotalWidth = 0;
        int rightTotalInterval = 0;
        for (int i = mSelectorIndex + 1; i < mNumbers.length; i++) {
            String rightNumber = mNumbers[i];

            int size;
            if (mNumberMaxSize - tempIndex * 10 > mNumberMinSize) {
                size = mNumberMaxSize - tempIndex * 10;
            } else {
                size = mNumberMinSize;
            }

            // 设置大小
            mPaintNumber.setTextSize(size);
            float rightTextViewWidth = getTextViewWidth(mPaintNumber, rightNumber);
            float rightTextHeight = getTextHeight(mPaintNumber, rightNumber);
            float rightBaseLine = getBaseLine(mPaintNumber, rightNumber);

            rightTotalInterval += mNumberInterval;
            float x = pointMiddle.endX + rightTotalInterval + rightTotalWidth;

            Point pointRight = getPoint(rightNumber);
            pointRight.startX = x;
            pointRight.textSize = size;
            pointRight.numberWidth = rightTextViewWidth;
            pointRight.y = mCenterRect.top + (mRoundRectHeight - rightTextHeight) / 2 + rightBaseLine;
            pointRight.endX = pointRight.startX + rightTextViewWidth;

            canvas.drawText(rightNumber, x, pointRight.y, mPaintNumber);
            tempIndex++;
            rightTotalWidth += rightTextViewWidth;

            setPoint(rightNumber, pointRight);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mDx = 0;
            String number = mNumbers[mSelectorIndex];
            Point point = mMapPoint.get(number);
            if (point != null) {
                point.startX = 0;
            }
            invalidate();
        }
        return true;
    }

    public Point getPoint(String number) {
        Point point = mMapPoint.get(number);
        if (point == null) {
            point = new Point();
        }
        return point;
    }

    public void setPoint(String number, Point point) {
        mMapPoint.put(number, point);
    }

    private void calculationPosition() {
        for (int i = 0; i < mMapPoint.size(); i++) {
            String mNumber = mNumbers[i];
            Point point = mMapPoint.get(mNumber);
            if (point != null) {
                if (mDx > 0) { //手指向左移动，
                    log("⭐️--->向左");
                    // 文字右侧位置 > 矩形左侧边缘  && 文字左侧位置 < 矩形右侧边缘
                    if (mCenterRect.left < point.endX && mCenterRect.right > point.startX) {
                        Log.e("选中位置", i + "");
                        mSelectorIndex = i;
                        break;
                    }
                } else if (mDx < 0) { //手指向右移动
                    log("⭐️--->向右");
                    // 文字左侧 小于 矩形右侧  && 文字右侧 大于 矩形左侧
                    if (mCenterRect.right > point.startX && mCenterRect.left < point.endX) {
                        Log.e("选中位置", i + "");
                        mSelectorIndex = i;
                        Log.e("L_mSelectorIndex", mSelectorIndex + "");
                        break;
                    }
                } else {
//                    mSelectorIndex = initSelectorIndex;
                    log("选中的是：" + mSelectorIndex);
                    break;
                }
            }
        }
    }

    public static class Point {
        private float numberWidth;
        private float startX;
        private float endX;
        private int textSize;
        private float y;

        public float getEndX() {
            return endX;
        }

        public void setEndX(float endX) {
            this.endX = endX;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public float getNumberWidth() {
            return numberWidth;
        }

        public void setNumberWidth(float numberWidth) {
            this.numberWidth = numberWidth;
        }

        public float getStartX() {
            return startX;
        }

        public void setStartX(float startX) {
            this.startX = startX;
        }

        @Override
        public String toString() {
            return "Point{" +
                    "width=" + numberWidth +
                    ", startX=" + startX +
                    ", textSize=" + textSize +
                    '}';
        }
    }

    /**
     * @param dp 具体的dp值
     * @return 使用标准的dp值
     */
    public static float toDp(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

    /**
     * @param paint   画笔
     * @param content 内容
     * @return 返回测量文字的宽度
     */
    public static float getTextViewWidth(Paint paint, String content) {
        if (paint == null || TextUtils.isEmpty(content)) {
            return 0;
        }
        return paint.measureText(content, 0, content.length());
    }

    /**
     * @param paint   画笔
     * @param content 内容
     * @return 获取文字的高度
     */
    public static float getTextHeight(Paint paint, String content) {
        if ((paint != null) && (!TextUtils.isEmpty(content))) {
            Rect rect = new Rect();
            paint.getTextBounds(content, 0, content.length(), rect);
            return rect.height();
        }
        return 0;
    }

    /**
     * @param paint   画笔
     * @param content 内容
     * @return 根据画笔和内容返回baseLine的基线, 适用于view写在开始的位置
     */
    public static float getBaseLine(Paint paint, String content) {
        if (paint == null || (TextUtils.isEmpty(content))) {
            return 0f;
        }
        Rect rect = new Rect();
        paint.getTextBounds(content, 0, content.length(), rect);
        return (float) Math.abs(rect.top);
    }

    public void log(String value) {
        if (!TextUtils.isEmpty(value)) {
            Log.e(TAG, value);
        }
    }

}
