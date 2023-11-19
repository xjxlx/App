package com.android.app.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityTestTouchBinding
import com.android.common.base.BaseBindingTitleActivity
import com.android.common.utils.LogUtil
import com.android.common.utils.ResourcesUtil

class TestTouchActivity : BaseBindingTitleActivity<ActivityTestTouchBinding>() {

    override fun getTitleContent(): String {
        return "测试事件分发"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestTouchBinding {
        return ActivityTestTouchBinding.inflate(layoutInflater, container, true)
    }

    override fun initListener() {
        val thread = object : Thread() {
            override fun run() {
                super.run()
                Looper.prepare()
                Looper.loop()

                val mHandler = @SuppressLint("HandlerLeak") object : Handler() {
                    override fun handleMessage(msg: Message) {
                        super.handleMessage(msg)
                        mBinding.tvTest.setBackgroundColor(ResourcesUtil.getColor(mActivity, R.color.blue_1))
                    }
                }

                mHandler.sendEmptyMessage(111)
            }
        }
        thread.start()
    }

    override fun initData(savedInstanceState: Bundle?) {}

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                LogUtil.e("dispatchTouchEvent--->down")
            }

            MotionEvent.ACTION_MOVE -> {
                LogUtil.e("dispatchTouchEvent--->move")
            }

            MotionEvent.ACTION_UP -> {

                LogUtil.e("dispatchTouchEvent--->up")
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                LogUtil.e("onTouchEvent--->down")
            }

            MotionEvent.ACTION_MOVE -> {
                LogUtil.e("onTouchEvent--->move")
            }

            MotionEvent.ACTION_UP -> {
                LogUtil.e("onTouchEvent--->up")
            }
        }
        return super.onTouchEvent(event)
    }
}
