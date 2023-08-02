package com.android.app.test;

import android.os.Bundle;
import android.view.View;

import com.android.app.R;
import com.android.common.utils.LogUtil;
import com.android.helper.base.AppBaseActivity;

/**
 * 测试滑动工具的帮助类
 */
public class TestScrollHelperActivity extends AppBaseActivity {

    private android.widget.ImageView mIvHead;
    private android.view.View mVReadView;
    private android.view.View mVBlueView;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_test_scroll_helper;
    }

    @Override
    public void initView() {
        mIvHead = findViewById(R.id.iv_head);
        mVReadView = findViewById(R.id.v_read_view);
        mVBlueView = findViewById(R.id.v_blue_view);

        int measureSpec = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.AT_MOST);

        mIvHead.measure(0, 0);

        int measuredWidth = mIvHead.getMeasuredWidth();
        int measuredHeight = mIvHead.getMeasuredHeight();

        LogUtil.e("measuredWidth:" + measuredWidth + "   measuredHeight：" + measuredHeight);

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }
}