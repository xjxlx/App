package com.android.app.ui.fragment

import android.os.Bundle
import android.view.View
import com.android.app.R
import com.android.app.test.TestMapActivity
import com.android.app.ui.activity.DemoMapTitleActivity
import com.android.app.ui.activity.animation.AnimationMapActivity
import com.android.app.ui.activity.java.JavaMapActivity
import com.android.app.ui.activity.jetpack.JetPackMapActivity
import com.android.app.ui.activity.kotlin.KotlinMap
import com.android.app.ui.activity.widget.ViewMapTitleActivity
import com.android.helper.base.BaseFragment

/**
 * 待办的fragment
 */
class TodoFragment : BaseFragment() {

    override fun getBaseLayout(): Int {
        return R.layout.fragment_todo
    }

    override fun initListener() {
        super.initListener()

        setViewClickListener(
            R.id.tv_custom_widget,
            R.id.tv_animation_map,
            R.id.tv_java_map,
            R.id.tv_test_map,
            R.id.tv_other,
            R.id.tv_jetpack,
            R.id.tv_kotlin
        )
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_custom_widget -> {
                startActivity(ViewMapTitleActivity::class.java)
            }
            R.id.tv_animation_map -> {
                startActivity(AnimationMapActivity::class.java)
            }
            R.id.tv_java_map -> {
                startActivity(JavaMapActivity::class.java)
            }
            R.id.tv_test_map -> {
                startActivity(TestMapActivity::class.java)
            }
            R.id.tv_other -> {
                startActivity(DemoMapTitleActivity::class.java)
            }
            R.id.tv_jetpack -> {
                startActivity(JetPackMapActivity::class.java)
            }
            R.id.tv_kotlin -> {
                startActivity(KotlinMap::class.java)
            }
        }
    }
}
