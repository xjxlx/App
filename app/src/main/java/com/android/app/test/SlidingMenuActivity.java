package com.android.app.test;

import android.os.Bundle;

import com.android.app.R;
import com.android.common.base.BaseActivity;

/**
 * 侧滑的view布局
 */
public class SlidingMenuActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_sliding_menu;
    }

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }
}