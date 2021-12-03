package com.android.app.ui.activity.widget;

import android.os.Bundle;

import com.android.app.R;

import com.android.helper.base.BaseActivity;
import com.android.helper.utils.LogUtil;

public class CustomTestActivity extends BaseActivity {

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_custom_test;
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
}