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

        setonClickListener(R.id.tv_array, R.id.tv_list, R.id.tv_lambda, R.id.tv_back, R.id.tv_czf)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.tv_back -> {
                finish()
            }

            R.id.tv_array -> {
                startActivity(kotArrayActivity::class.java)
            }

            R.id.tv_list -> {
                startActivity(KotListActivity::class.java)
            }

            R.id.tv_lambda -> {
                startActivity(LambdaActivity::class.java)
            }

            R.id.tv_czf -> {
                startActivity(CzfActivity::class.java)
            }
        }
    }
}