package com.android.app.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityCustomSlingScrollMainBinding
import com.android.common.base.BaseBindingTitleActivity

class CustomSlingScrollMainActivity : BaseBindingTitleActivity<ActivityCustomSlingScrollMainBinding>() {

    override fun initData(savedInstanceState: Bundle?) {}

    override fun getTitleContent(): String {
        return "自定义侧滑布局2"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCustomSlingScrollMainBinding {
        return ActivityCustomSlingScrollMainBinding.inflate(inflater, container, true)
    }
}
