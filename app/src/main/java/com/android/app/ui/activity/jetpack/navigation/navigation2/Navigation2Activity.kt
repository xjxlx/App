package com.android.app.ui.activity.jetpack.navigation.navigation2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityNavigation2Binding
import com.android.common.base.BaseBindingActivity

/** navigation 的练习 */
class Navigation2Activity : BaseBindingActivity<ActivityNavigation2Binding>() {
    /** Activity初始化view */
    override fun initView() {}

    override fun initData(savedInstanceState: Bundle?) {}

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityNavigation2Binding {
        return ActivityNavigation2Binding.inflate(layoutInflater, container, true)
    }
}
