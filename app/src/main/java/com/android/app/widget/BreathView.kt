package com.android.app.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import com.android.common.utils.LogUtil
import com.android.helper.utils.ConvertUtil
import com.android.helper.utils.GradientUtil
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * @author : 流星
 * @CreateDate: 2023/2/9-15:06
 * @Description:
 */
class BreathView
@JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attributeSet, defStyleAttr) {

    private var mCx: Float = 0F
    private var mCY: Float = 0F
    private var mMaxRadius = 0f
    private val mListCircle = mutableListOf<Circle>()
    private val mListAnimation = mutableListOf<ValueAnimator>()
    private var startTimeInterval = 0L
    private var mCurrentStatus = 0
    private var mCallBackListener: CallBackListener? = null

    companion object {
        // <editor-fold desc="1：Breath  common ">
        var RATIO: Float = 1F
            get() {
                return if (field > 0) {
                    field
                } else {
                    1F
                }
            }

        /** paint common stroke width */
        private val BREATH_COMMON_STROKE_WIDTH = ConvertUtil.toPx(8F)

        /** circle loop --- alpha max value */
        private const val BREATH_COMMON_ALPHA_LOOP_VALUE = 255F * 0.5F

        /** circle solid --- alpha max value */
        private const val BREATH_COMMON_ALPHA_SOLID_VALUE = 255F * 0.9F

        private val BREATH_COMMON_COLOR_VALUE: Int = Color.parseColor("#E6FFFFFF")

        /** breath finish loop interval duration */
        var BREATH_FINISH_LOOP_INTERVAL = 0L
            get() {
                return if (field > 0) {
                    field
                } else {
                    0L
                }
            }

        // </editor-fold>

        // <editor-fold desc="2：Breath  in ">
        /** *************************** breath in start */
        var BREATH_IN_ALL_DURATION: Long = 0
            get() {
                return if (field > 0) {
                    (field * RATIO).toLong()
                } else {
                    (6000L * RATIO).toLong()
                }
            }

        /** 【 breath in 】single circle duration */
        // private const val BREATH_IN_SINGLE_DURATION = 2000L
        private val BREATH_IN_SINGLE_DURATION: Long
            get() {
                return (0.3333F * BREATH_IN_ALL_DURATION).toLong()
            }

        /** 【 breath in 】 all count */
        private const val BREATH_IN_COUNT = 9

        /** 【 breath in 】single circle interval duration = single duration * 0.66F */
        private val BREATH_IN_SINGLE_INTERVAL_DURATION: Long
            get() {
                return (BREATH_IN_SINGLE_DURATION.toFloat() * 0.25F).toLong()
            }

        /** 【 breath in 】 all duration = single duration ratio * ( count - 1 ) + single duration */
        // var BREATH_IN_ALL_DURATION = ((BREATH_IN_SINGLE_INTERVAL_DURATION * (BREATH_IN_COUNT - 1)) +
        // BREATH_IN_SINGLE_DURATION).toLong()

        /** 【 breath in 】 single circle duration ratio = single duration / all duration */
        private val BREATH_IN_SINGLE_DURATION_RATIO: Float
            get() {
                return BREATH_IN_SINGLE_DURATION.toFloat() / BREATH_IN_ALL_DURATION
            }

        /**
         * 【 breath in 】 single circle interval duration occupy all duration ratio = single duration /
         * all duration
         */
        private val BREATH_IN_SINGLE_INTERVAL_DURATION_RATIO: Float
            get() {
                return BREATH_IN_SINGLE_INTERVAL_DURATION.toFloat() / BREATH_IN_ALL_DURATION
            }

        /**
         * 【 breath in 】 single circle alpha transparent to opaque occupy all duration ratio = 880F /
         * 2000
         */
        private const val BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO = 0.44F

        /**
         * 【 breath in 】single circle alpha opaque to transparent occupy all duration ratio = ( 920 +
         * 880 ) / 2000
         */
        private const val BREATH_IN_SINGLE_ALPHA_OPAQUE_TO_TRANSPARENT_RATIO = 0.9F

        /** 【 breath in 】single circle duration occupy all duration ratio = 1800L / 2000 */
        private const val BREATH_IN_SINGLE_SCALE_RATIO = 0.9F

        /** 【 breath in 】sold start time ratio */
        private val BREATH_IN_SOLD_START_TIME_RATIO: Float
            get() {
                // all duration ratio  - single duration ratio
                val intervalAllRatio = 1F - BREATH_IN_SINGLE_DURATION_RATIO
                // single interval ratio
                val intervalRatio: Float = intervalAllRatio / (BREATH_IN_COUNT - 1)

                // sold start time  = interval ratio * ( count -1 ) + interval /2
                return (intervalRatio * (BREATH_IN_COUNT - 1)) + (intervalRatio)
            }

        /** 【 breath in 】breath in all real duration = small to big sold start ratio * all duration */
        val BREATH_IN_ALL_REAL_DURATION: Long
            get() {
                return (BREATH_IN_SOLD_START_TIME_RATIO * BREATH_IN_ALL_DURATION).toLong()
            }

        /** *************************** breath in end */
        // </editor-fold>

        // <editor-fold desc="3：Breath  hold ">
        /** *************************** breath hold start */
        /** 【 breath hold 】 small to big duration */
        var BREATH_HOLD_ALL_DURATION: Long = 0
            get() {
                return if (field > 0) {
                    (field * RATIO).toLong()
                } else {
                    (2000L * RATIO).toLong()
                }
            }

        /** 【 breath hold 】breath hold wait duration */
        var BREATH_HOLD_WAIT_DURATION: Long = 0
            get() {
                return if (field > 0) {
                    (field * RATIO).toLong()
                } else {
                    (2000L * RATIO).toLong()
                }
            }

        /** *************************** breath hold end */
        // </editor-fold>

        // <editor-fold desc="4：Breath  out ">
        /** *************************** breath out start */

        // first + interval ratio * count -1 + single ratio
        // private const val firstIntervalDuration = 1000L
        // private const val intervalRatio = firstIntervalDuration * 0.5f
        // private const val singleDuration = 1000L

        /**
         * 【 breath out 】breath out all duration = first duration ratio + ( count - 1 ) * single
         * interval ratio + single duration ratio ，ths default duration value is 5000ms
         */
        var BREATH_OUT_ALL_DURATION: Long = 0
            get() {
                return if (field > 0) {
                    (field * RATIO).toLong()
                } else {
                    // return (firstIntervalDuration + (intervalRatio * (BREATH_OUT_ALL_COUNT - 1)) +
                    // singleDuration).toLong()
                    (5000 * RATIO).toLong()
                }
            }

        /** 【 breath out 】breath out count */
        private const val BREATH_OUT_ALL_COUNT: Int = 7

        /**
         * 【 breath out 】interval loop first duration ratio = first duration / all duration = 1000 /
         * 5000 = 0.2f
         */
        private const val BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO: Float = 0.2F
        // get() {
        //     return firstIntervalDuration.toFloat() / BREATH_OUT_ALL_DURATION
        // }

        /** 【 breath out 】loop single duration ratio = 1000F / 5000L = 0.2F */
        private const val BREATH_OUT_LOOP_SINGLE_DURATION_RATIO: Float = 0.2F
        //   get() {
        //       return singleDuration.toFloat() / BREATH_OUT_ALL_DURATION
        //   }

        /** 【 breath out 】interval loop duration ratio = single duration * 0.5F = 0.1 */
        private const val BREATH_OUT_LOOP_INTERVAL_RATIO: Float = BREATH_OUT_LOOP_SINGLE_DURATION_RATIO * 0.5F

        /** 【 breath out 】loop single duration 1000F / 5000L */
        // private val BREATH_OUT_LOOP_SINGLE_DURATION: Long = singleDuration
        private val BREATH_OUT_LOOP_SINGLE_DURATION: Long
            get() {
                return (BREATH_OUT_LOOP_SINGLE_DURATION_RATIO * BREATH_OUT_ALL_DURATION).toLong()
            }

        /**
         * 【 breath out 】animation all duration = single duration + loop interval duration * ( count -1
         * ) + sold interval loop duration
         */
        // private var BREATH_OUT_ALL_DURATION: Long = BREATH_OUT_LOOP_DURATION +
        // (BREATH_OUT_LOOP_INTERVAL_DURATION * (BREATH_OUT_ALL_COUNT - 1)) +
        // BREATH_OUT_SOLD_LOOP_INTERVAL_DURATION

        /** *************************** breath out end */
        // </editor-fold>
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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.let {
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
                circleLoop.paint.color = BREATH_COMMON_COLOR_VALUE
                circleLoop.paint.style = Paint.Style.STROKE
                circleLoop.type = 1

                mListCircle.add(circleLoop)
                return circleLoop
            } else if (tag == 2) {

                val circleSolid = Circle()
                circleSolid.radius = 0F
                circleSolid.paint.alpha = BREATH_COMMON_ALPHA_SOLID_VALUE.toInt()
                circleSolid.paint.color = BREATH_COMMON_COLOR_VALUE
                circleSolid.paint.style = Paint.Style.FILL
                circleSolid.type = 2

                mListCircle.add(circleSolid)
                return circleSolid
            }
        } else if (type == 2) {
            if (tag == 1) {
                val circleLoop = Circle()
                circleLoop.radius = mMaxRadius
                circleLoop.paint.color = BREATH_COMMON_COLOR_VALUE
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
                circleSolid.paint.color = BREATH_COMMON_COLOR_VALUE
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
            mCallBackListener?.onStart()
            animationSmallToBig()
        } else {
            LogUtil.e("startAnimation ---> running ....")
        }
    }

    /** small to big */
    private fun animationSmallToBig() {
        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_IN_ALL_DURATION
        LogUtil.e("animationSmallToBig --->--->---> duration: $duration")
        var tempCount = 0
        var startSolid = true

        // single interval ratio
        val intervalRatio: Float = (1F - BREATH_IN_SINGLE_DURATION_RATIO) / (BREATH_IN_COUNT - 1)

        animator.duration = duration
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            val fraction = it.animatedFraction
            if (fraction <= (intervalRatio * BREATH_IN_COUNT)) {
                if (fraction >= tempCount * intervalRatio) {
                    val startTime = tempCount * BREATH_IN_SINGLE_INTERVAL_DURATION_RATIO
                    val endTime = startTime + BREATH_IN_SINGLE_DURATION_RATIO
                    animationSmallToBigLoop(startTime, endTime, tempCount)
                    tempCount++
                }
            }

            // sold
            if (fraction >= BREATH_IN_SOLD_START_TIME_RATIO && startSolid) {
                LogUtil.e("animationSmallToBig --->  interval ---->>:------->>>>L> tempCount:$tempCount")
                startSolid = false
                animationSmallToBigSolid(tempCount)
            }

            invalidate()
        }
        animator.addListener(onStart = {
            if (mCurrentStatus != 1) {
                mCallBackListener?.statusChange(1)
                mCurrentStatus = 1
            }
        })
        animator.start()
        mListAnimation.add(animator)
        removeList()
    }

    /** small to big loop */
    private fun animationSmallToBigLoop(startTimeFrom: Float, endTimeFrom: Float, ratioCount: Int) {
        LogUtil.e("animationSmallToBigLoop --->   startTime: $startTimeFrom endTime: $endTimeFrom  ratioCount: $ratioCount  ")
        val circleLoop = getCircle(1, 1)
        LogUtil.e("animationSmallToBigLoop  ---> ratioCount---<<<--->>>: $ratioCount ---> $circleLoop")

        val animator = ValueAnimator.ofFloat(startTimeFrom, endTimeFrom)
        val duration = (BREATH_IN_SINGLE_DURATION_RATIO * BREATH_IN_ALL_DURATION).toLong()
        LogUtil.e("---=====----> fraction:  animationSmallToBigLoop!!! duration: $duration")
        LogUtil.e("--->>>animationSmallToBigLoop ---> BREATH_OUT_ALL_DURATION: $duration")
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            val fraction = it.animatedFraction

            // alpha  0 ---> 50 %
            if (fraction <= BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO) {
                val distanceAlpha = GradientUtil.getDistance(fraction,
                    0F,
                    BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO,
                    0F,
                    BREATH_COMMON_ALPHA_LOOP_VALUE,
                    false)
                val alpha = distanceAlpha.toInt()
                circleLoop.paint.alpha = alpha
                LogUtil.e("animationSmallToBigLoop ---> alpha from ---$ratioCount---> $alpha  ")
            }

            // alpha 50% --- 0
            if (fraction > BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO && fraction <= BREATH_IN_SINGLE_ALPHA_OPAQUE_TO_TRANSPARENT_RATIO) {
                val distanceAlpha = GradientUtil.getDistance(fraction,
                    BREATH_IN_SINGLE_ALPHA_TRANSPARENT_TO_OPAQUE_RATIO,
                    BREATH_IN_SINGLE_ALPHA_OPAQUE_TO_TRANSPARENT_RATIO,
                    BREATH_COMMON_ALPHA_LOOP_VALUE,
                    0F,
                    false)
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
        animator.start()

        mListAnimation.add(animator)
        removeList()
        LogUtil.e("----> remove list: --- end ---> " + mListCircle.size)
    }

    /** small to big solid */
    private fun animationSmallToBigSolid(count: Int) {
        LogUtil.e("animationSmallToBigSolid --->   startTime:  $count")
        val circleSolid = getCircle(1, 2)

        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_HOLD_ALL_DURATION
        LogUtil.e("--->>>animationSmallToBigSolid ---> BREATH_OUT_ALL_DURATION: $duration")
        animator.duration = duration
        animator.interpolator = DecelerateInterpolator()
        animator.addUpdateListener {
            // radius
            val distanceRadio = GradientUtil.getDistance(it.animatedFraction, 0F, 1F, 0F, mMaxRadius, false)
            LogUtil.e("animationSmallToBigSolid ---> mMaxRadius: $mMaxRadius  distanceRadio: $distanceRadio")
            circleSolid.radius = distanceRadio

            invalidate()
        }

        animator.addListener(onStart = {
            if (mCurrentStatus != 2) {
                mCallBackListener?.statusChange(2)
                mCurrentStatus = 2
            }
        }, onEnd = {
            LogUtil.e("animationSmallToBig -----> solid: end:" + (System.currentTimeMillis() - startTimeInterval))
            // breath hold wait
            animationHoldBreathWait()
        })
        animator.start()
        mListAnimation.add(animator)
    }

    /** breath hold wait */
    private fun animationHoldBreathWait() {
        LogUtil.e("animationHoldBreathWait --->   duration:  $BREATH_HOLD_WAIT_DURATION")
        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_HOLD_WAIT_DURATION
        animator.duration = duration
        animator.addListener(onEnd = {
            // breath out start
            animationBigToSmall()
        })
        animator.start()
        mListAnimation.add(animator)
    }

    /** loop animation wait if finish wait time > 0 , execute wait animation */
    private fun animationFinishLoopWait() {
        LogUtil.e("animationFinishLoopWait --->   duration:  $BREATH_FINISH_LOOP_INTERVAL")
        val animator = ValueAnimator.ofFloat(0F, 1F)
        val duration = BREATH_FINISH_LOOP_INTERVAL
        animator.duration = duration
        animator.addListener(onEnd = {
            // breath out start
            animationSmallToBig()
        })
        animator.addListener(onStart = {
            if (mCurrentStatus != 4) {
                mCallBackListener?.statusChange(4)
                mCurrentStatus = 4
            }
        })
        animator.start()
        mListAnimation.add(animator)
    }

    init {
        val firstInterval = 1000L
        val breathOutAllDuration = firstInterval + (BREATH_OUT_ALL_COUNT - 1) * (1000 * 0.5) + 1000L
        val first = firstInterval / breathOutAllDuration
        val singleRatio = 1000F / breathOutAllDuration

        val intervalRatio = singleRatio / 2

        LogUtil.e("=====>>>>> breath out duration = $breathOutAllDuration  first: $first  singleRatio: $singleRatio  intervalRatio: $intervalRatio")

        for (item in 0..BREATH_OUT_ALL_COUNT) {
            val start = first + (intervalRatio * item)
            LogUtil.e("=====>>>>> item = $item  start: $start  end: ${start + singleRatio} ")
        }
    }

    /** big to small */
    private fun animationBigToSmall() {
        val duration = BREATH_OUT_ALL_DURATION

        LogUtil.e("animationBigToSmall all duration: $duration")
        var count = 0
        var isStartSold = false
        val animator = ValueAnimator.ofFloat(0F, 1F)

        LogUtil.e(" [first ratio]:  $BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO [single ratio]: $BREATH_OUT_LOOP_SINGLE_DURATION_RATIO [interval single]: $BREATH_OUT_LOOP_INTERVAL_RATIO")

        animator.duration = duration
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            val fraction = it.animatedFraction
            // sold
            if (!isStartSold) {
                animationBigToSmallSold()
                isStartSold = true
            }

            val endRatio = (BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO + (BREATH_OUT_LOOP_INTERVAL_RATIO * (BREATH_OUT_ALL_COUNT - 2)))
            LogUtil.e("$$$$$$ first: $BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO  interval- ratio : $BREATH_OUT_LOOP_INTERVAL_RATIO  endRatio: $endRatio")

            // first
            if (fraction >= BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO && count == 0) {
                val startTime = BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO
                val endTime = startTime + BREATH_OUT_LOOP_SINGLE_DURATION_RATIO
                animationBigToSmallLoop(count, startTime, endTime)
                LogUtil.e("first: tempCount ccc: $count startTime: $startTime endTime: $endTime")
                count++
            } else {
                LogUtil.e("%%%%%%%%%%%%% fraction: ： :$fraction endRatio:  $endRatio  count: $count  startTime" + (BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO + (BREATH_OUT_LOOP_INTERVAL_RATIO * count)))

                // loop = start = loop first  + single ratio * count
                LogUtil.e("ffffff : 111  count: " + count + "  fraction:" + fraction + " end: " + ((BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO + (BREATH_OUT_LOOP_INTERVAL_RATIO * count))))

                if (count < BREATH_OUT_ALL_COUNT) {
                    if (fraction >= (BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO + (BREATH_OUT_LOOP_INTERVAL_RATIO * count))) {
                        LogUtil.e("ffffff : 222  count: " + count + "  fraction:" + fraction + " end: " + ((BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO + (BREATH_OUT_LOOP_INTERVAL_RATIO * count))))

                        // start  = first ratio + count * interval duration ratio
                        val startTime = BREATH_OUT_LOOP_FIRST_INTERVAL_DURATION_RATIO + (BREATH_OUT_LOOP_INTERVAL_RATIO * count)
                        val endTime = startTime + BREATH_OUT_LOOP_SINGLE_DURATION_RATIO
                        animationBigToSmallLoop(count, startTime, endTime)
                        LogUtil.e("_________>>>>>>>>>> count： :$count startTime:  $startTime  endTime： $endTime  ")
                        count++
                    }
                }
            }
            invalidate()
        }
        animator.addListener(onEnd = {
            // wait time
            if (BREATH_FINISH_LOOP_INTERVAL > 0) {
                animationFinishLoopWait()
            } else {
                animationSmallToBig()
            }
        })
        animator.start()

        mListAnimation.add(animator)
    }

    /** big to small sold */
    private fun animationBigToSmallSold() {
        val animator = ValueAnimator.ofFloat(0F, 1F)
        var temp = 0f
        val circleSolid = getCircle(2, 2)

        animator.duration = (BREATH_OUT_ALL_DURATION)
        animator.interpolator = DecelerateInterpolator(1.2F)
        animator.addUpdateListener {
            val fraction = it.animatedFraction
            if (temp != fraction) {
                // radius
                val distanceRadio = GradientUtil.getDistance(fraction, 0F, 1F, mMaxRadius, 0F, false)
                LogUtil.e("animationBigToSmallSolid ---> mMaxRadius: $mMaxRadius  distanceRadio: $distanceRadio")
                circleSolid.radius = distanceRadio

                // alpha
                val alphaEndTime = 0.75F
                if (fraction > alphaEndTime) {
                    val distanceAlpha = GradientUtil.getDistance(fraction,
                        alphaEndTime,
                        1F,
                        BREATH_COMMON_ALPHA_SOLID_VALUE,
                        BREATH_COMMON_ALPHA_SOLID_VALUE / 3,
                        false)
                    LogUtil.e("animationBigToSmallSolid ---> distanceAlpha: $distanceAlpha ")
                    circleSolid.paint.alpha = distanceAlpha.roundToInt()
                }
            }
            temp = fraction
        }
        animator.addListener(onStart = {
            if (mCurrentStatus != 3) {
                mCallBackListener?.statusChange(3)
                mCurrentStatus = 3
            }
        })
        animator.start()
        mListAnimation.add(animator)
        removeList()
    }

    /** big to small loop */
    private fun animationBigToSmallLoop(count: Int, startTimeFrom: Float, endTimeFrom: Float) {
        LogUtil.e("animationBigToSmallLoop ----> count: $count startTimeFrom: $startTimeFrom  endTimeFrom: $endTimeFrom")
        val animator = ValueAnimator.ofFloat(startTimeFrom, endTimeFrom)
        animator.duration = BREATH_OUT_LOOP_SINGLE_DURATION
        val alphaStartTime = 0.48f

        val circleLoop = getCircle(2, 1)
        animator.interpolator = DecelerateInterpolator(0.6F)
        animator.addUpdateListener {
            val fraction = it.animatedFraction
            LogUtil.e("!!!!!fraction:  $fraction")

            // alpha 0 ---> 50%
            if (fraction <= alphaStartTime) {
                val startAlpha = GradientUtil.getDistance(fraction, 0F, alphaStartTime, 0F, BREATH_COMMON_ALPHA_LOOP_VALUE, false)
                LogUtil.e("animationBigToSmallLoop ---> startAlpha  ratioCount$count   fraction:$fraction   distanceAlpha: $startAlpha")
                circleLoop.paint.alpha = startAlpha.toInt()
            }

            // alpha 50% ---> 0
            if (fraction > alphaStartTime) {
                val endAlpha = GradientUtil.getDistance(fraction, alphaStartTime, 1F, BREATH_COMMON_ALPHA_LOOP_VALUE, 0F, false)
                LogUtil.e("animationBigToSmallLoop ---> endAlpha  ratioCount$count  fraction:$fraction  distanceAlpha: $endAlpha")
                circleLoop.paint.alpha = endAlpha.toInt()
            }

            // stroke width
            val distanceStrokeWidth = GradientUtil.getDistance(fraction, 0F, 1F, BREATH_COMMON_STROKE_WIDTH, 0F, false)
            circleLoop.paint.strokeWidth = distanceStrokeWidth
            LogUtil.e("animationBigToSmallLoop ---> distanceStrokeWidth  ratioCount$count fraction:$fraction  distanceStrokeWidth: $distanceStrokeWidth")

            // radius
            val distanceRadius = GradientUtil.getDistance(fraction, 0F, 1F, mMaxRadius, 0F, false)
            circleLoop.radius = distanceRadius
            LogUtil.e("animationBigToSmallLoop ---> distanceRadius ratioCount  $count  fraction:$fraction  distanceRadius: $distanceRadius")
        }
        animator.start()
        mListAnimation.add(animator)
        removeList()
    }

    fun pause() {
        mListAnimation.forEach { it.pause() }
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

    class Circle {
        var paint: Paint = Paint()
        var radius: Float = 0f
        var type: Int = 1

        override fun toString(): String {
            return "Circle(paint=$paint, radius=$radius, type=$type)"
        }
    }

    fun setCallBackListener(callBackListener: CallBackListener) {
        this.mCallBackListener = callBackListener
    }

    interface CallBackListener {
        /** animation - status */
        fun onStart()

        /**
         * status - change 1: small to big loop 2: small to big sold 3: big to small loop 4: finish wait
         * start
         */
        fun statusChange(status: Int)
    }
}
