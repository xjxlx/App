package com.android.app.ui.activity.widget;

import com.android.app.R;
import com.android.app.databinding.ActivityNestSlidingViewBinding;
import com.android.app.widget.NestSlidingView;
import com.android.common.base.BaseBindingTitleActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NestSlidingViewActivity extends BaseBindingTitleActivity<ActivityNestSlidingViewBinding> {

    @Override
    public void initView() {
        View btnTest = findViewById(R.id.btn_test);
        View rlMiddle = findViewById(R.id.rl_middle);
        NestSlidingView nsv_layout = findViewById(R.id.nsv_layout);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // rlMiddle.scrollTo(0, -50);
                nsv_layout.testRefesh();

            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @NonNull
    @Override
    public ActivityNestSlidingViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityNestSlidingViewBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "";
    }
}