package com.android.app.widget

import android.animation.Animator
import android.animation.Animator.AnimatorListener
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
import com.android.helper.utils.ConvertUtil
import com.android.helper.utils.LogUtil
import kotlin.math.min

class BreatheView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val mRectStrokeLine = RectF()
    private val mPaintStrokeLine = Paint()
    private var mCx: Float = 0F
    private var mCY: Float = 0F
    private var mMaxRadius = 0f
    private var mAlphasZoom = 0f
    private var mStrokeWithZoom = 0f
    var durationAlphaSmallToBig: Long = 4000
    var durationSmallToBig: Long = 2000
    var durationBigToSmall: Long = 4000

    // mLoopType 1:small to big  2：big to small  3:pause
    private var mLoopType = 0

    // mLoopTag 1:alpha-loop  2: solid 3：pause
    private var mLoopTag = 0
    private val mListSmallToBig = mutableListOf<Circle>()
    private val mListBigToSmall = mutableListOf<Circle>()
    private val mStrokeWidth = ConvertUtil.toPx(10f)
    private val mColorValue = 150
    private val mColorMaxValue = 255

    private val mListAnimation = hashMapOf<Circle, ValueAnimator>()
    private lateinit var mAnimationLoop: ValueAnimator
    private lateinit var mAnimationInterval: ValueAnimator
    private var mCurrentStatus = 0

    init {
        mPaintStrokeLine.color = Color.BLACK
        mPaintStrokeLine.isAntiAlias = true
        mPaintStrokeLine.strokeWidth = 2f
        mPaintStrokeLine.style = Paint.Style.STROKE

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
                    removeList()
                    if (mListAnimation.size == 0 && mListBigToSmall.size == 0) {
                        mCallBackListener?.onRestart()
                        startSmallToBigAlphaLoop(false)
                    }
                }

                if (mLoopTag != 2) {
                    addPoint()
                }
            }
        })
        mAnimationLoop.start()

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
                            startSmallToBig()
                        }

                        2 -> {
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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            val w = right.minus(left)
            val h = bottom.minus(top)
            mCx = w.div(2F)
            mCY = h.div(2F)
            val size = min(w, h).toFloat()
            mMaxRadius = size.div(2F)
            mAlphasZoom = mColorValue.div(size)
            mStrokeWithZoom = mStrokeWidth.div(size)

            mRectStrokeLine.set(
                w
                    .minus(size)
                    .div(2F)
                    .plus(mPaintStrokeLine.strokeWidth.div(2F)),
                h
                    .minus(size)
                    .div(2F)
                    .plus(mPaintStrokeLine.strokeWidth.div(2F)),
                w
                    .plus(size)
                    .div(2F)
                    .minus(mPaintStrokeLine.strokeWidth.div(2F)),
                h
                    .plus(size)
                    .div(2F)
                    .minus(mPaintStrokeLine.strokeWidth.div(2F)),
            )
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let { it ->
            // it.drawRect(mRectStrokeLine, mPaintStrokeLine)

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

    private fun startSmallToBigAlphaLoop(restart: Boolean) {
        mLoopType = 1
        mLoopTag = 1

        if (restart) {
            mListAnimation.clear()
            mListBigToSmall.clear()
            mListSmallToBig.clear()
        }

        if (mAnimationLoop.isPaused) {
            mAnimationLoop.resume()
        }

        mCallBackListener?.onStart()

        mAnimationInterval.duration = durationAlphaSmallToBig
        mAnimationInterval.setFloatValues(0f, 1f)
        mAnimationInterval.start()
    }

    private fun startSmallToBig() {
        mLoopType = 1
        mLoopTag = 2
        if (mAnimationLoop.isPaused) {
            mAnimationLoop.resume()
        }
        addPoint()

        mAnimationInterval.duration = durationSmallToBig
        mAnimationInterval.setFloatValues(0f, 1f)
        mAnimationInterval.start()
    }

    private fun startBigToSmall() {
        mLoopType = 2
        mLoopTag = 2
        if (mAnimationLoop.isPaused) {
            mAnimationLoop.resume()
        }
        addPoint()
    }

    private fun startBigToSmallAlphaLoop() {
        LogUtil.e("发送小到大的轮询标记！")
        mLoopType = 2
        mLoopTag = 1
    }

    fun pause(isPause: Boolean) {
        if (isPause) {
            mAnimationInterval.pause()

            mAnimationLoop.pause()
            mListAnimation.forEach { (t, u) ->
                u.pause()
            }
        } else {
            if (mAnimationLoop.isPaused) {
                mAnimationLoop.resume()
            }

            if (mAnimationInterval.isPaused) {
                mAnimationInterval.resume()
            }

            mListAnimation.forEach { (t, u) ->
                if (u.isPaused) {
                    u.resume()
                }
            }
        }
    }

    private fun addPoint() {
        val circle = Circle()
        circle.tag = mLoopTag
        val paint = Paint()

        when (mLoopType) {
            1 -> {
                if (mLoopTag == 1) {
                    paint.color = Color.WHITE
                    paint.strokeWidth = mStrokeWidth
                    paint.style = Paint.Style.STROKE
                    paint.alpha = mColorValue

                    circle.paint = paint
                    circle.radius = 0f
                    mListSmallToBig.add(circle)

                    animationSmallToBigLoop(circle)
                } else if (mLoopTag == 2) {
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

            2 -> {
                if (mLoopTag == 2) {
                    paint.color = Color.WHITE
                    paint.style = Paint.Style.FILL
                    paint.alpha = mColorMaxValue
                    paint.strokeWidth = mStrokeWidth
                    circle.paint = paint
                    circle.radius = mMaxRadius

                    mListBigToSmall.add(circle)
                    animationBigToSmall(circle)
                } else if (mLoopTag == 1) {

                    paint.color = Color.WHITE
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = mStrokeWidth
                    paint.alpha = mColorValue
                    circle.paint = paint
                    circle.radius = mMaxRadius

                    mListBigToSmall.add(circle)
                    animationBigToSmallLoop(circle)
                }
            }
        }
    }

    private fun animationSmallToBigLoop(circle: Circle) {
        var temp = 0F
        val animator = ValueAnimator.ofFloat(0f, mMaxRadius)
        animator.duration = durationAlphaSmallToBig
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float
            if (temp != animatedValue) {
                circle.radius = animatedValue
                circle.paint?.alpha = (mColorValue - circle.radius * mAlphasZoom).toInt()
                circle.paint?.strokeWidth = mStrokeWidth - mStrokeWithZoom * circle.radius
                invalidate()
                temp = animatedValue
            }
        }
        animator.addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                if (mCurrentStatus != 1) {
                    mCallBackListener?.statusChange(1)
                    mCurrentStatus = 1
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        animator.start()

        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }

        removeList()
    }

    private fun animationSmallToBig(circle: Circle) {
        var temp = 0F
        val animator = ValueAnimator.ofFloat(0f, mMaxRadius)
        animator.duration = durationSmallToBig
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float

            if (temp != animatedValue) {
                circle.radius = animatedValue
                invalidate()
                temp = animatedValue
            }
        }

        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                LogUtil.e("小到大 -- 开始")
                if (mCurrentStatus != 2) {
                    mCallBackListener?.statusChange(2)
                    mCurrentStatus = 2
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
                LogUtil.e("小到大 -- 结束")
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })

        animator.start()

        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }
        removeList()
    }

    private fun animationBigToSmall(circle: Circle) {
        LogUtil.e("开始了小到大 --->")
        var temp = 0F
        var isSend = false

        val range = mMaxRadius * 0.8f

        val animator = ValueAnimator.ofFloat(mMaxRadius, 0f)
        animator.duration = durationBigToSmall
        animator.interpolator = AccelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float
            if (temp != animatedValue) {
                circle.radius = animatedValue
                if (animatedValue < range && !isSend) {
                    isSend = true
                    LogUtil.e("⭐️⭐️⭐️ ---> 大到小 实心 ---> 发送轮巡")
                    startBigToSmallAlphaLoop()
                }
                temp = animatedValue
            }
            invalidate()
        }
        animator.addListener(object : AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                if (mCurrentStatus != 3) {
                    mCallBackListener?.statusChange(3)
                    mCurrentStatus = 3
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })

        animator.start()

        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }

        removeList()
    }

    private fun animationBigToSmallLoop(circle: Circle) {
        LogUtil.e("开始了小到大 --->")
        var temp = 0F

        val animator = ValueAnimator.ofFloat(mMaxRadius - mStrokeWidth, 0f)
        animator.duration = 1500
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener { value ->
            val animatedValue = value.animatedValue as Float
            val animatedFraction = value.animatedFraction

            if (temp != animatedValue) {
                circle.radius = animatedValue
                circle.paint?.alpha = mColorValue - (mColorValue * animatedFraction).toInt()
                circle.paint?.strokeWidth = mStrokeWidth - (mStrokeWidth * animatedFraction)
                temp = animatedValue
            }
            invalidate()
        }
        animator.start()

        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }
        removeList()
    }

    private fun removeList() {
        val iteratorSmallToBig = mListSmallToBig.iterator()
        while (iteratorSmallToBig.hasNext()) {
            val next = iteratorSmallToBig.next()
            if (next.radius >= mMaxRadius) {
                iteratorSmallToBig.remove()
            }
        }

        val iteratorBigToSmall = mListBigToSmall.iterator()
        while (iteratorBigToSmall.hasNext()) {
            val next = iteratorBigToSmall.next()
            val radius = next.radius
            if (radius <= 0) {
                if (mLoopType == 2 && next.tag == 2) {
                    mLoopType = 3
                    mLoopTag = 3
                    LogUtil.e("发送轮训小到大的透明！")
                }
                iteratorBigToSmall.remove()
            }
        }

        val iteratorAnimation = mListAnimation.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (!next.value.isRunning) {
                next.value.removeAllUpdateListeners()
                iteratorAnimation.remove()
            }
        }

        LogUtil.e("删除了多余的圆圈 ---> 大到小渐变 size:" + mListBigToSmall.size + " 小到大的size:" + mListSmallToBig.size + "  animation:" + mListAnimation.size)
    }

    fun clear() {
        mAnimationLoop.pause()
        mAnimationLoop.cancel()
        mAnimationLoop.removeAllUpdateListeners()

        mAnimationInterval.pause()
        mAnimationInterval.cancel()
        mAnimationInterval.removeAllUpdateListeners()

        mListSmallToBig.clear()
        mListBigToSmall.clear()

        val iteratorAnimation = mListAnimation.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (!next.value.isRunning) {
                next.value.removeAllUpdateListeners()
                iteratorAnimation.remove()
                LogUtil.e("删除了多余的动画 ---> 大到大渐变  size:" + mListAnimation.size)
            }
        }
    }

    fun startAnimation() {
        mCallBackListener?.onStart()
        startSmallToBigAlphaLoop(true)
    }

    private var mCallBackListener: CallBackListener? = null
    fun setCallBackListener(callBackListener: CallBackListener) {
        this.mCallBackListener = callBackListener
    }

    interface CallBackListener {
        /**
         * animation - status
         */
        fun onStart()

        /**
         * animation - restart
         */
        fun onRestart()

        /**
         * status  - change
         */
        fun statusChange(status: Int)
    }
}
