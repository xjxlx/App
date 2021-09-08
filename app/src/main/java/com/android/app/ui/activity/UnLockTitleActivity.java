package com.android.app.ui.activity;

import android.content.Intent;
import com.android.app.R;
import com.android.app.databinding.ActivityUnLockBinding;
import android.view.View;

import com.android.helper.base.BaseActivity;
import com.android.helper.utils.SpUtil;

public class UnLockTitleActivity extends BaseActivity {

    private ActivityUnLockBinding binding;

    @Override
    public void initView() {
        super.initView();
        binding = ActivityUnLockBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initData() {


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