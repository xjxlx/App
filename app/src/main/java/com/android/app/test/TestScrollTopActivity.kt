package com.android.app.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityTestScrollTopBinding
import com.android.common.base.BaseBindingTitleActivity

class TestScrollTopActivity : BaseBindingTitleActivity<ActivityTestScrollTopBinding>() {

    override fun initData(savedInstanceState: Bundle?) {}

    override fun getTitleContent(): String {
        return "顶起键盘"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTestScrollTopBinding {
        return ActivityTestScrollTopBinding.inflate(inflater, container, true)
    }
}
