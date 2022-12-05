package com.android.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.helper.utils.ConvertUtil;
import com.android.helper.utils.LogUtil;

import java.util.HashMap;

public class ScrollNumberView extends View {

    private String TAG = "NumberTouch";
    private final String[] mNumbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"};

    private final RectF mCenterRect = new RectF();
    private float mMaxWidth;
    private float mMaxHeight;
    private final float mRoundRectWidth = ConvertUtil.toPx(135);
    private final float mRoundRectHeight = ConvertUtil.toPx(135);
    private final float minuteInterval = ConvertUtil.toPx(35);
    private final float mNumberInterval = ConvertUtil.toPx(50);
    private final Paint mPaintCenterRect = new Paint();
    private final Paint mPaintCenterRectLine = new Paint();

    private final Paint mPaintNumber = new Paint();
    private final float mNumberMinSize = 40;
    private final float mNumberMaxSize = 100;
    private final float mSizeRate = 0.23f;
    private int mSelectorIndex = 6;
    private float mCurrentDx;
    private final String minute = "分钟";
    private final int[] mColors = new int[]{Color.TRANSPARENT, Color.TRANSPARENT, Color.WHITE, Color.TRANSPARENT, Color.TRANSPARENT};
    private float[] positions = new float[]{0.1f, 0.1f, 0.5f, 0.8f, 1f};
    private Paint mPaintMinute;

    private final HashMap<String, Point> mMapPoint = new HashMap<>();
    private SelectorListener mSelectorListener;
    private float mMaxWidthNumber = 0f;
    private float mScreenX = 0f;
    private float mNumberCenterX = 0f;
    private boolean isUp = false;

    private final GestureDetector mGestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mCurrentDx = distanceX;
            boolean isStop = false;
            // last number
            if (mSelectorIndex == mNumbers.length - 1) {
                if (distanceX > 0) {
                    String right = mNumbers[mNumbers.length - 1];
                    Point pointRight = getPoint(right);
                    float numberCenterX = pointRight.numberCenterX;
                    if (numberCenterX - mCurrentDx <= mScreenX) {
                        isStop = true;
                    }
                }
            } else if (mSelectorIndex == 0) {
                if (distanceX < 0) {
                    String left = mNumbers[0];
                    Point pointLeft = getPoint(left);
                    float numberCenterX = pointLeft.numberCenterX;
                    if (numberCenterX - mCurrentDx > mScreenX) {
                        isStop = true;
                    }
                }
            }

            if (!isStop) {
                calculationPosition();
                invalidate();
            }
            return true;
        }
    });

    public ScrollNumberView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mPaintCenterRect.setColor(Color.parseColor("#4d000000"));
        mPaintNumber.setColor(Color.WHITE);
        mPaintNumber.setAntiAlias(true);
        mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);
        mPaintNumber.setTextAlign(Paint.Align.CENTER);
        mPaintNumber.setTextSize(mNumberMaxSize);

        mPaintCenterRectLine.setColor(Color.WHITE);

        mPaintMinute = new Paint();
        mPaintMinute.setColor(Color.WHITE);
        mPaintMinute.setTextSize(ConvertUtil.toPx(36));
        mPaintMinute.setStyle(Paint.Style.FILL);
        mPaintMinute.setTypeface(Typeface.DEFAULT_BOLD);
        mPaintMinute.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMaxWidth = MeasureSpec.getSize(widthMeasureSpec);

        float minuteHeight = getTextHeight(mPaintMinute, minute);
        if (mMaxHeight < mRoundRectHeight + minuteInterval + minuteHeight) {
            mMaxHeight = mRoundRectHeight + minuteInterval + minuteHeight;
        }
        setMeasuredDimension((int) mMaxWidth, (int) mMaxHeight);

        mScreenX = getMeasuredWidth() / 2;
        LogUtil.e("mCenterX: " + mScreenX);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCenterRect.left = (mMaxWidth - mRoundRectWidth) / 2;
        mCenterRect.right = mCenterRect.left + mRoundRectWidth;
        mCenterRect.top = 0;
        mCenterRect.bottom = mCenterRect.top + mRoundRectHeight;

        canvas.drawRoundRect(mCenterRect, 20, 20, mPaintCenterRect);

