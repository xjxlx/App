package com.android.app.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/10/20-10:59
 * @Description:
 */
class BreatheView2 : View {

    private val TAG = "Touch"
    private val mRectStrokeLine = RectF() // 边线
    private val mPaintStrokeLine = Paint() // 绘制边线

    private val mWidth = 600 // 绘制区域的宽度
    private val mHeight = 600 // 绘制区域的高度
    private var mFlagType = 1 // 1: 循环播放从小到大的圆圈，2：一个从小到大的实心圆圈，3：一个从大到小的实心圆圈
    private var intervalWidth = 10 // 每隔view间隔的宽度

    private val whatGradient = 1 // 渐变的what
    private val whatLoopSmallToBigAdd = 2 // 从小到大loop的渐变what
    private val whatSmallToBig = 3 // 从小到大的what
    private val whatBigToSmall = 4 // 从大到小的what
    private val whatLoopBigToSmallAdd = 5 // 从大到小loop的渐变what

    // 从小到大的扩散集合
    private val mListLoopSmallToBig = arrayListOf<Circle>()

    // 从小到大的实心圆圈
    private val mListSmallToBig = arrayListOf<Circle>()

    // 从大到小的实心圆圈
    private val mListBigToSmall = arrayListOf<Circle>()

    private var mCx: Float = 0F // 初始的X轴
    private var mCY: Float = 0F // 初始的Y轴
    private var mMaxRadius = 0f // 最大的扩散宽度
    private var mAlphasZoom = 0f // 透明度的比例

    private var isStartOne = false // 是否已经开始了第一个，避免重复性执行
    private var isStartSecond = false // 是否已经开始了第二个，避免重复性执行
    private var isStartThree = false // 是否已经开始了第三个，避免重复性执行

    private var isStopAll = false //  是否停止整体的播放逻辑
    private var isStopOne = false// 第一个是否已经结束了

    private var isPrepareTwo = false // 第二个是否已经准备好了
    private var isStopLoopThree = false // 停止第三个循环
    private val mColorValue = 150
    private val mColorMaxValue = 255

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    private fun initView(context: Context) {
        LogUtil.e("initView!")
        // 绘制边线
        mPaintStrokeLine.color = Color.BLACK
        mPaintStrokeLine.isAntiAlias = true
        mPaintStrokeLine.strokeWidth = 2f
        mPaintStrokeLine.style = Paint.Style.STROKE

        // 边线
        mRectStrokeLine.left = 0f
        mRectStrokeLine.top = 0f
        mRectStrokeLine.right = mRectStrokeLine.left + mWidth - mPaintStrokeLine.strokeWidth
        mRectStrokeLine.bottom = mRectStrokeLine.top + mHeight - mPaintStrokeLine.strokeWidth
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        setMeasuredDimension(mWidth, mHeight)

        // 中心X轴
        mCx = (measuredWidth / 2).toFloat()
        // 中心Y轴
        mCY = (measuredHeight / 2).toFloat()
        // 圆心最大的直径宽度
        mMaxRadius = (measuredWidth / 2).toFloat()
        // 每一份占据的透明度
        mAlphasZoom = (mColorValue / mMaxRadius)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { it ->
            // 绘制边线
            it.drawRect(mRectStrokeLine, mPaintStrokeLine)

            // 绘制透明圆圈
            when (mFlagType) {
                1 -> {
                    drawCircle(it, mListLoopSmallToBig)
                }

                2 -> {
                    drawCircle(it, mListSmallToBig)
                }

                3 -> {
                    drawCircle(it, mListBigToSmall)
                }
            }
        }
    }

    private fun drawCircle(canvas: Canvas?, list: List<Circle>) {
        for (item in list) {
            canvas?.drawCircle(mCx, mCY, item.radius, item.paint!!)
        }
    }

