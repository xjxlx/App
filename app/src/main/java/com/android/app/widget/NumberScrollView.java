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
 * @CreateDate: 2022/8/24-09:30
 * @Description:
 */
public class NumberScrollView extends View {

    // 中心区域
    private final Paint mPaintCenter = new Paint();
    private final RectF drawRoundRect = new RectF();
    // 中心区域的直径
    private final int mDiameter = 150;

    // 数字的内容
    private final String[] numbers = {"4", "5", "6", "7", "8", "9", "10"};
    private final Paint mPaintNumber = new Paint();
    /**
     * 屏幕的总宽度
     */
    private int mMeasuredWidth;
    /**
     * 屏幕的总高度
     */
    private int mMeasuredHeight;
    /**
     * 文字距离左侧的padding默认值
     */
    private int mNumberLeft = 80;// 默认数字的左侧位置
    /**
     * 文字距离顶部的值
     */
    private int mNumberTop = 0; // 默认数字的top值
    private final HashMap<String, float[]> mSizeMap = new HashMap<>();
    private int interval;// 文字中间的间距
    private int mDx;// 滑动的距离
    private final HashMap<String, Integer> mLocationMap = new HashMap<String, Integer>();
    private final HashMap<String, Integer> mLocationOriginalMap = new HashMap<String, Integer>();
    private int startX = 0;

    // 字体颜色
    private final int[] colors = new int[]{Color.TRANSPARENT, Color.WHITE, Color.TRANSPARENT};
    private final HashMap<String, Integer> mFontSizeMap = new HashMap<String, Integer>();
    private final HashMap<String, Integer> mAlpha = new HashMap<>();
    private final int mMaxSize = 40;

    private final Paint mPaintMinute = new Paint();
    private final String minute = "分钟";
    private float mFzX;
    private final Paint mPaintBg = new Paint();
    private int mSelectorIndex;
    private String mSelectorNumber = "";
    private int mCenterPosition = 0;// 中心位置
    private int mIntervalCenter = 0;// 间距的一半
    private int mIntervalWidth;// 宽度的一半
    private int ssssss = 0;

    public NumberScrollView(Context context) {
        super(context);
        initView(context, null);
    }

    public NumberScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        mPaintCenter.setAntiAlias(true);
        mPaintCenter.setColor(Color.LTGRAY);
        mPaintCenter.setStyle(Paint.Style.FILL);

        mPaintNumber.setAntiAlias(true);
        mPaintNumber.setColor(Color.WHITE);
        mPaintNumber.setStyle(Paint.Style.FILL);
        mPaintNumber.setTypeface(Typeface.DEFAULT_BOLD);


        mPaintMinute.setColor(Color.WHITE);
        mPaintMinute.setTextSize(ConvertUtil.toDp(20));
        mPaintMinute.setStyle(Paint.Style.FILL);
        mPaintMinute.setTypeface(Typeface.DEFAULT_BOLD);

        mPaintBg.setAntiAlias(true);
        mPaintBg.setColor(Color.GRAY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        mMeasuredHeight = MeasureSpec.getSize(heightMeasureSpec);
        mIntervalWidth = mMeasuredWidth / 2;
        mCenterPosition = mMeasuredWidth / 2;


        drawRoundRect.left = (mMeasuredWidth - mDiameter) / 2f;
        drawRoundRect.top = (mMeasuredHeight - mDiameter) / 2f;
        drawRoundRect.right = drawRoundRect.left + mDiameter;
        drawRoundRect.bottom = drawRoundRect.top + mDiameter;

        // 获取数字Y轴的top值
        int widths = 0;// 所有文字的总宽度
        for (int i = 0; i < numbers.length; i++) {
            String number = numbers[i];
            float[] textSize = CustomViewUtil.getTextSize(mPaintNumber, number);
            mSizeMap.put(number, textSize);

            float value = textSize[1];

            // 累计总的宽度
            widths += textSize[0];

            // 文字的高度  = 屏幕高度 - 文字高度 / 2 + baseline 高度
            mPaintNumber.setTextSize(ConvertUtil.toDp(18));
            float baseLine = CustomViewUtil.getBaseLine(mPaintNumber, number);
            int top = (int) ((mMeasuredHeight - value) / 2 + baseLine);

            if (mNumberTop < top) {
                mNumberTop = top;
            }
        }

        // 间距 = （总宽度 - 默认值X2 - 所有文字的宽度 ）/ 数组的长度
        interval = (mMeasuredWidth - widths - mNumberLeft * 2) / (numbers.length - 1);
        LogUtil.e("Interval: " + interval + " mMeasuredWidth " + mMeasuredWidth);
        mIntervalCenter = interval / 2;

        // 分钟的左侧间距
        float v = CustomViewUtil.getTextSize(mPaintMinute, minute)[0];
        mFzX = (mMeasuredWidth - v) / 2;
    }


