package com.android.app.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.app.R;
import com.android.app.databinding.ActivityUnLockBinding;
import com.android.common.base.BaseActivity;
import com.android.common.utils.SpUtil;
import com.android.common.base.BaseBindingTitleActivity;

public class UnLockTitleActivity extends BaseActivity {

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
                SpUtil.INSTANCE.putBoolean(packageName, false);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SpUtil.INSTANCE.putBoolean("show", false);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_un_lock;
    }
}