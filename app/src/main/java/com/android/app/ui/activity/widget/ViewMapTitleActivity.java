package com.android.app.ui.activity.widget;

import android.annotation.SuppressLint;
import com.android.app.R;
import com.android.app.databinding.ActivityViewMapBinding;
import com.android.app.test.SlidingMenuActivity;
import com.android.app.ui.activity.hmview.HmCustomViewActivity;
import android.view.View;

import com.android.helper.base.BaseTitleActivity;

public class ViewMapTitleActivity extends BaseTitleActivity {

    private ActivityViewMapBinding binding;

    @Override
    protected int getTitleLayout() {
        return R.layout.activity_view_map;
    }

    @Override
    protected void initView() {
        super.initView();
        binding = ActivityViewMapBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initListener() {
        super.initListener();

        setonClickListener(
                R.id.tv_progress,
                R.id.tv_multiple_list_view, R.id.tv_custom_text, R.id.tv_custom_round,
                R.id.tv_custom_random, R.id.tv_custom_left_and_right, R.id.tv_custom_progress,
                R.id.tv_custom_touch, R.id.tv_custom_input_password, R.id.tv_scroll_view,
                R.id.tv_page_view, R.id.tv_test_hm, R.id.tv_custom_menu, R.id.tv_custom_name_list
        );
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_progress:
                startActivity(ProgressTitleActivity.class);
                break;
            case R.id.tv_multiple_list_view:
                startActivity(MultipleListViewActivity.class);
                break;
            case R.id.tv_custom_text:
                startActivity(CustomTestActivity.class);
                break;
            case R.id.tv_custom_round:
                startActivity(CustomRoundImageActivity.class);
                break;
            case R.id.tv_custom_random:
                startActivity(RandomActivity.class);
                break;
            case R.id.tv_custom_left_and_right:
                startActivity(JointActivity.class);
                break;
            case R.id.tv_custom_progress:
                startActivity(ProgressActivity.class);
                break;
            case R.id.tv_custom_touch:
                startActivity(TouchUnlockActivity.class);
                break;
            case R.id.tv_custom_input_password:
                startActivity(InputPassWordActivity.class);
                break;

            case R.id.tv_scroll_view:
                startActivity(NestSlidingViewActivity.class);
                break;
            case R.id.tv_page_view:
                startActivity(ScrollPageViewActivity.class);
                break;

            case R.id.tv_test_hm:  // 自定义黑马的view
                startActivity(HmCustomViewActivity.class);
                break;
            case R.id.tv_custom_menu:
                startActivity(SlidingMenuActivity.class);
                break;
            case R.id.tv_custom_name_list:
                startActivity(NameListActivity.class);
                break;
        }
    }

}

