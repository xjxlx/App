package com.android.app.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.android.common.utils.LogUtil
import com.android.common.utils.ResourcesUtil
import com.android.helper.utils.CustomViewUtil

class ElevateChartView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private val mPaintContent: Paint by lazy {
        return@lazy Paint().apply {
            textSize = ResourcesUtil.getDimension(context, com.android.helper.R.dimen.sp_16)
            style = Paint.Style.FILL
            color = Color.WHITE
        }
    }
    private val mPaddingTop: Float = ResourcesUtil.getDimension(context, com.android.helper.R.dimen.dp_10)
    private val mPaddingBottom: Float = ResourcesUtil.getDimension(context, com.android.helper.R.dimen.dp_10)
    private val mRectangleWidth: Float = ResourcesUtil.getDimension(context, com.android.helper.R.dimen.dp_10)
    private val mContentArray by lazy {
        return@lazy arrayOf("情绪", "气息", "呼吸", "调息", "心肺")
    }
    private val mEveryInterval: Float by lazy {
        var mTotalWidth = 0F
        mContentArray.forEach {
            mTotalWidth += getTextWidth(it)
        }
        return@lazy (mWidth - mTotalWidth) / mContentArray.size + 1
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        LogUtil.e("View", "width: $mWidth height: $mHeight mEveryInterval:$mEveryInterval")
        mContentArray.forEachIndexed { index, content ->
            val textWidth = CustomViewUtil.getTextWidth(mPaintContent, content)
//            canvas?.drawText(content,)
        }
    }

    private fun getTextWidth(content: String): Float {
        return CustomViewUtil.getTextWidth(mPaintContent, content)
    }
}
