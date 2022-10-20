package com.android.app.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/10/9-11:15
 * @Description:呼吸的view
 */
class BreatheView : View {

    private val TAG = "Touch"
    private val mRectStrokeLine = RectF() // 边线
    private val mPaintStrokeLine = Paint() // 绘制边线

    private val mWidth = 600 // 绘制区域的宽度
    private val mHeight = 600 // 绘制区域的高度
    private var mFlagType = 1 // 1: 循环播放从小到大的圆圈，2：一个从小到大的实心圆圈，3：一个从大到小的实心圆圈

    private val mPaintBigSolidCircle = Paint()  // 大的实心圆圈
    private val mPaintSmallSolidCircle = Paint() // 小的实心圆圈

    private val mListRadius = mutableListOf<Point>() // 扩散的透明圆圈宽度
    private var mCx: Float = 0F // 初始的X轴
    private var mCY: Float = 0F // 初始的Y轴
    private var mMaxRadius = 0f // 最大的扩散宽度

    private var intervalWidth = 10f // 每隔view间隔的宽度
    private var mAlphasZoom = 0f // 透明度的比例

    private val whatGradient = 1 // 渐变的what
    private val whatGradientAdd = 2 // 渐变增加的what
    private val whatSmallToBig = 3 // 从小到大的what
    private val whatBigToSmall = 4 // 从大到小的what

    private var isStop = false// 是否停止并清除所有圆圈
    private var isStartOne = false // 是否已经开始了第一个
    private var isStartSecond = false // 是否已经开始了第二个
    private var isStartThree = false // 是否已经开始了第三个

    constructor(context: Context) : this(context, null) {}

    constructor(context: Context, attributes: AttributeSet?) : super(context, attributes) {
        initView(context, attributes)
    }

    private fun initView(context: Context, attributes: AttributeSet?) {
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

        // 大的实心圆圈
        mPaintBigSolidCircle.color = Color.WHITE
        mPaintBigSolidCircle.alpha = 255

        // 小的实心圆圈
        mPaintSmallSolidCircle.color = Color.WHITE
        mPaintSmallSolidCircle.alpha = 255
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
        mAlphasZoom = (255 / mMaxRadius)
        LogUtil.e("mAlphasZoom:$mAlphasZoom")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.let { it ->

            // 绘制边线
            it.drawRect(mRectStrokeLine, mPaintStrokeLine)

            // 绘制透明圆圈
            mListRadius.forEach { point ->
                point.paint?.let { paint ->
                    it.drawCircle(point.x, point.y, point.radius, paint)
                }
            }
        }
    }

    /**
     * 添加一个圆圈
     */
    private fun addPoint(what: Int) {
        if (!isStop) {

            val point = Point()
            point.x = mCx
            point.y = mCY

            if (mFlagType == 1 || mFlagType == 2) {
                point.radius = 0f
            } else if (mFlagType == 3) {
                point.radius = mMaxRadius
            }

            val paint = Paint()

            if (mFlagType == 1) {
                paint.style = Paint.Style.STROKE
            } else {
                paint.style = Paint.Style.FILL
            }

            // 透明圆圈
            paint.color = Color.WHITE
            paint.alpha = 255
            paint.strokeWidth = 15f
            paint.isAntiAlias = true

            point.paint = paint
            mListRadius.add(point)

            // 发送滚动的指令
            mHandler.sendEmptyMessage(what)
        }
    }

