package com.android.app.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.android.helper.utils.ConvertUtil;
import com.android.helper.utils.CustomViewUtil;
import com.android.helper.utils.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author : 流星
 * @CreateDate: 2022/10/17-11:56
 * @Description:
 */
public class ScrollNumberView3 extends View {

    // 默认选中的文字
    private String mDefaultNumberContent = "5";

    /**
     * 用来存储文字的字体大小
     */
    private final HashMap<String, Float> mMapFontSize = new HashMap<>();

    private final String[] mNumbers = new String[]{
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"
    };

    /**
     * 每个文字的间距
     */
    private final float mNumberInterval = ConvertUtil.toDp(165);

    // 中间的阴影矩形
    private final Paint mPaintCenterRect = new Paint();
    private final RectF mCenterRect = new RectF();
    private int mMaxWidth;// 屏幕最大的宽度
    private final int roundRectWidth = (int) ConvertUtil.toDp(125f); // 矩形的宽度
    private final int roundRectHeight = (int) ConvertUtil.toDp(125f); // 矩形的高度
    private final int minuteInterval = (int) ConvertUtil.toDp(26);

    // 阴影线
    private final Paint mPaintCenterRectLine = new Paint();

    private final Paint mPaintNumber = new Paint();// 数字的paint
    private final float mNumberMinSize = 48;// 文字的最小值
    private final float mNumberMaxSize = 100;// 文字的最大值

    private final HashMap<String, Position> mMapNumberPosition = new HashMap<>();// 存储每个文字位置的集合

    public ScrollNumberView3(Context context) {
        super(context);
        initView(context, null);
    }

    public ScrollNumberView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        mPaintCenterRect.setColor(Color.parseColor("#4d000000"));

        mPaintCenterRectLine.setColor(Color.WHITE);

        // 数字
        mPaintNumber.setTextSize(ConvertUtil.toDp(mNumberMaxSize));
        mPaintNumber.setColor(Color.WHITE);
        mPaintNumber.setAntiAlias(true);
        mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);
        mPaintNumber.setTextAlign(Paint.Align.CENTER);

//        drawRoundRect = new RectF();
//        mPaintRectangle = new Paint();
//        mPaintRectangle.setColor(Color.parseColor("#4d000000"));
//
//        mPaintMinute = new Paint();
//        mPaintMinute.setColor(Color.WHITE);
//        mPaintMinute.setTextSize(ConvertUtil.toSp(36));
//        mPaintMinute.setStyle(Paint.Style.FILL);
//        mPaintMinute.setTypeface(Typeface.DEFAULT_BOLD);
//
//        mPaintNumber = new Paint();
//        mPaintNumber.setStyle(Paint.Style.FILL);
//        mPaintNumber.setTextSize(ConvertUtil.toDp(mNumberMinSize));
//        mPaintNumber.setColor(Color.WHITE);
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
//
//        LogUtil.e("left:" + drawRoundRect.toString());
//
//        // 计算出总体的高度后，重新去设置高度
        setMeasuredDimension(mMaxWidth, 500);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 计算出默认文字和其他文字字体的大小
        calculateDefaultSelectorPosition(mDefaultNumberContent);

        // 绘制指定大小的透明背景
        canvas.drawRoundRect(mCenterRect, 20, 20, mPaintCenterRect);

        // 绘制阴影线
        int lineWidth = roundRectWidth / 2;
        int lineHeight = roundRectHeight / 2;
        canvas.drawLine(mCenterRect.left + lineWidth, mCenterRect.top, mCenterRect.left + lineWidth, mCenterRect.bottom, mPaintCenterRectLine);
        canvas.drawLine(mCenterRect.left, mCenterRect.top + lineHeight, mCenterRect.right, mCenterRect.top + lineHeight, mPaintCenterRectLine);

        // 绘制数字
        mPaintNumber.setTextAlign(Paint.Align.CENTER);
        float[] numberSize = CustomViewUtil.getTextSize(mPaintNumber, mDefaultNumberContent);
        float baseLine = CustomViewUtil.getBaseLine(mPaintNumber, mDefaultNumberContent);
        float x = mCenterRect.left + roundRectWidth / 2;
        float y = mCenterRect.top + (roundRectHeight - numberSize[1]) / 2 + baseLine;

