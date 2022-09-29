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
import com.android.helper.base.title.AppBaseBindingTitleActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NetworkUtil;
import com.android.helper.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class SocketClientActivity extends AppBaseBindingTitleActivity<ActivitySocketClientBinding> {

    private String mIpAddress;

    @Override
    protected String setTitleContent() {
        return "客户端Socket";
    }

    @Override
    public ActivitySocketClientBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return ActivitySocketClientBinding.inflate(inflater, container, true);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    private int port = 9999;// 端口号
    private String charsetName = "UTF-8";
    private final StringBuffer mStringBuffer = new StringBuffer();

    // 客户端
    private PrintStream mClientPrintStream; // 客户端的写数据流
    private BufferedReader mClientBufferedReader; // 客户端的读取数据流
    private Socket mClientSocket = null; // 客户端端口

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.btn_get_ip, R.id.btn_send_data, R.id.btn_get_data, R.id.btn_init_data);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {

            case R.id.btn_get_ip:

                mIpAddress = NetworkUtil.getIPAddress(this);
                LogUtil.e("------>" + mIpAddress);

                Message message = mHandler.obtainMessage();
                message.what = 999;
                message.obj = mIpAddress;
                mHandler.sendMessage(message);
                break;

            case R.id.btn_init_data: // 初始化socket

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        // 创建Socket对象
                        if (mClientSocket == null) {
                            try {
                                mClientSocket = new Socket(mIpAddress, port);
                            } catch (IOException e) {
                                e.printStackTrace();

                                Message message1 = mHandler.obtainMessage();
                                message1.what = 33;
                                message1.obj = e.getMessage();
                                mHandler.sendMessage(message1);

                                try {
                                    if (mClientSocket != null) {
                                        mClientSocket.close();
                                        mClientSocket = null;
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    LogUtil.e("client 链接错误：" + ex.getMessage());

                                    Message message = mHandler.obtainMessage();
                                    message1.what = 33;
                                    message1.obj = ex.getMessage();
                                    mHandler.sendMessage(message);
                                }
                            }
                        }
                    }
                }.start();

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

            case R.id.btn_get_data:
                // 接收数据

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        // 准备从服务器端接收回应的BufferedReader, 设置编码
                        if (mClientBufferedReader == null) {
                            try {
                                mClientBufferedReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream(), charsetName));

                                mStringBuffer.append("服务端：" + mClientBufferedReader.read());
                                // 设置服务端的回应数据
                                mBinding.tvContent.setText(mStringBuffer.toString());
                            } catch (IOException e) {
                                e.printStackTrace();

                                try {
                                    if (mClientBufferedReader != null) {
                                        mClientBufferedReader.close();
                                        mClientBufferedReader = null;
                                    }
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                    LogUtil.e("Socket读取数据异常：" + ex.getMessage());

                                    Message message1 = mHandler.obtainMessage();
                                    message1.what = 33;
                                    message1.obj = ex.getMessage();
                                    mHandler.sendMessage(message1);

                                }
                            }
                        }

                    }
                }.start();

                break;
        }
    }

    private boolean checked() {
        Message message = mHandler.obtainMessage();
        if (TextUtils.isEmpty(mBinding.etInput.getText().toString())) {
            message.obj = "Ip地址为空！";
            message.what = 11;
            mHandler.sendMessage(message);
            return false;
        }

        if (TextUtils.isEmpty(mBinding.etInputContent.getText().toString())) {
            message.obj = "要发送的内容为空！";
            message.what = 22;
            mHandler.sendMessage(message);
            return false;
        }
        return true;
    }

    private void sendClientCSocket() {
        boolean checked = checked();
        if (checked) {
            // 启动的时候，需要把socket设置为异步线程
            //准备向服务器端发送信息的socket
            String content = mBinding.etInputContent.getText().toString();
            try {
                // 发送数据，设置编码Utf-8
                if (mClientPrintStream == null) {
                    mClientPrintStream = new PrintStream(mClientSocket.getOutputStream(), true, charsetName);
                }

                // 发送数据出去
                mClientPrintStream.println(content);

                Message message1 = mHandler.obtainMessage();
                message1.what = 44;
                message1.obj = "发送接受完毕！";
            } catch (IOException e) {
                e.printStackTrace();
                String message2 = e.getMessage();
                LogUtil.e("error:" + message2);

                Message message1 = mHandler.obtainMessage();
                message1.what = 33;
                message1.obj = message2;
                mHandler.sendMessage(message1);

                if (mClientPrintStream != null) {
                    mClientPrintStream.close();
                    mClientPrintStream = null;
                }

            }
        }
    }

    private boolean flag = true;
    private Socket mServiceClient = null;

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

                case 11:
                case 22:
                    ToastUtil.show((String) msg.obj);
                    break;

                case 33:
                    mBinding.tvContent.setText("错误：" + msg.obj);
                    break;
                case 44:
                    mBinding.tvContent.setText("正常：" + msg.obj);
                    break;
            }
        }
    };
}