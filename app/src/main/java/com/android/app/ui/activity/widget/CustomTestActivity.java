package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomTestBinding;
import com.android.common.base.BaseBindingTitleActivity;
import com.android.common.utils.LogUtil;

public class CustomTestActivity extends BaseBindingTitleActivity<ActivityCustomTestBinding> {

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        float a = 43.1f;
        int round = Math.round(a);
        LogUtil.e("a --->" + round);
        float B = 43.9f;
        int round2 = Math.round(B);
        LogUtil.e("B --->" + round2);
    }

    @NonNull
    @Override
    public ActivityCustomTestBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityCustomTestBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "";
    }
}