    /**
     * 1:在中心区域绘制出来透明的图标
     * 2:从左侧开始向右侧绘制数字 4 -10
     */
    int previousX = mNumberLeft;// 默认等于左侧的间距
    private final RectF rectBg = new RectF(0, drawRoundRect.top, mMeasuredWidth, 0);

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtil.e("onDraw");
        float v1 = drawRoundRect.top + (drawRoundRect.bottom - drawRoundRect.top);
        float v2 = CustomViewUtil.getTextSize(mPaintMinute, minute)[1];
        float baseLine = CustomViewUtil.getBaseLine(mPaintMinute, minute);

        rectBg.left = 0;
        rectBg.top = drawRoundRect.top;
        rectBg.right = mMeasuredWidth;
        rectBg.bottom = v1 + v2 + 100 - baseLine;
        canvas.drawRect(rectBg, mPaintBg);

        // 绘制中心区域的透明色块
        canvas.drawRoundRect(drawRoundRect, 10, 10, mPaintCenter);

        // 绘制数字
        if (mLocationMap.size() < mSizeMap.size()) {
            for (int i = 0; i < numbers.length; i++) {
                String number = numbers[i];
                // 当前文字的宽度
                if (i > 0) {
                    // 上一个文字的宽度 + 间距 +滑动的距离
                    previousX += (interval) + mSizeMap.get(numbers[i - 1])[0];
                }
                mLocationMap.put(number, previousX);
                mLocationOriginalMap.put(number, previousX);
            }
        }
        LogUtil.e("mLocationMap: --->" + mLocationMap.toString());
        LogUtil.e("mLocationOriginalMap: ---> " + mLocationOriginalMap.toString());

        // 渐变
        float startX = 0;
        float startY = drawRoundRect.bottom;
        float endX = mMeasuredWidth;
        float endY = drawRoundRect.bottom;

        @SuppressLint("DrawAllocation")
        LinearGradient linearGradient = new LinearGradient(startX, startY, endX, endY, colors,
                null,
                Shader.TileMode.MIRROR);
        mPaintNumber.setShader(linearGradient);

        if (mFontSizeMap.size() <= 0) {
            initSelector();
        }

        for (String number : numbers) {
            // 当前文字的位置
            int integer = mLocationMap.get(number);
            // 从新设置字体的大小
            mPaintNumber.setTextSize(mFontSizeMap.get(number));
            Integer integer1 = mAlpha.get(number);
            LogUtil.e("alpha:  key:" + number + " value:" + integer1);
            mPaintNumber.setAlpha(integer1);
            // 绘制文字
            canvas.drawText(number, integer + ssssss, mNumberTop, mPaintNumber);
            // 重新存入当前的值 + dx的偏移值
            mLocationMap.put(number, integer + mDx);
        }

        canvas.drawText(minute, mFzX, drawRoundRect.bottom + 100, mPaintMinute);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                ssssss = 0;

                return true;

            case MotionEvent.ACTION_MOVE:
                float endX = event.getX();
                int xx = (int) (endX - startX);

