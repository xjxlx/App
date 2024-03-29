package com.android.app.widget.hm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.android.app.R;
import com.android.common.base.BaseView;
import com.android.common.utils.LogUtil;
import com.android.common.utils.RecycleUtil;
import com.android.common.utils.ResourcesUtil;
import com.android.helper.utils.ConvertUtil;
import com.android.helper.utils.BitmapUtil;
import com.android.helper.utils.CustomViewUtil;

/**
 * 检索通讯录名字的列表数据
 * 目标：
 * 写一个列表 从 A - z ,最上方在设置一个红心
 * <p>
 * 实现逻辑：
 * 1：获取红心的bitmap,并绘制
 * 2：设置总的高度，高度等于：红心的高度 + 每个固定大小的高度 * 集合的长度
 *
 * <p>
 * 1:顶部位置绘制一个红心，作为收藏的标记
 */
public class SelectorNameList extends BaseView {

    private final String[] mIndexArr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    private final float mTextTotalHeight = 30;// 每个字的高度
    private final float mPaddingLeft = 20;
    private final float mPaddingRight = 20;
    private final float mInterval = 20;
    private final float mBitmapTargetWidth = ConvertUtil.toDp(20);// bitmap的目标宽度
    private Paint mPaint;
    private float mTotalHeight;
    private float mTotalWidth;
    private Bitmap mBitmap;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private int mBitmapLeft;
    private float mTextCenter;

    public SelectorNameList(Context context) {
        super(context);
        initView(context, null);
    }

    public SelectorNameList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    @Override
    public void initView(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(60);
        mPaint.setTextAlign(Paint.Align.CENTER);
        // 获取bitmap
        Bitmap bitmap = ResourcesUtil.getBitmap(getContext(), R.mipmap.icon_rad_xin);
        // 生成一个新的bitmap
        mBitmap = BitmapUtil.getBitmapScaleForWidth(bitmap, mBitmapTargetWidth);
        // 计算出bitmap的高度
        int bitmapHeight = mBitmap.getHeight();
        // 获取所有的高度 = bitmap的高度 + 顶部间距的高度 + （ 固定rect的高度 + 间距） * 集合的长度 + 底部的间距
        mTotalHeight += (mInterval + bitmapHeight + ((mTextTotalHeight + mInterval) * mIndexArr.length));
        float tempWidth = 0;
        // 统计出所有字的高度
        for (String value : mIndexArr) {
            // 获取字的宽高
            float width = CustomViewUtil.getTextViewWidth(mPaint, value);
            // 对比出最大的宽度
            mTotalWidth = Math.max(tempWidth, width);
            // 变量的赋值
            tempWidth = width;
        }
        // 计算所有的宽度
        mTotalWidth += (mBitmap.getWidth() + mPaddingLeft + mPaddingRight);
        LogUtil.e("view的总高度为：" + mTotalHeight + "   view的宽度：" + mTotalWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 重新设置宽高
        setMeasuredDimension((int) mTotalWidth, (int) mTotalHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();
        // 获取bitmap的left值
        mBitmapLeft = (mMeasuredWidth - mBitmap.getWidth()) / 2;
        // 获取文字的间距
        if (mMeasuredWidth > 0) {
            mTextCenter = mMeasuredWidth / 2;
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float height = 0;
        height += mInterval;
        // 绘制一个红心
        canvas.drawBitmap(mBitmap, mBitmapLeft, height, mPaint);
        height += mBitmap.getHeight() + mInterval;
        // 绘制列表
        for (int i = 0; i < mIndexArr.length; i++) {
            String value = mIndexArr[i];
            height += (mTextTotalHeight + mInterval) * i;
            RectF rect = new RectF(0, height, mMeasuredWidth, height + mTextTotalHeight);
            Paint.FontMetrics fm = mPaint.getFontMetrics();
            float v = rect.centerY() + (fm.descent - fm.ascent) / 2 - fm.descent;
            canvas.drawText(value, mTextCenter, v, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float position = event.getY();
                if ((position >= 0) && (position <= mMeasuredHeight)) {
                    LogUtil.e("position:" + position);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;

        }
        return true;
    }
}
