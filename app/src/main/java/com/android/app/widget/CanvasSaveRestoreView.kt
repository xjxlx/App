package com.android.app.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.android.app.R
import com.android.common.utils.ResourcesUtil

class CanvasSaveRestoreView(private val context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val mPath: Path by lazy {
        return@lazy Path()
    }
    private val mPaint: Paint by lazy {
        return@lazy Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.RED
            strokeWidth = ResourcesUtil.getDimension(context, com.android.helper.R.dimen.dp_2)
        }
    }
    private val mBitmap: Bitmap? by lazy {
        return@lazy ResourcesUtil.getBitmap(context, R.drawable.icon_head)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Path
        // 1: 使用Path
        // 1.1 -> 先创建Path
        // 1.2 -> path添加路径
        // 1.3 -> Canvas.drawPath(path ,paint)

        canvas?.let {
            // save 之后，就可以进行平移、缩放、旋转、剪切等操作了
            // it.save()

            if (mBitmap != null) {
                val width = mBitmap!!.width
                val height = mBitmap!!.height

                it.save()
                // 添加圆形路径
                mPath.addCircle((width / 2).toFloat(), (height / 2).toFloat(), width.toFloat() / 2, Path.Direction.CW)

                // 将当前剪切与指定的路径相交,可以理解是在裁剪画布
                it.clipPath(mPath)

                // 绘制图片
                it.drawBitmap(mBitmap!!, 0f, 0f, mPaint)
                it.restore()

                it.save()
                // 位移画布
                it.translate(300f, 500f)

                it.drawBitmap(mBitmap!!, 0f, 0f, mPaint)
                it.restore()
            }
        }
    }
}