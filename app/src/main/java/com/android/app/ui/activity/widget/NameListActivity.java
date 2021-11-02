package com.android.app.ui.activity.widget;

import android.view.View;
import android.view.ViewGroup;

import com.android.app.R;
import com.android.helper.base.BaseActivity;
import com.android.helper.interfaces.listener.DialogChangeListener;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.ScreenUtil;
import com.android.helper.utils.dialog.PopupWindowUtil;
import com.android.helper.utils.statusBar.StatusBarUtil;

public class NameListActivity extends BaseActivity {

    private View viewById;
    private PopupWindowUtil instance;
    private ScreenUtil screenUtil;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_name_list;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleContent("自定义名字检测的列表");

        StatusBarUtil.getInstance(mContext).setStatusTranslucent();
        screenUtil = new ScreenUtil();

        viewById = findViewById(R.id.rl_root);
        View ssss = findViewById(R.id.tv_sss);
        ssss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instance.showAsDropDown(ssss);
            }
        });
        PopupWindowUtil.Builder builder = new PopupWindowUtil.Builder(mContext, R.layout.pop_test);
        instance = builder
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                .setPopupWindowChangeListener(new DialogChangeListener() {
                    @Override
                    public void onShow(View view) {
                        LogUtil.e("v:onShow");
                    }

                    @Override
                    public void onDismiss() {
                        LogUtil.e("v:onDismiss");
                    }
                }).Build();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        screenUtil.adapterFullScreen(mContext);

        if (instance != null) {
            if (instance.isShowing()) {
                instance.dismiss();
            }
        }
    }
}