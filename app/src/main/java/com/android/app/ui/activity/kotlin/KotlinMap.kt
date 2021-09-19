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

    /**
     *
     * 初始化数据
     */
    override fun initData() {
    }

    override fun initView() {
        super.initView()
    }

    /**
     * @param inflater  布局管理器
     * @param container 父类的根布局
     * @return 返回一个ViewBinding 的对象，如果不需要嵌套布局的话，就直接使用 xxxBinding.inflate(layoutInflater),
     * 如果需要嵌套布局的话，就使用xxxBinding.inflate(layoutInflater, mBaseBinding.root, true)
     */
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityKotlinMapBinding {
        return ActivityKotlinMapBinding.inflate(inflater, container, false)
    }
}