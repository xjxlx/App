package com.android.app.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.android.helper.interfaces.listener.CallBackListener
import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/10/20-10:59
 * @Description:使用这个组件的时候，必须给开始吸气，屏住呼吸 和 呼气的时间给加入到里面
 */
class BreatheView3(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val mRectStrokeLine = RectF() // 边线
    private val mPaintStrokeLine = Paint() // 绘制边线
    private var mCx: Float = 0F // 初始的X轴
    private var mCY: Float = 0F // 初始的Y轴
    private var mMaxRadius = 0f // 最大的扩散宽度
    private var mAlphasZoom = 0f // 透明度的比例
    private var mStrokeWithZoom = 0f // 文字宽度的比例

    /**
     * 从小到大的透明渐变圆的持续时间
     */
    var mDurationAlphaSmallToBig: Long = 4000

    /**
     * 从小到大的实心圆的持续时间
     */
    var mDurationSmallToBig: Long = 2000

    /**
     * 从大到小实心圆的持续时间
     */
    var mDurationBigToSmall: Long = 4000

    // 当前轮询的type 1:小到大，2： 大到小 ,3:暂停的状态
    private var mLoopType = 0

    // 1: 透明扩散 2: 实心 ，3：暂停的状态
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
    private lateinit var mAnimationLoop: ValueAnimator // 动画的轮询，使用无限动画的轮询去代替handler
    private lateinit var mAnimationInterval: ValueAnimator // 用来倒计时

    init {
        initView(context)
    }

    private fun initView(context: Context) {
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

        // 定时器，用来处理消息和停止消息
        mAnimationLoop = ValueAnimator.ofFloat(0f, 700f)
        mAnimationLoop.duration = 800
        mAnimationLoop.repeatCount = ValueAnimator.INFINITE
        mAnimationLoop.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                LogUtil.e("---->   onAnimationEnd")
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
                if (mLoopType == 3 && mLoopTag == 3) {
                    // 移除剩余的信息
                    removeList()
                    // 开始轮询小到大的透明
                    if (mListAnimation.size == 0 && mListBigToSmall.size == 0) {
                        startSmallToBigAlphaLoop()
                    }

                    // 解释的标记
                    mCallBackListener?.onEndBigToSmallAlpha()
                }

                if (mLoopTag != 2) {
                    addPoint()
                }
            }
        })
        mAnimationLoop.start()

        // 用来倒计时的动画
        mAnimationInterval = ValueAnimator()
        mAnimationInterval.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                LogUtil.e("***m: onAnimationStart ")
            }

            override fun onAnimationEnd(animation: Animator?) {
                LogUtil.e("***m: onAnimationEnd ")
                if (mLoopType == 1) {
                    when (mLoopTag) {
                        1 -> {
                            // 开启实心小到大
                            startSmallToBig()
                        }

                        2 -> {
                            // 开启大到小的实心
                            startBigToSmall()
                        }
                    }
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
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
        if (mAnimationLoop.isPaused) {
            mAnimationLoop.resume()
        }

        // 开始计时 ---> 实心小到大
        mAnimationInterval.duration = mDurationAlphaSmallToBig
        mAnimationInterval.setFloatValues(0f, 1f)
        mAnimationInterval.start()

        // 开始播放小到大的开始回调
        mCallBackListener?.onStartSmallToBigAlpha()
    }

    /**
     * 开启小到大的实心圆
     */
    public fun startSmallToBig() {
        mLoopType = 1
        mLoopTag = 2
        if (mAnimationLoop.isPaused) {
            mAnimationLoop.resume()
        }
        addPoint()

        // 开始计时 ---> 实心大到小
        mAnimationInterval.duration = mDurationSmallToBig
        mAnimationInterval.setFloatValues(0f, 1f)
        mAnimationInterval.start()
    }

    /**
     * 开启大到小的实心圆
     */
    public fun startBigToSmall() {
        mLoopType = 2
        mLoopTag = 2
        if (mAnimationLoop.isPaused) {
            mAnimationLoop.resume()
        }
        addPoint()
    }

    /**
     * 大道小的透明
     */
    private fun startBigToSmallAlphaLoop() {
        LogUtil.e("发送小到大的轮询标记！")
        mLoopType = 2
        mLoopTag = 1
    }

    /**
     * 暂停
     */
    public fun pause(isPause: Boolean) {
        if (isPause) {
            // 停止轮询的集合
            mAnimationLoop.pause()

            // 停止所有动画
            mListAnimation.forEach { (t, u) ->
                u.pause()
            }

        } else {
            // 开启轮询的集合
            if (mAnimationLoop.isPaused) {
                mAnimationLoop.resume()
            }
            // 开启所有动画
            mListAnimation.forEach { (t, u) ->
                if (u.isPaused) {
                    u.resume()
                }
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

        // 删除多余的数据
        removeList()
    }

    /**
     * 小到大的实心
     */
    private fun animationSmallToBig(circle: Circle) {
        LogUtil.e("开始了小到大 --->")
        var temp = 0F
        val animator = ValueAnimator.ofFloat(0f, mMaxRadius)
        animator.duration = mDurationSmallToBig
        // 减速插值器
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
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                // 开始播放小到大的开始回调
                mCallBackListener?.onStartSmallToBig()
                LogUtil.e("小到大 -- 开始")
            }

            override fun onAnimationEnd(animation: Animator?) {
                // 开始播放小到大的开始回调
                mCallBackListener?.onEndSmallToBig()
                LogUtil.e("小到大 -- 结束")
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })

        animator.start()

        // 加入集合
        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }
        // 删除多余的数据
        removeList()
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
                temp = animatedValue
            }
            // 刷新布局
            invalidate()
        }
        animator.start()

        // 加入集合
        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }

        // 删除多余的数据
        removeList()
    }

    /**
     * 大到小的透明
     */
    private fun animationBigToSmallLoop(circle: Circle) {
        LogUtil.e("开始了小到大 --->")
        var temp = 0F

        val animator = ValueAnimator.ofFloat(mMaxRadius - mStrokeWidth, 0f)
        animator.duration = 1500
        // 加速度插值器 todo  此处的插值器改用什么
        // animator.interpolator = AccelerateInterpolator()
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float
            val animatedFraction = value.animatedFraction

            if (temp != animatedValue) {
                // 宽度逐渐递减
                circle.radius = animatedValue
                // 透明逐渐变小
                circle.paint?.alpha = mColorValue - (mColorValue * animatedFraction).toInt()
                // 圆圈宽度的递减
                circle.paint?.strokeWidth = mStrokeWidth - (mStrokeWidth * animatedFraction)
                // 刷新布局
                temp = animatedValue
            }
            invalidate()
        }
        animator.start()

        // 加入集合
        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }
        // 删除多余的数据
        removeList()
    }

    /**
     * 移除闲置集合
     */
    private fun removeList() {
        // 删除小到大多余的圆环集合
        val iteratorSmallToBig = mListSmallToBig.iterator()
        while (iteratorSmallToBig.hasNext()) {
            val next = iteratorSmallToBig.next()
            if (next.radius >= mMaxRadius) {
                iteratorSmallToBig.remove()
            }
        }

        // 删除大到小多余的圆环集合
        val iteratorBigToSmall = mListBigToSmall.iterator()
        while (iteratorBigToSmall.hasNext()) {
            val next = iteratorBigToSmall.next()
            val radius = next.radius
            if (radius <= 0) {
                if (mLoopType == 2 && next.tag == 2) {
                    // 在某个阶段的时候，就开始停止轮询
                    mLoopType = 3
                    mLoopTag = 3
                    LogUtil.e("发送轮训小到大的透明！")
                }
                iteratorBigToSmall.remove()
            }
        }

        // 删除多余的动画集合
        val iteratorAnimation = mListAnimation.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (!next.value.isRunning) {
                // 停止动画
                next.value.cancel()
                // 取消监听
                next.value.removeAllUpdateListeners()
                iteratorAnimation.remove()
            }
        }

        LogUtil.e("删除了多余的圆圈 ---> 大到小渐变 size:" + mListBigToSmall.size + " 小到大的size:" + mListSmallToBig.size + "  animation:" + mListAnimation.size)
    }

    public fun clear() {
        mAnimationLoop.pause()
        mAnimationLoop.cancel()
        mAnimationLoop.removeAllUpdateListeners()

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
    }

    private var mCallBackListener: CallBackListener? = null
    public fun setCallBackListener(callBackListener: CallBackListener) {
        this.mCallBackListener = callBackListener
    }

    public interface CallBackListener {

        fun onStartSmallToBigAlpha();
        fun onStartSmallToBig()
        fun onEndSmallToBig()
        fun onEndBigToSmallAlpha()
    }
}
