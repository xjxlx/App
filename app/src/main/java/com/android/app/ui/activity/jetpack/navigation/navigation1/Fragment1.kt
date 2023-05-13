package com.android.app.ui.activity.jetpack.navigation.navigation1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.Fragment1Binding
import com.android.helper.base.BaseBindingFragment
import com.android.helper.utils.LogUtil

class Fragment1 : BaseBindingFragment<Fragment1Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): Fragment1Binding {
        return Fragment1Binding.inflate(layoutInflater,container,false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        LogUtil.e("当前是Fragment ---> 1,当前的地址是：${this.hashCode()}")
        mBinding.btn12.setOnClickListener {

            // todo
            //val bundle = Fragment1Args.Builder().setName("张三").setAge(11).build().toBundle()
//            findNavController().navigate(R.id.action_fragment1_to_fragment2, bundle)
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