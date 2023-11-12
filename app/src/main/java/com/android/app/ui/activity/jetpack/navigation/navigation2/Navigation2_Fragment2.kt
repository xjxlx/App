package com.android.app.ui.activity.jetpack.navigation.navigation2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.app.R
import com.android.app.databinding.FragmentNavigation22Binding
import com.android.helper.base.BaseBindingFragment

class Navigation2_Fragment2 : BaseBindingFragment<FragmentNavigation22Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentNavigation22Binding {
        return FragmentNavigation22Binding.inflate(layoutInflater,container,false)
    }

    override fun initView(view: View?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
        arguments?.let {
            val bundle = Navigation2_Fragment1Args.fromBundle(it)
            mBinding.tvNavigation2Args2.text = "获取到的Fragment3的参数为：$bundle"
        }

        mBinding.btnNavigation2Jump2.setOnClickListener {
            val builder = Navigation2_Fragment2Args.Builder()
            builder.name2 = "李四"
            builder.age2 = 23

            val bundle = builder.build().toBundle()
            findNavController().navigate(R.id.action_Fragment2_to_Fragment3, bundle)
        }
    }
}