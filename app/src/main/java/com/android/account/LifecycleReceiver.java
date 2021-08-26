package com.android.account;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.android.helper.utils.LogUtil;

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

//            boolean serviceRunning = ServiceUtil.isServiceRunning(context, BhService.class);
//            if (!serviceRunning) {
//                LogUtil.e("当前的后台服务已经挂掉，主动去拉活后台服务！");
//                LogUtil.writeLifeCycle("当前的后台服务已经挂掉，主动去拉活后台服务！");
//
//            } else {
//                LogUtil.e("当前的后台服务还活着，不用去主动拉活后台服务！");
//                LogUtil.writeLifeCycle("当前的后台服务还活着，不用去主动拉活后台服务！");
//            }

            Intent intentService = new Intent();
            intentService.setClassName("com.android.app", "com.android.app.test.app2.BhService");
            intentService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(intentService);
        }
    }
}