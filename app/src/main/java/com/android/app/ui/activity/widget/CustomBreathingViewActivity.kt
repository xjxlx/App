package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityCustomBreathingViewBinding
import com.android.app.widget.BreathView
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil

class CustomBreathingViewActivity : AppBaseBindingTitleActivity<ActivityCustomBreathingViewBinding>() {

    override fun setTitleContent(): String {
        return "呼吸效果"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomBreathingViewBinding {
        return ActivityCustomBreathingViewBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
        setonClickListener(mBinding.btnStart, mBinding.btnPause, mBinding.btnResume, mBinding.btnClear)

        mBinding.bvView.setCallBackListener(object : BreathView.CallBackListener {
            override fun onStart() {
                LogUtil.e(" -----> start 开始 -----> ")
                mBinding.tvStatus.text = "<--- 开始 --->"
            }

            override fun statusChange(status: Int) {
                when (status) {
                    1 -> {
                        mBinding.tvStatus.text = "<--- 吸气 --->"
                    }
                    2 -> {
                        mBinding.tvStatus.text = "<--- 屏气 --->"
                    }
                    3 -> {
                        mBinding.tvStatus.text = "<--- 呼气 --->"
                    }
                    4 -> {
                        mBinding.tvStatus.text = "<--- finish --->"
                    }
                }
            }
        })
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
            }
        }
    }

}