package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomChargingBinding;
import com.android.helper.base.title.BaseBindingTitleActivity;

import org.jetbrains.annotations.NotNull;

public class CustomChargingActivity extends BaseBindingTitleActivity<ActivityCustomChargingBinding> {
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

    }
}