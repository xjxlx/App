package com.android.app.ui.activity.java;

import android.os.Bundle;
import android.view.View;

import com.android.app.R;
import com.android.common.base.BaseActivity;

public class JavaMapActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_java_map;
    }

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.tv_java_life, R.id.tv_back);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_java_life:
                startActivity(TestJavaLifeActivity.class);
                break;
        }
    }

}