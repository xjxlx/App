package com.android.account;

import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.app.R;
import com.android.app.test.app.AppJobService;
import com.android.app.test.app2.BhService;
import com.android.helper.common.CommonConstants;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NotificationUtil;
import com.android.helper.utils.ServiceUtil;

/**
 * 账号的同步的拉活广播，在这里可以做自己想做的任意事情
 */
public class LifecycleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, "com.android.app.lifecycle")) {
            LogUtil.e("接收到了账号同步的信息！");
            LogUtil.writeLifeCycle("接收到了账号同步的信息！");

            Intent intentService = new Intent();
            intentService.setClassName("com.android.app", "com.android.app.test.app2.BhService");
            intentService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentService.putExtra(CommonConstants.KEY_LIFECYCLE_FROM, CommonConstants.KEY_LIFECYCLE_FROM_ACCOUNT);
            ServiceUtil.startService(context, intentService);

            sendNotification(context);

            // 如果JobService没有运行着的时候，顺便把它也唤醒
//            boolean serviceRunning = ServiceUtil.isServiceRunning(context, JobService.class);
//            if (!serviceRunning) {
//                AppJobService.startJob(context, BhService.class, false);
//            }
        }
    }

    private void sendNotification(Context context) {
        NotificationUtil instance = NotificationUtil.getInstance(context);
        instance.setChannelName("789");
        instance.setSmallIcon(R.mipmap.ic_launcher);
        instance.setContentText("我是账号拉活的");
        instance.createNotification();
        instance.getNotification().when = System.currentTimeMillis();
        instance.sendNotification(333);
    }
}