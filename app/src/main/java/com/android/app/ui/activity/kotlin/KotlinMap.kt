package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityKotlinMapBinding
import com.android.helper.base.title.BaseBindingTitleActivity

/**
 * @author XJX
 * @CreateDate: 2021/9/19-16:52
 * @Description: Kotlin的项目
 */
class KotlinMap : BaseBindingTitleActivity<ActivityKotlinMapBinding>() {

    var ssss: String = "sss"

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun setTitleContent(): String {
        return "ktolin集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityKotlinMapBinding {
        return ActivityKotlinMapBinding.inflate(inflater, container, false)
    }
}