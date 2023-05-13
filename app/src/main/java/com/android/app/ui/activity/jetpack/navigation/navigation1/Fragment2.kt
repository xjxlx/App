package com.android.app.ui.activity.jetpack.navigation.navigation1

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.app.R
import com.android.app.databinding.Fragment2Binding
import com.android.helper.base.BaseBindingFragment
import com.android.helper.utils.LogUtil

class Fragment2 : BaseBindingFragment<Fragment2Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): Fragment2Binding {
        return Fragment2Binding.inflate(layoutInflater,container,false)
    }

    @SuppressLint("SetTextI18n")
    override fun initData(savedInstanceState: Bundle?) {
        LogUtil.e("当前是Fragment ---> 2,当前的地址是：${this.hashCode()}")

        arguments?.let {
            // todo
//            val bundle = Fragment1Args.fromBundle(it)
//            tv_content2.text = "获取到的名字是：${bundle.name} 获取到的年龄为：${bundle.age}"
        }

        mBinding.btn23.setOnClickListener {
            findNavController().navigate(R.id.action_fragment2_to_fragment3)
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