package com.android.app.widget

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.android.apphelper2.utils.LogUtil

class ElevateChartView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        LogUtil.e("View", "width: $mWidth height: $mHeight")
    }
}
