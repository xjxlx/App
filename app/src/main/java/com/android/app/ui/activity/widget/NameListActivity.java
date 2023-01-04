package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.View;

import com.android.app.R;
import com.android.helper.base.AppBaseActivity;
import com.android.helper.utils.ScreenUtil;
import com.android.helper.utils.dialog.PopupWindowUtil;
import com.android.helper.utils.statusBar.StatusBarUtil;

public class NameListActivity extends AppBaseActivity {

    private ScreenUtil screenUtil;
    private PopupWindowUtil popupWindowUtil;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_name_list;
    }

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
}