package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomTestBinding;
import com.android.common.base.BaseBindingTitleActivity;

public class CustomTestViewActivity extends BaseBindingTitleActivity<ActivityCustomTestBinding> {

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    public ActivityCustomTestBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityCustomTestBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "自定义view的练习";
    }
}