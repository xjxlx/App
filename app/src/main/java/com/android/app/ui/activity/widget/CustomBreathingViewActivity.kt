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
        setonClickListener(mBinding.btnStart, mBinding.btnPause, mBinding.btnClear)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btn_start -> {
                mBinding.bvView.startAnimation()
            }

            R.id.btn_pause -> {
                val pause = mBinding.bvView.isPause()
                mBinding.bvView.pause(!pause)
            }

            R.id.btn_clear -> {
                mBinding.bvView.clear()
            }
        }
    }

}