package com.android.app.test.app2;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
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

        Message message = mHandler.obtainMessage();
        message.what = CODE_NOTIFICATION;
        mHandler.sendMessage(message);
        return START_REDELIVER_INTENT;
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