package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityCustomBreathingViewBinding
import com.android.app.widget.BreatheView4
import com.android.helper.base.title.AppBaseBindingTitleActivity
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
        setonClickListener(mBinding.btnStart, mBinding.btnPause, mBinding.btnClear)

        mBinding.bvView.setCallBackListener(object : BreatheView4.CallBackListener {
            override fun onStart() {
                mBinding.tvType.text = "开始"
            }

            override fun onRestart() {
                mBinding.tvType.text = "重新开始" + (count++)
            }

            override fun statusChange(status: Int) {
                var msg = ""
                when (status) {
                    1 -> {
                        msg = " 吸气"
                    }
                    2 -> {
                        msg = " 憋气"
                    }
                    3 -> {
                        msg = " 呼气"
                    }
                }
                mBinding.tvStatus.text = msg
            }
        })
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btn_start -> {
                //   mBinding.bvView.startAnimation()
//                mBinding.ivAnimation.translationX = 200f
                val translationX = mBinding.ivAnimation.translationX
                LogUtil.e("translationX: "+translationX)
            }

            R.id.btn_pause -> {
//                val pause = mBinding.bvView.isPause()
//                mBinding.bvView.pause(!pause)
                mBinding.ivAnimation.x = 200f
            }

            R.id.btn_clear -> {
                mBinding.bvView.clear()
            }
        }
    }

}