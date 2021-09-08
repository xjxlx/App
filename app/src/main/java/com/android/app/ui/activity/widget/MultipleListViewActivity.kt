package com.android.app.ui.activity.widget

import com.android.app.R
import com.android.helper.base.BaseActivity
import kotlinx.android.synthetic.main.activity_multiple_list_view.*

/**
 * 多列表的listView
 */
class MultipleListViewActivity : BaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_multiple_list_view
    }

    override fun initData() {

        setTitleContent("多列表的ListView")

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