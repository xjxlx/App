package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityCustomBreathingViewBinding
import com.android.app.widget.BreatheView3
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
        setonClickListener(mBinding.btnStart1, mBinding.btnStart2, mBinding.btnStart3, mBinding.btnStop)

        mBinding.bvView.setCallBackListener(object : BreatheView3.CallBackListener {
            override fun onStartSmallToBigAlpha() {
                LogUtil.e(" **** 小到大 透明  的开始")
            }

            override fun onStartSmallToBig() {
                LogUtil.e(" **** 小到大 实心  的开始")
            }

            override fun onEndSmallToBig() {
                LogUtil.e(" **** 小到大 实心  的结束")
            }

            override fun onEndBigToSmallAlpha() {
                LogUtil.e(" **** 大到小 透明  的结束")
            }
        })
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btn_start1 -> {
                mBinding.bvView.startSmallToBigAlphaLoop()
            }

            R.id.btn_start2 -> {
                mBinding.bvView.startSmallToBig()
            }

            R.id.btn_start3 -> {
                mBinding.bvView.startBigToSmall()
//                mBinding.bvView.startBigToSmallAlphaLoop()
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