    private val mHandler = Handler(Looper.myLooper()!!) { msg ->
        LogUtil.e("what:" + msg.what)

        if (isStop) {
            return@Handler true
        }

        when (msg.what) {
            // 处理渐变的圆圈
            whatGradient -> {
                isStartSecond = true // 从小到大的开始标记
                isStartThree = true // 从小到大的标记

                val iterator = mListRadius.iterator()
                while (iterator.hasNext()) {
                    // 获取每一个元素
                    val next = iterator.next()

                    // 圆圈的半径逐渐变大
                    val radius = next.radius
                    if (radius < mMaxRadius) {
                        // 长度逐渐加大
                        next.radius += intervalWidth
                    }

                    // 透明度逐渐减小
                    val paint = next.paint
                    if (paint != null) {
                        paint.alpha = (255 - next.radius * mAlphasZoom).toInt()
                    }

                    LogUtil.e("radius:" + next.radius + "  alpha:" + paint?.alpha + "  size:" + mListRadius.size)
                    if (radius >= mMaxRadius || paint?.alpha!! <= 0) {
                        iterator.remove()
                        LogUtil.e("删除！！")
                        invalidate()
                    }
                }

                if (mListRadius.size > 0) {
                    updateStatus(whatGradient)
                } else {
                    isStartThree = false // 从小到大的开始标记
                    isStartSecond = false // 从小到大的开始标记
                    isStartOne = false

                    // 等到数据清空了，开始处理从小到大的源泉
                    if (mFlagType == 2) {
                        LogUtil.e("集合数据为空，开始处理从小到大的逻辑！")
                        initSmallToBig()
                    }
                }
            }

            // 循环添加圆圈
            whatGradientAdd -> {
                addPoint(whatGradient)
                loopAdd()
            }

            // 从小到大的逻辑
            whatSmallToBig -> {
                val iterator = mListRadius.iterator()
                while (iterator.hasNext()) {
                    // 获取每一个元素
                    val next = iterator.next()

                    // 圆圈的半径逐渐变大
                    val radius = next.radius
                    if (radius < mMaxRadius) {
                        // 长度逐渐加大
                        next.radius += intervalWidth
                    }

                    // 透明度逐渐减小
                    val paint = next.paint

                    LogUtil.e("radius:" + next.radius + "  alpha:" + paint?.alpha + "  size:" + mListRadius.size)
                    if (radius >= mMaxRadius || paint?.alpha!! <= 0) {
                        iterator.remove()
                        LogUtil.e("删除 --- 从小到大的圆圈！！")
                        isStartThree = false // 从小到大的开始标记
                        isStartSecond = false // 从小到大的开始标记
                        isStartOne = false

                        // 设置从大到小的圆圈
                        if (mFlagType == 3) {
                            initBigToSmall()
                        }
                    }
                }

                if (mListRadius.size > 0) {
                    updateStatus(whatSmallToBig)
                }
            }

            // 从大到小的逻辑
            whatBigToSmall -> {
                val iterator = mListRadius.iterator()
                LogUtil.e("mListRadius:" + mListRadius.size)

                while (iterator.hasNext()) {
                    // 获取每一个元素
                    val next = iterator.next()

                    val radius = next.radius

                    if (radius > 0) {
                        // 长度逐渐加大
                        next.radius -= intervalWidth
                    }

                    LogUtil.e("radius:" + next.radius + "  size:" + mListRadius.size)
                    if (radius <= 0) {
                        iterator.remove()
                        isStartThree = false // 从小到大的开始标记
                        isStartSecond = false // 从小到大的开始标记
                        isStartOne = false
                        LogUtil.e("删除 --- 从大到小的圆圈！！")
                    }
                }

                if (mListRadius.size > 0) {
                    updateStatus(whatBigToSmall)
                }
            }
        }
        true
    }

    /**
     * 更新动效
     */
    private fun updateStatus(what: Int) {
        if (isStop) {
            return
        }

        // 循环便利对象集合，让他动起来
        mHandler.removeMessages(what)
        mHandler.sendEmptyMessageDelayed(what, 50)
        invalidate()
    }

    /**
     * 开始扩散圆圈
     */
    fun startTransparentCircle() {
        isStop = false
        if (!isStop) {
            mFlagType = 1
            if (!isStartOne) {
                isStartOne = true
                addPoint(whatGradient)
                // 开始循环添加圆圈
                loopAdd()
                invalidate()
            }
        }
    }

    /**
     * 循环添加圆圈
     */
    private fun loopAdd() {
        if (!isStop) {
            // 只有等于1的时候，才会去循环添加
            if (mFlagType == 1) {
                mHandler.removeMessages(whatSmallToBig)
                mHandler.removeMessages(whatBigToSmall)

                // 每隔多少时间新增一个圈
                mHandler.sendEmptyMessageDelayed(whatGradientAdd, 700)
            }
        }
    }

    /**
     * 从小到大的处理逻辑
     */
    private fun initSmallToBig() {
        if (!isStop) {
            if (!isStartSecond) {
                mHandler.removeCallbacksAndMessages(null)
                // 添加一个圆圈
                addPoint(whatSmallToBig)
                isStartSecond = true
                invalidate()
            }
        }
    }

    /**
     * 从大到小的圆圈
     */
    private fun initBigToSmall() {
        if (!isStop) {
            if (!isStartThree) {
                mHandler.removeCallbacksAndMessages(null)
                addPoint(whatBigToSmall)
                isStartThree = true

                // 循环添加圆圈
                loopAdd()

                invalidate()
            }
        }
    }

    /**
     * 开始从小到大圆圈
     */
    fun startBigSolidCircle() {
        isStop = false
        if (!isStop) {
            mFlagType = 2
            initSmallToBig()
        }
    }

    /**
     * 开始从大到小圆圈
     */
    fun startSmallSolidCircle() {
        isStop = false
        if (!isStop) {
            mFlagType = 3
            initBigToSmall()
        }
    }

    /**
     * 清除掉所有的view
     */
    fun clear() {
        isStop = true
        mListRadius.clear()

        isStartThree = false // 从小到大的开始标记
        isStartSecond = false // 从小到大的开始标记
        isStartOne = false
    }

    internal class Point {

        var x = 0f
        var y = 0f
        var radius = 0f
        var paint: Paint? = null
        override fun toString(): String {
            return "Point(x=$x, y=$y, radius=$radius, paint=${paint?.alpha})"
        }
    }

}

