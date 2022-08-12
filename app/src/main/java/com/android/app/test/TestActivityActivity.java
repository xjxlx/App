package com.android.app.test;

import android.os.Bundle;
import android.text.TextUtils;

import com.android.app.R;
import com.android.helper.base.title.AppBaseTitleActivity;
import com.android.helper.utils.LogUtil;

import java.security.MessageDigest;
import java.util.Stack;

public class TestActivityActivity extends AppBaseTitleActivity {

    @Override
    protected int getTitleLayout() {
        return R.layout.activity_test_activity;
    }

    @Override
    protected String setTitleContent() {
        return "测试Activity";
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        String encrypt = encrypt("哈哈");
        LogUtil.e("encrypt:" + encrypt);

        String md5 = "8c8fa3529ee34d4e69a0baafb7069da3";
        if (TextUtils.equals(md5, encrypt)) {
            LogUtil.e("md5想通！");
        } else {
            LogUtil.e("md5不想通！");
        }
    }

    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        stack1.push(node);
    }

    public int pop() {
        if (stack2.empty()) {
            while (!stack1.empty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }

    public static String encrypt(String dataStr) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(dataStr.getBytes("UTF8"));
            byte[] s = m.digest();
            String result = "";
            for (int i = 0; i < s.length; i++) {
                result += Integer.toHexString((0x000000FF & s[i]) | 0xFFFFFF00).substring(6);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}