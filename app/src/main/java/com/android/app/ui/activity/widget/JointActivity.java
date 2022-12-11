package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.android.app.R;
import com.android.helper.base.AppBaseActivity;
import com.android.helper.utils.photo.GlideUtil;

/**
 * 自定义左右的布局
 */
public class JointActivity extends AppBaseActivity {

    private GlideUtil mGlideUtil;

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

//        View viewById = findViewById(R.id.iv_yl);
//        viewById.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int height = viewById.getHeight();
//                int width = viewById.getWidth();
//                if (!ClickUtil.isDoubleClick(1500)) {
//                    ToastUtil.show("width:" + width + "   height:" + height);
//                }
//            }
//        });

        String url = "http://file.jollyeng.com/picture_book/201903/1553490074.png";

        ImageView left = findViewById(R.id.iv_left);
        ImageView iv_right = findViewById(R.id.iv_right);

        if (mGlideUtil == null) {
            mGlideUtil = new GlideUtil.Builder(mActivity)
                    .setPlaceholderResource(R.mipmap.icon_face_authentication_bg)
                    .build();
        }


        View ll_left = findViewById(R.id.ll_left);
        View ll_right = findViewById(R.id.ll_right);
        ImageView iv____ss = findViewById(R.id.iv____ss);
        mGlideUtil.loadUrl(left, url);
        mGlideUtil.loadUrl(iv_right, url);
        mGlideUtil.loadUrl(iv____ss, url);


        findViewById(R.id.change).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (change == 1) {
                    ll_left.setVisibility(View.VISIBLE);
                    ll_right.setVisibility(View.VISIBLE);
                    iv____ss.setVisibility(View.GONE);
                    change = 2;
                } else if (change == 2) {
                    ll_left.setVisibility(View.GONE);
                    ll_right.setVisibility(View.GONE);
                    iv____ss.setVisibility(View.VISIBLE);
                    change = 1;
                }
            }
        });
    }

    int change = 1;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_joint_view;
    }
}
