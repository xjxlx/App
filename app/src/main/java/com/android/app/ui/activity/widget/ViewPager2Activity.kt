package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.android.app.R
import com.android.app.databinding.ActivityViewPager2Binding
import com.android.common.base.BaseBindingTitleActivity

class ViewPager2Activity : BaseBindingTitleActivity<ActivityViewPager2Binding>() {

    override fun getTitleContent(): String {
        return "ViewPager2"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityViewPager2Binding {
        return ActivityViewPager2Binding.inflate(inflater, container, true)
    }

    private val mImageIds = intArrayOf(R.mipmap.a1, R.mipmap.a2, R.mipmap.a3, R.mipmap.a4, R.mipmap.a5, R.mipmap.a6)

    override fun initData(savedInstanceState: Bundle?) {
        for (item in mImageIds) {
            val view = ImageView(this)
            view.setBackgroundResource(item)
            mBinding.vp2.addView(view)
        }

        val inflate = LayoutInflater.from(this).inflate(R.layout.test_item, null, false)
        mBinding.vp2.addView(inflate, 3)
    }
}
