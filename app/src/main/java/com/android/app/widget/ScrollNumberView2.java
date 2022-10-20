package com.android.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.helper.utils.ConvertUtil;
import com.android.helper.utils.CustomViewUtil;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author : 流星
 * @CreateDate: 2022/9/22-18:01
 * @Description:
 */
public class ScrollNumberView2 extends View {

    /**
     * 透明的矩形
     */
    private Paint mPaintRectangle;
    /**
     * 滑动的数字
     */
    private Paint mPaintNumber;
    /**
     * 底部的分钟
     */
    private Paint mPaintMinute;
    private RectF drawRoundRect;
    private final float roundRectWidth = ConvertUtil.toDp(125f);
    private final float roundRectHeight = ConvertUtil.toDp(125f);
    private final float minuteInterval = ConvertUtil.toDp(26);

    // 文字的最小值
    private final float mNumberMinSize = 48;
    // 文字的最大值
    private final float mNumberMaxSize = 100;

    // 字体颜色
    private final int[] mColors = new int[]{Color.TRANSPARENT, Color.TRANSPARENT, Color.WHITE, Color.TRANSPARENT, Color.TRANSPARENT};
    float[] positions = new float[]{0.1f, 0.28f, 0.5f, 0.73f, 1f};
    // 文字左侧的左边距
    private final int mNumberLeftValue = 0;

