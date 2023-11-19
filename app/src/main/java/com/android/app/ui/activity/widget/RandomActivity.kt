package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.android.app.R
import com.android.app.databinding.ActivityRandomBinding
import com.android.common.base.BaseBindingTitleActivity
import com.android.helper.utils.TextViewUtil
import com.android.helper.utils.ToastUtil

/** 随机布局的activity */
class RandomActivity : BaseBindingTitleActivity<ActivityRandomBinding>() {

  override fun initData(savedInstanceState: Bundle?) {
    val listData = arrayListOf<String>()
    listData.add("this")
    listData.add("in")
    listData.add("water")
    listData.add("live")
    listData.add("Many")
    listData.add("gnimals")
    val inflate = layoutInflater.inflate(R.layout.item_random, null)
    val randomContent = inflate.findViewById<TextView>(R.id.tv_random_content)
    TextViewUtil.setTextFont(mActivity, randomContent, "DINCondensedBold.ttf")

    mBinding.rlLayout.textView = randomContent
    mBinding.rlLayout.setRandomRotatingView(true)
    mBinding.rlLayout.setRandomClickListener { _, position: Int, t ->
      ToastUtil.show("position:$position   value:$t")
    }
    mBinding.btnStart.setOnClickListener { mBinding.rlLayout.setDataList(listData) }
  }

  override fun getTitleContent(): String {
    return "测试事件分发"
  }

  override fun getBinding(
      inflater: LayoutInflater,
      container: ViewGroup?,
      attachToRoot: Boolean
  ): ActivityRandomBinding {
    return ActivityRandomBinding.inflate(layoutInflater, container, true)
  }
}
