package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomRound2Binding;
import com.android.common.base.BaseBindingTitleActivity;

public class CustomRound2Activity extends BaseBindingTitleActivity<ActivityCustomRound2Binding> {

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "自定义圆形图片";
    }

    @NonNull
    @Override
    public ActivityCustomRound2Binding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityCustomRound2Binding.inflate(inflater, container, true);
    }
}