package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityLambdaBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import org.json.JSONArray

class LambdaActivity : AppBaseBindingTitleActivity<ActivityLambdaBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityLambdaBinding {
        return ActivityLambdaBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "Kotlin中的Lambda表达式"
    }

    override fun initData(savedInstanceState: Bundle?) {
        off(2, 3)

        of1(12)

        var index = 0
        do {
            LogUtil.e("index:${index++}")
        } while (index < 3)


    }

    private fun off(a: Int, b: Int): Int {
        return if (a > b) a else b
    }

    private fun of1(a: Any) {
        when (a) {
            2090 -> LogUtil.e("2090")
            is Int -> LogUtil.e("int")
            is Float -> LogUtil.e("int")
            is Double -> LogUtil.e("int")
            else -> LogUtil.e("any")
        }
    }

    // 泛型约束 ,多个类型的约束
    private fun <A> off5(t: A) where A : JSONArray, A : Comparable<A> {
        LogUtil.e("off4")
    }

}

open class TestBase {

}

class Test1 : TestBase() {

}