package com.android.app.ui.activity.hmview;

import com.android.app.R;
import com.android.helper.base.BaseActivity;
import com.android.helper.utils.ToastUtil;

/**
 * 自定义view的练习
 */
public class HmCustomViewActivity extends BaseActivity {

    private com.android.app.widget.hm.SwitchView sv;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_hm_custom_view;
    }

    @Override
    public void initView() {
        super.initView();
        setTitleContent("自定义开关和水波纹");

        sv = findViewById(R.id.sv);

        sv.setSwitchChangeListener(isOpen -> ToastUtil.show("当前的状态是：" + isOpen));
    }

    @Override
    public void initData() {

    }
}