package com.android.app.ui.activity.animation

import android.os.Bundle
import android.view.View
import com.android.app.R
import com.android.helper.base.AppBaseActivity
import com.android.helper.utils.ToastUtil

class AnimationMapActivity : AppBaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_animation_map
    }

    override fun initData(savedInstanceState: Bundle?) {
        setonClickListener(
            R.id.tv_back,
            R.id.tv_gif,
            R.id.tv_radiation_animation,
            R.id.tv_selector_time_dialog
        )
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

    /**
     * Activity初始化view
     */
    override fun initView() {

    }
}