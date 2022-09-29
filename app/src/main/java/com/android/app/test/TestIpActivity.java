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
import com.android.app.databinding.ActivityTestIpBinding;
import com.android.helper.base.title.AppBaseBindingTitleActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NetworkUtil;
import com.android.helper.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestIpActivity extends AppBaseBindingTitleActivity<ActivityTestIpBinding> {

    private int port = 9999;// 端口号
    private int mFlagType = 0; // 1:客户端  2：服务端
    private String TAG = "";
    private String charsetName = "UTF-8";
    private final StringBuffer mStringBuffer = new StringBuffer();

    // 客户端
    private PrintStream mClientPrintStream; // 客户端的写数据流
    private BufferedReader mClientBufferedReader; // 客户端的读取数据流
    private Socket mClientSocket = null; // 客户端端口

    // 服务端

    private ServerSocket mServerSocket = null;
    private BufferedReader mServiceReader;
    private PrintStream mServiceOut;

    @Override
    protected String setTitleContent() {
        return "测试Ip地址";
    }

    @Override
    public ActivityTestIpBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return ActivityTestIpBinding.inflate(inflater, container, true);
    }

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.btn_get_ip, R.id.btn_send_data, R.id.btn_user, R.id.btn_service);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            case R.id.btn_user:
                mFlagType = 1;
                TAG = "客户端 :";
                break;

            case R.id.btn_service:
                mFlagType = 2;
                TAG = "服务端: ";
                break;

            case R.id.btn_get_ip:
                if (mFlagType == 0) {
                    ToastUtil.show("请先选择身份");
                    return;
                }
                String ipAddress = NetworkUtil.getIPAddress(this);
                LogUtil.e("------>" + ipAddress);

                Message message = mHandler.obtainMessage();
                message.what = 999;
                message.obj = ipAddress;
                mHandler.sendMessage(message);

                break;
            case R.id.btn_send_data:
                if (mFlagType == 0) {
                    ToastUtil.show("请先选择身份");
                    return;
                }
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (mFlagType == 1) {
                            sendClientCSocket();
                        } else if (mFlagType == 2) {
                            sendServiceSocket();
                        }
                    }
                };
                new Thread(runnable).start();
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
            String ipAddress = mBinding.etInput.getText().toString();
            String content = mBinding.etInputContent.getText().toString();

            try {
                // 创建Socket对象
                if (mClientSocket == null) {
                    mClientSocket = new Socket(ipAddress, port);
                }

                // 发送数据，设置编码Utf-8
                if (mClientPrintStream == null) {
                    mClientPrintStream = new PrintStream(mClientSocket.getOutputStream(), true, charsetName);
                }

                // 发送数据出去
                mClientPrintStream.println(content);

                // 准备从服务器端接收回应的BufferedReader, 设置编码
                if (mClientBufferedReader == null) {
                    mClientBufferedReader = new BufferedReader(new InputStreamReader(mClientSocket.getInputStream(), charsetName));
                }

                mStringBuffer.append("服务端：" + mClientBufferedReader.read());
                // 设置服务端的回应数据
                mBinding.tvContent.setText(mStringBuffer.toString());

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

                try {
                    if (mClientBufferedReader != null) {
                        mClientBufferedReader.close();
                        mClientBufferedReader = null;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    LogUtil.e("Socket读取数据异常：" + ex.getMessage());
                }

                try {
                    if (mClientSocket != null) {
                        mClientSocket.close();
                        mClientSocket = null;
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    LogUtil.e("client 链接错误：" + ex.getMessage());
                }
            }
        }
    }

    private boolean flag = true;
    private Socket mServiceClient = null;

    private void sendServiceSocket() {
        boolean checked = checked();
        if (checked) {
            try {
                // 启动一个ServerSocket

                if (mServerSocket == null) {
                    mServerSocket = new ServerSocket(port);
                }

                while (flag) {
                    if (mServiceClient == null || mServiceClient.isClosed() || !mServiceClient.isConnected()) {
                        mServiceClient = mServerSocket.accept(); // 接受客户端请求
                    }

                    if (mServiceClient.isConnected()) {
                        LogUtil.e("connected!");
                        flag = false;
                    }
                    // 取得输入流读取客户端传送的数据,要接收中文只需将编码设置为"UTF-8"
                    if (mServiceReader == null) {
                        mServiceReader = new BufferedReader(new InputStreamReader(mServiceClient.getInputStream(), charsetName));
                    }

                    mStringBuffer.append("客户端：" + mServiceReader.read());
                    Message message = mHandler.obtainMessage();
                    message.what = 66;
                    message.obj = mStringBuffer.toString().trim();
                    // 设置收到的数据
                    mHandler.sendMessage(message);

                    // 给客户端发送数据

                    // 取得输出流向客户端返回应答数据, 编码要与输入流匹配
                    if (mServiceOut == null) {
                        mServiceOut = new PrintStream(mServiceClient.getOutputStream(), true, "gbk");
                    }
                    mServiceOut.println(mBinding.etInputContent.toString().trim());
                }

            } catch (IOException e) {
                e.printStackTrace();

                Message message = mHandler.obtainMessage();
                message.what = 66;
                message.obj = "服务端：异常：" + e.getMessage();
                mHandler.sendMessage(message);

                // 关掉所有

                if (mServiceClient != null) {
                    try {
                        mServiceClient.close();
                        mServiceClient = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                if (mServerSocket != null) {
                    try {
                        mServerSocket.close();
                        mServerSocket = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                if (mServiceReader != null) {
                    try {
                        mServiceReader.close();
                        mServiceReader = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                if (mServiceOut != null) {
                    mServiceOut.close();
                    mServiceOut = null;
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