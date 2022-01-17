package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityKotlinMapBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

/**
 * @author XJX
 * @CreateDate: 2021/9/19-16:52
 * @Description: Kotlin的项目
 */
class KotlinMap : AppBaseBindingTitleActivity<ActivityKotlinMapBinding>() {

    override fun setTitleContent(): String {
        return "kotlin集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityKotlinMapBinding {
        return ActivityKotlinMapBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        setonClickListener(R.id.tv_array_and_list)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_array_and_list -> {
                startActivity(ArrayAndListActivity::class.java)
            }
        }
    }
}