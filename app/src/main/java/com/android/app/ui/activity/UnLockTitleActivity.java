package com.android.app.ui.activity;

import android.content.Intent;

import com.android.app.R;
import com.android.app.databinding.ActivityUnLockBinding;

import android.os.Bundle;
import android.view.View;

import com.android.helper.base.AppBaseActivity;
import com.android.helper.utils.SpUtil;

public class UnLockTitleActivity extends AppBaseActivity {

    private ActivityUnLockBinding binding;

    @Override
    public void initView() {
         binding = ActivityUnLockBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String packageName = intent.getStringExtra("packageName");
        binding.btnUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.putBoolean(packageName, false);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpUtil.putBoolean("show", false);
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_un_lock;
    }

}