//        canvas.drawText(mDefaultNumberContent, x, y, mPaintNumber);

        for (int i = 0; i < mNumbers.length; i++) {
            String number = mNumbers[i];
            Position position = mMapNumberPosition.get(number);
            if (position != null) {
                float x1 = position.x;
                float y1 = position.y;
                Paint paint = position.paint;
                float startX = position.startX;
                float startY = position.startY;

                if (TextUtils.equals(number, mDefaultNumberContent)) {
                    canvas.drawText(number, x1 + mDx, y1, paint);
                } else {
                    canvas.drawText(number, startX + mDx, startY, paint);
                }
            }
        }

//        if (mIntegerHalf <= 0) {
//            mIntegerHalf = mNumberInterval / 2;
//        }
//
//        LogUtil.e("--->onDraw");
//        // 绘制指定大小的透明背景
//        canvas.drawRoundRect(drawRoundRect, 20, 20, mPaintRectangle);
//
//        // 绘制底部固定文字
//        float minuteX = 0;
//        float minuteY = 0;
//        String minute = "分钟";
//        float[] minuteSize = CustomViewUtil.getTextSize(mPaintMinute, minute);
//        if (minuteSize != null) {
//            float minuteWidth = minuteSize[0];
//            minuteX = drawRoundRect.left + ((roundRectWidth - minuteWidth) / 2);
//
//            float minuteHeight = minuteSize[1];
//            minuteY = drawRoundRect.bottom + minuteInterval + minuteHeight;
//        }
//        canvas.drawText(minute, minuteX, minuteY, mPaintMinute);
//
//        // 计算当前默认选中的view以及其他view的字体大小
//        calculateDefaultSelectorPosition(mDefaultNumberContent);
//
//        // 计算出绘制文字宽度的总和
//        calculateNumberSum();
//
//        // 绘制滑动的文字
//        float mNumberStartX = 0;
//        float mNumberStartY = drawRoundRect.bottom;
//        float mNumberEndX = mMaxWidth;
//        float mNumberEndY = drawRoundRect.bottom;
//
//        @SuppressLint("DrawAllocation")
//        LinearGradient linearGradient = new LinearGradient(
//                mNumberStartX,
//                mNumberStartY,
//                mNumberEndX,
//                mNumberEndY,
//                mColors,
//                positions,
//                Shader.TileMode.MIRROR);
//
//        mPaintNumber.setShader(linearGradient);
//
//        /*
//         * 上一个文字的宽度
//         */
//        float previousNumberWidth = 0;
//        // 第一个数据，文字的x轴等于开始文字的X轴位置
//
//        // 文字最左侧的X轴 =  文字的左侧开始位置 + ( 布局的总体宽度 - 所有文字的总和 + 文字间距的总和 ) / 2
//        int x = mNumberLeftValue + ((mMaxWidth - mSum) / 2);
//        int y = 0;
//
//        for (int i = 0; i < mNumbers.length; i++) {
//            String number = mNumbers[i];
//
//            // 获取文字的大小
//            float[] itemSize = mMapSize.get(number);
//
//            float itemWidth = itemSize[0];
//            float itemHeight = itemSize[1];
//
//            // X轴 = 初始的左侧间距 + 上一个文字的宽度
//            x += previousNumberWidth;
//            if (i > 0) {
//                x += mNumberInterval;
//            }
//
//            // 设置粗体
//            if (TextUtils.equals(number, mDefaultNumberContent)) {
//                mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);
//            } else {
//                mPaintNumber.setTypeface(Typeface.DEFAULT);
//            }
//
//            // 设置文字大小
//            mPaintNumber.setTextSize(ConvertUtil.toSp(mMapFontSize.get(number)));
//
//            // 高度 = 矩形的高度 - 文字的高度 - 基准线的高度
//            float baseLine = CustomViewUtil.getBaseLine(mPaintNumber, number);
//            y = (int) ((drawRoundRect.bottom - itemHeight) / 2 + baseLine);
//
//            canvas.drawText(number, x + dx + touchOffsetX, y, mPaintNumber);
//            previousNumberWidth = itemWidth;
//
//            // 存储view当前的位置
//            mMapPosition.put(number, new ScrollNumberView2.Position((x + dx), (int) (x + dx + itemWidth)));
//        }
//
//        mPaintNumber.setStrokeWidth(1);
//        mPaintNumber.setColor(Color.WHITE);
//        canvas.drawLine(centerLines, 0, centerLines + 2, roundRectHeight, mPaintNumber);
//        canvas.drawLine(drawRoundRect.left, drawRoundRect.top + (roundRectHeight / 2), drawRoundRect.right, drawRoundRect.top + (roundRectHeight / 2), mPaintNumber);
//
//        if (isFirst) {
//            // 滑动的时候，动态改变当前选中的角标
//            scrollDefault();
//        }

    }
