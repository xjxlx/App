package com.android.app.widget

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import com.android.helper.utils.ConvertUtil
import com.android.helper.utils.LogUtil
import kotlin.math.min

class BreatheView4 @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {

    private val mRectStrokeLine = RectF()
    private val mPaintStrokeLine = Paint()
    private var mCx: Float = 0F
    private var mCY: Float = 0F
    private val mColorValue = 200
    private val mColorMaxValue = 255
    private var mMaxRadius = 0f
    // mLoopType 1:small to big  2：big to small  3:pause
    private var mLoopType = 0
    // mLoopTag 1:alpha-loop  2: solid 3：pause
    private var mLoopTag = 0
    private val mListSmallToBig = mutableListOf<Circle>()
    private val mListBigToSmall = mutableListOf<Circle>()
    private val mStrokeWidth = ConvertUtil.toPx(10f)

    private val mListAnimation = hashMapOf<Circle, ValueAnimator>()
    private var mCurrentStatus = 0
    private var mIsPause = false

    /**
     * ------------------------------
     */
    // todo 间隔多少时间后重新生成一个新的圆圈
    val mDurationUnitInterval = 700L
    // todo 小到大 透明 到 实心的 等待 时间
    val mDurationLoopToSolid = 4000L

    // todo  从小到大实心 到 大到小 实心中间的等待时间
    var mDdurationSmallToBigSolidAwait: Long = 2000L
    // todo 单个 小到大 透明 的单个持续时间
    val mDurationSmallToBigLoop = 4000L
    // todo 单个 小到大 实心 的单个持续时间
    var mDdurationSmallToBigSolid: Long = 2000L
    // todo 单个 大到小 实心 的单个持续时间
    var mDdurationBigToSmallSolid: Long = 2000L
    // todo 单个 大到小 透明 的单个持续时间
    var mDdurationBigToSmallLoop: Long = 2000L

    // todo 大到小透明 等待多久之后，开启大到小的透明圆环
    var mDdurationBigToSmallLoopAwait: Long = 200L

    private val mTimerTask: ValueAnimator by lazy {
        ValueAnimator.ofFloat(0f, 1f)
    }

