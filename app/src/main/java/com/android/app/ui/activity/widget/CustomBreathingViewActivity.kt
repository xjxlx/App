package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityCustomBreathingViewBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.DeviceUtil
import com.android.helper.utils.LogUtil

class CustomBreathingViewActivity : AppBaseBindingTitleActivity<ActivityCustomBreathingViewBinding>() {

    override fun setTitleContent(): String {
        return "呼吸效果"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomBreathingViewBinding {
        return ActivityCustomBreathingViewBinding.inflate(inflater, container, true)
    }

    private var count = 0
    override fun initData(savedInstanceState: Bundle?) {
        setonClickListener(mBinding.btnStart, mBinding.btnPause, mBinding.btnResume, mBinding.btnClear)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btn_start -> {
                val translationX = mBinding.ivAnimation.translationX
                LogUtil.e("translationX: $translationX")
                mBinding.bvView.startAnimation()
            }

            R.id.btn_resume -> {
                mBinding.bvView.resume()
            }

            R.id.btn_pause -> {
                mBinding.bvView.pause()
            }

            R.id.btn_clear -> {
                mBinding.bvView.stop()
                DeviceUtil.instance?.getDeviceId2("111")
            }
        }
    }

}