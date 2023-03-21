package com.java.model.adb;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * @author : 流星
 * @CreateDate: 2023/3/21-22:49
 * @Description:
 */
public class TestAdbSocket {
    private Socket mSocket = null;
    private final Logger mLogger = Logger.getLogger(TestAdbSocket.class.getName());
    private ScheduledFuture<?> mScheduledFuture = null;

    public TestAdbSocket() {
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mSocket == null) {
                    try {
                        mSocket = new Socket("localhost", 18000);
                    } catch (IOException e) {
                        e.printStackTrace();
                        mLogger.info("链接异常：" + e.getMessage());
                    }
                }
                while (!mSocket.isConnected()) {
                    session();
                }
                mLogger.info("客户端的socket 已经链接成功！");
            }
        };

        ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        // 每隔3秒周期性的执行心跳检测动作。
        mScheduledFuture = mScheduledThreadPoolExecutor.scheduleAtFixedRate(mRunnable, 0, 3, TimeUnit.SECONDS);
    }

    private void session() {
        DataInputStream dis = null;
        DataOutputStream dos = null;
        try {
            dis = new DataInputStream(mSocket.getInputStream());
            dos = new DataOutputStream(mSocket.getOutputStream());

            while (true) {
                String data = "PC时间:" + System.currentTimeMillis();
                dos.writeUTF(data);
                dos.flush();

                String s = dis.readUTF();
                mLogger.info("收到数据:" + s);

                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                mSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mSocket = null;
        }
    }

    public static void main(String[] args) {
        new TestAdbSocket();
    }
}
