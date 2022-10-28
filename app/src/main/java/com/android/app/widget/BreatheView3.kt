package com.android.app.widget

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/10/20-10:59
 * @Description:
 */
class BreatheView3(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val mRectStrokeLine = RectF() // 边线
    private val mPaintStrokeLine = Paint() // 绘制边线
    private var intervalWidth = 10 // 每隔view间隔的宽度
    private var mCx: Float = 0F // 初始的X轴
    private var mCY: Float = 0F // 初始的Y轴
    private var mMaxRadius = 0f // 最大的扩散宽度
    private var mAlphasZoom = 0f // 透明度的比例
    private var mStrokeWithZoom = 0f // 文字宽度的比例

    /**
     * 从小到大的透明渐变圆的持续时间
     */
    private var mDurationAlphaSmallToBig: Long = 4000

    /**
     * 从小到大的实心圆的持续时间
     */
    private var mDurationSmallToBig: Long = 2000

    /**
     * 从大到小实心圆的持续时间
     */
    private var mDurationBigToSmall: Long = 4000

    // handler的自动循环
    private val mWhatLoop = 1;

    // 当前轮询的type 1:小到大，2： 大到小
    private var mLoopType = 0

    // 1: 透明扩散 2: 实心
    private var mLoopTag = 0

    // 从小到大的圆圈集合
    private val mListSmallToBig = mutableListOf<Circle>()

    // 从大到小的圆圈集合
    private val mListBigToSmall = mutableListOf<Circle>()
    private val mStrokeWidth = 10f // 文字的宽度
    private val mColorValue = 150
    private val mColorMaxValue = 255

    private val mWidth = 300f // 绘制区域的宽度
    private val mHeight = 300f // 绘制区域的高度

    // 用来存储动画的集合
    private val mListAnimation = hashMapOf<Circle, ValueAnimator>()

    private var isStopAll = false // 是否停止所有的操作

    init {
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

        setMeasuredDimension(mWidth.toInt(), mHeight.toInt())

        // 中心X轴
        mCx = (measuredWidth / 2).toFloat()
        // 中心Y轴
        mCY = (measuredHeight / 2).toFloat()
        // 圆心最大的直径宽度
        mMaxRadius = (measuredWidth / 2).toFloat()
        // 每一份占据的透明度
        mAlphasZoom = (mColorValue / mMaxRadius)
        // 文字的宽度比例
        mStrokeWithZoom = mStrokeWidth / mMaxRadius
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { it ->
            // 绘制边线
            it.drawRect(mRectStrokeLine, mPaintStrokeLine)

            // 绘制透明圆圈
            when (mLoopType) {
                1 -> {
                    drawCircle(it, mListSmallToBig)
                }

                2 -> {
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

    class Circle {

        var paint: Paint? = null
        var radius: Float = 0f
        var tag = 0
        override fun toString(): String {
            return "Point(paint=$paint, radius=$radius)"
        }
    }

    /**
     * 开启轮询透明的圆圈
     */
    public fun startSmallToBigAlphaLoop() {
        mLoopType = 1
        mLoopTag = 1
        removeAll()
        sendMessage()
    }

    /**
     * 开启小到大的实心圆
     */
    public fun startSmallToBig() {
        mLoopType = 1
        mLoopTag = 2
        removeAll()
        addPoint()
    }

    /**
     * 开启大到小的实心圆
     */
    public fun startBigToSmall() {
        mLoopType = 2
        mLoopTag = 2
        removeAll()
        addPoint()
    }

    /**
     * 大道小的透明
     */
    fun startBigToSmallAlphaLoop() {
        LogUtil.e("发送小到大的轮询标记！")
        mLoopType = 2
        mLoopTag = 1
        removeAll()
        sendMessage()
    }

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if (isStop) {
                LogUtil.e("isStop:" + isStop)
            } else {

            }

            // 开启自循环
            if (msg.what == mWhatLoop) {
                addPoint()

                // 发送轮询消息
                sendDelayedMessageLoop()
            }
        }
    }

    /**
     * 发送轮询消息
     */
    private fun sendDelayedMessageLoop() {
        mHandler.removeMessages(mWhatLoop, null)
        mHandler.sendEmptyMessageDelayed(mWhatLoop, 700)
    }

    /**
     * 开启轮询
     */
    private fun sendMessage() {
        mHandler.removeMessages(mWhatLoop, null)
        mHandler.sendEmptyMessage(mWhatLoop)
    }

    private fun removeAll() {
        mHandler.removeMessages(mWhatLoop, null)
        mHandler.removeCallbacksAndMessages(null)
        LogUtil.e("⭐️⭐️⭐️ ---> 移除了所有的操作")
    }

    /**
     * 暂停
     */
    public fun pause(isPause: Boolean) {
        if (isPause) {
            removeAll()
            // 停止所有动画
            mListAnimation.forEach { (t, u) ->
                u.pause()
            }
        } else {
            sendMessage()
            // 开启所有动画
            mListAnimation.forEach { (t, u) ->
                u.resume()
            }

        }
    }

    /**
     * 1:透明扩散，2：实心
     */
    private fun addPoint() {
        val circle = Circle()
        circle.tag = mLoopTag// 增加标记
        val paint = Paint()

        when (mLoopType) {
            1 -> {  // 小到大
                if (mLoopTag == 1) { // 从小到大的透明扩散
                    paint.color = Color.WHITE
                    paint.strokeWidth = mStrokeWidth
                    paint.style = Paint.Style.STROKE
                    paint.alpha = mColorValue

                    circle.paint = paint
                    circle.radius = 0f
                    mListSmallToBig.add(circle)

                    animationSmallToBigLoop(circle)

                } else if (mLoopTag == 2) { // 小到大的实心圆
                    paint.color = Color.WHITE
                    paint.strokeWidth = 0F
                    paint.style = Paint.Style.FILL
                    paint.alpha = mColorMaxValue

                    circle.paint = paint
                    circle.radius = 0f
                    mListSmallToBig.add(circle)

                    animationSmallToBig(circle)
                }

            }

            2 -> { // 从大到小的
                if (mLoopTag == 2) {
                    // 从大到小的实心圆圈
                    paint.color = Color.WHITE
                    paint.style = Paint.Style.FILL
                    paint.alpha = mColorMaxValue
                    paint.strokeWidth = mStrokeWidth
                    circle.paint = paint
                    circle.radius = mMaxRadius

                    LogUtil.e("⭐️⭐️⭐️ ---> 大到小 实心 ---> 添加")

                    mListBigToSmall.add(circle)
                    animationBigToSmall(circle)

                } else if (mLoopTag == 1) {
                    // 从大到小的渐变圆
                    paint.color = Color.WHITE
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = mStrokeWidth
                    paint.alpha = mColorValue
                    circle.paint = paint
                    circle.radius = mMaxRadius

                    LogUtil.e("⭐️⭐️⭐️ ---> 大到小 透明 ---> 添加")

                    mListBigToSmall.add(circle)

                    animationBigToSmallLoop(circle)
                }
            }
        }
    }

    /**
     * 小到大的渐变扩散
     */
    private fun animationSmallToBigLoop(circle: Circle) {
        var temp = 0F
        val animator = ValueAnimator.ofFloat(0f, mMaxRadius)
        animator.duration = mDurationAlphaSmallToBig
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float
            if (temp != animatedValue) {

                // 宽度逐渐增加
                circle.radius = animatedValue
                // 透明度逐渐减小
                circle.paint?.alpha = (mColorValue - circle.radius * mAlphasZoom).toInt()
                // 画笔宽度逐渐减小
                circle.paint?.strokeWidth = mStrokeWidth - mStrokeWithZoom * circle.radius

                // 刷新布局
                invalidate()
                temp = animatedValue;
            }
        }
        animator.start()

        // 加入集合
        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }

        // 删除小到大多余的圆圈
        removeSmallToBigList()
    }

    /**
     * 小到大的实心
     */
    private fun animationSmallToBig(circle: Circle) {
        LogUtil.e("开始了小到大 --->")
        var temp = 0F
        val animator = ValueAnimator.ofFloat(0f, mMaxRadius)
        animator.duration = mDurationSmallToBig
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float

            if (temp != animatedValue) {
                // 宽度逐渐增加
                circle.radius = animatedValue
                // 刷新布局
                invalidate()
                temp = animatedValue;
            }
        }
        animator.start()

        // 加入集合
        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }

        // 删除小到大多余的圆圈
        removeSmallToBigList()
    }

    /**
     * 移除小到大的闲置集合
     */
    private fun removeSmallToBigList() {
        // 删除多余的动画集合
        val iteratorAnimation = mListAnimation.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (next.key.radius >= mMaxRadius) {
                // 停止动画
                next.value.cancel()
                // 取消监听
                next.value.removeAllUpdateListeners()
                iteratorAnimation.remove()
                LogUtil.e("删除了多余的动画 ---> 小到大渐变  size:" + mListAnimation.size)
            }
        }

        // 删除多余的圆环集合
        val iteratorSmallToBig = mListSmallToBig.iterator()
        while (iteratorSmallToBig.hasNext()) {
            val next = iteratorSmallToBig.next()
            if (next.radius >= mMaxRadius) {
                iteratorSmallToBig.remove()
                LogUtil.e("删除了多余的圆圈 ---> 小到大渐变 size:" + mListSmallToBig.size)
            }
        }
    }

    /**
     * 大到小的实心
     */
    private fun animationBigToSmall(circle: Circle) {
        LogUtil.e("开始了小到大 --->")
        var temp = 0F
        var isSend = false // 是否发送了轮询的标记

        // 区间
        val range = mMaxRadius * 0.8f

        val animator = ValueAnimator.ofFloat(mMaxRadius, 0f)
        animator.duration = mDurationBigToSmall
        // 加速度插值器
        animator.interpolator = AccelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float
            LogUtil.e("⭐️⭐️⭐️ ---> 大到小 实心  $animatedValue")

            if (temp != animatedValue) {
                // 宽度逐渐增加
                circle.radius = animatedValue
                // 在小于
                if (animatedValue < range && !isSend) {
                    isSend = true
                    LogUtil.e("⭐️⭐️⭐️ ---> 大到小 实心 ---> 发送轮巡")
                    // 大到小的透明
                    startBigToSmallAlphaLoop()
                }

                // 刷新布局
                if (animatedValue >= 0) {
                    invalidate()
                }
                temp = animatedValue
            }
        }
        animator.start()

        // 加入集合
        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }

        // 删除小到大多余的圆圈
        removeBigToSmallList()
    }

    /**
     * 大到小的透明
     */
    private fun animationBigToSmallLoop(circle: Circle) {
        LogUtil.e("开始了小到大 --->")
        var temp = 0F

        val animator = ValueAnimator.ofFloat(mMaxRadius - mStrokeWidth, 0f)
        animator.duration = mDurationBigToSmall
        // 加速度插值器
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float
            val animatedFraction = value.animatedFraction

            if (temp != animatedValue) {
                // 宽度逐渐递减
                circle.radius = animatedValue

                LogUtil.e("animatedValue ---> $animatedFraction")
                // 透明逐渐变小
                circle.paint?.alpha = (animatedValue * mAlphasZoom).toInt()
                // 圆圈宽度的递减
                circle.paint?.strokeWidth = mStrokeWithZoom * animatedValue

                // 刷新布局
                temp = animatedValue

                invalidate()
            }
        }
        animator.start()

        // 加入集合
        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }

        // 删除小到大多余的圆圈
        removeBigToSmallList()
    }

    /**
     * 移除大到小的闲置集合
     */
    private fun removeBigToSmallList() {
        // 删除多余的动画集合
        val iteratorAnimation = mListAnimation.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (next.key.radius <= 0) {
                // 停止动画
                next.value.cancel()
                // 取消监听
                next.value.removeAllUpdateListeners()
                iteratorAnimation.remove()
                LogUtil.e("删除了多余的动画 ---> 大到大渐变  size:" + mListAnimation.size)
            }
        }

        // 删除多余的圆环集合
        val iteratorSmallToBig = mListBigToSmall.iterator()
        while (iteratorSmallToBig.hasNext()) {
            val next = iteratorSmallToBig.next()
            if (next.radius <= 0) {
                // 标记大到小的实心圆已经结束了
                if (mLoopType == 2 && next.tag == 2) {
                    LogUtil.e("⭐️⭐️⭐️ ---> 大到小 透明 ---> 移除所有")
                    isStop = true
                    removeAll()
                }
                iteratorSmallToBig.remove()
                LogUtil.e("删除了多余的圆圈 ---> 大到小渐变 size:" + mListBigToSmall.size)
            }
        }
    }

    var isStop = false
}
