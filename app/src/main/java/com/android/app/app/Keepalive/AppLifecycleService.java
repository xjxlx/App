package com.android.app.app.Keepalive;

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
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.android.app.R;
import com.android.helper.common.CommonConstants;
import com.android.helper.utils.BluetoothUtil;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NotificationUtil;
import com.android.helper.utils.ServiceUtil;
import com.android.helper.utils.account.LifecycleAppEnum;

public class AppLifecycleService extends Service {

    @SuppressLint("StaticFieldLeak")
    private static final int CODE_NOTIFICATION = 19900713;
    private static final int CODE_INTERVAL = 10 * 1000;

    public AppLifecycleService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("onStartCommand --->");
        LogUtil.writeLifeCycle("onStartCommand --->");

        String fromType = intent.getStringExtra(CommonConstants.KEY_LIFECYCLE_FROM);
        sendNotification(fromType);

        // 启动变为前台服务
        startNotificationForeground();

        // 取消前台的通知栏
        Message message = mHandler.obtainMessage();
        message.what = CODE_NOTIFICATION;
        mHandler.sendMessage(message);
        return START_REDELIVER_INTENT;
    }

    private void startNotificationForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "前台服务";
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
                    .setContentTitle("提升为前台服务")//标题
                    .setContentText("前台服务运行中...")//内容
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)//小图标一定需要设置,否则会报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
                    .build();
            startForeground(1, notification);//服务前台化只能使用startForeground()方法,不能使用 notificationManager.notify(1,notification); 这个只是启动通知使用的,使用这个方法你只需要等待几秒就会发现报错了
        }
    }

    private void sendNotification(String type) {
        NotificationUtil instance = NotificationUtil.getInstance(getApplicationContext());
        instance.setChannelName(CommonConstants.KEY_LIFECYCLE_NOTIFICATION_CHANNEL_NAME);
        instance.setSmallIcon(R.mipmap.ic_launcher);
        if (TextUtils.equals(type, LifecycleAppEnum.From_Intent.getFrom())) {
            instance.setContentText("我是后台服务，我是被直接启动的");
            LogUtil.writeLifeCycle("我是后台服务，我是被直接启动的");
        } else if (TextUtils.equals(type, LifecycleAppEnum.FROM_JOB.getFrom())) {
            instance.setContentText("我是后台服务，我是被JobService启动的");
            LogUtil.writeLifeCycle("我是后台服务，我是被JobService启动的");
        } else if (TextUtils.equals(type, LifecycleAppEnum.FROM_ACCOUNT.getFrom())) {
            instance.setContentText("我是后台服务，我是被账号拉活的");
            LogUtil.writeLifeCycle("我是后台服务，我是被账号拉活的");
        }
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

            // 3:启动jobService
            String jobServiceName = LifecycleManager.getInstance().getJobServiceName();
            boolean jobServiceRunning = ServiceUtil.isJobServiceRunning(getApplicationContext(), jobServiceName);
            LogUtil.e("☆☆☆☆☆---我是后台服务，当前jobService的状态为:" + jobServiceRunning);
            LogUtil.writeLifeCycle("☆☆☆☆☆---我是后台服务，当前jobService的状态为:" + jobServiceRunning);

            if (!jobServiceRunning) {
                AppJobService.startJob(getApplicationContext(), LifecycleManager.getInstance().getServiceName(), LifecycleAppEnum.FROM_SERVICE);
            }

            if (msg.what == CODE_NOTIFICATION) {
                LogUtil.e("---> 开始发送了消息的轮询！");
                Message message = mHandler.obtainMessage();
                message.what = CODE_NOTIFICATION;
                sendMessageDelayed(message, CODE_INTERVAL);

                // onLifecycle();
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