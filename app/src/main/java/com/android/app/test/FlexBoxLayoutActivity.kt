package com.android.app.test

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.android.app.R
import com.android.app.databinding.ActivityFlexBoxLayoutBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.google.android.flexbox.FlexboxLayout

/**
 * 测试动态布局
 */
class FlexBoxLayoutActivity : AppBaseBindingTitleActivity<ActivityFlexBoxLayoutBinding>() {

    override fun initData(savedInstanceState: Bundle?) {
        mBinding.fblRoot.post {
            val width1 = mBinding.fblRoot.width
            LogUtil.e("width1:$width1")
            for (a in 0 until 21) {

                val frameLayout = FrameLayout(mActivity)
                val textView = TextView(mActivity)
//                textView.text = "测试动态布局 --->$a"
                textView.text = "--->$a"
                textView.gravity = Gravity.LEFT
                textView.setBackgroundColor(resources.getColor(R.color.blue_10))
                textView.setPadding(30, 30, 30, 30)
                textView.setTextColor(resources.getColor(R.color.blue_1))

                frameLayout.addView(textView)
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.leftMargin = 10
                layoutParams.rightMargin = 10
                layoutParams.topMargin = 20
                textView.layoutParams = layoutParams

                val parameter = FlexboxLayout.LayoutParams(width1 / 2, FlexboxLayout.LayoutParams.WRAP_CONTENT)
                parameter.flexBasisPercent = 0.5f
                frameLayout.layoutParams = parameter

                mBinding.fblRoot.addView(frameLayout)
            }
        }
    }

    override fun setTitleContent(): String {
        return "测试FlexboxLayout"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityFlexBoxLayoutBinding {
        return ActivityFlexBoxLayoutBinding.inflate(layoutInflater, container, true)
    }
}