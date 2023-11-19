package com.android.app.ui.activity.jetpack.model

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.app.databinding.FragmentModel1Binding
import com.android.common.base.BaseBindingFragment

class Model1Fragment : BaseBindingFragment<FragmentModel1Binding>() {

  override fun getBinding(
      inflater: LayoutInflater,
      container: ViewGroup?,
      attachToRoot: Boolean
  ): FragmentModel1Binding {
    return FragmentModel1Binding.inflate(layoutInflater, container, false)
  }

  override fun initData(savedInstanceState: Bundle?) {
    mBinding.btnF1Change.setOnClickListener {
      val get = ViewModelProvider(this).get(TestViewModel::class.java)
      mBinding.tvContentFr1.text = get.name
    }
  }

  companion object {
    private val fragment: Model1Fragment by lazy {
      return@lazy Model1Fragment()
    }

    fun newInstance(): Model1Fragment {
      return fragment
    }
  }
}
