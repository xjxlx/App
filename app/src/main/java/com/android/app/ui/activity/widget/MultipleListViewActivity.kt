package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityMultipleListViewBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

/**
 * 多列表的listView
 */
class MultipleListViewActivity : AppBaseBindingTitleActivity<ActivityMultipleListViewBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        val arrayListOf = arrayListOf<String>()
//        arrayListOf.add("house")
//        arrayListOf.add("kitchen")
//        arrayListOf.add("bathroom")
//        arrayListOf.add("bedroom")
//        arrayListOf.add("backyard")
//        us, right, upon, sit
        arrayListOf.add("us")
        arrayListOf.add("right")
        arrayListOf.add("upon")
        arrayListOf.add("sit")

        arrayListOf.add("us")
        arrayListOf.add("right")
        arrayListOf.add("upon")
        arrayListOf.add("sit")

        mBinding.mlvList.setList(arrayListOf)
    }

    override fun setTitleContent(): String {
        return "测试事件分发"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityMultipleListViewBinding {
        return ActivityMultipleListViewBinding.inflate(layoutInflater, container, true)
    }
}