    private final String[] mNumbers = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"
    };

    /**
     * 每个文字的间距
     */
    private final float mNumberInterval = ConvertUtil.toDp(65);

    /**
     * 文字间距的一半距离
     */
    private float mIntegerHalf = 0;

    /**
     * 文字大小的size存储
     */
    private final HashMap<String, float[]> mMapSize = new HashMap<>();

    /**
     * 文字和间距的总和
     */
    private int mSum = 0;

    // 最大的文字高度
    private float mContentMaxHeight;

    // view 的整体宽度
    private int mMaxWidth;
    // 中线
    private float centerLines = 0;

    // 默认选中的文字
    private String mDefaultNumberContent = "7";

    /**
     * 用来存储文字的字体大小
     */
    private final HashMap<String, Float> mMapFontSize = new HashMap<>();

    /**
     * 用来存储当前的角标位置，存储的位置是当前计算过dx的位置
     */
    private final HashMap<String, Position> mMapPosition = new HashMap<>();
    private float touchOffsetX;

    public ScrollNumberView2(Context context) {
        super(context);
        initView(context, null);
    }

    public ScrollNumberView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        drawRoundRect = new RectF();
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.parseColor("#4d000000"));

        mPaintMinute = new Paint();
        mPaintMinute.setColor(Color.WHITE);
        mPaintMinute.setTextSize(ConvertUtil.toSp(36));
        mPaintMinute.setStyle(Paint.Style.FILL);
        mPaintMinute.setTypeface(Typeface.DEFAULT_BOLD);

        mPaintNumber = new Paint();
        mPaintNumber.setStyle(Paint.Style.FILL);
        mPaintNumber.setTextSize(ConvertUtil.toDp(mNumberMinSize));
        mPaintNumber.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.e("--->onMeasure");
        // 获取view的宽度
        mMaxWidth = MeasureSpec.getSize(widthMeasureSpec);
        if (mMaxWidth > 0) {
            centerLines = mMaxWidth / 2;
        }

        drawRoundRect.left = (mMaxWidth - roundRectWidth) / 2;
        drawRoundRect.right = drawRoundRect.left + roundRectWidth;
        drawRoundRect.top = 0;
        drawRoundRect.bottom = drawRoundRect.top + roundRectHeight;

        LogUtil.e("left:" + drawRoundRect);

        // 计算出总体的高度后，重新去设置高度
        setMeasuredDimension(mMaxWidth, 500);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mIntegerHalf <= 0) {
            mIntegerHalf = mNumberInterval / 2;
        }

        LogUtil.e("--->onDraw");
        // 绘制指定大小的透明背景
        canvas.drawRoundRect(drawRoundRect, 20, 20, mPaintRectangle);

        // 绘制底部固定文字
        float minuteX = 0;
        float minuteY = 0;
        String minute = "分钟";
        float textViewWidth = CustomViewUtil.getTextViewWidth(mPaintMinute, minute);
        minuteX = drawRoundRect.left + ((roundRectWidth - textViewWidth) / 2);
        float minuteHeight = CustomViewUtil.getTextHeight(mPaintMinute, minute);
        minuteY = drawRoundRect.bottom + minuteInterval + minuteHeight;
        canvas.drawText(minute, minuteX, minuteY, mPaintMinute);

        // 计算当前默认选中的view以及其他view的字体大小
        calculateDefaultSelectorPosition(mDefaultNumberContent);

        // 计算出绘制文字宽度的总和
        calculateNumberSum();

        // 绘制滑动的文字
        float mNumberStartX = 0;
        float mNumberStartY = drawRoundRect.bottom;
        float mNumberEndX = mMaxWidth;
        float mNumberEndY = drawRoundRect.bottom;

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

        /*
         * 上一个文字的宽度
         */
        float previousNumberWidth = 0;
        // 第一个数据，文字的x轴等于开始文字的X轴位置

        // 文字最左侧的X轴 =  文字的左侧开始位置 + ( 布局的总体宽度 - 所有文字的总和 + 文字间距的总和 ) / 2
        int x = mNumberLeftValue + ((mMaxWidth - mSum) / 2);
        int y = 0;

        for (int i = 0; i < mNumbers.length; i++) {
            String number = mNumbers[i];

            // 获取文字的大小
            float[] itemSize = mMapSize.get(number);

            float itemWidth = itemSize[0];
            float itemHeight = itemSize[1];

            // X轴 = 初始的左侧间距 + 上一个文字的宽度
            x += previousNumberWidth;
            if (i > 0) {
                x += mNumberInterval;
            }

            // 设置粗体
            if (TextUtils.equals(number, mDefaultNumberContent)) {
                mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);
            } else {
                mPaintNumber.setTypeface(Typeface.DEFAULT);
            }

            // 设置文字大小
            mPaintNumber.setTextSize(ConvertUtil.toSp(mMapFontSize.get(number)));

            // 高度 = 矩形的高度 - 文字的高度 - 基准线的高度
            float baseLine = CustomViewUtil.getBaseLine(mPaintNumber, number);
            y = (int) ((drawRoundRect.bottom - itemHeight) / 2 + baseLine);

            if (TextUtils.equals(number, mDefaultNumberContent) && isUp) {
                canvas.drawText(number, (getWidth() - itemWidth) / 2, y, mPaintNumber);
            } else {
                canvas.drawText(number, x + dx + touchOffsetX, y, mPaintNumber);
            }

            previousNumberWidth = itemWidth;

            // 存储view当前的位置
            mMapPosition.put(number, new Position((x + dx), (int) (x + dx + itemWidth)));
        }

        mPaintNumber.setStrokeWidth(1);
        mPaintNumber.setColor(Color.WHITE);
        canvas.drawLine(centerLines, 0, centerLines + 2, roundRectHeight, mPaintNumber);
        canvas.drawLine(drawRoundRect.left, drawRoundRect.top + (roundRectHeight / 2), drawRoundRect.right, drawRoundRect.top + (roundRectHeight / 2), mPaintNumber);

        if (isFirst) {
            // 滑动的时候，动态改变当前选中的角标
            scrollDefault();
        }
    }

    /**
     * 计算出所有文字的位置和，使文字占据中间的位置
     */
    private void calculateNumberSum() {
        mSum = 0;
        for (int i = 0; i < mNumbers.length; i++) {
            String number = mNumbers[i];

            // 重新设置文字大大小，避免测量出现异常
            mPaintNumber.setTextSize(ConvertUtil.toDp(mMapFontSize.get(number)));

            // 计算文字的大小
            float textHeight = CustomViewUtil.getTextHeight(mPaintNumber, number);
            if (mContentMaxHeight < textHeight) {
                mContentMaxHeight = textHeight;
            }
            float[] arr = new float[2];

            float textViewWidth = CustomViewUtil.getTextViewWidth(mPaintNumber, number);
            arr[0] = textViewWidth;
            arr[1] = textHeight;

            mSum += textViewWidth;
            mMapSize.put(number, arr);

            if (i > 0) {
                mSum += mNumberInterval;
            }
        }
    }

    /**
     * 计算默认选中的内容，并设定其他位置文字的大小
     *
     * @param selectorContent 当前选中的文字
     */
    private void calculateDefaultSelectorPosition(String selectorContent) {
        // 获取当前选中文字的
        int index = 0;
        for (int i = 0; i < mNumbers.length; i++) {
            String mNumber = mNumbers[i];
            if (TextUtils.equals(mNumber, selectorContent)) {
                index = i;
                break;
            }
        }

        // 存入最大的数据
        mMapFontSize.put(selectorContent, mNumberMaxSize);
        // 左侧的文字，大小逐渐减小
        float valueLeft;
        int temp1 = 0;

        for (int i = index; i >= 0; i--) {
            String item = mNumbers[i];
            temp1++;

            valueLeft = mNumberMaxSize - temp1 * 6;
            if (valueLeft < mNumberMinSize) {
                valueLeft = mNumberMinSize;
            }

            mMapFontSize.put(item, valueLeft);
        }

        // 右侧的文字，大小逐渐变大
        int temp2 = 0;
        float valueRight;
        for (int i = index; i < mNumbers.length; i++) {
            String item = mNumbers[i];
            temp2++;
            valueRight = mNumberMaxSize - temp2 * 6;

            if (valueRight < mNumberMinSize) {
                valueRight = mNumberMinSize;
            }
            mMapFontSize.put(item, valueRight);
        }
    }

    /**
     * 偏移的x轴的数据
     */
    private int dx;
    private int startX;
    private boolean isFirst = true;
    private boolean isUp = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isUp = false;
                startX = (int) event.getX();
                return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_HOVER_MOVE:
                isUp = false;
                LogUtil.e("----ACTION_MOVE");
                float endX = event.getX();
                int v = (int) (endX - startX);

                // 滑动的时候，动态改变当前选中的角标
                calculateSelectorPosition();

                // 计算边界的位置，禁止滑动
                String left = mNumbers[0];
                Position LeftPosition = mMapPosition.get(left);
                if (LeftPosition != null) {
                    float startX1 = LeftPosition.getStartX();
                    float[] leftArray = mMapSize.get(left);
                    if (leftArray != null) {
                        // 开始位置 + 偏移值 > = 中线 -文字的一半
                        if (startX1 + v >= centerLines - (leftArray[0]) / 2) {
                            LogUtil.e("到了最左侧了，停止后续的动作！");
                            return true;
                        }
                    }
                }

                String right = mNumbers[mNumbers.length - 1];
                Position rightPosition = mMapPosition.get(right);
                if (rightPosition != null) {
                    float startX = rightPosition.getStartX();
                    float[] rightArray = mMapSize.get(right);
                    if (rightArray != null) {
                        if (startX + v <= centerLines - rightArray[0] / 2) {
                            LogUtil.e("到了最迟右侧的距离了！");
                            return true;
                        }
                    }
                }

                dx += v;
                invalidate();
                startX = (int) endX;
                LogUtil.e("----->继续执行！");

                break;

            case MotionEvent.ACTION_UP:
                isUp = true;
                LogUtil.e("----ACTION_UP");

                scrollDefault();

                ToastUtil.show("当前选中的值是：" + mDefaultNumberContent);
                if (mCallBackListener != null) {
                    mCallBackListener.onCallBack(mDefaultNumberContent);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 计算出当前选中的是哪个内容
     */
    private void calculateSelectorPosition() {
        Set<Map.Entry<String, Position>> entries = mMapPosition.entrySet();
        for (Map.Entry<String, Position> entry : entries) {
            String key = entry.getKey();
            Position position = entry.getValue();
            float startX = position.getStartX();
            float endX = position.getEndX();

            if (startX >= drawRoundRect.left - mIntegerHalf && endX <= drawRoundRect.right + mIntegerHalf) {
                LogUtil.e("当前的选中为：" + key);
                mDefaultNumberContent = key;
                return;
            }
        }
    }

    /**
     * 滑动到指定的位置
     */
    private void scrollDefault() {
        // 让当前文字滑动到最中间的位置
        Position position = mMapPosition.get(mDefaultNumberContent);
        if (position != null) {
            // 存储文字的开始值
            int startX = position.getStartX();

            // 计算文字应该放到哪里
            // 文字的宽度
            float[] floats = mMapSize.get(mDefaultNumberContent);
            if (floats != null) {
                float width = floats[0];

                // 真正该摆放的位置
                float positionX = centerLines - width / 2;
                // 摆放的位置 - 开始的位置 = 抬起偏移的位置
                touchOffsetX = positionX - startX;
                LogUtil.e(" 文字的初始值是：" + startX + "\r\n"
                                + " 中线：" + centerLines + "\r\n"
                                + " 文字宽度:" + width + "\r\n"
//                        + " 矩形 - 文字宽度的一半：" + offsetX + "\r\n"
                                + " 文字正确的位置：" + positionX + "\r\n"
                                + " mv3:" + touchOffsetX
                );
            }
        }
        invalidate();
        LogUtil.e("--->onDraw :isFirst :" + isFirst);
        isFirst = false;
    }

    static class Position {
        private int startX;
        private int endX;

        public int getStartX() {
            return startX;
        }

        public void setStartX(int startX) {
            this.startX = startX;
        }

        public int getEndX() {
            return endX;
        }

        public void setEndX(int endX) {
            this.endX = endX;
        }

        public Position(int startX, int endX) {
            this.startX = startX;
            this.endX = endX;

        }

        @Override
        public String toString() {
            return "Position{" +
                    "startX=" + startX +
                    ", endX=" + endX +
                    '}';
        }
    }

    public interface CallBackListener {
        void onCallBack(String selector);
    }

    private CallBackListener mCallBackListener;

    public void setOnCallBackListener(CallBackListener listener) {
        if (listener != null) {
            mCallBackListener = listener;
        }
    }
}
