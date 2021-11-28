package com.android.app.ui.activity.personal

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityRouseDingDingBinding
import com.android.helper.base.BaseBindingTitleActivity

/**
 * 唤醒钉钉的页面
 */
class RouseDingDingActivity : BaseBindingTitleActivity<ActivityRouseDingDingBinding>() {

    override fun initData() {
    }

    override fun setTitleContent(): String {
        return "唤醒钉钉"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityRouseDingDingBinding {
        return ActivityRouseDingDingBinding.inflate(inflater, container, true)
    }
}