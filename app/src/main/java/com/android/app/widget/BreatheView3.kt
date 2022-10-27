package com.android.app.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
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
    private val mFaultTolerant = 5 // 绘制圆圈的容错值


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
            when (mLoopType2) {
                1 -> {
                    drawCircle(it, mListSmallToBig2)
                }

                2 -> {
                    drawCircle(it, mListBigToSmall2)
                }
            }
        }
    }

    private fun drawCircle(canvas: Canvas?, list: List<Circle>) {
        for (item in list) {
            canvas?.drawCircle(mCx, mCY, item.radius, item.paint!!)
        }
    }


    val mHandler: Handler = Handler(object : Handler.Callback {
        override fun handleMessage(msg: Message): Boolean {
//
//            when (msg.what) {
//                // 开始扩散
//                whatGradient -> {
//                    val iterator = mListLoopSmallToBig.iterator()
//
//                    while (iterator.hasNext()) {
//                        val point = iterator.next()
//                        // 只有不在集合内，通过动画插值器去控制渐变的效果
//                        val values = mListCircleSmallToBig.values
//                        if (point !in values) {
//                            animationSmallToBigGradual(point)
//                        }
//                        LogUtil.e("---->circle:size::  " + mListCircleSmallToBig.size)
//                        LogUtil.e("---->animation:size::  " + mListAnimation.size)
////
////                        // 开始处理动画
////                        animationSmallToBigGradual(point)
////                        //  宽度增加
////                        point.radius += intervalWidth
////                        // 透明逐渐变小
////                        point.paint?.alpha = (mColorValue - point.radius * mAlphasZoom).toInt()
////                        // 圆圈宽度的递减
////                        point.paint?.strokeWidth = mStrokeWidth - mStrokeWithZoom * point.radius
//
//                        // 删除多余的数据
//                        if (point.radius >= mMaxRadius - mFaultTolerant || point.paint?.alpha!! <= 0) {
//                            iterator.remove()
//                            LogUtil.e("删除！！")
//                            invalidate()
//                        }
//                    }
//
//                    // 移除多余已经停止的动画
//                    val iteratorAnimation = mListAnimation.iterator()
//                    while (iteratorAnimation.hasNext()) {
//                        val next = iteratorAnimation.next()
//                        if (!next.value.isRunning) {
//                            // 动画都停止了，圆圈也就没用了,干掉它
//                            iteratorAnimation.remove()
//                        }
//                    }
//
//                    // 动画都停止了，圆圈也就没用了,干掉它 todo  怎么删掉它
////                    val iteratorCircle = mListCircleSmallToBig.iterator()
////                    while (iteratorCircle.hasNext()) {
////                        val next1 = iteratorCircle.next()
////                        val key = next1.key
////                        LogUtil.e("key:----" + key + "   ###  " + (System.currentTimeMillis() - key))
////                        if (System.currentTimeMillis() - key > 3000) {
//////                            iteratorCircle.remove()
////                        }
////                    }
//
//                    // 更新UI
//                    if (mListLoopSmallToBig.size > 0) {
////                        updateStatus(whatGradient)
//                    } else {
//                        isStartOne = false // 重置第一个动画的播放状态
//                        isStopOne = true // 标记第一个动画已经播放完毕
//
//                        LogUtil.e("开始播放第二个动画！")
//                        initSmallToBigCircle()
//                    }
//                }
//
//                // 从小到大的loop
//                whatLoopSmallToBigAdd -> {
//
//                    // 开始添加渐变的圆圈
//                    addPoint(whatGradient, 0)
//
//                }
//
//                // 从小到大的实心圆圈
//                whatSmallToBig -> {
//
////                    val iterator = mListSmallToBig.iterator()
////                    while (iterator.hasNext()) {
////                        val circle = iterator.next()
////                        circle.radius += intervalWidth
////
////                        // 删除多余的数据
////                        if (circle.radius >= mMaxRadius) {
////                            iterator.remove()
////                            LogUtil.e("删除 ---> 实心 从小到大 ！！")
////                        }
////                    }
////                    if (mListSmallToBig.size > 0) {
////                        updateStatus(whatSmallToBig)
////                    } else {
////                        isStartSecond = false
////                    }
//                }
//
//                // 从大到小的实心圆圈
//                whatBigToSmall -> {
//                    val iterator = mListBigToSmall.iterator()
//                    while (iterator.hasNext()) {
//                        val next = iterator.next()
//
//                        if (next.tag == 1) { // 实心圆圈
//                            // next.radius -= intervalWidth
//                            if (next.radius <= 0) {
//                                isStartThree = false
//                            }
//                        } else if (next.tag == 2) {
//                            next.radius -= intervalWidth
//                            next.paint?.alpha = (next.radius * mAlphasZoom).toInt()
//
//                            // 圆圈宽度的递减
////                            next.paint?.strokeWidth =
//                        }
//
//                        // 删除多余的数据
//                        if (next.radius <= 0 || next.paint?.alpha!! <= 0) {
//                            if (next.tag == 1) {
//                                isStopLoopThree = true
//                            }
//                            iterator.remove()
//                            invalidate()
//                            LogUtil.e("删除 ---> 实心 从大到小 ！！")
//                        }
//                    }
//
//                    if (mListBigToSmall.size > 0) {
////                        updateStatus(whatBigToSmall)
//                    } else {
//                        isStartThree = false
//                    }
//                }
//
//                // 从大到小的loop
//                whatLoopBigToSmallAdd -> {
//                    // 开始添加渐变的圆圈
//                    if (!isStopLoopThree) {
//                        // 只有在实心圆圈大于 40 的时候，采取发送轮训，否则就不发送，避免最后大圆圈消失了，还有几个小圆圈
//                        if (mCircleBigToSmall?.radius!! >= 80f) {
//
//                            addPoint(whatBigToSmall, 2)
//
//                            // 指定间隔时间，生成一个新的圆圈
////                            sendMessageDelayed(whatLoopBigToSmallAdd, 500)
//                        } else {
//                            LogUtil.e("移除了多余的handler")
////                            removeHandler(whatLoopBigToSmallAdd)
//                        }
//                    }
//                }
//            }
            return true
        }
    })


    fun addPoint(type: Int, tag: Int) {

        val circle = Circle()
        val paint = Paint()

        // 添加从小到大渐变的圆圈
//        if (type == whatGradient) {
//            paint.color = Color.WHITE
//            paint.strokeWidth = mStrokeWidth
//            paint.style = Paint.Style.STROKE
//            paint.alpha = mColorValue
//
//            circle.paint = paint
//            circle.radius = 0f
//
//            mListLoopSmallToBig.add(circle)
//        } else if (type == whatSmallToBig) {
//            // 从小到大的实心圆圈
//            paint.color = Color.WHITE
//            paint.strokeWidth = mStrokeWidth
//            paint.style = Paint.Style.FILL_AND_STROKE
//            paint.alpha = mColorMaxValue
//
//            circle.paint = paint
//            circle.radius = 0f
//
//            mListSmallToBig.clear()
//            mListSmallToBig.add(circle)
//        } else if (type == whatBigToSmall) {
//
//            if (tag == 1) {
//                // 从大到小的实心圆圈
//                paint.color = Color.WHITE
//                paint.style = Paint.Style.FILL_AND_STROKE
//                paint.alpha = mColorMaxValue
//                paint.strokeWidth = mStrokeWidth
//
//                circle.paint = paint
//                circle.radius = mMaxRadius
//                circle.tag = tag// 增加标记
//
//            } else if (tag == 2) {
//                // 从大到小的渐变圆
//                paint.color = Color.WHITE
//                paint.style = Paint.Style.STROKE
//                paint.strokeWidth = mStrokeWidth
//                paint.alpha = mColorValue
//
//                circle.paint = paint
//                circle.radius = mMaxRadius
//                circle.tag = tag// 增加标记
//            }
//
//            mListBigToSmall.add(circle)
//        }
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
     * 从小到大的插值气动画效果
     */
    private fun animationSmallToBig() {

        var changeValue = 0f
        val animation = ValueAnimator.ofFloat(0f, mMaxRadius - mFaultTolerant)

        animation.duration = 1500
        animation.interpolator = DecelerateInterpolator()
        animation.addUpdateListener { anim ->
            if (changeValue != anim.animatedValue) {
                changeValue = anim.animatedValue as Float
//
//                next.radius = changeValue
//
//                invalidate()
//
//                // 删除多余的数据
//                if (next.radius >= mMaxRadius - mFaultTolerant) {
//                    LogUtil.e("删除 ---> 实心 从小到大 ！！")
//                    animation.cancel()
//                    isStartSecond = false
//                }
            }
        }
        animation.start()
    }

    /**
     * 从大到小的动画
     */
    private fun animationBigToSmall() {
//        val iterator = mListBigToSmall.iterator()
//        while (iterator.hasNext()) {
//            val next = iterator.next()
//            if (next.tag == 1) {
//                mCircleBigToSmall = next
//                break
//            }
//        }

        var changeValue = 0f
        val animation = ValueAnimator.ofFloat(mMaxRadius - mFaultTolerant, 0f)

        animation.duration = 2500
        // 开始慢，后面快的速度插值器
        animation.interpolator = AccelerateDecelerateInterpolator()
        // 在半径70%的时候，去进行发送轮训的标记
        val flag = (mMaxRadius * 0.7f).toInt()
        var isSend = true
        animation.addUpdateListener { anim ->
            if (changeValue != anim.animatedValue) {
                LogUtil.e("animation:" + anim.animatedValue)
                changeValue = anim.animatedValue as Float

//                mCircleBigToSmall?.radius = changeValue

                if (changeValue <= flag && isSend) {
                    isSend = false
                    // 发送循环标记
                    LogUtil.e("开始发送标记！")
//                    sendMessage(whatLoopBigToSmallAdd)
                }

                // 删除多余的数据
                if (changeValue == 0f) {
                    LogUtil.e("删除 ---> 实心 从大到小 ！！")
                    animation.cancel()
                }
            }
        }
        animation.start()
    }

    /**
     * 从小到大动画的渐变
     */
    private fun animationSmallToBigGradual(circle: Circle?) {
        if (circle != null) {
            var changeValue = 0f
            val animation = ValueAnimator.ofFloat(0f, mMaxRadius - mFaultTolerant)


            var temp = 0
            animation.duration = 2500
            animation.interpolator = DecelerateInterpolator()
            animation.addUpdateListener { anim ->
                if (changeValue != anim.animatedValue) {
                    LogUtil.e("animation:" + anim.animatedValue)
                    changeValue = anim.animatedValue as Float

                    // 宽度增加
                    circle.radius = changeValue
                    // 透明逐渐变小
                    circle.paint?.alpha = (mColorValue - circle.radius * mAlphasZoom).toInt()
                    // 圆圈宽度的递减
                    circle.paint?.strokeWidth = mStrokeWidth - mStrokeWithZoom * circle.radius

//                    if (isPrepareTwo) {
//                        circle.radius = (changeValue + temp * mFaultTolerant)
//                        if (temp < 3) {
//                            temp++
//                        }
//                        LogUtil.e("增大 ---> ")
//                    }
                }
            }
            animation.start()
        }
    }

    // 清空动画的集合
    private fun clearAnimationList() {
//        clearHandler()
//        LogUtil.e("mListAnimation:" + mListAnimation.size)
//        for (item in mListAnimation) {
//            item.value.cancel()
//            item.value.removeAllUpdateListeners()
//        }
    }

    init {
        initView(context)
    }

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
    private val mWhatLoop2 = 1;

    // 当前轮询的type 1:小到大，2： 大到小 
    private var mLoopType2 = 0

    // 1: 透明扩散 2: 实心
    private var mLoopTag = 0

    // 从小到大的圆圈集合
    private val mListSmallToBig2 = mutableListOf<Circle>()

    // 从大到小的圆圈集合
    private val mListBigToSmall2 = mutableListOf<Circle>()
    private val mStrokeWidth = 10f // 文字的宽度
    private val mColorValue = 150
    private val mColorMaxValue = 255

    private val mWidth = 300f // 绘制区域的宽度
    private val mHeight = 300f // 绘制区域的高度

    // 用来存储动画的集合
    private val mListAnimation2 = hashMapOf<Circle, ValueAnimator>()

    /**
     * 开启轮询透明的圆圈
     */
    public fun startSmallToBigAlphaLoop() {
        mLoopType2 = 1
        mLoopTag = 1
        removeAll2()
        sendMessage2()
    }

    /**
     * 开启小到大的实心圆
     */
    public fun startSmallToBig() {
        mLoopType2 = 1
        mLoopTag = 2
        removeAll2()
        addPoint2()
    }

    /**
     * 开启大到小的实心圆
     */
    public fun startBigToSmall() {
        mLoopType2 = 2
        mLoopTag = 2
        removeAll2()
        addPoint2()
    }

    /**
     * 大道小的透明
     */
    public fun startBigToSmallAlphaLoop() {
        LogUtil.e("发送小到大的轮询标记！")
        mLoopType2 = 2
        mLoopTag = 1
        removeAll2()
        sendMessage2()
    }

    /***
     * 使用handler 做消息的轮询便利
     */
    private val mHandler2 = Handler(Looper.myLooper()!!, Handler.Callback { msg ->
        // 开启自循环
        if (msg.what == mWhatLoop2) {

            addPoint2()

            // 发送轮询消息
            sendDelayedMessageLoop2()
        }
        true
    })

    /**
     * 发送轮询消息
     */
    private fun sendDelayedMessageLoop2() {
        mHandler2.removeMessages(mWhatLoop2)
        mHandler2.sendEmptyMessageDelayed(mWhatLoop2, 600)
    }

    /**
     * 开启轮询
     */
    private fun sendMessage2() {
        mHandler2.removeMessages(mWhatLoop2)
        mHandler2.sendEmptyMessage(mWhatLoop2)
    }

    private fun removeAll2() {
        mHandler2.removeMessages(mWhatLoop2)
        mHandler2.removeCallbacksAndMessages(null)
    }

    /**
     * 暂停
     */
    public fun pause(isPause: Boolean) {
        if (isPause) {
            removeAll2()
        } else {
            sendMessage2()
        }
    }

    /**
     * 1:透明扩散，2：实心
     */
    private fun addPoint2() {
        val circle = Circle()
        circle.tag = mLoopTag// 增加标记
        val paint = Paint()

        when (mLoopType2) {
            1 -> {  // 小到大
                if (mLoopTag == 1) { // 从小到大的透明扩散
                    paint.color = Color.WHITE
                    paint.strokeWidth = mStrokeWidth
                    paint.style = Paint.Style.STROKE
                    paint.alpha = mColorValue

                    circle.paint = paint
                    circle.radius = 0f
                    mListSmallToBig2.add(circle)

                    animationSmallToBigLoop2(circle)

                } else if (mLoopTag == 2) { // 小到大的实心圆
                    paint.color = Color.WHITE
                    paint.strokeWidth = 0F
                    paint.style = Paint.Style.FILL
                    paint.alpha = mColorMaxValue

                    circle.paint = paint
                    circle.radius = 0f
                    mListSmallToBig2.add(circle)

                    animationSmallToBig2(circle)
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

                    mListBigToSmall2.add(circle)
                    animationBigToSmall2(circle)

                } else if (mLoopTag == 1) {
                    // 从大到小的渐变圆
                    paint.color = Color.WHITE
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = mStrokeWidth
                    paint.alpha = mColorValue
                    circle.paint = paint
                    circle.radius = mMaxRadius

                    mListBigToSmall2.add(circle)

                    animationBigToSmallLoop(circle)
                }
            }
        }
    }

    /**
     * 小到大的渐变扩散
     */
    private fun animationSmallToBigLoop2(circle: Circle) {
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
        if (!mListAnimation2.containsKey(circle)) {
            mListAnimation2[circle] = animator
        }

        // 删除小到大多余的圆圈
        removeSmallToBigList()
    }

    /**
     * 小到大的实心
     */
    private fun animationSmallToBig2(circle: Circle) {
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
        if (!mListAnimation2.containsKey(circle)) {
            mListAnimation2[circle] = animator
        }

        // 删除小到大多余的圆圈
        removeSmallToBigList()
    }


    /**
     * 移除小到大的闲置集合
     */
    private fun removeSmallToBigList() {
        // 删除多余的动画集合
        val iteratorAnimation = mListAnimation2.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (next.key.radius >= mMaxRadius) {
                // 停止动画
                next.value.cancel()
                // 取消监听
                next.value.removeAllUpdateListeners()
                iteratorAnimation.remove()
                LogUtil.e("删除了多余的动画 ---> 小到大渐变  size:" + mListAnimation2.size)
            }
        }

        // 删除多余的圆环集合
        val iteratorSmallToBig = mListSmallToBig2.iterator()
        while (iteratorSmallToBig.hasNext()) {
            val next = iteratorSmallToBig.next()
            if (next.radius >= mMaxRadius) {
                iteratorSmallToBig.remove()
                LogUtil.e("删除了多余的圆圈 ---> 小到大渐变 size:" + mListSmallToBig2.size)
            }
        }
    }


    /**
     * 大到小的实心
     */
    private fun animationBigToSmall2(circle: Circle) {
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

            if (temp != animatedValue) {
                // 宽度逐渐增加
                circle.radius = animatedValue

                // 在小于
                if (animatedValue < range && !isSend) {
                    LogUtil.e("---> animatedValue:$animatedValue  range:$range")
                    isSend = true
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
        if (!mListAnimation2.containsKey(circle)) {
            mListAnimation2[circle] = animator
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

//                circle.paint?.alpha = (circle.paint?.alpha!! - mStrokeWidth).toInt()
                // 圆圈宽度的递减
//                circle.paint?.strokeWidth = mStrokeWithZoom * animatedValue

                // 刷新布局
                temp = animatedValue

                invalidate()
            }
        }
        animator.start()

        // 加入集合
        if (!mListAnimation2.containsKey(circle)) {
            mListAnimation2[circle] = animator
        }

        // 删除小到大多余的圆圈
        removeBigToSmallList()
    }

    /**
     * 移除大到小的闲置集合
     */
    private fun removeBigToSmallList() {
        // 删除多余的动画集合
        val iteratorAnimation = mListAnimation2.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (next.key.radius <= 0) {
                // 停止动画
                next.value.cancel()
                // 取消监听
                next.value.removeAllUpdateListeners()
                iteratorAnimation.remove()
                LogUtil.e("删除了多余的动画 ---> 大到大渐变  size:" + mListAnimation2.size)
            }
        }

        // 删除多余的圆环集合
        val iteratorSmallToBig = mListBigToSmall2.iterator()
        while (iteratorSmallToBig.hasNext()) {
            val next = iteratorSmallToBig.next()
            if (next.radius <= 0) {
                // 标记大到小的实心圆已经结束了
                if (mLoopType2 == 2 && next.tag == 2) {
                    LogUtil.e("移除所有的小到大的渐变动画！")
                    removeAll2()
                }
                iteratorSmallToBig.remove()
                LogUtil.e("删除了多余的圆圈 ---> 大到小渐变 size:" + mListBigToSmall2.size)
            }
        }
    }

}
