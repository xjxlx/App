package com.android.app.ui.activity.widget;

import com.android.app.R;
import com.android.app.databinding.ActivityProgressBinding;

import com.android.helper.base.BaseTitleActivity;

public class ProgressTitleActivity extends BaseTitleActivity {

    private ActivityProgressBinding binding;

    @Override
    protected int getTitleLayout() {
        return R.layout.activity_progress;
    }

    @Override
    protected void initView() {
        super.initView();
        binding = ActivityProgressBinding.inflate(getLayoutInflater());
    }
}