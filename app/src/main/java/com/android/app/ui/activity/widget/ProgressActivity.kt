package com.android.app.ui.activity.widget

import android.os.Bundle
import android.text.TextUtils
import com.android.app.R
import com.android.helper.base.AppBaseActivity
import com.android.helper.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_progress2.*

class ProgressActivity : AppBaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_progress2
    }
    
    /**
     * Activity初始化view
     */
    override fun initView() {
    
    }
    
    override fun initData(savedInstanceState: Bundle?) {
        // 进度条1
        btn1.setOnClickListener { spv.startAnimation() }
        btn2.setOnClickListener { spv.cancelAnimation() }
        // 进度条2
        btn.setOnClickListener {
            val toString = ed_input.text.toString()
            if (TextUtils.isEmpty(toString)) {
                ToastUtil.show("数据不能为空")
                return@setOnClickListener
            }
            pb2.setCharging(true)
            pb2.startAnimation(toString.toInt())
        }
    }
}