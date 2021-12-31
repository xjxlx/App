package com.android.app.ui.activity.widget;

import android.os.Bundle;
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
            mGlideUtil = new GlideUtil.Builder(mActivity).setPlaceholderResource(R.mipmap.icon_face_authentication_bg).build();
        }

        mGlideUtil.loadUrl(left, url);
        mGlideUtil.loadUrl(iv_right, url);
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_joint_view;
    }
}
