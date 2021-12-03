package com.android.app.ui.activity.java;

import android.os.Bundle;
import android.view.View;

import com.android.app.R;
import com.android.helper.base.BaseActivity;

public class JavaMapActivity extends BaseActivity {

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_java_map;
    }

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.tv_java_life);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_java_life:
                startActivity(TestJavaLifeActivity.class);
                break;
        }
    }

}