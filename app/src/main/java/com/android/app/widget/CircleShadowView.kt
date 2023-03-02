package com.android.app.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.android.helper.utils.ConvertUtil
import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2023/2/24-17:56
 * @Description:
 */
class CircleShadowView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    private var cx = 1f
    private var cy = 1f
    private var mRadius = 300f
    private var interval = 1.5F

    private val mPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = ConvertUtil.toPx(10F)
        maskFilter = BlurMaskFilter(50f, BlurMaskFilter.Blur.SOLID)
        style = Paint.Style.STROKE
    }

    val animation = ValueAnimator().apply {
        duration = 3000L
        setFloatValues(0F, 1F)
        addUpdateListener {
            val fraction = it.animatedFraction
            if (fraction > 0) {
                mRadius += (fraction + interval)
                invalidate()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        cx = measuredWidth.toFloat() / 2
        cy = measuredHeight.toFloat() / 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        LogUtil.e("onDraw --->  cx: $cx  cy: $cy   radius: $mRadius")
        canvas?.drawCircle(cx, cy, mRadius, mPaint)
    }

    fun startAnimation() {
        animation.start()
    }

}