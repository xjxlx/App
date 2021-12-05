package com.android.app.test;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.app.R;
import com.android.app.adapters.AppInfoAdapter;
import com.android.app.bean.AppInfoBean;
import com.android.app.databinding.ActivityControlAppBinding;
import com.android.app.services.LookDogService;
import com.android.helper.base.BaseActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.permission.RxPermissionsUtil;
import com.android.helper.utils.permission.SinglePermissionsCallBackListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ControlAppTitleActivity extends BaseActivity {

    private ActivityControlAppBinding binding;
    private List<AppInfoBean> mListAppInfo1 = new ArrayList<>();
    private List<AppInfoBean> mListAppInfo2 = new ArrayList<>();
    private AppInfoAdapter adapter;
    private long startTime;
    private long endTime;
    private UsageStatsManager usageStatsManager;

    @Override
    public void initView() {
        super.initView();
        binding = ActivityControlAppBinding.inflate(getLayoutInflater());

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initData(Bundle savedInstanceState) {

        runOnUiThread(() -> {
            // 获取应用的所有信息
            PackageManager pm = getPackageManager();
            // Return a List of all packages that are installed on the device.
            List<PackageInfo> packages = pm.getInstalledPackages(0);  // 不解析其他额外额信息
            for (PackageInfo packageInfo : packages) {
                AppInfoBean bean = new AppInfoBean();
                // app包名
                bean.setPackageName(packageInfo.packageName);
                // app名字
                bean.setAppName(packageInfo.applicationInfo.loadLabel(pm).toString());
                // app的图标
                bean.setAppIcon(packageInfo.applicationInfo.loadIcon(pm));
                // 文件大小
                bean.setAppSize(packageInfo.applicationInfo.sourceDir.length());

                if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    bean.setSystem(true);
                    mListAppInfo1.add(bean);
                } else {
                    bean.setSystem(false);
                    mListAppInfo2.add(bean);
                }
            }

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new AppInfoAdapter(mContext, mListAppInfo1, mListAppInfo2);
        binding.rvList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.rvList.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            new RxPermissionsUtil.Builder(mContext,
                    Manifest.permission.PACKAGE_USAGE_STATS,
                    Manifest.permission.FOREGROUND_SERVICE
            ).setSinglePerMissionListener((status, permission) -> LogUtil.e("权限：" + permission + "  状态：" + status))
                    .build()
                    .startRequestPermission();
        }

        usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        Calendar calendar = Calendar.getInstance();
        endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_WEEK, -2);
        startTime = calendar.getTimeInMillis();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mContext)) {
                //若未授权则请求权限
                Intent intent3 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent3.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent3, 123);
            }
        } else {
            // 跳转应用
            List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);
            if (usageStats == null || usageStats.size() == 0) {// 没有权限，获取不到数据
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }

        Intent intent = new Intent(mContext, LookDogService.class);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            // 跳转应用
            List<UsageStats> usageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, startTime, endTime);
            if (usageStats == null || usageStats.size() == 0) {// 没有权限，获取不到数据
                Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_control_app;
    }
}