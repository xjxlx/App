package com.android.app.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.android.helper.utils.LogUtil;

/**
 * 绘制一个运动的圆圈
 */
public class CircleView extends View {

    private float mStrokeWidth = 0;

    private final float mStartAngle = 270f; // 开始的角度
    private float mChangeAngle = 0f; // 动态改变的角度
    private final RectF mRectF;
    private final Paint mPaint;
    private StatusChangeListener mStatusChangeListener;
    private int mMeasuredWidth;
    private int mMeasuredHeight;
    private boolean isStrokeWidth; // 是否设置了宽度

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRectF = new RectF();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mMeasuredWidth = getMeasuredWidth();
        mMeasuredHeight = getMeasuredHeight();

        if (mMeasuredWidth != mMeasuredHeight) {
            int min = Math.min(mMeasuredWidth, mMeasuredHeight);
            mMeasuredWidth = min;
            mMeasuredHeight = min;
        }

        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // oval :指定圆弧的外轮廓矩形区域。
        // startAngle: 圆弧起始角度，单位为度。
        // sweepAngle: 圆弧扫过的角度，顺时针方向，单位为度,从右中间开始为零度。
        // useCenter:为True时，在绘制圆弧时将圆心包括在内，通常用来绘制扇形。
        // paint: 绘制圆弧的画板属性

        if (!isStrokeWidth) {
            float strokeWidth = mPaint.getStrokeWidth();
            if (strokeWidth <= 0) {
                mPaint.setStrokeWidth(mStrokeWidth);

                mRectF.left = mStrokeWidth;
                mRectF.top = mStrokeWidth;
                mRectF.right = mMeasuredWidth - mStrokeWidth;
                mRectF.bottom = mMeasuredHeight - mStrokeWidth;

                isStrokeWidth = true;
            }
        }

        LogUtil.e("strokeWidth:" + mStrokeWidth);
        canvas.drawArc(mRectF, mStartAngle, mChangeAngle, false, mPaint);
    }

    /**
     * 开始绘制圆环
     *
     * @param duration 绘制圆环的时间
     */
    public void start(int duration, int strokeWidth) {
        mStrokeWidth = strokeWidth;
        animate(duration);
    }

    private void animate(int time) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setRepeatCount(0);
        animator.setDuration(time);
        animator.addUpdateListener(animation -> {
            mChangeAngle = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                LogUtil.e("onAnimationStart");
                if (mStatusChangeListener != null) {
                    mStatusChangeListener.onStart();
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.e("onAnimationEnd");
                if (mStatusChangeListener != null) {
                    mStatusChangeListener.onEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public void setStatusChangeListener(StatusChangeListener statusChangeListener) {
        mStatusChangeListener = statusChangeListener;
    }

    public interface StatusChangeListener {
        void onStart();

        void onEnd();
    }
}