//
//    /**
//     * 计算出所有文字的位置和，使文字占据中间的位置
//     */
//    private void calculateNumberSum() {
//        mSum = 0;
//        for (int i = 0; i < mNumbers.length; i++) {
//            String number = mNumbers[i];
//
//            // 重新设置文字大大小，避免测量出现异常
//            mPaintNumber.setTextSize(ConvertUtil.toDp(mMapFontSize.get(number)));
//
//            // 计算文字的大小
//            float[] itemSize = CustomViewUtil.getTextSize(mPaintNumber, number);
//            if (mContentMaxHeight < itemSize[1]) {
//                mContentMaxHeight = itemSize[1];
//            }
//
//            mMapSize.put(number, itemSize);
//
//            mSum += itemSize[0];
//            if (i > 0) {
//                mSum += mNumberInterval;
//            }
//        }
//    }

    /**
     * 计算默认选中的内容，并设定其他位置文字的大小
     *
     * @param selectorContent 当前选中的文字
     */
    private void calculateDefaultSelectorPosition(String selectorContent) {
        // 获取当前选中文字的角标
        int index = 0;
        for (int i = 0; i < mNumbers.length; i++) {
            String mNumber = mNumbers[i];
            if (TextUtils.equals(mNumber, selectorContent)) {
                index = i;
                // 存入最大的数据
                mMapFontSize.put(selectorContent, mNumberMaxSize);

                calculateSelectorPosition(mNumber, 0, index);
                break;
            }
        }

        // 左侧的文字，大小逐渐减小
        float valueLeft;
        int temp = 0;

        for (int i = index - 1; i >= 0; i--) {
            String item = mNumbers[i];
            temp++;

            valueLeft = mNumberMaxSize - temp * 6;
            if (valueLeft < mNumberMinSize) {
                valueLeft = mNumberMinSize;
            }

            // 存储字体的大小
            mMapFontSize.put(item, valueLeft);

            // 存储位置的内容
            calculateSelectorPosition(item, 1, i);
        }

        // 右侧的文字，大小逐渐变大
        float valueRight;
        temp = 0;
        for (int i = index + 1; i < mNumbers.length; i++) {
            String item = mNumbers[i];
            temp++;
            valueRight = mNumberMaxSize - temp * 6;

            if (valueRight < mNumberMinSize) {
                valueRight = mNumberMinSize;
            }
            mMapFontSize.put(item, valueRight);

            calculateSelectorPosition(item, 2, i);
        }

    }

    private void calculateSelectorPosition(String selectorContent, int type, int index) {
        // 获取存储的position对象
        Position position = mMapNumberPosition.get(selectorContent);
        if (position == null) {
            position = new Position();
        }

        // 获取指定的paint
        Paint paint = position.getPaint();
        if (paint == null) {
            paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            position.paint = paint;
        }

        // 获取选中文字的大小
        Float size = mMapFontSize.get(selectorContent);

        // 设置文字大小
        if (size != null) {
            paint.setTextSize(size);
        }

        // 获取选中文字的大小
        float[] numberSize = CustomViewUtil.getTextSize(paint, selectorContent);

        // 获取选中文字的基线
        float baseLine = CustomViewUtil.getBaseLine(paint, selectorContent);

        // 存入默认选中数据的position值
        if (TextUtils.equals(selectorContent, mDefaultNumberContent)) {
            position.x = mCenterRect.right - roundRectWidth / 2;
            position.y = mCenterRect.top + (roundRectHeight - numberSize[1]) / 2 + baseLine;
            position.paint.setTextAlign(Paint.Align.CENTER);

            // 计算出中心位置距离矩形的间距
            float mStartX = mCenterRect.left + (roundRectWidth - numberSize[0]) / 2;
            float mEndX = mStartX + numberSize[0];
            position.startX = mStartX;
            position.endX = mEndX;

        } else {
            // 左侧的值
            if (type == 1) {
                if (index >= 0) {
                    // 获取当前值右侧的值
                    String number = mNumbers[index + 1];
                    Position positionRight = mMapNumberPosition.get(number);
                    float startX = 0;
                    if (positionRight != null) {
                        // 上一个view左侧的值
                        startX = positionRight.startX;
                    }

                    // 获取当前的position
                    position.endX = startX - mNumberInterval;
                    // right  = startX + 当前文字的宽度
                    position.startX = position.endX - numberSize[0];

                    // y = 矩形的高度 - 文字的高度 /2
                    position.startY = (roundRectHeight - numberSize[1]) / 2 + baseLine;
                }
                // x轴 = 上一个的值 - 间距 - 文字的宽度

            } else if (type == 2) {
                // 获取左侧的值
                float endX = 0;
                String number = mNumbers[index - 1];
                Position positionLeft = mMapNumberPosition.get(number);
                if (positionLeft != null) {
                    endX = positionLeft.endX;
                }

                position.startX = endX + mNumberInterval;
                position.endX = position.startX + numberSize[0];
                position.startY = (roundRectHeight - numberSize[1]) / 2 + baseLine;
            }
            paint.setTextAlign(Paint.Align.LEFT);
        }

        // 存储位置的信息
        mMapNumberPosition.put(selectorContent, position);
    }

    boolean isTouch = true;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
        }
        return super.dispatchTouchEvent(event);
    }

    float mStartX;
    float mEndX;
    int mDx = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                return isTouch;

            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                mDx += (int) (endX - mStartX);
                calculateSelectorPosition();

                invalidate();

                mStartX = endX;
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onTouchEvent(event);
    }

    //    /**
