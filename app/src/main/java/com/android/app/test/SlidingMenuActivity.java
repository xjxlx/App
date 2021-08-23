package com.android.app.test;

import com.android.app.R;

import com.android.helper.base.BaseTitleActivity;

/**
 * 侧滑的view布局
 */
public class SlidingMenuActivity extends BaseTitleActivity {

    @Override
    protected int getTitleLayout() {
        return R.layout.activity_sliding_menu;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitleContent("侧滑的View");
    }
}