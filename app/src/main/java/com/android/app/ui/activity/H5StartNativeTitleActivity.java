package com.android.app.ui.activity;

import android.content.Intent;
import com.android.app.R;
import com.android.app.databinding.ActivityH5StartNativeBinding;
import android.net.Uri;

import com.android.helper.base.BaseTitleActivity;
import com.android.helper.utils.ToastUtil;

public class H5StartNativeTitleActivity extends BaseTitleActivity {

    private ActivityH5StartNativeBinding binding;

    @Override
    protected int getTitleLayout() {
        return R.layout.activity_h5_start_native;
    }

    @Override
    protected void initView() {
        super.initView();
        binding = ActivityH5StartNativeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initData() {
        super.initData();
        Intent intent = getIntent();
        if (intent != null) {
            Uri data = intent.getData();
            if (data != null) {
                String arg1 = data.getQueryParameter("arg1");
                String arg2 = data.getQueryParameter("arg2");
                ToastUtil.show("arg1:" + arg1 + "   ----->  arg2:" + arg2);
            }
        }
    }

}