package com.android.app.ui.activity.jetpack.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.app.databinding.FragmentModel2Binding
import com.android.common.base.BaseBindingFragment

class Model2Fragment : BaseBindingFragment<FragmentModel2Binding>() {

  override fun getBinding(
      inflater: LayoutInflater,
      container: ViewGroup?,
      attachToRoot: Boolean
  ): FragmentModel2Binding {
    return FragmentModel2Binding.inflate(layoutInflater, container, false)
  }

  override fun initData(savedInstanceState: Bundle?) {
    mBinding.btnF2Change.setOnClickListener {
      val get = ViewModelProvider(this).get(TestViewModel::class.java)
      mBinding.tvContentFr2.text = get.name
    }
  }

  companion object {
    private val fragment by lazy {
      return@lazy Model2Fragment()
    }

    fun getInstance(): Model2Fragment {
      return fragment
    }
  }
}
