package com.android.app.ui.activity.java;

import android.os.Bundle;
import android.widget.TextView;

import com.android.app.R;
import com.android.common.utils.LogUtil;
import com.android.helper.base.AppBaseActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestJavaLifeActivity extends AppBaseActivity {

    private static List<String> list;

    private String a = "abc";

    private static String b = "edf";

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_test_java_life;
    }

    @Override
    public void initView() {
        list.add("构造方法！");
        list.add("\r\n");

        test1();
        test2();
        TextView textView = findViewById(R.id.tv_content);
        textView.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText(Arrays.toString(list.toArray()));
            }
        }, 3000);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    {
        LogUtil.e("普通代码块！");
        list.add("普通代码块！");
        list.add("\r\n");
        list.add("普通成员变量：a:" + a);
        list.add("\r\n");
        list.add("静态成员变量：b:" + b);
        list.add("\r\n");
    }

    static {
        list = new ArrayList<>();
        LogUtil.e("静态代码块！");
        list.add("\r\n");

        list.add("静态代码块！");
        list.add("\r\n");

        list.add("静态成员变量：b:" + b);
        list.add("\r\n");

    }

    public void test1() {
        LogUtil.e("我是普通方法！");
        list.add("我是普通方法！");
        list.add("\r\n");

        list.add("普通成员变量：a:" + a);
        list.add("\r\n");

        list.add("静态成员变量：b:" + b);
        list.add("\r\n");

    }

    public static void test2() {
        LogUtil.e("我是静态方法！");
        list.add("我是静态方法:");
        list.add("\r\n");

        list.add("静态成员变量：b:" + b);
        list.add("\r\n");

    }
}