package com.android.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.app.R;
import com.android.app.app.Keepalive.AppJobService;
import com.android.app.test.app.BhService;
import com.android.helper.common.CommonConstants;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NotificationUtil;
import com.android.helper.utils.ServiceUtil;
import com.android.helper.utils.account.LifecycleAppEnum;

/**
 * 账号的同步的拉活广播，在这里可以做自己想做的任意事情
 */
public class LifecycleReceiver extends BroadcastReceiver {

    private final String tagService = "com.android.app.test.app.BhService";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, "com.android.app.lifecycle")) {
            LogUtil.e("接收到了账号同步的信息！");
            LogUtil.writeLifeCycle("接收到了账号同步的信息！");

            sendNotification(context);

            // 启动主应用
            if (!ServiceUtil.isServiceRunning(context, tagService)) {
                Intent intentService = new Intent();
                intentService.setClassName("com.android.app", tagService);
                intentService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentService.putExtra(CommonConstants.KEY_LIFECYCLE_FROM, LifecycleAppEnum.FROM_ACCOUNT.getFrom());
                ServiceUtil.startService(context, intentService);
            }

            // 如果JobService没有运行着的时候，顺便把它也唤醒
            boolean jobServiceRunning = ServiceUtil.isJobServiceRunning(context, AppJobService.class);
            LogUtil.e("jobServiceRunning:" + jobServiceRunning);
            if (!jobServiceRunning) {
                AppJobService.startJob(context, BhService.class, LifecycleAppEnum.FROM_ACCOUNT);
            }
        }
    }

    private void sendNotification(Context context) {
        NotificationUtil notificationUtil = NotificationUtil
                .getInstance(context)
                .setChannelName(CommonConstants.KEY_LIFECYCLE_NOTIFICATION_CHANNEL_NAME)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("账号同步开始了，主动检测服务存活");
        notificationUtil.getNotification().when = System.currentTimeMillis();
        notificationUtil.createNotification();
        notificationUtil.sendNotification(333);
    }
}