package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import com.android.app.databinding.ActivityPostChartBinding
import com.android.common.base.BaseBindingTitleActivity

class PostChartActivity : BaseBindingTitleActivity<ActivityPostChartBinding>() {

    override fun getTitleContent(): String {
        return "柱状图"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityPostChartBinding {
        return ActivityPostChartBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
