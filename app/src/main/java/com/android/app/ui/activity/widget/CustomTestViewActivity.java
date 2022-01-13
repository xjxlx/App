package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomTestBinding;
import com.android.helper.base.title.AppBaseBindingTitleActivity;

import org.jetbrains.annotations.NotNull;

public class CustomTestViewActivity extends AppBaseBindingTitleActivity<ActivityCustomTestBinding> {

    @Override
    protected String setTitleContent() {
        return "自定义view的练习";
    }

    @Override
    public ActivityCustomTestBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ActivityCustomTestBinding.inflate(inflater, container, true);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }
}