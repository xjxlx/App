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
import com.android.helper.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServiceSocketActivity extends AppBaseBindingTitleActivity<ActivityServiceSocketBinding> {

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

    }

    private int port = 9999;// 端口号
    private String charsetName = "UTF-8";
    private final StringBuffer mStringBuffer = new StringBuffer();

    private ServerSocket mServerSocket = null;
    private BufferedReader mServiceReader;
    private PrintStream mServiceOut;

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.btn_get_ip, R.id.btn_send_data, R.id.btn_init_data, R.id.btn_send_data, R.id.btn_get_data);
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

                new Thread() {
                    @Override
                    public void run() {
                        super.run();

                        initSocket();
                    }
                }.start();

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

            case R.id.btn_get_data:

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        getData();
                    }
                }.start();

                break;
        }
    }

    private void getData() {
        try {
            // 取得输入流读取客户端传送的数据,要接收中文只需将编码设置为"UTF-8"
            if (mServiceReader == null) {
                mServiceReader = new BufferedReader(new InputStreamReader(mServiceClient.getInputStream(), charsetName));
            }

            mStringBuffer.append("客户端：" + mServiceReader.read());
            Message message = mHandler.obtainMessage();
            message.what = 44;
            message.obj = mStringBuffer.toString().trim();

            // 设置收到的数据
            mHandler.sendMessage(message);
        } catch (Exception e) {

            if (mServiceReader != null) {
                try {
                    mServiceReader.close();
                    mServiceReader = null;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void initSocket() {

        boolean checked = checked();
        if (checked) {
            try {
                // 启动一个ServerSocket

                Message message = mHandler.obtainMessage();
                message.what = 44;

                if (mServerSocket == null) {
                    mServerSocket = new ServerSocket(port);
                }

                while (flag) {
                    if (mServiceClient == null) {
                        mServiceClient = mServerSocket.accept(); // 接受客户端请求
                    }

                    if (mServiceClient.isConnected()) {
                        LogUtil.e("connected!");
                        flag = false;
                    }
                }

                message.obj = "service 启动成功！";
                mHandler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();

                Message message = mHandler.obtainMessage();
                message.what = 33;
                message.obj = "服务端：异常：" + e.getMessage();
                mHandler.sendMessage(message);

                // 关掉所有
                if (mServerSocket != null) {
                    try {
                        mServerSocket.close();
                        mServerSocket = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                if (mServiceClient != null) {
                    try {
                        mServiceClient.close();
                        mServiceClient = null;
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean checked() {
        Message message = mHandler.obtainMessage();
        if (TextUtils.isEmpty(mBinding.etInputContent.getText().toString())) {
            message.obj = "要发送的内容为空！";
            message.what = 22;
            mHandler.sendMessage(message);
            return false;
        }
        return true;
    }

    private boolean flag = true;
    private Socket mServiceClient = null;

    private void sendServiceSocket() {
        boolean checked = checked();
        if (checked) {
            try {
                // 取得输出流向客户端返回应答数据, 编码要与输入流匹配
                if (mServiceClient != null) {
                    if (mServiceOut == null) {
                        mServiceOut = new PrintStream(mServiceClient.getOutputStream(), true, "gbk");
                    }
                }

                String trim = mBinding.etInputContent.toString().trim();
                if (mServiceOut != null) {
                    mServiceOut.println(trim);
                    Message message = mHandler.obtainMessage();
                    message.what = 44;
                    message.obj = "服务端：发送完毕：" + trim;
                    mHandler.sendMessage(message);
                }

            } catch (IOException e) {
                e.printStackTrace();

                Message message = mHandler.obtainMessage();
                message.what = 33;
                message.obj = "服务端：异常：" + e.getMessage();
                mHandler.sendMessage(message);

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