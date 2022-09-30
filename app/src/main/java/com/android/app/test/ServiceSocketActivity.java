package com.android.app.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.R;
import com.android.app.databinding.ActivityServiceSocketBinding;
import com.android.helper.base.title.AppBaseBindingTitleActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NetworkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceSocketActivity extends AppBaseBindingTitleActivity<ActivityServiceSocketBinding> {

    private ServerSocket mServerSocket = null;
    private Socket mSocket;
    private BufferedReader mBufferedReader;
    private PrintStream mPrintStream;
    private final int port = 9999;// 端口号
    private final String encoding = "UTF-8";
    private boolean flag = true;
    private final StringBuffer mStringBuffer = new StringBuffer();

    private int number = 0;

    @Override
    protected String setTitleContent() {
        return "我是服务端Socket";
    }

    @Override
    public ActivityServiceSocketBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return ActivityServiceSocketBinding.inflate(inflater, container, true);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    mServerSocket = new ServerSocket(port);
                    sendMessage("服务端初始化Service成功！");

                    if (mServerSocket != null) {
                        while (flag) {
                            // 等待客户端连接，阻塞线程
                            sendMessage("开始阻塞线程，等待客户端链接 ~~~~~~~！");
                            mSocket = mServerSocket.accept();

                            InetAddress inetAddress = mSocket.getInetAddress();
                            sendMessage("客户端连接成功：主机地址：" + inetAddress.getHostAddress() + "\r\n" + "主机名字：" + inetAddress.getHostName());

                            // 循环读取数据
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        boolean connected = mSocket.isConnected();
                                        sendMessage("客户端是否连接成功：" + connected);

                                        // 取得输入流读取客户端传送的数据,要接收中文只需将编码设置为"UTF-8"
                                        if (connected) {
                                            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), encoding));
                                        }

                                        String info = null;
                                        sendMessage("开始循环读取客户端发来的数据！");
                                        while ((info = mBufferedReader.readLine()) != null) {
                                            sendMessage("客户端：：" + info);
                                        }

                                    } catch (Exception e) {
                                        sendMessage("读取客户端数据失败：" + e.getMessage());
                                        if (mBufferedReader != null) {
                                            try {
                                                mBufferedReader.close();
                                                mBufferedReader = null;
                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }).start();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    sendMessage("服务端初始化Service失败：" + e.getMessage());
                }
            }
        }.start();
    }

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.btn_get_ip, R.id.btn_send_data);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.btn_get_ip:
                String ipAddress = NetworkUtil.getIPAddress(this);
                LogUtil.e("------>" + ipAddress);

                Message message = mHandler.obtainMessage();
                message.what = 999;
                message.obj = ipAddress;
                mHandler.sendMessage(message);

                break;

            case R.id.btn_send_data:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        sendServiceSocket();
                    }
                }.start();
                break;
        }
    }

    private void sendServiceSocket() {
        String content = mBinding.etInputContent.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            // 发送数据
            if (mSocket == null) {
                sendMessage("Socket为空！");
            } else {
                boolean connected = mSocket.isConnected();
                if (connected) {
                    try {
                        if (mPrintStream == null) {
                            mPrintStream = new PrintStream(mSocket.getOutputStream(), true, encoding);
                        }
                        mPrintStream.println(mBinding.etInputContent.getText().toString() + " 【 " + number + " 】");
                        number++;
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (mPrintStream != null) {
                            mPrintStream.close();
                            mPrintStream = null;
                        }
                        sendMessage("Socket发送数据异常：" + e.getMessage());
                    }
                } else {
                    sendMessage("Socket断开中！！！");
                }
            }
        }
    }

    private String value = "";
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 999:
                    String address = (String) msg.obj;
                    value += address + "    ";
                    mBinding.tvAddress.setText("Address:" + value);
                    break;

                case 66:
                    mBinding.tvContent.setText(mStringBuffer.toString());
                    break;
            }
        }
    };

    private void sendMessage(String content) {
        if (!TextUtils.isEmpty(content)) {
            Message message = mHandler.obtainMessage();
            message.what = 66;
            mStringBuffer.append(content);
            mStringBuffer.append("\r\n");
            message.obj = mStringBuffer.toString();

            mHandler.sendMessage(message);

            LogUtil.e(content);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;

        if (mServerSocket != null) {
            try {
                mServerSocket.close();
                mServerSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mSocket != null) {
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mBufferedReader != null) {
            try {
                mBufferedReader.close();
                mBufferedReader = null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (mPrintStream != null) {
            mPrintStream.close();
            mPrintStream = null;
        }

    }
}