package com.android.app.test.app2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.android.app.R;
import com.android.app.app.App;
import com.android.app.test.app.AppLifecycleAdapter;
import com.android.app.test.app.DeviceAdapter;
import com.android.app.test.app.LifecycleManager;
import com.android.app.test.app.account.AccountHelper;
import com.android.helper.base.BaseActivity;
import com.android.helper.common.EventMessage;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.LogWriteUtil;
import com.android.helper.utils.RecycleUtil;
import com.android.helper.utils.RxPermissionsUtil;
import com.android.helper.utils.SystemUtil;
import com.android.helper.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.android.helper.common.CommonConstants.FILE_LIFECYCLE_NAME;

public class BhActivity extends BaseActivity {

    private LifecycleManager mLifecycleManager;
    private LogWriteUtil mWriteUtil;
    private AppLifecycleAdapter mAppLifecycleAdapter;
    private RecyclerView mRvBluetoothList;
    private RecyclerView mRvLogList;
    private DeviceAdapter mDeviceAdapter;
    private final Map<String, String> map = new HashMap<>();

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_bh;
    }

    @Override
    protected void initView() {
        super.initView();

        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }

        Button btOpenDc = findViewById(R.id.bt_open_dc);
        Button btOpenAutoQd = findViewById(R.id.bt_open_auto_qd);
        Button btRefreshData = findViewById(R.id.bt_refresh_data);
        View btShowBluetooth = findViewById(R.id.bt_show_bluetooth);

        mRvBluetoothList = findViewById(R.id.rv_bluetooth_list);
        mRvLogList = findViewById(R.id.rv_log_list);

        btOpenDc.setOnClickListener(this);
        btOpenAutoQd.setOnClickListener(this);
        btRefreshData.setOnClickListener(this);
        btShowBluetooth.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    protected void initData() {
        super.initData();
        if (mLifecycleManager == null) {
            mLifecycleManager = LifecycleManager.getInstance();
        }

        // 检测运行权限
        checkPermission();

        // 账号保活
        AccountHelper.addAccount(getApplication());//添加账户
        AccountHelper.autoSync(getApplication());//调用告知系统自动同步

        // 后台服务写日志
        Intent intent = new Intent(mContext, BhService.class);
        startService(intent);

        mAppLifecycleAdapter = new AppLifecycleAdapter(mContext);
        RecycleUtil.getInstance(mContext, mRvLogList)
                .setVertical()
                .setAdapter(mAppLifecycleAdapter);

        mWriteUtil = new LogWriteUtil();
        List<String> read = mWriteUtil.read(FILE_LIFECYCLE_NAME);
        if (read != null && read.size() > 0) {
            Collections.reverse(read);
            mAppLifecycleAdapter.setList(read);
        }

        mDeviceAdapter = new DeviceAdapter(mContext);
        RecycleUtil
                .getInstance(mContext, mRvBluetoothList)
                .setVertical()
                .setAdapter(mDeviceAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_open_dc:
                // 充电权限
                boolean batteryOptimizations = SystemUtil.getInstance(App.getInstance()).isIgnoringBatteryOptimizations();
                if (!batteryOptimizations) {
                    // 3: 打开充电的权限
                    mLifecycleManager.checkBatteryPermissions(mContext);
                } else {
                    ToastUtil.show("电池优化权限已经打开了！");
                }
                break;

            case R.id.bt_open_auto_qd:
                // 自动启动权限
                // 4：自动启动的权限
                mLifecycleManager.checkAutoStartupPermissions(mContext);
                break;
            case R.id.bt_refresh_data:
                // 刷新数据
                List<String> read = mWriteUtil.read(FILE_LIFECYCLE_NAME);
                if (read != null && read.size() > 0) {
                    Collections.reverse(read);
                    mAppLifecycleAdapter.setList(read);
                }
                break;

            case R.id.bt_show_bluetooth:
                int visibility = mRvBluetoothList.getVisibility();
                if (visibility == View.GONE) {
                    mRvBluetoothList.setVisibility(View.VISIBLE);
                } else if (visibility == View.VISIBLE) {
                    mRvBluetoothList.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void checkPermission() {
        String[] list = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            list = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            };
        } else {
            list = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            };
        }

        RxPermissionsUtil util = new RxPermissionsUtil(mContext, list);
        util.setAllPermissionListener((havePermission, permission) -> {
            if (!havePermission) {
                LogUtil.e("异常的权限：" + permission.name);
            } else {
                LogUtil.e("正常的权限：" + havePermission);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventMessage event) {
        if (event != null) {
            int code = event.getCode();
            if (code == 111) {
                // 更新数据
                LogUtil.e("开始更新数据了！");
                if (mDeviceAdapter != null) {
                    Bundle bundle = event.getBundle();
                    String name = bundle.getString("name");
                    String address = bundle.getString("address");

                    map.put(address, name);

                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    ArrayList<Map.Entry<String, String>> entries1 = new ArrayList<>(entries);

                    mDeviceAdapter.setList(entries1);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (registered) {
            EventBus.getDefault().unregister(this);
        }

//        if (mLifecycleManager != null) {
//            mLifecycleManager.stopLifecycle(mContext);
//        }
    }
}