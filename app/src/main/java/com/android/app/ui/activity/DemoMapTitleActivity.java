package com.android.app.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.R;
import com.android.app.app.Keepalive.AppLifecycleActivity;
import com.android.app.databinding.ActivityDemoMapBinding;
import com.android.app.ui.activity.otherutils.AudioPlayerActivity;
import com.android.app.ui.activity.otherutils.LocationActivity;
import com.android.app.ui.activity.otherutils.dingding.OpenDingDingActivity;
import com.android.helper.base.BaseBindingActivity;

import org.jetbrains.annotations.NotNull;

public class DemoMapTitleActivity extends BaseBindingActivity<ActivityDemoMapBinding> {

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        setonClickListener(R.id.tv_open_qywx, R.id.tv_receive_map_result,
                R.id.tv_xml_write_data, R.id.tv_rxjava2, R.id.tv_download, R.id.tv_uploading,
                R.id.tv_selector_image, R.id.tv_send_sms, R.id.tv_audio_player, R.id.tv_app_lifecycle,
                R.id.tv_location, R.id.tv_open_dingding
        );
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

            case R.id.tv_open_qywx:
                startActivity(WorkWxTitleActivity.class);
                break;

            case R.id.tv_receive_map_result:
                startActivity(WorkWxTitleActivity.class);
                break;

            case R.id.tv_xml_write_data:
                startActivity(WriteXmlTitleActivity.class);
                break;
            case R.id.tv_rxjava2:
                startActivity(RxJava2Activity.class);
                break;

            case R.id.tv_download:
                startActivity(DownLoadListActivity.class);
                break;

            case R.id.tv_uploading:
                startActivity(UploadingActivity.class);
                break;

            case R.id.tv_selector_image:
                startActivity(SelectorImageActivity.class);
                break;

            case R.id.tv_send_sms:
                startActivity(SendSmsActivity.class);
                break;

            case R.id.tv_audio_player:
                startActivity(AudioPlayerActivity.class);
                break;
            case R.id.tv_app_lifecycle:
                startActivity(AppLifecycleActivity.class);
                break;

            case R.id.tv_location:
                startActivity(LocationActivity.class);
                break;
            case R.id.tv_open_dingding:
                startActivity(OpenDingDingActivity.class);
                break;
        }
    }

    @Override
    public ActivityDemoMapBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ActivityDemoMapBinding.inflate(inflater, container, false);
    }
}