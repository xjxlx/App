package com.android.app.ui.activity.kotlin

import org.json.JSONObject

/**
 * @author : ζ΅ζ
 * @CreateDate: 2022/1/20-2:17 δΈε
 * @Description:
 */
interface KtTest1 {

    private fun <T> test1(t: T) where  T : JSONObject, T : Comparable<T> {

    }
}