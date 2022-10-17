package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.android.app.R;
import com.android.app.widget.ScrollNumberView3;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_number_scroll);

        ScrollNumberView3 scrollNumberView3 = findViewById(R.id.sc3);

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollNumberView3.invalidate();
            }
        });
    }
}