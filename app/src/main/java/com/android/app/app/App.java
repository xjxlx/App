package com.android.app.app;

import android.app.Application;

import com.amap.api.maps.MapsInitializer;
import com.amap.api.services.core.ServiceSettings;
import com.android.app.BuildConfig;
import com.android.app.R;
import com.android.helper.app.BaseApplication;
import com.android.helper.base.title.TitleBar;
import com.android.helper.base.title.TitleBuilder;
import com.android.helper.httpclient.AutoInterceptor;
import com.android.helper.interfaces.ICommonApplication;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

public class App extends Application {

    private static App mApp;

    public static App getInstance() {
        return mApp;
    }

    public static final List<Class> mClassList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        BaseApplication.getInstance().setICommonApplication(new ICommonApplication() {
            @Override
            public Application getApplication() {
                return App.this;
            }

            @Override
            public boolean isDebug() {
                return BuildConfig.DEBUG;
            }

            @Override
            public String logTag() {
                return "AppHelper";
            }

            @Override
            public String getAppName() {
                return getResources().getString(R.string.app_name);
            }

            @Override
            public void initApp() {

                // 设置title的资源信息
                TitleBuilder builder = new TitleBuilder()
                        .setTitleLayoutId(R.layout.base_title_activity)
                        .setLeftBackLayoutId(R.id.ll_base_title_back)
                        .setLeftBackTextId(R.id.tv_base_title_back_title)
                        .setShowBackText(true)
                        .setTitleId(R.id.tv_base_title)
                        .setRightLayoutId(R.id.fl_base_title_right_parent)
                        .setShowRightLayout(true)
                        .setRightTextId(R.id.tv_base_title_right_title)
                        .setShowRightText(true)
                        .setContentLayoutId(R.id.fl_activity_content);
                TitleBar.setGlobalTitleBar(builder);

            }

            @Override
            public String getBaseUrl() {
                return "http://api-zhgj-app.beixin.hi-cloud.net:8000/gateway-api/";
            }

            @Override
            public Interceptor[] getInterceptors() {
                return new Interceptor[]{new AutoInterceptor()};
            }
        });

        initData();
    }

    private void initData() {
        // Bugly2.0
        CrashReport.initCrashReport(getApplicationContext(), "09c9b19788", true);

        // 高德地图
        // 地图 合规接口
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);
        // 搜索 合规接口
        ServiceSettings.updatePrivacyShow(this, true, true);
        ServiceSettings.updatePrivacyAgree(this, true);
    }
}
