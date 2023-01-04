package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityCustomYhBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

/**
 * 自定义圆环
 */
class CustomYhActivity : AppBaseBindingTitleActivity<ActivityCustomYhBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.clView.start(10000, 10)
    }

    override fun setTitleContent(): String {
        return "自定义圆环"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomYhBinding {
        return ActivityCustomYhBinding.inflate(inflater, container, true)
    }
}