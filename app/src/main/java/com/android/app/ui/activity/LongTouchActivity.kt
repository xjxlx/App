package com.android.app.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import com.android.app.databinding.ActivityLongTouchBinding
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.DownCountTime
import com.android.helper.utils.ToastUtil
import kotlin.random.Random

class LongTouchActivity : AppBaseBindingTitleActivity<ActivityLongTouchBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityLongTouchBinding {
        return ActivityLongTouchBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "长按功能"
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initData(savedInstanceState: Bundle?) {
        var countTime: DownCountTime? = null

        // 测试倒计时，当在某个时间点的时候，触发操作
        mBinding.tvClick.setOnTouchListener { v, event ->
            if (countTime == null) {
                countTime = DownCountTime()
                countTime?.setCountdown(11, 1000L, object : DownCountTime.CallBack {
                    override fun onTick(current: Long, countdown: Long) {
                        LogUtil.e("xxxx", "current: $current countdown: $countdown")
                        if (current == 5L) {
                            ToastUtil.show("可以展示了！")

                            for (item in 0..100) {
                                 val nextInt = Random.nextInt(3, 6)
//                                val nextInt = Random.nextInt(3)
                                LogUtil.e("nextInt: $nextInt")
                            }
                        }
                    }

                    override fun onFinish() {
                        LogUtil.e("xxxx", "current:onFinish!")
                    }
                })
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    countTime?.start()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    countTime?.restart()
                }
            }
            return@setOnTouchListener false
        }
    }
}