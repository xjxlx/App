package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityLambdaBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil

class LambdaActivity : AppBaseBindingTitleActivity<ActivityLambdaBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityLambdaBinding {
        return ActivityLambdaBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "Kotlin中的Lambda表达式"
    }

    override fun initData(savedInstanceState: Bundle?) {
        val tz = TZ()
//
//        val add = tz.add(1, 2)
//        LogUtil.e("add --->  $add")
//
//        val add2 = tz.add2("oo", "xx")
//        LogUtil.e("add2  :  $add2")
//
//        val arrayListOf = arrayListOf<String>()
//        for (item in 0..5) {
//            arrayListOf.add("hello word ---> index:$item")
//        }

        val ktTz = KtTz()
        val abc2 = ktTz.abc2
        LogUtil.e("abc2 ---> $abc2")

        ktTz.abc2 = "哈哈哈哈哈哈！"
        val abc21 = ktTz.abc2
        LogUtil.e("abc2 【2】 ---> $abc21")
        LogUtil.e("abc2 【2】 ---> ${ktTz.ts}")
    }

    fun TZ.add(a: Int, b: Int): Int {
        return a + b
    }

    fun TZ.add2(a: String, b: String): String {
        return a + b
    }

    fun <T> ArrayList<T>.add3(t: T): ArrayList<T> {
        this.add(t)
        return this
    }

    //添加一个对象到集合中，并且返回这个集合
    fun <T> MutableList<T>.addBy(t: T): MutableList<T> {
        this.add(t)
        return this //返回集合本身
    }

    //自定义扩展属性，
    var <T> ArrayList<T>.lastData: T
        //获取集合中最后一个对象
        get() = this[this.size - 1]
        //设置集合中最后一个对象的值
        set(value) {
            this[this.size - 1] = value
        }

    var KtTz.abc2: String
        get() {
            return ts
        }
        set(value) {
            ts = value
        }

}
// 拓展函数

// 拓展属性

// 伴生类拓展

class TZ {
    val abc = "abc";
    fun test1() {
        LogUtil.e("logu----> test ---> 1")
    }
}

