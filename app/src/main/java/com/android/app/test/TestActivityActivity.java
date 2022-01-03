package com.android.app.test;

import android.os.Bundle;

import com.android.app.R;
import com.android.helper.base.title.AppBaseTitleActivity;

public class TestActivityActivity extends AppBaseTitleActivity {

    @Override
    protected int getTitleLayout() {
        return R.layout.activity_test_activity;
    }

    @Override
    protected String setTitleContent() {
        return "测试Activity";
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }
}