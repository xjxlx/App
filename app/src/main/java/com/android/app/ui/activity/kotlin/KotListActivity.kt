package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.arrayMapOf
import androidx.collection.arraySetOf
import com.amap.api.mapcore.util.it
import com.android.app.databinding.ActivityKotListBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import kotlin.String as String1

class KotListActivity : AppBaseBindingTitleActivity<ActivityKotListBinding>() {

    override fun setTitleContent(): String1 {
        return "Kotlin集合"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityKotListBinding {
        return ActivityKotListBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        // 创建可变集合列表
        // 普通方式
        val arrayListOf = arrayListOf<String1>()
        arrayListOf.add("1")

        // 推荐方式
        val mutableListOf = mutableListOf<String1>()
        mutableListOf.add("1")

        // 创建集合的时候，就直接进行数据的赋值
        val mutableListOf1 = mutableListOf<Int>(123, 324)
        mutableListOf1.add(1)

        // 创建不可变的集合,只能在创建的时候进行数据的赋值，创建之后，不能改变集合中的数据
        val listOf = listOf<Int>(1, 2)

        // 二：map集合

        // 可变集合
        // 第一种创建方式
        val arrayMapOf = arrayMapOf<kotlin.String, kotlin.String>()
        arrayMapOf["key"] = "value"

        // 第二种创建方式，使用Pair去包装元素
        val arrayMapOf1 = arrayMapOf(Pair("key", "value"))
        arrayMapOf1["1"] = "2"

        val mutableMapOf = mutableMapOf<kotlin.String, Int>()
        mutableMapOf["key"] = 123

        // 不可变集合，长度限制，不可改变
        val mapOf = mapOf<Int, Int>(Pair(1, 1))

        // 三：set集合

        // 1:可变集合
        val arraySetOf = arraySetOf<kotlin.String>()
        arraySetOf.add("1")

        val mutableSetOf = mutableSetOf<Int>()
        mutableSetOf.add(2)

        // 2:不可变集合
        val of = setOf<Int>(1, 2, 4)

        for (item in of) {
            LogUtil.e("item:$item")
        }

        of.forEach { it ->
            LogUtil.e("item forEach:$it")
        }

        of.forEachIndexed { index, item ->
            LogUtil.e("item forEachIndexed: index:$index  item： $item")
        }

    }
}