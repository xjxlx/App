package com.android.app.ui.activity.widget;

import android.os.Bundle;

import com.android.app.R;
import com.android.app.databinding.ActivityProgressBinding;
import com.android.helper.base.AppBaseActivity;

public class ProgressTitleActivity extends AppBaseActivity {

    private ActivityProgressBinding binding;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_progress;
    }

    @Override
    public void initView() {
        binding = ActivityProgressBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }
}