                // 左侧的位置
                int left = mLocationMap.get(numbers[0]);
                // 右侧位置
                int right = mLocationMap.get(numbers[numbers.length - 1]);

                // 左侧滑动限制
                if (left + xx > drawRoundRect.left + (mDiameter / 2)) {
                    LogUtil.e("左侧停止滑动！");
                    return true;
                } else if (right + xx < drawRoundRect.left) {
                    LogUtil.e("右侧停止滑动！");
                    return true;
                } else {
                    mDx = (int) xx;
                    initSelector();

                    LogUtil.e("dx: " + mDx + " 开始移动！");
                    invalidate();
                    startX = (int) endX;
                }
                break;
            case MotionEvent.ACTION_UP:
                LogUtil.e("当前选中的是: " + mSelectorNumber);
                ToastUtil.show("当前选中的是：" + mSelectorNumber);
                // 当前position的位置
                Integer currentPosition = mLocationMap.get(mSelectorNumber);
                ssssss = (int) (mIntervalWidth - currentPosition - mSizeMap.get(mSelectorNumber)[0]);

                LogUtil.e(" 中心：" + mIntervalWidth + " \r\n"
                        + " current:" + currentPosition + "\r\n"
                        + " 文字宽度：" + mSizeMap.get(mSelectorNumber)[0] + "\r\n"
                        + " 差值：" + (mIntervalWidth - currentPosition - mSizeMap.get(mSelectorNumber)[0]) + "\r\n"
                        + " 间距一半：" + mIntervalCenter
                );
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }


    private void initSelector() {
        // 当前选中的字体最大，剩余的逐渐减小
        Set<Map.Entry<String, Integer>> entries = mLocationMap.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            String key = entry.getKey();
            int value = entry.getValue();
            /*
             * 1：文字左侧的位置：> 文字的位置 - 间距的一半
             * 2：文字右侧的位置：< 文字的位置 + 文字的宽度 + 间距的一半
             */
            // 当前的位置
            float width = mSizeMap.get(key)[0];
            /**
             * 1:左侧距离： x > 文字的原始左侧 + 间距的一半
             * 2: 右侧距离：x < 文字的原始位置+ 文字宽度 + 间距的一半
             */

//            LogUtil.e(" 中心位置是：" + mIntervalWidth + "\r\n"
//                    + " key:" + key + "\r\n"
//                    + " value:" + value + "\r\n"
//                    + " 文字的宽度 ：" + width + "\r\n"
//                    + " 左侧的边界：" + (value - mIntervalCenter) + "\r\n"
//                    + " 右侧的边界：" + (value + width + mIntervalCenter) + "\r\n"
//            );

            // 左侧：中心线 > 当前
            if ((mIntervalWidth > value - mIntervalCenter) && (mIntervalWidth < (value + mIntervalCenter + width * 2))) {
                LogUtil.e("挡圈选中的是：" + key);
                mSelectorNumber = key;
                mFontSizeMap.put(key, (int) ConvertUtil.toDp(mMaxSize));
                break;
            }
        }


        for (int i = 0; i < numbers.length; i++) {
            String number = numbers[i];
            if (TextUtils.equals(number, mSelectorNumber)) {
                mFontSizeMap.put(number, mMaxSize);
                mSelectorIndex = i;
                break;
            }
        }

        // 倒叙
        int b = 1;
        for (int j = mSelectorIndex; j >= 0; j--) {
            b++;
            mFontSizeMap.put(numbers[j], (int) ConvertUtil.toDp(mMaxSize - (b * 2)));
            mAlpha.put(numbers[j], 255);
        }

        // 正序
        int a = 1;
        for (int k = mSelectorIndex; k < numbers.length; k++) {
            a++;
            mFontSizeMap.put(numbers[k], (int) ConvertUtil.toDp(mMaxSize - (a * 2)));
            mAlpha.put(numbers[k], 255);
        }
        LogUtil.e(" --->" + mFontSizeMap.toString());

        mAlpha.put(mSelectorNumber, (int) (255 * 0.4));
    }

}