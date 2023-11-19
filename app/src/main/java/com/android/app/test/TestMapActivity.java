package com.android.app.test;

import static com.android.helper.common.CommonConstants.KEY_BASE_WEB_VIEW_URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.app.R;
import com.android.common.base.BaseActivity;
import com.android.common.base.BaseBindingTitleActivity;

/**
 * 测试功能的集合
 */
public class TestMapActivity extends BaseActivity {

    @Override
    public int getLayout() {
        return R.layout.activity_test_map;
    }

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {
    }

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.tv_back,
                R.id.tv_test_handler,
                R.id.tv_elv,
                R.id.tv_flex_box,
                R.id.tv_test_js_to_android,
                R.id.tv_test_scroll_help,
                R.id.tv_test_more_adapter,
                R.id.tv_test_ch,
                R.id.tv_test_web_socket,
                R.id.tv_test_webview,
                R.id.tv_test_touch,
                R.id.tv_test_lifecycle,
                R.id.tv_test_adapter,
                R.id.tv_test_util,
                R.id.tv_test_activity,
                R.id.tv_test_ip,
                R.id.tv_test_client_socket,
                R.id.tv_test_service_socket,
                R.id.tv_test_sf,
                R.id.tv_scroll_top,
                R.id.tv_android_socket);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_test_handler:
                startActivity(TestHandlerActivity.class);
                break;
            case R.id.tv_elv:
                startActivity(ExpandableActivity.class);
                break;
            case R.id.tv_flex_box:
                startActivity(FlexBoxLayoutActivity.class);
                break;
            case R.id.tv_test_js_to_android:
                startActivity(AndroidJSActivity.class);
                break;
            case R.id.tv_test_scroll_help:
                startActivity(TestScrollHelperActivity.class);
                break;
            case R.id.tv_test_more_adapter:
                startActivity(MoreAdapterActivity.class);
                break;
            case R.id.tv_test_ch:
                startActivity(SlidingMenuActivity.class);
                break;
            case R.id.tv_test_web_socket:
                startActivity(TestWebSocketActivity.class);
                break;
            case R.id.tv_test_webview:
                Intent intent = new Intent(mActivity, TestWebViewActivity.class);
                String url = "http://wx.smartservice.bjev.com.cn/BAIC_C62X_OM_HTML5_demo/index.html";
                intent.putExtra(KEY_BASE_WEB_VIEW_URL, url);
                startActivity(intent);
                break;
            case R.id.tv_test_touch:
                startActivity(TestTouchActivity.class);
                break;
            case R.id.tv_test_lifecycle:
                startActivity(ActivityOrFragmentLifecycleActivity.class);
                break;
            case R.id.tv_test_adapter:
                startActivity(AdapterActivity.class);
                break;
            case R.id.tv_test_util:
                startActivity(TestUtilActivity.class);
                break;
            case R.id.tv_test_activity:
                startActivity(TestActivityActivity.class);
                break;
            case R.id.tv_test_ip:
                startActivity(TestIpActivity.class);
                break;
            case R.id.tv_test_client_socket:
                startActivity(SocketClientActivity.class);
                break;
            case R.id.tv_test_service_socket:
                startActivity(ServiceSocketActivity.class);
                break;
            case R.id.tv_test_sf:
                startActivity(TestSfActivity.class);
                break;
            case R.id.tv_scroll_top:
                startActivity(TestScrollTopActivity.class);
                break;
            case R.id.tv_android_socket:
                startActivity(Socket_Android_Activity.class);
                break;
        }
    }

}