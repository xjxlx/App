package com.android.app.ui.activity.widget;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.android.app.R;

public class CustomNumberScrollActivity extends FragmentActivity {
//
//    @Override
//    protected int getTitleLayout() {
//        return R.layout.activity_custom_number_scroll;
//    }
//
//    @Override
//    protected String setTitleContent() {
//        return "自定义数字滑动";
//    }
//
//    @Override
//    public void initData(Bundle savedInstanceState) {
//
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_number_scroll);
    }
}