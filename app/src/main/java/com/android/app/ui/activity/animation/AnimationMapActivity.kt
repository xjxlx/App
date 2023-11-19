package com.android.app.ui.activity.animation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityAnimationMapBinding
import com.android.common.base.BaseBindingTitleActivity
import com.android.helper.utils.ToastUtil

class AnimationMapActivity : BaseBindingTitleActivity<ActivityAnimationMapBinding>() {

    override fun getTitleContent(): String {
        return ""
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityAnimationMapBinding {
        return ActivityAnimationMapBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        setonClickListener(R.id.tv_back, R.id.tv_gif, R.id.tv_radiation_animation, R.id.tv_selector_time_dialog)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_back -> {
                finish()
            }

            R.id.tv_gif -> {
                startActivity(GifViewActivity::class.java)
            }

            R.id.tv_radiation_animation -> {
                startActivity(RadiationAnimationActivity::class.java)
            }

            R.id.tv_selector_time_dialog -> {
                ToastUtil.show("自定义时间选择器的dialog")
            }
        }
    }
}
