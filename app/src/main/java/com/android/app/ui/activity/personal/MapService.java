package com.android.app.ui.activity.personal;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.android.app.R;
import com.android.helper.common.CommonConstants;
import com.android.helper.utils.NotificationUtil;

public class MapService extends Service {
    public MapService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // 开启前台服务
        Intent pendingIntent = new Intent(getBaseContext(), RouseDingDingActivity.class);

        NotificationUtil.Builder builder = new NotificationUtil.Builder(getApplicationContext())
                .setChannelName(CommonConstants.KEY_LIFECYCLE_NOTIFICATION_CHANNEL_NAME)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("地图保活的服务")
                .setContentText("地图正在持续保活中...")
                .setActivityPendingIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setChannelDescription("地图保活");
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            builder.setNotificationLevel(NotificationManager.IMPORTANCE_MAX);
        } else {
            builder.setNotificationLevel(Notification.PRIORITY_MAX);
        }
        NotificationUtil notificationUtil = builder.build();
        notificationUtil.startLoopForeground(123, 3000, this);

        return super.onStartCommand(intent, flags, startId);
    }
}