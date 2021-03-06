package com.android.app.test

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MotionEvent
import com.android.app.R
import com.android.helper.base.AppBaseActivity
import com.android.helper.utils.LogUtil
import com.android.helper.utils.ResourceUtil
import kotlinx.android.synthetic.main.activity_test_touch.*

class TestTouchActivity : AppBaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_test_touch
    }
 
    override fun initListener() {

        val thread = object : Thread() {
            override fun run() {
                super.run()
                Looper.prepare()
                Looper.loop()

                val mHandler = @SuppressLint("HandlerLeak")
                object : Handler() {
                    override fun handleMessage(msg: Message) {
                        super.handleMessage(msg)
                        tv_text.setBackgroundColor(ResourceUtil.getColor(R.color.blue_1))
                    }
                };

                mHandler.sendEmptyMessage(111)

            }
        }
        thread.start()
    }

     override fun initData(savedInstanceState: Bundle?) {

    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                LogUtil.e(tag, "dispatchTouchEvent--->down")
            }

            MotionEvent.ACTION_MOVE -> {
                LogUtil.e(tag, "dispatchTouchEvent--->move")
            }

            MotionEvent.ACTION_UP -> {

                LogUtil.e(tag, "dispatchTouchEvent--->up")
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                LogUtil.e(tag, "onTouchEvent--->down")

            }
            MotionEvent.ACTION_MOVE -> {
                LogUtil.e(tag, "onTouchEvent--->move")
            }

            MotionEvent.ACTION_UP -> {

                LogUtil.e(tag, "onTouchEvent--->up")
            }
        }
        return super.onTouchEvent(event)
    }
    
    /**
     * Activity?????????view
     */
    override fun initView() {
    
    }
    
}