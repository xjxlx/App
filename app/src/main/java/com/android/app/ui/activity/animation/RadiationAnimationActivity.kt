package com.android.app.ui.activity.animation

import android.os.Bundle
import com.android.app.R
import android.widget.Button
import com.android.helper.base.BaseActivity
import kotlinx.android.synthetic.main.activity_radiation_animation.*

/**
 * 自定义放射动画
 */
class RadiationAnimationActivity : BaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_radiation_animation
    }

    override fun initData(savedInstanceState: Bundle?) {
        findViewById<Button>(R.id.btn).setOnClickListener {
            cst.startAnim()
        }
    }
}