package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityProgressBinding;
import com.android.common.base.BaseBindingTitleActivity;

public class ProgressTitleActivity extends BaseBindingTitleActivity<ActivityProgressBinding> {

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    public ActivityProgressBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityProgressBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "";
    }
}