    /**
     * 开始扩散
     */
    fun startTransparentCircle() {
        if (!isStartOne) {
            isStartOne = true // 第一个已经开始了
            isStopAll = false // 重置整体播放状态

            isStopOne = false // 重置第一个播放完毕的状态
            isPrepareTwo = false // 重置第二个准备好的状态

            mFlagType = 1

            sendMessage(whatLoopSmallToBigAdd)
        }
    }

    /**
     * 从小到大的处理逻辑
     */
    fun startSmallSolidCircle() {
        isPrepareTwo = true // 标记：第二个已经准备好了

        initSmallToBigCircle()
    }

    private fun initSmallToBigCircle() {
        /**
         * 逻辑：
         * 1：当前没有执行动画
         * 2：第二个已经准备好了
         * 3：第一个已经结束了
         */
        if (!isStartSecond && isPrepareTwo && isStopOne) {

            isStopAll = false // 重置整体播放状态
            isStartSecond = true // 标记第二个已经开始了
            mFlagType = 2

            // 单独添加一个从小到大的圆圈
            addPoint(whatSmallToBig, 0)
        }
    }

    /**
     * 开始从大到小圆圈
     */
    fun startBigToSmallCircle() {
        if (!isStartThree) {
            isStartThree = true
            isStopAll = false // 重置整体播放状态
            mFlagType = 3
            isStopLoopThree = false // 第三个loop

            // 添加一个圆圈
            addPoint(whatBigToSmall, 1)

            // 发送循环标记
            sendMessageDelayed(whatLoopBigToSmallAdd, 500)
        }
    }

    /**
     * 清除掉所有的view
     */
    fun clear() {
        isStartThree = false // 从小到大的开始标记
        isStartSecond = false // 从小到大的开始标记
        isStartOne = false

        isStopOne = false
        isPrepareTwo = false
        isStopAll = true
    }

    fun sendMessage(type: Int) {
        if (!isStopAll) {
            mHandler.removeMessages(type)
            mHandler.sendEmptyMessage(type)
        }
    }

    /**
     * 刷新UI展示，默认的刷新频率为50毫秒，可以控制动画的快慢，数值越大，更新频率越慢，数字越小，更新速度越快
     */
    fun updateStatus(type: Int) {
        mHandler.removeMessages(type)
        mHandler.sendEmptyMessageDelayed(type, 50)
        invalidate()
    }

    /**
     * 发送轮训消息
     */
    fun sendMessageDelayed(type: Int, delayMillis: Long) {
        if (!isStopAll) {
            // 如果loop从小到大的逻辑，如果第二个准备好了，就停止
            if (type == whatLoopSmallToBigAdd) {
                if (isPrepareTwo) {
                    return
                }
            }

            // 如果loop从大
            if (type == whatLoopBigToSmallAdd) {
                if (isStopLoopThree) {
                    return
                }
            }

            mHandler.removeMessages(type)
            mHandler.sendEmptyMessageDelayed(type, delayMillis)
        }
    }

