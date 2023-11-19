package com.android.app.ui.activity.widget

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityProgress2Binding
import com.android.common.base.BaseBindingTitleActivity
import com.android.helper.utils.ToastUtil

class ProgressActivity : BaseBindingTitleActivity<ActivityProgress2Binding>() {

    override fun initData(savedInstanceState: Bundle?) {
        // 进度条1
        mBinding.btn1.setOnClickListener { mBinding.spv.startAnimation() }
        mBinding.btn2.setOnClickListener { mBinding.spv.cancelAnimation() }
        // 进度条2
        mBinding.btn.setOnClickListener {
            val toString = mBinding.edInput.text.toString()
            if (TextUtils.isEmpty(toString)) {
                ToastUtil.show("数据不能为空")
                return@setOnClickListener
            }
            mBinding.pb2.setCharging(true)
            mBinding.pb2.startAnimation(toString.toInt())
        }
    }

    override fun getTitleContent(): String {
        return "测试事件分发"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityProgress2Binding {
        return ActivityProgress2Binding.inflate(layoutInflater, container, true)
    }
}
