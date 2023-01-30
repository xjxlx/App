package com.android.app.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityTestScrollTopBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

class TestScrollTopActivity : AppBaseBindingTitleActivity<ActivityTestScrollTopBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun setTitleContent(): String {
        return "顶起键盘"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityTestScrollTopBinding {
        return ActivityTestScrollTopBinding.inflate(inflater, container, true)
    }
}