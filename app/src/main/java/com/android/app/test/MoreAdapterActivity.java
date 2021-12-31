package com.android.app.test;

import android.graphics.Bitmap;

import com.android.app.R;

import android.os.Bundle;
import android.widget.ImageView;

import com.android.helper.base.AppBaseActivity;
import com.android.helper.interfaces.listener.CallBackListener;
import com.android.helper.utils.BitmapUtil;
import com.android.helper.utils.LogUtil;

public class MoreAdapterActivity extends AppBaseActivity {

    private androidx.recyclerview.widget.RecyclerView mRvList;
    private ImageView iv_image;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_more_adapter;
    }

    @Override
    public void initView() {
        mRvList = findViewById(R.id.rv_list);
        iv_image = findViewById(R.id.iv_image);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        String url = "http://file.jollyeng.com/anims/201903/1552874954.jpg";

        BitmapUtil.getBitmapForService(mActivity, "", new CallBackListener<Bitmap>() {
            @Override
            public void onBack(boolean successful, Object tag, Bitmap bitmap) {
                LogUtil.e("successful:" + successful + " --->tag:" + tag + "  --->bitmap:" + bitmap);

                if (successful) {
                    iv_image.setImageBitmap(bitmap);
                }
            }
        });
    }
}