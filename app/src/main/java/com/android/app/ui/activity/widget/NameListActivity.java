package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.R;
import com.android.app.databinding.ActivityNameListBinding;
import com.android.common.base.BaseBindingTitleActivity;
import com.android.helper.utils.ScreenUtil;
import com.android.helper.utils.dialog.PopupWindowUtil;
import com.android.helper.utils.statusBar.StatusBarUtil;

public class NameListActivity extends BaseBindingTitleActivity<ActivityNameListBinding> {

    private ScreenUtil screenUtil;
    private PopupWindowUtil popupWindowUtil;

    @Override
    public void initView() {
        StatusBarUtil.getInstance(mActivity).setStatusTranslucent();
        screenUtil = new ScreenUtil();
        View ssss = findViewById(R.id.tv_sss);
        ssss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowUtil.showAsDropDown(ssss);
            }
        });
        popupWindowUtil = new PopupWindowUtil();
        popupWindowUtil.setContentView(this, R.layout.pop_test).build();
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        screenUtil.adapterFullScreen(mActivity);
    }

    @NonNull
    @Override
    public ActivityNameListBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityNameListBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "";
    }
}