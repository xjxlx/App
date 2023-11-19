package com.android.app.ui.activity.jetpack.navigation.navigation2

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.app.R
import com.android.app.databinding.FragmentNavigation21Binding
import com.android.common.base.BaseBindingFragment

class Navigation2_Fragment1 : BaseBindingFragment<FragmentNavigation21Binding>() {

  override fun getBinding(
      inflater: LayoutInflater,
      container: ViewGroup?,
      attachToRoot: Boolean
  ): FragmentNavigation21Binding {
    return FragmentNavigation21Binding.inflate(layoutInflater, container, false)
  }

  @SuppressLint("SetTextI18n")
  override fun initData(savedInstanceState: Bundle?) {
    arguments?.let {
      val bundle = Navigation2_Fragment3Args.fromBundle(it)
      mBinding.tvNavigation2Args1.text = "获取到的Fragment3的参数为：$bundle"
    }

    mBinding.btnNavigation2Jump1.setOnClickListener {
      val builder = Navigation2_Fragment1Args.Builder()
      builder.age = 12
      builder.name = "张三"
      val bundle = builder.build().toBundle()
      findNavController().navigate(R.id.action_Fragment1_to_Fragment2, bundle)
    }
  }
}
