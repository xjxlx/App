package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomChargingBinding;
import com.android.helper.base.title.AppBaseBindingTitleActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.widget.ChargingProgressView;
import com.android.helper.widget.ElectricityView;

import org.jetbrains.annotations.NotNull;

public class CustomChargingActivity extends AppBaseBindingTitleActivity<ActivityCustomChargingBinding> {

    @Override
    protected String setTitleContent() {
        return "自定义充电";
    }

    @Override
    public ActivityCustomChargingBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ActivityCustomChargingBinding.inflate(inflater, container, true);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        mBinding.cpv.setInterval(0.6f, 1f);
        mBinding.cpv.setOptimumValue(0.8f);
        mBinding.cpv.setRemainingChargeTime("5小时20分");
        mBinding.cpv.setProgressListener(new ChargingProgressView.ProgressListener() {

            @Override
            public void onTouchUp(String progress) {
                LogUtil.e("ProgressListener ---> onTouchUp:" + progress);
            }
        });

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
        mBinding.ev.setProgressListener(progress -> {

        });

    }
}