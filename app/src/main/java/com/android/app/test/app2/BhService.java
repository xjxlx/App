package com.android.app.test.app2;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.NonNull;

import com.android.app.R;
import com.android.helper.utils.BluetoothUtil;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NotificationUtil;

public class BhService extends Service {

    @SuppressLint("StaticFieldLeak")
    private static final int CODE_NOTIFICATION = 19900713;
    private static final int CODE_INTERVAL = 10 * 1000;

    public BhService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("onStartCommand --->");
        LogUtil.writeLifeCycle("onStartCommand --->");

        sendNotification();

        // 启动变为前台服务
        startNotificationForeground();

        Message message = mHandler.obtainMessage();
        message.what = CODE_NOTIFICATION;
        mHandler.sendMessage(message);
        return START_REDELIVER_INTENT;
    }

    private void startNotificationForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "前台的服务";
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel Channel = new NotificationChannel(CHANNEL_ID, "主服务", NotificationManager.IMPORTANCE_HIGH);
            Channel.enableLights(true);//设置提示灯
            Channel.setLightColor(Color.RED);//设置提示灯颜色
            Channel.setShowBadge(true);//显示logo
            Channel.setDescription("notification");//设置描述
            Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
            if (manager != null) {
                manager.createNotificationChannel(Channel);
            }

            Notification notification = new Notification.Builder(this)
                    .setChannelId(CHANNEL_ID)
                    .setAutoCancel(false)
                    .setContentTitle("主服务")//标题
                    .setContentText("运行中...")//内容
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)//小图标一定需要设置,否则会报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
                    .build();
            startForeground(1, notification);//服务前台化只能使用startForeground()方法,不能使用 notificationManager.notify(1,notification); 这个只是启动通知使用的,使用这个方法你只需要等待几秒就会发现报错了
        }
    }

    private void sendNotification() {
        NotificationUtil instance = NotificationUtil.getInstance(getApplicationContext());
        instance.setChannelName("123");
        instance.setSmallIcon(R.mipmap.ic_launcher);
        instance.setContentText("直接启动的服务");
        instance.createNotification();
        instance.getNotification().when = System.currentTimeMillis();
        instance.sendNotification(111);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // 避免重复性发送消息
            removeMessages(CODE_NOTIFICATION);
            removeCallbacksAndMessages(null);

            // 隐藏通知栏
            stopForeground(true);

            if (msg.what == CODE_NOTIFICATION) {
                LogUtil.e("---> 开始发送了消息的轮询！");
                // 写入本地的日志信息
                LogUtil.writeLifeCycle("我是服务的轮询日志哦！");

                Message message = mHandler.obtainMessage();
                message.what = CODE_NOTIFICATION;
                sendMessageDelayed(message, CODE_INTERVAL);

                onLifecycle();
            }
        }
    };

    /**
     * 回调保活的信息
     */
    private void onLifecycle() {
        BluetoothUtil bluetoothUtil = BluetoothUtil.getInstance(getApplication());
        bluetoothUtil.startScan();
    }
}