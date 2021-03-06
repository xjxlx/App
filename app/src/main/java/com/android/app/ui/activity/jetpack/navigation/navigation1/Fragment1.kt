package com.android.app.ui.activity.jetpack.navigation.navigation1

import com.android.app.R
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.helper.base.AppBaseFragment
import com.android.helper.utils.LogUtil
import kotlinx.android.synthetic.main.fragment_1.*

class Fragment1 : AppBaseFragment() {

    override fun getBaseLayout(): Int {
        return R.layout.fragment_1
    }

    override fun initView(view: View?) {
    }

     override fun initData(savedInstanceState: Bundle?) {
        LogUtil.e("当前是Fragment ---> 1,当前的地址是：${this.hashCode()}")
        btn_1_2.setOnClickListener {

            val bundle = Fragment1Args.Builder()
                    .setName("张三")
                    .setAge(11)
                    .build()
                    .toBundle()

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