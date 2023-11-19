package com.android.app.ui.activity.jetpack.navigation.navigation1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.app.R
import com.android.app.databinding.Fragment1Binding
import com.android.common.base.BaseBindingFragment
import com.android.common.utils.LogUtil

class Fragment1 : BaseBindingFragment<Fragment1Binding>() {

  override fun getBinding(
      inflater: LayoutInflater,
      container: ViewGroup?,
      attachToRoot: Boolean
  ): Fragment1Binding {
    return Fragment1Binding.inflate(layoutInflater, container, false)
  }

  override fun initData(savedInstanceState: Bundle?) {
    LogUtil.e("当前是Fragment ---> 1,当前的地址是：${this.hashCode()}")
    mBinding.btn12.setOnClickListener {
      val builder = Fragment1Args.Builder()
      builder.age = 11
      builder.name = "张三"
      val bundle = builder.build().toBundle()
      findNavController().navigate(R.id.action_fragment1_to_fragment2, bundle)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LogUtil.e("oncreate ----> ${this.toString()}")
  }

  override fun onDestroyView() {
    super.onDestroyView()
    LogUtil.e("onDestroyView ----> ${this.toString()}")
  }

  override fun onDestroy() {
    super.onDestroy()
    LogUtil.e("onDestroy ----> ${this.toString()}")
  }
}
