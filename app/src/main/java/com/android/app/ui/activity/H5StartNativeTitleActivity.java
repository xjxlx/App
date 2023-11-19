package com.android.app.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityH5StartNativeBinding;
import com.android.common.base.BaseBindingTitleActivity;
import com.android.helper.utils.ToastUtil;

public class H5StartNativeTitleActivity extends BaseBindingTitleActivity<ActivityH5StartNativeBinding> {

    @Override
    public void initData(Bundle savedInstanceState) {
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

    @NonNull
    @Override
    public ActivityH5StartNativeBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityH5StartNativeBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "H5 - 调用";
    }
}