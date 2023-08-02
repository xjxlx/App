package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.collection.arrayMapOf
import androidx.collection.arraySetOf
import com.android.app.databinding.ActivityKotListBinding
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
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

        of.forEach { it -> LogUtil.e("item forEach:$it") }

        of.forEachIndexed { index, item -> LogUtil.e("item forEachIndexed: index:$index  item： $item") }

        val arrayListOf1 = arrayListOf<kotlin.String>()
        //        arrayListOf1.add("1")
        //        arrayListOf1.add("2")
        //        arrayListOf1.add("3")
        //        arrayListOf1.add("4")
        //        arrayListOf1.add("5")

        arrayListOf1.add("赵")
        arrayListOf1.add("钱")
        arrayListOf1.add("孙")
        arrayListOf1.add("李")

        arrayListOf1.forEach { LogUtil.e("forEach ---> 普通:$it") }

        // 反转
        arrayListOf1.reverse()

        arrayListOf1.forEach { LogUtil.e("forEach ---> reverse:$it") }

        // 随机排序
        arrayListOf1.shuffle()
        arrayListOf1.forEach { LogUtil.e("forEach ---> shuffle:$it") }

        // 从小到大
        arrayListOf1.sort()
        arrayListOf1.forEach { LogUtil.e("forEach ---> sort:$it") }

        Person().test1(1)

        Dialog.showDialog()

        Person.test2()

        // 普通的调用
        foo(1, 2, qux = {
            return@foo "21"
        })

        // 具名参数的调用方式1
        foo(baz = 1, qux = {
            return@foo "1"
        })

        // 具名参数的调用方式2
        foo(baz = 2) {
            return@foo "222"
        }

        foo1(str2 = 1)
        foo1(1, 2, str2 = 3)
    }

    public fun test3(a: Int = 3) {}

    class Person {

        private fun max(a: Int, b: kotlin.String): kotlin.String {
            return "abc"
        }

        public fun test1(a: Int) {}

        companion object {

            public fun test2() {
                LogUtil.e("我是伴生类的成员方法！")
            }
        }
    }

    object Dialog {

        public fun showDialog() {
            LogUtil.e("showDialog!")
        }
    }

    //    具名参数
    private fun foo(bar: Int = 0, baz: Int = 1, qux: () -> String1) {}

    private fun foo1(vararg str: Int, str2: Int) {}
}
