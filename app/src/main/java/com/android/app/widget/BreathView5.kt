package com.android.app.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.addListener
import com.android.helper.utils.ConvertUtil
import com.android.helper.utils.GradientUtil
import com.android.helper.utils.LogUtil
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @author : 流星
 * @CreateDate: 2023/2/9-15:06
 * @Description:
 */
class BreathView5 @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attributeSet, defStyleAttr) {

    private var mCx: Float = 0F
    private var mCY: Float = 0F
    private var mMaxRadius = 0f
    private val mListCircle = mutableListOf<Circle>()
    private val mListAnimation = mutableListOf<ValueAnimator>()
    private var startTimeInterval = 0L

    companion object {
        //<editor-fold desc="1：Breath  common ">

        /**
         *  paint common  stroke width
         */
        private val BREATH_COMMON_STROKE_WIDTH = ConvertUtil.toPx(20f)

        /**
         * circle loop --- alpha max value
         */
        private const val BREATH_COMMON_ALPHA_LOOP_VALUE = 255F * 0.5F

        /**
         * circle solid --- alpha max value
         */
        private const val BREATH_COMMON_ALPHA_SOLID_VALUE = 255F * 0.9F

        /**
         * breath finish loop interval duration
         */
        private val BREATH_COMMON_FINISH_LOOP_INTERVAL = 0L

        //</editor-fold>

        //<editor-fold desc="2：Breath  in ">
        /***************************** breath in start *************************************/
        /**
         *【 breath in 】  all duration
         */
        var BREATH_IN_ALL_DURATION = 7000L

        /**
         *【 breath in 】 all  count
         */
        private const val BREATH_IN_COUNT = 9

        /**
         * 【 breath in 】single  circle duration
         */
        private const val BREATH_IN_SINGLE_DURATION = 2000L

        /**
         *【 breath in 】 single circle duration occupy all duration ratio  =【 2000F / BREATH_IN_ALL_DURATION 】
         */
        private val BREATH_IN_SINGLE_DURATION_RATIO = getFloatValue(BREATH_IN_SINGLE_DURATION.toFloat() / BREATH_IN_ALL_DURATION)

        /**
         *【 breath in 】single circle interval duration = ( all duration - single duration ) / count
         *  t = s * v  t = interval * (count -1) + single duration
         *  v = s / t
         *  interval  = all duration - single duration / count -1
         */
        private val BREATH_IN_SINGLE_INTERVAL_DURATION = getFloatValue((BREATH_IN_ALL_DURATION - (BREATH_IN_SINGLE_DURATION_RATIO * BREATH_IN_ALL_DURATION)) / (BREATH_IN_COUNT - 1))

        /**
         *【 breath in 】 single circle interval duration occupy all duration ratio = single duration / all duration
         */
        private val BREATH_IN_SINGLE_INTERVAL_DURATION_RATIO = getFloatValue(BREATH_IN_SINGLE_INTERVAL_DURATION / BREATH_IN_ALL_DURATION)

        /**
         *【 breath in 】 single circle  alpha  transparent to opaque occupy all duration ratio = 880F / 2000
         */
        private const val BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO = 0.44F

        /**
         *【 breath in 】single circle alpha opaque to transparent occupy all duration ratio = ( 920 + 880 ) / 2000
         */
        private const val BREATH_IN_SINGLE_ALPHA_OPAQUE_TO_TRANSPARENT_RATIO = 0.9F

        /**
         *【 breath in 】single circle duration occupy all duration ratio = 1800L / 2000
         */
        private const val BREATH_IN_SINGLE_SCALE_RATIO = 0.9F
        /***************************** breath in end *************************************/
        //</editor-fold>

        //<editor-fold desc="3：Breath  hold ">
        /***************************** breath hold start *************************************/
        /**
         *【 breath hold 】 small to big duration
         */
        private const val BREATH_HOLD_DURATION = 2000L

        /**
         *【 breath hold 】breath hold wait duration
         */
        private const val BREATH_HOLD_WAIT_DURATION = 1000L

        /***************************** breath hold end *************************************/
        //</editor-fold>

        //<editor-fold desc="4：Breath  out ">
        /***************************** breath out start *************************************/

        /**
         *【 breath out 】breath out all duration
         */
        private var BREATH_OUT_ALL_DURATION = 4000L

        /**
         * 【 breath out 】breath count
         */
        private const val BREATH_OUT_COUNT = 7

        /**
         *【 breath out 】breath out sold duration occupy all duration ration
         */
        private const val BREATH_OUT_SOLID_RATIO = 0.9F

        /**
         *【 breath out 】breath out loop single duration  1000F / BREATH_OUT_ALL_DURATION
         */
        private const val BREATH_OUT_LOOP_RATIO = 0.25F

        /**
         *【 breath out 】breath out single interval duration
         *  s = v * t
         *  v =  all duration  -  single duration / count
         */
        private var BREATH_OUT_SINGLE_INTERVAL_DURATION = getFloatValue((BREATH_OUT_ALL_DURATION - (BREATH_OUT_ALL_DURATION * BREATH_OUT_LOOP_RATIO)) / BREATH_OUT_COUNT)

        /**
         *【 breath out 】breath out single interval duration ratio
         */
        private var BREATH_OUT_SINGLE_INTERVAL_DURATION_RATIO = getFloatValue(BREATH_OUT_SINGLE_INTERVAL_DURATION / BREATH_OUT_ALL_DURATION)

        /**
         *【 breath out 】breath out loop single alpha end time ratio
         */
        private const val BREATH_OUT_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO = 0.48F

        /***************************** breath out end *************************************/
        //</editor-fold>

        private fun getFloatValue(float: Float): Float {
            return if (float > 0) {
                "%.4f"
                    .format(float)
                    .toFloat()
            } else {
                0F
            }
        }
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
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            for (item in mListCircle) {
                it.drawCircle(mCx, mCY, item.radius, item.paint)
            }
        }
    }

    private fun getCircle(type: Int, tag: Int): Circle {
        if (type == 1) {
            if (tag == 1) {
                val circleLoop = Circle()
                circleLoop.radius = 0F
                circleLoop.paint.alpha = 0
                circleLoop.paint.strokeWidth = BREATH_COMMON_STROKE_WIDTH
                circleLoop.paint.color = Color.WHITE
                circleLoop.paint.style = Paint.Style.STROKE
                circleLoop.type = 1

                mListCircle.add(circleLoop)
                return circleLoop
            } else if (tag == 2) {

                val circleSolid = Circle()
                circleSolid.radius = 0F
                circleSolid.paint.alpha = BREATH_COMMON_ALPHA_SOLID_VALUE.toInt()
                circleSolid.paint.color = Color.WHITE
                circleSolid.paint.style = Paint.Style.FILL
                circleSolid.type = 2

                mListCircle.add(circleSolid)
                return circleSolid
            }

        } else if (type == 2) {
            if (tag == 1) {
                val circleLoop = Circle()
                circleLoop.radius = mMaxRadius
                circleLoop.paint.color = Color.WHITE
                circleLoop.paint.strokeWidth = BREATH_COMMON_STROKE_WIDTH
                circleLoop.paint.style = Paint.Style.STROKE
                circleLoop.paint.alpha = BREATH_COMMON_ALPHA_LOOP_VALUE.toInt()
                circleLoop.type = 3

                mListCircle.add(circleLoop)
                return circleLoop

            } else if (tag == 2) {
                val circleSolid = Circle()
                circleSolid.radius = mMaxRadius
                circleSolid.paint.alpha = BREATH_COMMON_ALPHA_SOLID_VALUE.toInt()
                circleSolid.paint.color = Color.WHITE
                circleSolid.paint.style = Paint.Style.FILL
                circleSolid.type = 4

                mListCircle.add(circleSolid)
                return circleSolid
            }
        }
        return Circle()
    }

    private fun removeList() {
        val iterator = mListCircle.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            when (next.type) {
                1 -> {
                    if (next.paint.strokeWidth <= 0F) {
                        iterator.remove()
                    }
                }
                2 -> {
                    // sold  small to big
                    if (next.radius >= mMaxRadius) {
                        iterator.remove()
                    }
                }

                3, 4 -> {
                    if (next.radius <= 0) {
                        iterator.remove()
                    }
                }
            }
        }
        LogUtil.e("----> remove list: " + mListCircle.size)

        val it = mListAnimation.iterator()
        while (it.hasNext()) {
            val next = it.next()
            if (!next.isRunning) {
                it.remove()
            }
        }
        LogUtil.e("----> remove animation list: " + mListCircle.size)
    }

    fun startAnimation() {
        startTimeInterval = 0
        val size = mListAnimation.size
        if (size <= 0) {
            LogUtil.e("startAnimation ---> start")
            animationSmallToBig()
        } else {
            LogUtil.e("startAnimation ---> running ....")
        }
    }

    /**
     * small  to big
     */
    private fun animationSmallToBig() {
        var temp = 0F
        LogUtil.e("animationSmallToBig --->")
        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_IN_ALL_DURATION
        var ratioCount = 0
        var startSolid = true

        startTimeInterval = System.currentTimeMillis()

        LogUtil.e("--->>>animationSmallToBig ---> BREATH_IN_ALL_DURATION: $duration")
        var lastTime = 0F

        animator.duration = duration
        animator.interpolator = DecelerateInterpolator(0.6F)
        animator.addUpdateListener {
            val fraction = it.animatedFraction
            if (temp != fraction) {
                // loop
                val tempCount = (fraction / BREATH_IN_SINGLE_INTERVAL_DURATION_RATIO).roundToInt()
                if ((ratioCount != tempCount) && (ratioCount < BREATH_IN_COUNT)) {
                    val startTime = getFloatValue(BREATH_IN_SINGLE_INTERVAL_DURATION_RATIO * ratioCount)
                    val endTime = startTime + BREATH_IN_SINGLE_DURATION_RATIO
                    lastTime = startTime
                    ratioCount = tempCount
                    animationSmallToBigLoop(startTime, endTime, ratioCount)
                    LogUtil.e("animationSmallToBig --->--->---> loop ---> ratioCount: $ratioCount   startTime：  $startTime  duration:starTime  ${startTime * BREATH_IN_ALL_DURATION}   endTime: $endTime  durationEndTime: ${endTime * BREATH_IN_ALL_DURATION}")
                }
                /**
                 * sold circle
                 * startTime = last circle startTime + single circle interval ratio
                 */
                if (ratioCount == BREATH_IN_COUNT) {
                    val alpha = BREATH_IN_SINGLE_INTERVAL_DURATION_RATIO

                    val scaleTime = lastTime + alpha
                    LogUtil.e("animationSmallToBig --->  lastTime:$lastTime  alpha:$alpha  scaleTime:$scaleTime")

                    LogUtil.e("animationSmallToBig --->  interval ---->>:${lastTime.times(BREATH_IN_ALL_DURATION)}" + "------->>>>L>${(scaleTime.times(BREATH_IN_ALL_DURATION))}")

                    if (fraction >= scaleTime && startSolid) {
                        startSolid = false
                        animationSmallToBigSolid(ratioCount)
                    }
                }
            }

            invalidate()
            temp = fraction
        }
        animator.addListener(onStart = {

        }, onEnd = {
            LogUtil.e("animationSmallToBig -----> all ${(System.currentTimeMillis() - startTimeInterval)} ")
        })
        animator.start()
        mListAnimation.add(animator)
        removeList()
    }

    /**
     * small to  big loop
     */
    private fun animationSmallToBigLoop(startTimeFrom: Float, endTimeFrom: Float, ratioCount: Int) {
        LogUtil.e("animationSmallToBigLoop --->   startTime: $startTimeFrom endTime: $endTimeFrom")
        val circleLoop = getCircle(1, 1)

        LogUtil.e("animationSmallToBigLoop  ---> ratioCount---<<<--->>>: $ratioCount ---> $circleLoop")

        val animator = ValueAnimator.ofFloat(startTimeFrom, endTimeFrom)
        val duration = (BREATH_IN_SINGLE_DURATION_RATIO * BREATH_IN_ALL_DURATION).toLong()
        LogUtil.e("--->>>animationSmallToBigLoop ---> BREATH_OUT_ALL_DURATION: $duration")
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            val fraction = it.animatedFraction

            // alpha  0 ---> 50 %
            if (fraction <= BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO) {
                val distanceAlpha = GradientUtil.getDistance(fraction, 0F, BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO, 0F, BREATH_COMMON_ALPHA_LOOP_VALUE, false)
                val alpha = distanceAlpha.toInt()
                circleLoop.paint.alpha = alpha
                LogUtil.e("animationSmallToBigLoop ---> alpha from ---$ratioCount---> $alpha  ")
            }

            // alpha 50% --- 0
            if (fraction > BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO && fraction <= BREATH_IN_SINGLE_ALPHA_OPAQUE_TO_TRANSPARENT_RATIO) {
                val distanceAlpha = GradientUtil.getDistance(fraction, BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO, BREATH_IN_SINGLE_ALPHA_OPAQUE_TO_TRANSPARENT_RATIO, BREATH_COMMON_ALPHA_LOOP_VALUE, 0F, false)
                val alpha = distanceAlpha.toInt()
                circleLoop.paint.alpha = alpha
                LogUtil.e("animationSmallToBigLoop ---> alpha to <---$ratioCount--- $alpha ")
            }

            // strokeWidth 0 ---> 1
            val strokeWidth = GradientUtil.getDistance(fraction, 0F, 1F, BREATH_COMMON_STROKE_WIDTH, 0F, false)
            circleLoop.paint.strokeWidth = strokeWidth

            LogUtil.e("animationSmallToBigLoop ---> strokeWidth ---$ratioCount---> $strokeWidth  ")

            // radius
            if (fraction <= BREATH_IN_SINGLE_SCALE_RATIO) {
                val distanceRadio = GradientUtil.getDistance(fraction, 0F, BREATH_IN_SINGLE_SCALE_RATIO, 0F, mMaxRadius, false)
                LogUtil.e("animationSmallToBigLoop ---> distanceRadio ---$ratioCount---> $distanceRadio ")
                circleLoop.radius = distanceRadio
            }
        }
        animator.addListener(onStart = {
            LogUtil.e("animationSmallToBig -----> loop ---> start count: " + (ratioCount) + "  interval " + (System.currentTimeMillis() - startTimeInterval))
        }, onEnd = {
            LogUtil.e("animationSmallToBig -----> loop ---> end count: " + (ratioCount) + "  interval " + (System.currentTimeMillis() - startTimeInterval))
        })
        animator.start()

        mListAnimation.add(animator)
        removeList()
        LogUtil.e("----> remove list: --- end ---> " + mListCircle.size)
    }

    /**
     * small to big solid
     */
    private fun animationSmallToBigSolid(count: Int) {
        LogUtil.e("animationSmallToBigSolid --->   startTime:  $count")
        val circleSolid = getCircle(1, 2)

        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_HOLD_DURATION
        LogUtil.e("--->>>animationSmallToBigSolid ---> BREATH_OUT_ALL_DURATION: $duration")
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            // radius
            val distanceRadio = GradientUtil.getDistance(it.animatedFraction, 0F, 1F, 0F, mMaxRadius, false)
            LogUtil.e("animationSmallToBigSolid ---> mMaxRadius: $mMaxRadius  distanceRadio: $distanceRadio")
            circleSolid.radius = distanceRadio
        }

        animator.addListener(onStart = {
            LogUtil.e("animationSmallToBig -----> solid: start: count: --->>>>>>>> " + count + "ssssssssssssssss: " + (System.currentTimeMillis() - startTimeInterval))
        }, onEnd = {
            LogUtil.e("animationSmallToBig -----> solid: end:" + (System.currentTimeMillis() - startTimeInterval))

            // breath hold wait
            animationHoldBreathWait()
        })
        animator.start()
        mListAnimation.add(animator)
    }

    /**
     * breath hold wait
     */
    private fun animationHoldBreathWait() {
        LogUtil.e("animationHoldBreathWait --->   duration:  $BREATH_HOLD_WAIT_DURATION")
        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_HOLD_WAIT_DURATION
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addListener(onEnd = {
            // breath out start
            animationBigToSmall()
        })
        animator.start()
        mListAnimation.add(animator)
    }

    /**
     *  big  to small
     */
    private fun animationBigToSmall() {
        var temp = 0F
        LogUtil.e("animationBigToSmallSolid --->")
        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_OUT_ALL_DURATION

        val circleSolid = getCircle(2, 2)
        var ratioCount = 0

        LogUtil.e("--->>>animationBigToSmallSolid ---> BREATH_OUT_ALL_DURATION: $duration")
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            val fraction = it.animatedFraction
            if (temp != fraction) {

                // solid
                if (fraction <= BREATH_OUT_SOLID_RATIO) {
                    // radius
                    val distanceRadio = GradientUtil.getDistance(fraction, 0F, BREATH_OUT_SOLID_RATIO, mMaxRadius, 0F, false)
                    LogUtil.e("animationBigToSmallSolid ---> mMaxRadius: $mMaxRadius  distanceRadio: $distanceRadio")
                    circleSolid.radius = distanceRadio

                    // alpha
                    val alphaStartTime = 0.8F
                    if (fraction in alphaStartTime..BREATH_OUT_SOLID_RATIO) {
                        val distanceAlpha = GradientUtil.getDistance(fraction, alphaStartTime, BREATH_OUT_SOLID_RATIO, BREATH_COMMON_ALPHA_SOLID_VALUE, 0F, false)
                        LogUtil.e("animationBigToSmallSolid ---> distanceAlpha: $distanceAlpha ")
                        circleSolid.paint.alpha = distanceAlpha.roundToInt()
                    }
                }

                // loop
                if (fraction >= BREATH_OUT_SINGLE_INTERVAL_DURATION_RATIO && fraction <= (1F - BREATH_OUT_LOOP_RATIO)) {
                    val tempCount = (fraction / BREATH_OUT_SINGLE_INTERVAL_DURATION_RATIO).roundToInt()
                    if (ratioCount != tempCount) {
                        ratioCount = tempCount

                        // startTime =  BREATH_OUT_SINGLE_INTERVAL_RATIO * ratio
                        val startTime = BREATH_OUT_SINGLE_INTERVAL_DURATION_RATIO * ratioCount
                        // endTime = startTime +  BREATH_OUT_LOOP_RATIO
                        val endTime = startTime + BREATH_OUT_LOOP_RATIO

                        LogUtil.e("ratioCount: $ratioCount startTime: $startTime  endTime: $endTime")
                        animationBigToSmallLoop(startTime, endTime, ratioCount)
                    }
                }
            }
            temp = fraction
            invalidate()
        }

        animator.addListener(onEnd = {
            // add  big to small loop
            if (BREATH_COMMON_FINISH_LOOP_INTERVAL > 0) {
                //  if breath finish loop wait > 0 ,execute wait animation
                animationFinishLoopWait()
            } else {
                // if breath finish loop interval > 0 , restart animation
                animationSmallToBig()
            }
        })

        animator.start()
        mListAnimation.add(animator)
        removeList()
    }

    /**
     * big to small loop
     */
    private fun animationBigToSmallLoop(startTime: Float, endTime: Float, ratioCount: Int) {
        LogUtil.e("animationBigToSmallLoop --->   startTime: $startTime endTime: $endTime")

        val circleLoop = getCircle(2, 1)

        val animator = ValueAnimator.ofFloat(startTime, endTime)
        val duration = (BREATH_OUT_LOOP_RATIO * BREATH_OUT_ALL_DURATION).toLong()
        LogUtil.e("--->>>animationBigToSmallLoop ---> BREATH_OUT_ALL_DURATION: $duration")
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            val fraction = it.animatedFraction

            // alpha 0 ---> 50%
            if (fraction <= BREATH_OUT_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO) {
                val startAlpha = GradientUtil.getDistance(fraction, 0F, BREATH_OUT_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO, 0F, BREATH_COMMON_ALPHA_LOOP_VALUE, false)
                LogUtil.e("animationBigToSmallLoop ---> startAlpha  ratioCount$ratioCount   fraction:$fraction   distanceAlpha: $startAlpha")
                circleLoop.paint.alpha = startAlpha.toInt()
            }

            // alpha 50% ---> 0
            if (fraction > BREATH_OUT_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO) {
                val endAlpha = GradientUtil.getDistance(fraction, BREATH_OUT_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO, 1F, BREATH_COMMON_ALPHA_LOOP_VALUE, 0F, false)
                LogUtil.e("animationBigToSmallLoop ---> endAlpha  ratioCount$ratioCount  fraction:$fraction  distanceAlpha: $endAlpha")
                circleLoop.paint.alpha = endAlpha.toInt()
            }

            // stroke width
            val distanceStrokeWidth = GradientUtil.getDistance(fraction, 0F, 1F, BREATH_COMMON_STROKE_WIDTH, 0F, false)
            circleLoop.paint.strokeWidth = distanceStrokeWidth
            LogUtil.e("animationBigToSmallLoop ---> distanceStrokeWidth  ratioCount$ratioCount fraction:$fraction  distanceStrokeWidth: $distanceStrokeWidth")

            // radius
            val distanceRadius = GradientUtil.getDistance(fraction, 0F, 1F, mMaxRadius, 0F, false)
            circleLoop.radius = distanceRadius
            LogUtil.e("animationBigToSmallLoop ---> distanceRadius ratioCount  $ratioCount  fraction:$fraction  distanceRadius: $distanceRadius")
        }
        animator.start()

        mListAnimation.add(animator)
        removeList()
    }

    /**
     * loop animation wait
     * if finish wait time > 0 , execute wait animation
     */
    private fun animationFinishLoopWait() {
        LogUtil.e("animationFinishLoopWait --->   duration:  $BREATH_COMMON_FINISH_LOOP_INTERVAL")
        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_COMMON_FINISH_LOOP_INTERVAL
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addListener(onEnd = {
            // breath out start
            animationSmallToBig()
        })
        animator.start()
        mListAnimation.add(animator)
    }

    fun pause() {
        mListAnimation.forEach {
            it.pause()
        }
    }

    fun resume() {
        mListAnimation.forEach {
            if (it.isPaused) {
                it.resume()
            }
        }
    }

    fun stop() {
        val iterator = mListAnimation.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            next.pause()
            next.removeAllListeners()
            next.cancel()
            iterator.remove()
        }

        mListCircle.clear()
    }

    fun clear() {
        mListCircle.clear()
        mListAnimation.clear()
        invalidate()
    }

    class Circle {
        var paint: Paint = Paint()
        var radius: Float = 0f
        var type: Int = 1

        override fun toString(): String {
            return "Circle(paint=$paint, radius=$radius, type=$type)"
        }

    }
}