package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomNumberScrollBinding;
import com.android.common.base.BaseBindingTitleActivity;

public class CustomNumberScrollActivity extends BaseBindingTitleActivity<ActivityCustomNumberScrollBinding> {

    private final String[] mNumbers = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "20"};

    @Override
    public void initData(Bundle savedInstanceState) {
        mBinding.btnStart.setOnClickListener((View.OnClickListener) v -> {
            mBinding.sc3.invalidate();
            mBinding.sc3.requestLayout();
            // mBinding.sc3.requestLayout();
        });

    }

    @NonNull
    @Override
    public ActivityCustomNumberScrollBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityCustomNumberScrollBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "滑动数字";
    }
}