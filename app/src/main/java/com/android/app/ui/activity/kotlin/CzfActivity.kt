package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityCzfBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

class CzfActivity() : AppBaseBindingTitleActivity<ActivityCzfBinding>() {

    override fun setTitleContent(): String {
        return "Kotlin操作符"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCzfBinding {
        return ActivityCzfBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}