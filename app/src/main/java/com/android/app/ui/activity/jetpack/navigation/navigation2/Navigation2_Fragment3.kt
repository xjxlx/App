package com.android.app.ui.activity.jetpack.navigation.navigation2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.app.R
import com.android.app.databinding.FragmentNavigation23Binding
import com.android.helper.base.BaseBindingFragment

class Navigation2_Fragment3 : BaseBindingFragment<FragmentNavigation23Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentNavigation23Binding {
        return FragmentNavigation23Binding.inflate(layoutInflater,container,false)
    }

    override fun initView(view: View?) {
    }

    override fun initData(savedInstanceState: Bundle?) {
        arguments?.let {
            val bundle = Navigation2_Fragment2Args.fromBundle(it)
            mBinding.tvNavigation2Args3.text = "获取到的Fragment3的参数为：$bundle"
        }

        mBinding.btnNavigation2Jump3.setOnClickListener {

//            val bundle= Bundle()
//              Navigation2_Fragment3Args.fromBundle(bundle)
//                .setAge3(34)
//                .setName3("王五")
//                .build()
//                .toBundle()

            val builder = Navigation2_Fragment3Args.Builder()
            builder.age3 = 34
            builder.name3 = "王五"
            val bundle = builder.build().toBundle()
            findNavController().navigate(R.id.action_Fragment3_to_Fragment1, bundle)
        }
    }
}