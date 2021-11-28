package com.android.app.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.FragmentPersonalBinding
import com.android.app.ui.activity.personal.RouseDingDingActivity
import com.android.helper.base.BaseBindingFragment

/**
 * 个人中心的fragment
 */
class PersonalFragment : BaseBindingFragment<FragmentPersonalBinding>() {

    override fun initData() {
        mBinding.tvRouseDd.setOnClickListener {
            startActivity(RouseDingDingActivity::class.java)
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentPersonalBinding {
        return FragmentPersonalBinding.inflate(inflater, container, false)
    }
}