//        int lineWidth = mRoundRectWidth / 2;
//        int lineHeight = mRoundRectHeight / 2;
//        canvas.drawLine(mCenterRect.left + lineWidth, mCenterRect.top, mCenterRect.left + lineWidth, mCenterRect.bottom, mPaintCenterRectLine);
//        canvas.drawLine(mCenterRect.left, mCenterRect.top + lineHeight, mCenterRect.right, mCenterRect.top + lineHeight, mPaintCenterRectLine);

        float minuteX = mCenterRect.left + (mRoundRectWidth / 2);
        float baseLine = getBaseLine(mPaintMinute, minute);
        float minuteY = mCenterRect.bottom + minuteInterval + baseLine;
        canvas.drawText(minute, minuteX, minuteY, mPaintMinute);

        drawNewTextNumber(canvas);
    }

    private void drawNewTextNumber(Canvas canvas) {
        if (mMaxWidthNumber <= 0) {
            for (String maxWidthNumber : mNumbers) {
                float textViewWidth = getTextViewWidth(mPaintNumber, maxWidthNumber);
                if (textViewWidth > mMaxWidthNumber) {
                    mMaxWidthNumber = textViewWidth;
                }
            }
        }
        LogUtil.e("mMaxWidthNumber:" + mMaxWidthNumber);

        for (int index = 0; index < mNumbers.length; index++) {
            String number = mNumbers[index];
            // centerX value
            if (index < mSelectorIndex) {
                mNumberCenterX = mScreenX - (mSelectorIndex - index) * mMaxWidthNumber - (mSelectorIndex - index) * mNumberInterval;
            } else if (index > mSelectorIndex) {
                mNumberCenterX = mScreenX + (index - mSelectorIndex) * mMaxWidthNumber + (index - mSelectorIndex) * mNumberInterval;
            } else {
                mNumberCenterX = mScreenX;
            }
            LogUtil.e("numberCenterX  :" + mNumberCenterX + "  number: " + number + "   mSelectorIndex: " + mSelectorIndex);

            // up or  init  offset position
            Point point = getPoint(mNumbers[index]);
            if (point.numberCenterX == 0 || isUp) {
                if (index == mSelectorIndex) {
                    point.numberCenterX = mScreenX;
                } else {
                    point.numberCenterX = mNumberCenterX;
                }
            } else {
                // scroll
                // right -> add ,left -> add --> Subtract the negative and add the positive
                point.numberCenterX -= mCurrentDx;
            }

            //根据滑动的距离与中间点的位置计算文字大小
            point.textSize = getDrawTextSize(number, point.numberCenterX, index, mMaxWidthNumber, mNumberCenterX);

            setPoint(number, point);
            float alpha = getAlpha(number, index);
            mPaintNumber.setAlpha((int) alpha);
            mPaintNumber.setTextSize(point.textSize);
            float middleBaseLine = getBaseLine(mPaintNumber, number);
            float newY = mCenterRect.top + (mRoundRectHeight - getTextHeight(mPaintNumber, number)) / 2 + middleBaseLine;
            canvas.drawText(number, point.numberCenterX, newY, mPaintNumber);
        }

        isUp = false;
    }

    private int getAlpha(String number, int index) {
        int alpha;
        float alphaRate = 0.25f;
        int distance = Math.abs(mSelectorIndex - index);
        alpha = (int) (255 - ((255) * distance * alphaRate));
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 255) {
            alpha = 255;
        }
        LogUtil.e("alhpa: " + alpha + "    number: " + number + "  distance: " + distance);
        return alpha;
    }

    private float getDrawTextSize(String number, float storageCenterX, int index, float middleWidthNew, float currentCenterX) {
        float size;
        int distance = Math.abs(mSelectorIndex - index);
        // default value  =  maxSize - ( maxSize * distance)
        size = mNumberMaxSize - ((mNumberMaxSize) * distance * mSizeRate);
        LogUtil.e("size  size：" + size + "  number :" + number);

        // 滑动的情况
        if (storageCenterX != currentCenterX) {
            // unit width
            float unitWidth = mNumberInterval + middleWidthNew;

            float ratio = Math.abs(storageCenterX - mScreenX) / unitWidth;
            LogUtil.e("ratio: " + ratio);
            size = mNumberMaxSize - ((mNumberMaxSize * mSizeRate) * ratio);
        }

        if (size < mNumberMinSize) {
            size = mNumberMinSize;
        } else if (size > mNumberMaxSize) {
            size = mNumberMaxSize;
        }
        return size;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mGestureDetector.onTouchEvent(event);
                return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            if (mSelectorListener != null) {
                String mNumber = mNumbers[mSelectorIndex];
                int time = 0;
                if (!TextUtils.isEmpty(mNumber)) {
                    time = Integer.parseInt(mNumber);
                }
                mSelectorListener.onSelector(time);
                Log.e("ScrollNumber3", "selector --- time:" + time);
            }
            isUp = true;
            invalidate();
        }
        return true;
    }

    private Point getPoint(String number) {
        Point point = mMapPoint.get(number);
        if (point == null) {
            point = new Point();
        }
        return point;
    }

    private void setPoint(String number, Point point) {
        mMapPoint.put(number, point);
    }

    private void calculationPosition() {
        float minDistance = -1f;
        int minIndex = -1;
        for (int i = 0; i < mMapPoint.size(); i++) {
            String mNumber = mNumbers[i];
            Point point = mMapPoint.get(mNumber);
            if (point != null) {
                if (minDistance == -1f) {
                    minDistance = Math.abs(point.numberCenterX - mScreenX);
                    minIndex = i;
                } else {
                    float newDistance = Math.abs(point.numberCenterX - mScreenX);
                    if (newDistance < minDistance) {
                        minDistance = newDistance;
                        minIndex = i;
                    }
                }
            }
        }
        mSelectorIndex = minIndex;
    }

    public static class Point {
        private float numberCenterX;
        private float textSize;

        public float getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public float getNumberCenterX() {
            return numberCenterX;
        }

        public void setNumberCenterX(float numberCenterX) {
            this.numberCenterX = numberCenterX;
        }

        @Override
        public String toString() {
            return "Point{ startX = " + numberCenterX + ", textSize = " + textSize + '}';
        }
    }

    /**
     * @param paint   画笔
     * @param content 内容
     * @return 返回测量文字的宽度
     */
    private float getTextViewWidth(Paint paint, String content) {
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
    private float getTextHeight(Paint paint, String content) {
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
    private float getBaseLine(Paint paint, String content) {
        if (paint == null || (TextUtils.isEmpty(content))) {
            return 0f;
        }
        Rect rect = new Rect();
        paint.getTextBounds(content, 0, content.length(), rect);
        return (float) Math.abs(rect.top);
    }

    public void setSelectorListener(SelectorListener selectorListener) {
        if (selectorListener != null) {
            mSelectorListener = selectorListener;
        }

        if (mSelectorListener != null) {
            String mNumber = mNumbers[mSelectorIndex];
            int time = 0;
            if (!TextUtils.isEmpty(mNumber)) {
                time = Integer.parseInt(mNumber);
            }
            mSelectorListener.onSelector(time);
            Log.e("ScrollNumber", "selector --- time:" + time);
        }
    }

    public interface SelectorListener {
        void onSelector(int selector);
    }

}
