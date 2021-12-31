package com.android.app.ui.activity.widget;

import com.android.app.R;
import com.android.app.widget.NestSlidingView;
import com.android.helper.base.AppBaseActivity;

import android.os.Bundle;
import android.view.View;

public class NestSlidingViewActivity extends AppBaseActivity {

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_nest_sliding_view;
    }

    @Override
    public void initView() {
        View btnTest = findViewById(R.id.btn_test);
        View rlMiddle = findViewById(R.id.rl_middle);
        NestSlidingView nsv_layout = findViewById(R.id.nsv_layout);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                rlMiddle.scrollTo(0, -50);
                nsv_layout.testRefesh();

            }
        });
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

}