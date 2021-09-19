package com.android.app.ui.activity.kotlin

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityKotlinMapBinding
import com.android.helper.base.BaseBindingActivity

/**
 * @author XJX
 * @CreateDate: 2021/9/19-16:52
 * @Description: Kotlin的项目
 */
class KotlinMap : BaseBindingActivity<ActivityKotlinMapBinding>() {

    var ssss: String = "sss"

    override fun initData() {
    }

    override fun initView() {
        super.initView()

    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityKotlinMapBinding {
        return ActivityKotlinMapBinding.inflate(inflater, container, false)
    }
}