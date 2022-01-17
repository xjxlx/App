package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityKotlinMapBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil

/**
 * @author XJX
 * @CreateDate: 2021/9/19-16:52
 * @Description: Kotlin的项目
 */
class KotlinMap : AppBaseBindingTitleActivity<ActivityKotlinMapBinding>() {
    
    var ssss: String = "sss"
    
    override fun initData(savedInstanceState: Bundle?) {
        val s = "abcdefg"
        var a = s[0]
        LogUtil.e("abc  ${a + "sss"}")
        
        LogUtil.e("{\"key+:\"value\"}")
        LogUtil.e("{\"key:value}\"")
        LogUtil.e("{$s:$a}")
        // && 和 ||
        val a1 = true
        val a2 = false
        val and = a1.and(a2)
        val and2 = a2.or(a1)
        if (and) {
            LogUtil.e("true")
        } else {
            LogUtil.e("false")
        }
        if (and2) {
            LogUtil.e("true")
        } else {
            LogUtil.e("false")
        }
        LogUtil.e("java && " + { and })
        // 区间运算符
        for (a in 1..3) {
            LogUtil.e("a :$a")
        }
        
        for (b in 3 downTo 1) {
            LogUtil.e("b :$b")
        }
        
    }
    
    override fun setTitleContent(): String {
        return "ktolin集合"
    }
    
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityKotlinMapBinding {
        return ActivityKotlinMapBinding.inflate(inflater, container, false)
    }
}