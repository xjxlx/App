package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import com.android.app.databinding.ActivityCzfBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.common.CommonConstants
import com.android.helper.utils.LogUtil
import com.android.helper.widget.ChargingProgressView
import com.android.helper.widget.ElectricityView

class CzfActivity() : AppBaseBindingTitleActivity<ActivityCzfBinding>() {

    override fun setTitleContent(): String {
        return "Kotlin操作符"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCzfBinding {
        return ActivityCzfBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {

        mBinding.electricityView.setCurrentValue(62)
        mBinding.electricityView.setMinIntervalScope(5)
        mBinding.electricityView.setMaxIntervalScope(62)
        mBinding.electricityView.setProgressListener(object : ElectricityView.ProgressListener {
            override fun onTouchUp(progress: Int) {
                LogUtil.e("progress 手指抬起：：:$progress")
            }

            override fun onMove(progress: String?) {
                LogUtil.e("progress onMove：：:$progress")

            }
        })

        mBinding.cpvProgress.setInterval(0.6f, 1f) // 拖动SOC的区域
        mBinding.cpvProgress.setOptimumValue(0.9f) // 最佳soc值
        mBinding.cpvProgress.setProgressListener(object : ChargingProgressView.ProgressListener {
            /**
             * @param progress 手指抬起时候，当前的百分比
             */
            override fun onTouchUp(progress: String) {
                LogUtil.writeAll(CommonConstants.FILE_CHARGING_CENTER_NAME, "充电中心：", "--------> 充电中心--->充电SOC指令已发送  soc:" + progress.toInt())
            }

            override fun onMove(progress: String) {
                LogUtil.writeAll(CommonConstants.FILE_CHARGING_CENTER_NAME, "充电中心：", "--------> onMove 充电中心--->充电SOC指令已发送  soc:" + progress.toInt())
            }
        })

        mBinding.seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                mBinding.cpvProgress.setPercentage(progress.toFloat() / 100)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}