    private val mTimerTaskListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            LogUtil.e("-----> mTimerTask")
            // small to big ---> solid
            if (mLoopType == 1 && mLoopTag == 1) {
                mLoopType = 1
                mLoopTag = 2
                mLooper.cancel()
                addPoint()
                // small to big solid --->  big to small solid
            } else if (mLoopType == 1 && mLoopTag == 2) {
                mLoopType = 2
                mLoopTag = 2
                addPoint()
                // big to small solid  ---> transparent
            } else if (mLoopType == 2 && mLoopTag == 2) {
                mLoopType = 2
                mLoopTag = 1
                mLooper.start()

            } else if (mLoopType == 2 && mLoopTag == 1) {
                // big to small finish ,cancel animation loop
                mLooper.cancel()
            }

        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }

    private val mLooper: ValueAnimator by lazy {
        ValueAnimator
            .ofFloat(0f, 1f)
            .apply {
                duration = mDurationUnitInterval
                repeatMode = ValueAnimator.RESTART
                repeatCount = ValueAnimator.INFINITE
            }
    }
    private val mLooperListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationRepeat(animation: Animator?) {
            LogUtil.e("-----> mLooper")
            if (mLoopType == 1 || mLoopType == 2) {
                if (mLoopTag == 1) {
                    addPoint()
                }
            }
        }
    }

    init {
        mPaintStrokeLine.color = Color.BLACK
        mPaintStrokeLine.isAntiAlias = true
        mPaintStrokeLine.strokeWidth = 2f
        mPaintStrokeLine.style = Paint.Style.STROKE
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
            LogUtil.e("draw --- " + item.paint!!.alpha)
            canvas?.drawCircle(mCx, mCY, item.radius, item.paint!!)
        }
    }

    @SuppressLint("Recycle")
    private fun addPoint() {
        val circle = Circle()
        val paint = Paint()
        LogUtil.e("--------------------> mLoopType : $mLoopType  mLoopTag: $mLoopTag")

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

                    animationSmallToBigSolid(circle)
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

    /**
     * small to  big  loop
     */
    private fun animationSmallToBigLoop(circle: Circle) {
        val animator = ValueAnimator
            .ofFloat(0f, 1f)
            .apply {
                duration = mDurationSmallToBigLoop
                //  interpolator ---
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val fraction = it.animatedFraction
                    if (fraction < 0.5) {
                        // transparent  ---> solid
                        val ratioAlphaTransparentSolid = getDistance(fraction, 0f, 0.5f, 0f, 0.5f)
                        val toInt = (ratioAlphaTransparentSolid * mColorValue).toInt()
                        circle.paint?.alpha = toInt
                    } else {
                        // solid ---> transparent
                        val ratioBigToSmall = getDistance(fraction, 0.5f, 1f, 0.5f, 0f)
                        circle.paint?.alpha = (ratioBigToSmall * mColorValue).toInt()
                    }
                    // radius : small ---> big
                    val ratioSmallToBig = getDistance(fraction, 0f, 1f, 0f, 1f)
                    circle.radius = ratioSmallToBig * mMaxRadius

                    // stroke : big ---> small
                    val ratioStroke = getDistance(fraction, 0f, 1f, 1f, 0f)
                    circle.paint?.strokeWidth = ratioStroke * mStrokeWidth
                    LogUtil.e("fraction ------>" + fraction + "alpha: " + circle.paint?.alpha)
                    invalidate()
                }

                addListener(onStart = {
                    if (mCurrentStatus != 1) {
                        mCallBackListener?.statusChange(1)
                        mCurrentStatus = 1
                    }
                })
            }
        animator.start()

        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }
    }

    /**
     * small to big solid
     */
    private fun animationSmallToBigSolid(circle: Circle) {
        val animator = ValueAnimator
            .ofFloat(0f, 1f)
            .apply {
                duration = mDdurationSmallToBigSolid
                //  interpolator --
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val fraction = it.animatedFraction
                    val radius = getDistance(fraction, 0f, 1f, 0f, mMaxRadius)
                    circle.radius = radius
                    invalidate()
                }

                addListener(onStart = {
                    if (mCurrentStatus != 2) {
                        mCallBackListener?.statusChange(2)
                        mCurrentStatus = 2
                    }
                }, onEnd = {
                    // await the time after , start big to small solid
                    postDelayed(mDdurationSmallToBigSolidAwait)
                })
            }
        animator.start()

        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }
        removeList()
    }

    /**
     * big to small solid
     */
    private fun animationBigToSmall(circle: Circle) {
        val animator = ValueAnimator
            .ofFloat(0f, 1f)
            .apply {
                duration = mDdurationBigToSmallSolid
                // interpolator ++
//                interpolator = AccelerateInterpolator()
                addUpdateListener {
                    val fraction = it.animatedFraction
                    val radius = getDistance(fraction, 0f, 1f, 1f, 0f)
                    circle.radius = radius * mMaxRadius
                    invalidate()
                }

                addListener(onStart = {
                    if (mCurrentStatus != 3) {
                        mCallBackListener?.statusChange(3)
                        mCurrentStatus = 3
                    }
                    // await the  tim after ,start big to small transparent
                    postDelayed(mDdurationBigToSmallLoopAwait)
                }, onEnd = {
                    // finish big to small loop
                    postDelayed(0)
                })
                start()
            }

        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }
        removeList()
    }

    /**
     * big to small loop transparent
     */
    private fun animationBigToSmallLoop(circle: Circle) {
        val animator = ValueAnimator
            .ofFloat(0f, 1f)
            .apply {
                duration = mDdurationBigToSmallLoop
                //  interpolator --
                interpolator = DecelerateInterpolator()
                addUpdateListener {
                    val fraction = it.animatedFraction
                    val distance = getDistance(fraction, 0f, 1f, 1f, 0f)
                    circle.radius = mMaxRadius * distance
                    circle.paint?.alpha = (mColorValue * distance).toInt()
                    circle.paint?.strokeWidth = mStrokeWidth * distance
                    invalidate()
                }

                addListener(onEnd = {
                    removeList()
                    if (mListBigToSmall.size == 0) {
                        // restart
                        mCallBackListener?.onRestart()
                        smallToBigAlphaLoop()
                    }
                })
                start()
            }

        if (!mListAnimation.containsKey(circle)) {
            mListAnimation[circle] = animator
        }
        removeList()
    }

    /**
     * release resource
     */
    private fun removeList() {
        val iteratorSmallToBig = mListSmallToBig.iterator()
        while (iteratorSmallToBig.hasNext()) {
            val next = iteratorSmallToBig.next()
            if (next.radius >= mMaxRadius || next.paint?.alpha!! <= 0) {
                iteratorSmallToBig.remove()
            }
        }

        val iteratorBigToSmall = mListBigToSmall.iterator()
        while (iteratorBigToSmall.hasNext()) {
            val next = iteratorBigToSmall.next()
            val radius = next.radius
            val alpha = next.paint?.alpha
            LogUtil.e(" big  to  small  ---> radius:$radius  alpha: $alpha")
            if (radius <= 0 || alpha!! <= 0) {
                iteratorBigToSmall.remove()
            }
        }

        val iteratorAnimation = mListAnimation.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (!next.value.isRunning) {
                next.value.removeAllListeners()
                iteratorAnimation.remove()
            }
        }
    }

    fun isPause(): Boolean {
        return mIsPause
    }

    fun pause(isPause: Boolean) {
        if (isPause) {
            mListAnimation.forEach { (t, u) ->
                if (u.isRunning) {
                    u.pause()
                }
            }

            if (mTimerTask.isRunning) {
                mTimerTask.pause()
            }

            if (mLooper.isRunning) {
                mLooper.pause()
            }
            mIsPause = true
        } else {
            mListAnimation.forEach { (t, u) ->
                if (u.isPaused) {
                    u.resume()
                }
            }

            if (mTimerTask.isPaused) {
                mTimerTask.resume()
            }

            if (mLooper.isPaused) {
                mLooper.resume()
            }
            mIsPause = false
        }
    }

    fun clear() {
        mTimerTask.pause()
        mTimerTask.cancel()
        mTimerTask.removeAllListeners()

        mLooper.pause()
        mLooper.cancel()
        mLooper.removeAllListeners()

        val iteratorAnimation = mListAnimation.iterator()
        while (iteratorAnimation.hasNext()) {
            val next = iteratorAnimation.next()
            if (!next.value.isRunning) {
                next.value.pause()
                next.value.cancel()
                next.value.removeAllListeners()
                iteratorAnimation.remove()
            }
        }

        mListSmallToBig.clear()
        mListBigToSmall.clear()
        mLoopType = 0
        mLoopTag = 0
    }

    fun startAnimation() {
        mCallBackListener?.onStart()
        smallToBigAlphaLoop()
    }

    private fun smallToBigAlphaLoop() {
        mListAnimation.clear()
        mListBigToSmall.clear()
        mListSmallToBig.clear()
        clear()

        addPoint()

        // await the time ,start small to big solid  round
        postDelayed(mDurationLoopToSolid)

        mLooper.start()
        if (mLooper.listeners == null || mLooper.listeners.size == 0) {
            mLooper.addListener(mLooperListener)
        }

        mLoopType = 1
        mLoopTag = 1
    }

    private fun postDelayed(durationValue: Long) {
        mTimerTask.apply {
            duration = durationValue
            if (listeners == null || listeners.size <= 0) {
                addListener(mTimerTaskListener)
            }
            start()
        }
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

    private fun getDistance(currentTime: Float, startTime: Float, endTime: Float, startDistance: Float, endDistance: Float): Float {
        var distance = 0f
        val intervalTim = endTime - startTime
        val distanceInterval = endDistance - startDistance
        // v = s / t
        val speed = (distanceInterval / intervalTim)
        // s = t * v
        val currentDistance = (currentTime - endTime) * speed
        distance = endDistance + currentDistance
        // LogUtil.e(" 当前 SSS： $currentTime 最终的缩放值：$distance   isReverse： $isReverse  currentTime: $currentTime")
        return distance
    }

    inner class Circle {
        var paint: Paint? = null
        var radius: Float = 0f
        var tag = 0
        override fun toString(): String {
            return "Point(paint=$paint, radius=$radius)"
        }
    }

}
