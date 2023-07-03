package com.android.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.android.app.R
import com.android.apphelper2.utils.ResourcesUtil
import com.android.helper.utils.CustomViewUtil
import kotlin.random.Random

class PostChartView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val mPercentageArray: Array<String> by lazy {
        return@lazy arrayOf("100%", "75%", "50%", "25%", "0%")
    }
    private val mPaintPercentage: Paint by lazy {
        return@lazy Paint().apply {
            color = Color.parseColor("#60ffffff")
            strokeWidth = ResourcesUtil.getDimension(context, R.dimen.dp_1_5)
            textSize = ResourcesUtil.getDimension(context, R.dimen.sp_16)
        }
    }
    private val mPercentageEveryInterval = ResourcesUtil.getDimension(context, R.dimen.dp_15)
    private val mPercentageLeftInterval = ResourcesUtil.getDimension(context, R.dimen.dp_10)
    private val mPercentageTopInterval = ResourcesUtil.getDimension(context, R.dimen.dp_10)
    private val mLineVerticalLeftInterval = ResourcesUtil.getDimension(context, R.dimen.dp_10)
    private val mLineLandscapeMaxWidth by lazy {
        return@lazy width.toFloat()
    }

    private val mPillarArray: Array<String> by lazy {
        return@lazy arrayOf("情绪", "气息", "呼吸", "调息", "心肺")
    }
    private val mPaintRectangle: Paint by lazy {
        return@lazy Paint().apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#ffffff")
            strokeWidth = ResourcesUtil.getDimension(context, R.dimen.dp_1_5)
            textSize = ResourcesUtil.getDimension(context, R.dimen.sp_16)
        }
    }
    private val mRectangleEveryInterval: Float = ResourcesUtil.getDimension(context, R.dimen.dp_25)
    private var mMeasureHeight: Int = 500

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(measuredWidth, mMeasureHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 百分比文字的累计高度
        var percentageTotalHeight = 0F
        // 百分比文字，最大的宽度
        var percentMaxWidth = 0F

        // 绘制左侧的百分比
        mPercentageArray.forEachIndexed { index, content ->
            val textHeight = getTextHeight(content)
            val textWidth = getTextWidth(content)
            if (percentMaxWidth < textWidth) {
                percentMaxWidth = textWidth
            }

            if (index > 0) {
                percentageTotalHeight += mPercentageEveryInterval
            }
            percentageTotalHeight += textHeight
            canvas?.drawText(content, mPercentageLeftInterval, percentageTotalHeight + mPercentageTopInterval, mPaintPercentage)
        }

        // 绘制竖向的线
        val verticalX = mPercentageLeftInterval + percentMaxWidth + mLineVerticalLeftInterval
        val verticalLineStartY = mPercentageTopInterval
        val verticalLineEndY = percentageTotalHeight + mPercentageTopInterval
        canvas?.drawLine(verticalX, verticalLineStartY, verticalX, verticalLineEndY, mPaintPercentage)

        // 绘制横向的线
        val landscapeLineY = mPercentageTopInterval + percentageTotalHeight
        val landscapeLineStartX = mPercentageLeftInterval + percentMaxWidth + mLineVerticalLeftInterval
        val landscapeLineEndX = mLineLandscapeMaxWidth
        canvas?.drawLine(landscapeLineStartX, landscapeLineY, landscapeLineEndX, landscapeLineY, mPaintPercentage)

        // 绘制文字
        var contentMaxWidth = landscapeLineStartX
        mPillarArray.forEachIndexed { index, content ->
            // measure content width
            val textWidth = getTextWidth(content)
            // measure content baseline
            val baseLine = CustomViewUtil.getBaseLine(mPaintRectangle, content)
            // the adding up only when index is greater than zero
            if (index > 0) {
                contentMaxWidth += textWidth
            }
            contentMaxWidth += mRectangleEveryInterval
            val contentStartX = contentMaxWidth
            val contentStartY = landscapeLineY + baseLine + 20
            canvas?.drawText(content, contentStartX, contentStartY, mPaintRectangle)

            val nextInt = Random.nextInt(percentageTotalHeight.toInt() / 5, percentageTotalHeight.toInt())
            // draw Pillar
            val rect = RectF(contentStartX, nextInt.toFloat(), contentStartX + textWidth, percentageTotalHeight)
            canvas?.drawRect(rect, mPaintRectangle)
        }
    }

    private fun getTextHeight(content: String): Float {
        return CustomViewUtil.getTextHeight(mPaintPercentage, content)
    }

    private fun getTextWidth(content: String): Float {
        return CustomViewUtil.getTextWidth(mPaintPercentage, content)
    }
}