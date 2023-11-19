package com.android.app.test;

import android.os.Bundle;
import android.view.View;

import com.android.app.R;
import com.android.common.base.BaseActivity;
import com.android.common.utils.LogUtil;

/**
 * 测试滑动工具的帮助类
 */
public class TestScrollHelperActivity extends BaseActivity {

    private android.widget.ImageView mIvHead;

    @Override
    public int getLayout() {
        return R.layout.activity_test_scroll_helper;
    }

    @Override
    public void initView() {
        mIvHead = findViewById(R.id.iv_head);
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