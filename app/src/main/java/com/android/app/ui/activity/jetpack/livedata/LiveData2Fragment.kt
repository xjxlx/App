package com.android.app.ui.activity.jetpack.livedata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.app.databinding.FragmentLiveData2Binding
import com.android.helper.base.BaseBindingFragment

class LiveData2Fragment : BaseBindingFragment<FragmentLiveData2Binding>() {

    override fun initData(savedInstanceState: Bundle?) {
        // liveData
        val viewModel = ViewModelProvider(this).get(LiveDataModel::class.java)
        viewModel.liveData.observe(this) { t -> mBinding.tvLiveDateText2.text = t?.name }

        // mutableLiveModel
        val mutableLiveModel = ViewModelProvider(this).get(MutableLiveModel::class.java)
        mutableLiveModel.data.observe(this) { t -> mBinding.tvLiveDateText2.text = t?.name }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLiveData2Binding {
        return FragmentLiveData2Binding.inflate(layoutInflater, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = LiveData2Fragment()
    }
}