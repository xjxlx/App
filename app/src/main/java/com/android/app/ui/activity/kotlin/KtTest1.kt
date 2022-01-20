package com.android.app.ui.activity.kotlin

import org.json.JSONObject

/**
 * @author : 流星
 * @CreateDate: 2022/1/20-2:17 下午
 * @Description:
 */
interface KtTest1 {

    private fun <T> test1(t: T) where  T : JSONObject, T : Comparable<T> {

    }
}