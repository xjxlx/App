package com.android.app.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.android.helper.utils.LogUtil

/**
 * @author : 流星
 * @CreateDate: 2022/10/18-0:02
 * @Description:
 */
@SuppressLint("ViewConstructor")
class ViewPager2 : ViewGroup {

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in 0 until childCount) {
            LogUtil.e("index:$index")
            val childAt = getChildAt(index)
            // 开始摆放位置
            childAt.layout(width * index, 0, (width + 1) * index + width, height)
        }
    }


}