    val mHandler: Handler = Handler(object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {

            when (msg.what) {
                // 开始扩散
                whatGradient -> {
                    val iterator = mListLoopSmallToBig.iterator()
                    while (iterator.hasNext()) {
                        val point = iterator.next()
                        // 宽度增加
                        point.radius += intervalWidth
                        // 透明逐渐变小
                        point.paint?.alpha = (mColorValue - point.radius * mAlphasZoom).toInt()

                        // 删除多余的数据
                        if (point.radius >= mMaxRadius || point.paint?.alpha!! <= 0) {
                            iterator.remove()
                            LogUtil.e("删除！！")
                            invalidate()
                        }
                    }
                    // 更新UI
                    if (mListLoopSmallToBig.size > 0) {
                        updateStatus(whatGradient)
                    } else {
                        isStartOne = false // 重置第一个动画的播放状态
                        isStopOne = true // 标记第一个动画已经播放完毕

                        initSmallToBigCircle()
                    }
                }

                // 从小到大的loop
                whatLoopSmallToBigAdd -> {

                    // 开始添加渐变的圆圈
                    addPoint(whatGradient, 0)

                    // 指定间隔时间，生成一个新的圆圈
                    sendMessageDelayed(whatLoopSmallToBigAdd, 700)
                }

                // 从小到大的实心圆圈
                whatSmallToBig -> {

                    val iterator = mListSmallToBig.iterator()
                    while (iterator.hasNext()) {
                        val circle = iterator.next()
                        circle.radius += intervalWidth

                        // 删除多余的数据
                        if (circle.radius >= mMaxRadius) {
                            iterator.remove()
                            LogUtil.e("删除 ---> 实心 从小到大 ！！")
                        }
                    }
                    if (mListSmallToBig.size > 0) {
                        updateStatus(whatSmallToBig)
                    } else {
                        isStartSecond = false
                    }
                }

                // 从大到小的实心圆圈
                whatBigToSmall -> {
                    val iterator = mListBigToSmall.iterator()
                    while (iterator.hasNext()) {
                        val next = iterator.next()

                        if (next.tag == 1) { // 实心圆圈
                            next.radius -= intervalWidth
                        } else if (next.tag == 2) {
                            next.radius -= intervalWidth
                            next.paint?.alpha = (next.radius * mAlphasZoom).toInt()
                        }

                        // 删除多余的数据
                        if (next.radius <= 0 || next.paint?.alpha!! <= 0) {
                            if (next.tag == 1) {
                                isStopLoopThree = true
                                LogUtil.e("-------------->")
                                clearHandler()
                            }
                            iterator.remove()
                            invalidate()
                            LogUtil.e("删除 ---> 实心 从大到小 ！！")
                        }
                    }

                    if (mListBigToSmall.size > 0) {
                        updateStatus(whatBigToSmall)
                    } else {
                        isStartThree = false
                    }
                }

                // 从大到小的loop
                whatLoopBigToSmallAdd -> {
                    // 开始添加渐变的圆圈
                    if (!isStopLoopThree) {

                        addPoint(whatBigToSmall, 2)

                        // 指定间隔时间，生成一个新的圆圈
                        sendMessageDelayed(whatLoopBigToSmallAdd, 600)
                    }
                }
            }
            return true
        }
    })

    private fun clearHandler() {
        mHandler.removeCallbacksAndMessages(null)
    }

    fun addPoint(type: Int, tag: Int) {
        if (isStopAll) {
            return
        }

        val circle = Circle()
        val paint = Paint()

        // 添加从小到大渐变的圆圈
        if (type == whatGradient) {
            paint.color = Color.WHITE
            paint.strokeWidth = 15f
            paint.style = Paint.Style.STROKE
            paint.alpha = mColorValue

            circle.paint = paint
            circle.radius = 0f

            mListLoopSmallToBig.add(circle)
        } else if (type == whatSmallToBig) {
            // 从小到大的实心圆圈
            paint.color = Color.WHITE
            paint.strokeWidth = 15f
            paint.style = Paint.Style.FILL_AND_STROKE
            paint.alpha = mColorMaxValue

            circle.paint = paint
            circle.radius = 0f

            mListSmallToBig.add(circle)
        } else if (type == whatBigToSmall) {

            if (tag == 1) {
                // 从大到小的实心圆圈
                paint.color = Color.WHITE
                paint.style = Paint.Style.FILL_AND_STROKE
                paint.alpha = mColorMaxValue

                circle.paint = paint
                circle.radius = mMaxRadius
                circle.tag = tag// 增加标记

            } else if (tag == 2) {
                // 从大到小的渐变圆
                paint.color = Color.WHITE
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 15f
                paint.alpha = mColorValue

                circle.paint = paint
                circle.radius = mMaxRadius
                circle.tag = tag// 增加标记
            }

            mListBigToSmall.add(circle)
        }
        // 发送更新的UI
        sendMessage(type)
    }

    internal class Circle {

        var paint: Paint? = null
        var radius: Float = 0f
        var tag = 0
        override fun toString(): String {
            return "Point(paint=$paint, radius=$radius)"
        }
    }

}
