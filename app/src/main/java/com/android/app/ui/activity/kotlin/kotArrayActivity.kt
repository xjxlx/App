package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityArrayBinding
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity

class kotArrayActivity : AppBaseBindingTitleActivity<ActivityArrayBinding>() {

    override fun setTitleContent(): String {
        return "数组和集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityArrayBinding {
        return ActivityArrayBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // 指定类型
        var array1 = arrayOf(1, 2, 3)

        // 指定长度
        var array2 = BooleanArray(1)

        // 不指定类型的数组，自动推断，但是不能为null
        val array3 = arrayOf(5)

        // array3[2] = null
        // 可空的数组
        val arrayOfNulls = arrayOfNulls<String>(3)
        arrayOfNulls[1] = null
        arrayOfNulls[2] = ""
        val intArray = IntArray(4) { 21 }

        // 角标便利
        for (a in 0 until intArray.size) {
            LogUtil.e("a 1:" + intArray[a])
        }

        // 高级角标便利
        for (a in intArray.indices) {
            LogUtil.e("a 2:" + intArray[a])
        }

        // 元素便利
        for (element in intArray) {
            LogUtil.e("a 3:$element")
        }

        // 同时便利角标和元素
        for ((index, item) in intArray.withIndex()) {
            LogUtil.e("角标：$index 元素：$item")
        }

        // forEach 循环,it代表数据的元素
        intArray.forEach { LogUtil.e("forEach:$it") }

        // 增强版的forEach
        intArray.forEachIndexed { index, item -> LogUtil.e("增强forEach:  index：$index   item:$item") }

        // -----------------------------------

        //        val arrayListOf = arrayListOf<String>()
    }
}
