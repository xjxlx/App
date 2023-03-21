package com.java.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class SocketTest {

    private ScheduledFuture<?> mScheduledFuture = null;
    private Socket mSocket = null;
    private final Logger mLogger = Logger.getLogger(SocketTest.class.getName());

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

    public SocketTest() {
        ScheduledThreadPoolExecutor mScheduledThreadPoolExecutor =
                new ScheduledThreadPoolExecutor(1);

        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mSocket == null || !mSocket.isConnected()) {
                    mLogger.info("尝试建立连接...");
                    try {
                        mSocket = new Socket("localhost", 18000);
                        mLogger.info("建立新连接:" + mSocket);

                        CompletableFuture.runAsync(new Runnable() {
                            @Override
                            public void run() {
                                session();
                            }
                        });
                    } catch (Exception e) {
                        mLogger.info("连接异常");
                    }
                } else {
                    mLogger.info("连接心跳检测:当前已经建立连接，无需重连");
                }
            }
        };

        // 每隔3秒周期性的执行心跳检测动作。
        mScheduledFuture =
                mScheduledThreadPoolExecutor.scheduleAtFixedRate(mRunnable, 0, 3, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        new SocketTest();
    }
}