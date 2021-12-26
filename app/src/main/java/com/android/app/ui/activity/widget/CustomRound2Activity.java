package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomRound2Binding;
import com.android.helper.base.title.BaseBindingTitleActivity;

import org.jetbrains.annotations.NotNull;

public class CustomRound2Activity extends BaseBindingTitleActivity<ActivityCustomRound2Binding> {

    @Override
    public ActivityCustomRound2Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ActivityCustomRound2Binding.inflate(inflater, container, true);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected String setTitleContent() {
        return "自定义圆形图片";
    }
}