//     * 偏移的x轴的数据
//     */
//    private int dx;
//    private int startX;
//    private boolean isFirst = true;
//
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startX = (int) event.getX();
//                return true;
//
//            case MotionEvent.ACTION_MOVE:
//            case MotionEvent.ACTION_HOVER_MOVE:
//                LogUtil.e("----ACTION_MOVE");
//                float endX = event.getX();
//                int v = (int) (endX - startX);
//
//                // 滑动的时候，动态改变当前选中的角标
//                calculateSelectorPosition();
//
//                // 计算边界的位置，禁止滑动
//                String left = mNumbers[0];
//                ScrollNumberView2.Position LeftPosition = mMapPosition.get(left);
//                if (LeftPosition != null) {
//                    float startX1 = LeftPosition.getStartX();
//                    float[] leftArray = mMapSize.get(left);
//                    if (leftArray != null) {
//                        // 开始位置 + 偏移值 > = 中线 -文字的一半
//                        if (startX1 + v >= centerLines - (leftArray[0]) / 2) {
//                            LogUtil.e("到了最左侧了，停止后续的动作！");
//                            return true;
//                        }
//                    }
//                }
//
//                String right = mNumbers[mNumbers.length - 1];
//                ScrollNumberView2.Position rightPosition = mMapPosition.get(right);
//                if (rightPosition != null) {
//                    float startX = rightPosition.getStartX();
//                    float[] rightArray = mMapSize.get(right);
//                    if (rightArray != null) {
//                        if (startX + v <= centerLines - rightArray[0] / 2) {
//                            LogUtil.e("到了最迟右侧的距离了！");
//                            return true;
//                        }
//                    }
//                }
//
//                dx += v;
//                invalidate();
//                startX = (int) endX;
//                LogUtil.e("----->继续执行！");
//
//                break;
//
//            case MotionEvent.ACTION_UP:
//                LogUtil.e("----ACTION_UP");
//
//                scrollDefault();
//
//                ToastUtil.show("当前选中的值是：" + mDefaultNumberContent);
//                if (mCallBackListener != null) {
//                    mCallBackListener.onCallBack(mDefaultNumberContent);
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }

    /**
     * 计算出当前选中的是哪个内容
     */
    private void calculateSelectorPosition() {
        Set<Map.Entry<String, Position>> entries = mMapNumberPosition.entrySet();
        for (Map.Entry<String, Position> entry : entries) {
            String key = entry.getKey();
            Position position = entry.getValue();
            float startX = position.getStartX();
            float endX = position.getEndX();

            // 判定哪个view在中间矩形的位置
            // 逻辑：
            /*
             *逻辑：
             *  1：大于文字左侧的开始位置 + 间距的一半
             *  2：小于文字右侧的结束位置 + 间距的一半
             */

            LogUtil.e("mCenterRect.left:" + mCenterRect.left + "   mCenterRect.right:" + mCenterRect.right);
            LogUtil.e("XXX:" + startX + mDx + "  XXXX:" + endX + mDx);
            if (startX + mDx - mNumberInterval / 2 > (mCenterRect.left) && (endX + mDx + mNumberInterval / 2) < mCenterRect.right) {
                LogUtil.e("当前选中的是：" + key);
//                mDefaultNumberContent = key;
                return;
            }

//
//            if (startX >= drawRoundRect.left - mIntegerHalf && endX <= drawRoundRect.right + mIntegerHalf) {
//                LogUtil.e("当前的选中为：" + key);
//                mDefaultNumberContent = key;
//                return;
//            }
        }
    }
