package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomChargingBinding;
import com.android.common.utils.LogUtil;
import com.android.common.base.BaseBindingTitleActivity;

import org.jetbrains.annotations.NotNull;

public class CustomChargingActivity extends BaseBindingTitleActivity<ActivityCustomChargingBinding> {

    @Override
    public void initData(Bundle savedInstanceState) {
        mBinding.cpv.setInterval(0.6f, 1f);
        mBinding.cpv.setOptimumValue(0.8f);
        mBinding.cpv.setRemainingChargeTime("5小时20分");
        // mBinding.cpv.setProgressListener(progress -> LogUtil.e("ProgressListener --->
        // onTouchUp:" + progress));
        mBinding.pb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LogUtil.e("progress:" + progress);
                float v = progress * 0.1f;
                float v1 = v / 10;
                LogUtil.e("v:" + v + "   v1:" + v1);
                mBinding.cpv.setPercentage(v1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mBinding.ev.setMaxIntervalScope(62);
        mBinding.ev.setCurrentValue(20);

    }

    @NonNull
    @Override
    public ActivityCustomChargingBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityCustomChargingBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "自定义充电";
    }
}