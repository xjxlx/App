package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityCustomBreathingViewBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

class CustomBreathingViewActivity : AppBaseBindingTitleActivity<ActivityCustomBreathingViewBinding>() {

    override fun setTitleContent(): String {
        return "呼吸效果"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomBreathingViewBinding {
        return ActivityCustomBreathingViewBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        setonClickListener(mBinding.btnStart1, mBinding.btnStart2, mBinding.btnStart3, mBinding.btnStop)

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btn_start1 -> {
                mBinding.bvView.startTransparentCircle()
            }

            R.id.btn_start2 -> {
                mBinding.bvView.startSmallSolidCircle()
            }

            R.id.btn_start3 -> {
                mBinding.bvView.startBigToSmallCircle()
            }

            R.id.btn_stop -> {
                mBinding.bvView.clear()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        mBinding.bvView.clear()
    }
}