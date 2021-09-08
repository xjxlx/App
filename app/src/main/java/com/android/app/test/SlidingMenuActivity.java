package com.android.app.test;

import com.android.app.R;
import com.android.helper.base.BaseActivity;

/**
 * 侧滑的view布局
 */
public class SlidingMenuActivity extends BaseActivity {

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_sliding_menu;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleContent("侧滑的View");
    }

    @Override
    public void initData() {

    }
}