//
//    /**
//     * 滑动到指定的位置
//     */
//    private void scrollDefault() {
//        // 让当前文字滑动到最中间的位置
//        ScrollNumberView2.Position position = mMapPosition.get(mDefaultNumberContent);
//        if (position != null) {
//            // 存储文字的开始值
//            int startX = position.getStartX();
//
//            // 计算文字应该放到哪里
//            // 文字的宽度
//            float[] floats = mMapSize.get(mDefaultNumberContent);
//            if (floats != null) {
//                float width = floats[0];
//
//                // 真正该摆放的位置
//                float positionX = centerLines - width / 2;
//                // 摆放的位置 - 开始的位置 = 抬起偏移的位置
//                touchOffsetX = positionX - startX;
//                LogUtil.e(" 文字的初始值是：" + startX + "\r\n"
//                                + " 中线：" + centerLines + "\r\n"
//                                + " 文字宽度:" + width + "\r\n"
////                        + " 矩形 - 文字宽度的一半：" + offsetX + "\r\n"
//                                + " 文字正确的位置：" + positionX + "\r\n"
//                                + " mv3:" + touchOffsetX
//                );
//            }
//        }
//        invalidate();
//        LogUtil.e("--->onDraw :isFirst :" + isFirst);
//        isFirst = false;
//    }

    static class Position {
        // 给中心view使用的x、y轴位置
        private float x = 0;
        private float y = 0;
        private Paint paint; // 绘制文字的画笔
        private float startX = 0; // 开始绘制的X轴位置
        private float endX = 0;  // 结束绘制的X轴位置
        private float startY = 0; // 开始绘制的Y轴位置

        public Position() {
        }

        public float getStartY() {
            return startY;
        }

        public void setStartY(float startY) {
            this.startY = startY;
        }

        public float getStartX() {
            return startX;
        }

        public void setStartX(float startX) {
            this.startX = startX;
        }

        public float getEndX() {
            return endX;
        }

        public void setEndX(float endX) {
            this.endX = endX;
        }

        public Paint getPaint() {
            return paint;
        }

        public void setPaint(Paint paint) {
            this.paint = paint;
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

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public interface CallBackListener {
        void onCallBack(String selector);
    }

    private ScrollNumberView3.CallBackListener mCallBackListener;

    public void setOnCallBackListener(ScrollNumberView3.CallBackListener listener) {
        if (listener != null) {
            mCallBackListener = listener;
        }
    }
}
