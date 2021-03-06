package com.android.app.ui.activity.jetpack.navigation.navigation1

import com.android.app.R
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.android.helper.base.AppBaseFragment
import com.android.helper.utils.LogUtil
import kotlinx.android.synthetic.main.fragment_3.*

class Fragment3 : AppBaseFragment() {

    override fun getBaseLayout(): Int {
        return R.layout.fragment_3
    }

    override fun initView(view: View?) {
    }

     override fun initData(savedInstanceState: Bundle?) {
        LogUtil.e("当前是Fragment ---> 3,当前的地址是：${this.hashCode()}")

        btn_3_1.setOnClickListener {
            findNavController().navigate(R.id.action_fragment3_to_fragment1)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e("oncreate ----> ${this.toString()}" )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        LogUtil.e("onDestroyView ----> ${this.toString()}" )

    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e("onDestroy ----> ${this.toString()}" )

    }
}