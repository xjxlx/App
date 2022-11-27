package com.android.app.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityCustomSlingScrollMainBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

class CustomSlingScrollMainActivity : AppBaseBindingTitleActivity<ActivityCustomSlingScrollMainBinding>() {

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun setTitleContent(): String {
        return "自定义侧滑布局2"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomSlingScrollMainBinding {
        return ActivityCustomSlingScrollMainBinding.inflate(inflater, container, true)
    }
}