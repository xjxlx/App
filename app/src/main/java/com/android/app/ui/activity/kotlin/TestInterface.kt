package com.android.app.ui.activity.kotlin

import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/1/19-23:29
 * @Description:
 */

/**
 * 泛型接口
 */
interface TestInterface<T> {

    private fun off(t: T) {
    }
}

// 泛型类
class Color<R> {

    private var a: R? = null
    private fun off() {

    }

    // 泛型方法
    private fun <T> off2(t: T) {

    }

    // 返回值可空
    private fun <T> off3(t: T?): T? {
        return t
    }

    // 泛型约束
    private fun <T : Package> off4(t: T) {
        LogUtil.e("off4")
    }

}