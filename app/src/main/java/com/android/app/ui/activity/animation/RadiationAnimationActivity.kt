package com.android.app.ui.activity.animation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.android.app.R
import com.android.app.databinding.ActivityRadiationAnimationBinding
import com.android.common.base.BaseBindingTitleActivity

/** 自定义放射动画 */
class RadiationAnimationActivity : BaseBindingTitleActivity<ActivityRadiationAnimationBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        findViewById<Button>(R.id.btn).setOnClickListener { mBinding.cst.startAnim() }
    }

    override fun getTitleContent(): String {
        return "测试事件分发"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityRadiationAnimationBinding {
        return ActivityRadiationAnimationBinding.inflate(layoutInflater, container, true)
    }
}
