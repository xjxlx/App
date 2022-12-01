package com.android.app.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityTestSfBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil

class TestSfActivity : AppBaseBindingTitleActivity<ActivityTestSfBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        val list = arrayListOf<Float>()

        for (item in 1 until 9) {
            list.add(item * 0.1F)
        }

        LogUtil.e("list  :$list")

        for (item in list.indices) {
            val current = list[item]
//            LogUtil.e("item  :" + current)
            if (current >= start && current <= end) {
                getSodRatio(current, start, end, ratio)
            }
        }
    }

    private val start = 0.2f
    private val end = 0.6f
    private val ratio = 1.5f

    override fun setTitleContent(): String {
        return "测试算法"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityTestSfBinding {
        return ActivityTestSfBinding.inflate(inflater, container, true)
    }

    private fun getSodRatio(percentage: Float, startTime: Float, endTime: Float, scaleRatio: Float): Float {
        // 使用的时间
        val intervalTim = endTime - startTime
        // 移动的距离
        val ratioInterval = scaleRatio - 1
        // 距离 = 时间 * 单位
        // 距离 = ratioInterval ，时间 = intervalTim ，单位 =距离 / 时间
        // 得到每一份单位的缩放值的比例
        val unitRatio = (ratioInterval / intervalTim)
        // 缩放的最大值 - (最大进度 -当前进度 = 从当前开始计算的时间 )  * 比值
        val currentRatio = scaleRatio - (endTime - percentage) * unitRatio
        LogUtil.e(" 当前： " + percentage + " 最终的缩放值：" + currentRatio)
        return currentRatio
    }
}