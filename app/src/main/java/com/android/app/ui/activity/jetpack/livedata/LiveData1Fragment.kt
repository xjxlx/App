package com.android.app.ui.activity.jetpack.livedata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.app.databinding.FragmentLiveData1Binding
import com.android.helper.base.BaseBindingFragment

class LiveData1Fragment : BaseBindingFragment<FragmentLiveData1Binding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLiveData1Binding {
        return FragmentLiveData1Binding.inflate(layoutInflater, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // liveData
//        mViewModel = ViewModelProvider(it).get(ThemeViewModel::class.java)
//        // 监听数据变化
//        val observer = observer()
//        if (observer != null) {
//            mViewModel?.liveData?.observe(it, observer)
//        }

        val viewModel = ViewModelProvider(this)[LiveDataModel::class.java]
        viewModel.liveData.observe(this) { t -> mBinding.tvLiveDateText1.text = t?.name }

        // mutableLiveModel
        val mutableLiveModel = ViewModelProvider(this)[MutableLiveModel::class.java]
        mutableLiveModel.data.observe(this) { t -> mBinding.tvLiveDateText1.text = t?.name }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LiveData1Fragment()
    }
}