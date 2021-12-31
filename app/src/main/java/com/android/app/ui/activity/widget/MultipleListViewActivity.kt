package com.android.app.ui.activity.widget

import android.os.Bundle
import com.android.app.R
import com.android.helper.base.AppBaseActivity
import kotlinx.android.synthetic.main.activity_multiple_list_view.*

/**
 * 多列表的listView
 */
class MultipleListViewActivity : AppBaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_multiple_list_view
    }
    
    /**
     * Activity初始化view
     */
    override fun initView() {
    
    }
    
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

        mlv_list.setList(arrayListOf)
    }
}