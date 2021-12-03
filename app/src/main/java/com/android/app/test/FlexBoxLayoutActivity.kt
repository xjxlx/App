package com.android.app.test

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.android.app.R
import com.android.helper.base.BaseActivity
import com.android.helper.utils.LogUtil
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_flex_box_layout.*

/**
 * 测试动态布局
 */
class FlexBoxLayoutActivity : BaseActivity() {

    override fun initListener() {

    }

     override fun initData(savedInstanceState: Bundle?) {

        fbl_root.post {
            val width1 = fbl_root.width
            LogUtil.e("width1:$width1")
            for (a in 0 until 21) {

                val frameLayout = FrameLayout(mContext)

                val textView = TextView(mContext)
//                textView.text = "测试动态布局 --->$a"
                textView.text = "--->$a"
                textView.gravity = Gravity.LEFT
                textView.setBackgroundColor(resources.getColor(R.color.blue_10))
                textView.setPadding(30, 30, 30, 30)
                textView.setTextColor(resources.getColor(R.color.blue_1))

                frameLayout.addView(textView)
                val layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.leftMargin = 10
                layoutParams.rightMargin = 10
                layoutParams.topMargin = 20
                textView.layoutParams = layoutParams

                val parameter =
                    FlexboxLayout.LayoutParams(width1 / 2, FlexboxLayout.LayoutParams.WRAP_CONTENT)
                parameter.flexBasisPercent = 0.5f
                frameLayout.layoutParams = parameter

                fbl_root.addView(frameLayout)
            }
        }
    }

    override fun getBaseLayout(): Int {
        return R.layout.activity_flex_box_layout
    }
}