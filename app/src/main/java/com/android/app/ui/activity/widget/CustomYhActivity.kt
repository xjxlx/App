package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityCustomYhBinding
import com.android.common.base.BaseBindingTitleActivity

/** 自定义圆环 */
class CustomYhActivity : BaseBindingTitleActivity<ActivityCustomYhBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.clView.start(10000, 10)
    }

    override fun getTitleContent(): String {
        return "自定义圆环"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomYhBinding {
        return ActivityCustomYhBinding.inflate(inflater, container, true)
    }
}
