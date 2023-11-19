package com.android.app.test

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityTestSfBinding
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil

class TestSfActivity : BaseBindingTitleActivity<ActivityTestSfBinding>() {
  val allDistance = 300f

  private val animation by lazy {
    ValueAnimator.ofFloat(0f, 2f).apply {
      duration = 3000
      addUpdateListener {
        val animatedValue = it.animatedValue as Float
        if (animatedValue >= start && animatedValue <= end) {
          //                       getDistance(animatedValue, start, end, startRatio, endRatio,
          // reverse)
        }
        val animatedFraction = it.animatedFraction

        if (animatedFraction >= 0.6f) {
          getBigToSmallValue(it.animatedFraction, 0.6f, 1f, 255f, 0f)
        }
      }
    }
  }

  private var reverse = false
  private val start = 0.2f
  private val end = 0.8f
  private val startRatio = 2f
  private val endRatio = 6f

  override fun initData(savedInstanceState: Bundle?) {
    mBinding.btnStart.setOnClickListener {
      reverse = false
      animation.start()
    }

    mBinding.btnStop.setOnClickListener {
      reverse = true
      animation.reverse()
    }
  }

  override fun getTitleContent(): String {
    return "测试算法"
  }

  override fun getBinding(
      inflater: LayoutInflater,
      container: ViewGroup?,
      attachToRoot: Boolean
  ): ActivityTestSfBinding {
    return ActivityTestSfBinding.inflate(inflater, container, true)
  }

  private fun getDistance(
      currentTime: Float,
      startTime: Float,
      endTime: Float,
      startDistance: Float,
      endDistance: Float,
      isReverse: Boolean
  ): Float {
    var distance = 0f
    // 求出时间差值
    val intervalTim = endTime - startTime
    // 求出距离的差值
    val distanceInterval = endDistance - startDistance
    // v = s / t
    val speed = (distanceInterval / intervalTim)
    if (!isReverse) {
      // s = v * t
      // 从0开始计算
      distance = speed * (currentTime - startTime) + startDistance
    } else {
      // s = t * v
      // 从0 开始计算，此处是负数
      val currentDistance = (currentTime - endTime) * speed
      distance = endDistance + currentDistance
    }
    LogUtil.e(
        " 当前 SSS： $currentTime 最终的缩放值：$distance   isReverse： $isReverse  currentTime: $currentTime"
    )
    return distance
  }

  private fun getBigToSmallValue(
      currentTime: Float,
      startTime: Float,
      endTime: Float,
      startDistance: Float,
      endDistance: Float
  ): Float {
    val gradient: Float
    val intervalTime = endTime - startTime
    val intervalDistance = startDistance - endDistance
    val speed = intervalDistance / intervalTime
    // s = v * t
    gradient = startDistance - speed * (currentTime - startTime)
    LogUtil.e(
        "gradient - currentTime:$currentTime startTime:$startTime  endTime: $endTime  startDistance:$startDistance endDistance: $endDistance sss: $gradient"
    )
    return gradient
  }
}
