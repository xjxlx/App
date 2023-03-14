package com.android.app.test;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.app.R;
import com.android.helper.base.AppBaseActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.Proxy.ProxyInterface;
import com.android.helper.utils.Proxy.ProxyUtil;

import java.util.ArrayList;
import java.util.List;

public class TestHandlerActivity extends AppBaseActivity implements ProxyInterface {

    private TextView textView;
    private final List<String> mList = new ArrayList<String>();

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_test_handler;
    }

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

        setonClickListener(R.id.button, R.id.button2, R.id.button3, R.id.button4);
        textView = findViewById(R.id.textView);

        ProxyUtil proxyUtil = new ProxyUtil();
        ProxyUtil.setObject(this);
        ProxyInterface instance = proxyUtil.getInstance();
        instance.invoke("333333");
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.button:
                Message message2 = mHandler.obtainMessage();
                message2.what = 456;
                mHandler.sendMessage(message2);
                break;

            case R.id.button2:
                Message message = mHandler.obtainMessage();
                message.what = 123;
                mHandler.sendMessageDelayed(message, 1000);
                break;

            case R.id.button3:
                mHandler.removeMessages(456);
                break;
            case R.id.button4:
                mHandler.removeMessages(123);
                break;
        }
    }

    int position = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 123:
                    mList.add("123");

                    LogUtil.write("蓝牙数据", "我是测试数据：" + (++position));

                    Message message = mHandler.obtainMessage();
                    message.what = 123;
                    mHandler.sendMessageDelayed(message, 1000);

                    break;
                case 456:
                    mList.add("456");

                    Message message2 = mHandler.obtainMessage();
                    message2.what = 456;
                    mHandler.sendMessageDelayed(message2, 1000);

                    break;
            }

            textView.setText(mList.toString());
        }
    };

    @Override
    public void invoke(Object object) {
        LogUtil.e("object:" + object);
    }
}