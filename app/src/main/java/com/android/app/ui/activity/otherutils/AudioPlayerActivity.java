package com.android.app.ui.activity.otherutils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.android.app.R;
import com.android.app.ui.activity.java.JavaMapActivity;
import com.android.common.utils.LogUtil;
import com.android.helper.base.AppBaseActivity;
import com.android.helper.utils.AssetsUtil;
import com.android.helper.utils.NotificationUtil;
import com.android.helper.utils.ToastUtil;
import com.android.helper.utils.media.audio.AudioConstant;
import com.android.helper.utils.media.audio.AudioEntity;
import com.android.helper.utils.media.audio.AudioPlayerCallBackListener;
import com.android.helper.utils.media.audio.AudioPlayerUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * 音频播放工具类
 */
public class AudioPlayerActivity extends AppBaseActivity {

    private android.widget.Button mBtnPlayer;
    private android.widget.Button mBtnPause;
    private android.widget.Button mBtnStop;
    private android.widget.SeekBar mSeekbar;
    private android.widget.TextView mTvLeft;
    private android.widget.TextView mTvRight;
    private android.widget.ImageView mIvStart;

    private AudioPlayerUtil playerUtil;
    private String url = "http://file.jollyeng.com/anims/201906/1560665673.mp3";

    private final AudioPlayerCallBackListener audioPlayerCallBackListener = new AudioPlayerCallBackListener() {
        public void onComplete() {
            playerUtil.setResource(url);
        }

        @Override
        public void onNotificationCallInfo(int position, AudioEntity audioEntity) {
            super.onNotificationCallInfo(position, audioEntity);
            ToastUtil.show("当前返回的数据对象的角标为：" + position + "   数据对象为：" + audioEntity.toString());
        }
    };
    private NotificationManager manager;
    private NotificationUtil mNotificationUtil;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_audio_player;
    }

    @Override
    public void initView() {
        mBtnPlayer = findViewById(R.id.btn_player);
        mBtnPause = findViewById(R.id.btn_pause);
        mBtnStop = findViewById(R.id.btn_stop);
        mSeekbar = findViewById(R.id.seekbar);

        setonClickListener(mBtnPlayer, mBtnPause, mBtnStop);
        mTvLeft = findViewById(R.id.tv_left);
        mTvRight = findViewById(R.id.tv_right);
        mIvStart = findViewById(R.id.iv_start);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        LogUtil.e(AudioConstant.TAG, "initData");

        String json = AssetsUtil.getInstance().getJsonForAssets(mActivity, "Audio.json");

        Gson gson = new Gson();

        List<AudioEntity> list = gson.fromJson(json, new TypeToken<List<AudioEntity>>() {
        }.getType());

        LogUtil.e("list:" + list);

        playerUtil = new AudioPlayerUtil(mActivity);
        playerUtil.bindService(success -> {
            playerUtil.autoPlayer(false);
            playerUtil.setSeekBar(mSeekbar);
            playerUtil.setSeekBarProgressTime(mTvLeft);
            playerUtil.setSeekBarTotalTime(mTvRight);
            playerUtil.setStartButton(mIvStart);
            playerUtil.setNotificationSmallIcon(R.mipmap.ic_launcher);
            playerUtil.setPendingIntentActivity(JavaMapActivity.class);
            playerUtil.setNotificationIcon(com.android.helper.R.drawable.icon_music_start, com.android.helper.R.drawable.icon_music_pause, com.android.helper.R.drawable.icon_music_left, com.android.helper.R.drawable.icon_music_right);
            playerUtil.setNotificationList(list);
            playerUtil.setAudioCallBackListener(audioPlayerCallBackListener);
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_player:

                playerUtil.setResource(url);

                break;

            case R.id.btn_pause:
                playerUtil.pause();
                break;

            case R.id.btn_stop:

                if (mNotificationUtil == null) {
                    mNotificationUtil = new NotificationUtil.Builder(mActivity).build();
                }

                boolean openNotify = mNotificationUtil.checkOpenNotify(mActivity);
                LogUtil.e("是否有悬浮通知的权限：" + openNotify);
                if (openNotify) {
                    mNotificationUtil.goToSetNotify(mActivity);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.e(AudioConstant.TAG, "onDestroy");

        playerUtil.destroy();
    }

    private void CreateNotification() {
        // 创建Notification的管理类
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建渠道

        String id = "sssss";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 用户可以看到的通知渠道的名字.
            CharSequence name = getString(R.string.app_name);
            // 用户可以看到的通知渠道的描述
            String description = "sssssss";
            // 优先级
            int importance = NotificationManager.IMPORTANCE_HIGH;
            // 渠道对象
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            // 通知NotifitacionManager去创建渠道
            manager.createNotificationChannel(mChannel);
        }

        // 创建Notification的兼容类
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mActivity, id);
        builder.setSmallIcon(R.mipmap.ic_launcher);// 设置小图标
        builder.setContentTitle("这是一个测试类！");// 设置测试类
        builder.setContentText("Hello World！"); // 设置内容
        builder.setChannelId(id); // 设置渠道

        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);// 7.0新特性-设置优先级

        // 声音
        // 默认的声音
        // builder.setDefaults(Notification.DEFAULT_SOUND);

        // 设置声音循环播放
        // builder.setDefaults(Notification.FLAG_INSISTENT);
        builder.setLights(Color.GREEN, 1000, 1000);
        // 设置是否可以触摸取消
        builder.setAutoCancel(true);
        // 全部默认
        // builder.setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        // 发送通知
        manager.notify(17, notification);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (playerUtil != null) {
            playerUtil.onActivityResult(requestCode, resultCode, data);
        }
    }
}