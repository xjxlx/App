package com.android.app.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.R;
import com.android.app.databinding.ActivityServiceSocketBinding;
import com.android.app.ui.services.KeepService;
import com.android.helper.base.title.AppBaseBindingTitleActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NetworkUtil;
import com.android.helper.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

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
    private String mIpAddress;

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
        setonClickListener(R.id.btn_get_ip, R.id.btn_send_data, R.id.btn_send_broadcast, R.id.btn_start, R.id.btn_keep, R.id.btn_stop);
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

            case R.id.btn_send_data:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        sendServiceSocket();
                    }
                }.start();
                break;

            case R.id.btn_send_broadcast:
                sendBroadcast();
                break;

            case R.id.btn_start:
                //  吸气
                KeepService.start();

                break;

            case R.id.btn_keep:
                // 屏住呼吸
                KeepService.keep();

                break;

            case R.id.btn_stop:
                // 呼气
                KeepService.stop();

                try {
                    WebSettings settings = mBinding.wb.getSettings();
//                    String urlValue = "https://www.baidu.com/";
                    String urlValue = "http://192.168.8.126:8080/abc.json";

                    // 开始配置webView
                    settings.setBuiltInZoomControls(true);// 显示缩放按钮(wap网页不支持)
                    settings.setJavaScriptEnabled(true);// 支持js功能

                    //设置自适应屏幕，两者合用
                    settings.setUseWideViewPort(true);// 支持双击缩放(wap网页不支持)
                    settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
                    settings.setLoadsImagesAutomatically(true); //支持自动加载图片
                    settings.setDefaultTextEncodingName("utf-8");//设置编码格式
                    //不显示webview缩放按钮
                    settings.setDisplayZoomControls(false);
                    //禁止屏幕缩放
                    settings.setSupportZoom(false);
                    settings.setBuiltInZoomControls(false);

                    settings.setBlockNetworkImage(false);//解决图片不显示

                    //  WebSettings.LOAD_DEFAULT 如果本地缓存可用且没有过期则使用本地缓存，否加载网络数据 默认值
                    //  WebSettings.LOAD_CACHE_ELSE_NETWORK 优先加载本地缓存数据，无论缓存是否过期
                    //  WebSettings.LOAD_NO_CACHE  只加载网络数据，不加载本地缓存
                    //  WebSettings.LOAD_CACHE_ONLY 只加载缓存数据，不加载网络数据
                    //Tips:有网络可以使用LOAD_DEFAULT 没有网时用LOAD_CACHE_ELSE_NETWORK
                    settings.setCacheMode(WebSettings.LOAD_DEFAULT);

                    // 解决图片不显示问题
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                    }
                    // 使用该方法，就不会去启动其他的浏览器
                    mBinding.wb.setWebViewClient(new WebViewClient());

                    // 使用该方法，去加载进度条
                    mBinding.wb.setWebChromeClient(new WebChromeClient() {
                        @Override
                        public void onProgressChanged(WebView view, int newProgress) {
                            if (newProgress >= 100) {

                            } else {
                                //更新进度
                            }
                            super.onProgressChanged(view, newProgress);
                        }

                        @Override
                        public void onShowCustomView(View view, CustomViewCallback callback) {
                            super.onShowCustomView(view, callback);
                        }

                        @Override
                        public void onHideCustomView() {
                            super.onHideCustomView();
                        }

                        @Override
                        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                            return super.onJsAlert(view, url, message, result);
                        }
                    });
                    //加载网页链接
                    mBinding.wb.loadUrl(urlValue);
                } catch (Exception e) {
                    mBinding.wb.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show("数据异常：" + e.getMessage());
                        }
                    });
                }

                break;
        }
    }

    private void sendBroadcast() {
        if (TextUtils.isEmpty(mIpAddress)) {
            mIpAddress = "xxx.xxx.xxx.xxx";
        }

        //:在android中数据的传递都是使用的Intent
        Intent intent = new Intent();
        //:设置Intent_filter
        intent.setAction("com.xjx.test");
        intent.putExtra("ip", mIpAddress);
        // 显示指定应用
        intent.setPackage("com.xjx.kotlin");
        //:发送无序广播
        sendBroadcast(intent);
//        ToastUtil.show("广播发送成功，Ip地址为：" + mIpAddress);

        String packageName = getPackageName();
        LogUtil.e("packageName:::  " + packageName);
        String canonicalName = KeepService.class.getCanonicalName();
        LogUtil.e("canonicalName:::  " + canonicalName);
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