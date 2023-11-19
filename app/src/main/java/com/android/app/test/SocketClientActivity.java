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
import com.android.app.databinding.ActivitySocketClientBinding;
import com.android.common.utils.LogUtil;
import com.android.common.base.BaseBindingTitleActivity;
import com.android.helper.utils.NetworkUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SocketClientActivity extends BaseBindingTitleActivity<ActivitySocketClientBinding> {

    private final int port = 9999;// 端口号
    private final String encoding = "UTF-8";
    private final StringBuffer mStringBuffer = new StringBuffer();
    private BufferedReader mClientBufferedReader; // 客户端的读取数据流
    private int number = 0;
    private Socket mClientSocket = null; // 客户端端口
    private PrintStream mClientPrintStream; // 客户端的写数据流

    @NonNull
    @Override
    public String getTitleContent() {
        return "客户端Socket";
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.btn_get_ip, R.id.btn_send_data, R.id.btn_init_data);
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
            case R.id.btn_init_data:
                initSocket();
                break;
            case R.id.btn_send_data:
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        sendClientCSocket();
                    }
                };
                new Thread(runnable).start();
                break;
        }
    }

    private void initSocket() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                // 创建Socket对象
                try {
                    mClientSocket = new Socket(mBinding.etInput.getText().toString(), port);
                    sendMessage("初始化客户端Socket成功");
                    // 循环读取数据
                    new Thread(() -> {
                        sendMessage("开始读取服务端数据！");
                        try {
                            // 取得输入流读取客户端传送的数据,要接收中文只需将编码设置为"UTF-8"
                            boolean connected = mClientSocket.isConnected();
                            if (connected) {
                                mClientBufferedReader = new BufferedReader(
                                        new InputStreamReader(mClientSocket.getInputStream(), encoding));
                                sendMessage("开始循环获取数据！");
                                String info = null;
                                while ((info = mClientBufferedReader.readLine()) != null) {
                                    sendMessage("服务端消息：" + info);
                                }
                            } else {
                                sendMessage("服务端Socket链接断开！");
                            }
                        } catch (Exception e) {
                            sendMessage("读取服务器数据异常：" + e.getMessage());
                            if (mClientBufferedReader != null) {
                                try {
                                    mClientBufferedReader.close();
                                    mClientBufferedReader = null;
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                    sendMessage("初始化客户端Socket失败：" + e.getMessage());
                    try {
                        if (mClientSocket != null) {
                            mClientSocket.close();
                            mClientSocket = null;
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void sendClientCSocket() {
        String content = mBinding.etInputContent.getText().toString();
        if (!TextUtils.isEmpty(content)) {
            // 启动的时候，需要把socket设置为异步线程
            // 准备向服务器端发送信息的socket
            try {
                if (mClientSocket != null) {
                    boolean connected = mClientSocket.isConnected();
                    if (connected) {
                        if (mClientPrintStream == null) {
                            mClientPrintStream = new PrintStream(mClientSocket.getOutputStream(), true, encoding);
                        }
                        // 发送数据出去
                        mClientPrintStream.println(content + number);
                        ++number;
                    } else {
                        sendMessage("客户端链接失败！");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                sendMessage("客户端数据发送失败：" + e.getMessage());
                if (mClientPrintStream != null) {
                    mClientPrintStream.close();
                    mClientPrintStream = null;
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 999:
                    String address = (String) msg.obj;
                    mBinding.tvAddress.setText("Address:" + address);
                    break;
                case 66:
                    String content = (String) msg.obj;
                    mBinding.tvContent.setText(content);
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
        try {
            if (mClientSocket != null) {
                mClientSocket.close();
                mClientSocket = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (mClientBufferedReader != null) {
            try {
                mClientBufferedReader.close();
                mClientBufferedReader = null;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @NonNull
    @Override
    public ActivitySocketClientBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivitySocketClientBinding.inflate(inflater, container, true);
    }
}