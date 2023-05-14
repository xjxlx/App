package com.android.app.ui.activity.jetpack.navigation.navigation1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.app.R
import com.android.app.databinding.Fragment3Binding
import com.android.helper.base.BaseBindingFragment
import com.android.helper.utils.LogUtil

class Fragment3 : BaseBindingFragment<Fragment3Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): Fragment3Binding {
        return Fragment3Binding.inflate(layoutInflater, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        LogUtil.e("当前是Fragment ---> 3,当前的地址是：${this.hashCode()}")

        mBinding.btn31.setOnClickListener {
            findNavController().navigate(R.id.action_fragment3_to_fragment1)
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