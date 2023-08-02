package com.android.app.app.Keepalive;

import static com.android.helper.common.CommonConstants.FILE_LIFECYCLE_NAME;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.R;
import com.android.app.app.App;
import com.android.app.databinding.ActivityAppLifecycleBinding;
import com.android.common.utils.LogUtil;
import com.android.common.utils.LogWriteUtil;
import com.android.helper.base.BaseBindingActivity;
import com.android.helper.common.CommonConstants;
import com.android.helper.common.EventMessage;
import com.android.helper.utils.RecycleUtil;
import com.android.helper.utils.ServiceUtil;
import com.android.helper.utils.SystemUtil;
import com.android.helper.utils.ToastUtil;
import com.android.helper.utils.permission.RxPermissionsUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * App保活
 */
public class AppLifecycleActivity extends BaseBindingActivity<ActivityAppLifecycleBinding> {

    private LifecycleManager mLifecycleManager;
    private LogWriteUtil mWriteUtil;
    private AppLifecycleAdapter mAppLifecycleAdapter;
    private DeviceAdapter mDeviceAdapter;
    private final Map<String, String> map = new HashMap<>();

    @Override
    public void initView() {
        boolean registered = EventBus.getDefault().isRegistered(this);
        if (!registered) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void initListener() {
        super.initListener();
        mBinding.btOpenDc.setOnClickListener(this);
        mBinding.btOpenAutoQd.setOnClickListener(this);
        mBinding.btRefreshData.setOnClickListener(this);
        mBinding.btShowBluetooth.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    @Override
    public void initData(Bundle savedInstanceState) {
        if (mLifecycleManager == null) {
            mLifecycleManager = LifecycleManager.getInstance();
        }

        // 检测运行权限
        checkPermission();

        // 开启保活
        String name = AppLifecycleService.class.getName();
        String jobName = AppJobService.class.getName();

        mLifecycleManager.startLifecycle(getApplication(), name, jobName);

        mAppLifecycleAdapter = new AppLifecycleAdapter(mActivity);
        RecycleUtil.getInstance(mActivity, mBinding.rvLogList).setVertical().setAdapter(mAppLifecycleAdapter);

        mWriteUtil = new LogWriteUtil(FILE_LIFECYCLE_NAME);
//        List<String> read = mWriteUtil.read(FILE_LIFECYCLE_NAME);
//        if (read != null && read.size() > 0) {
//            Collections.reverse(read);
//            mAppLifecycleAdapter.setList(read);
//        }

        mDeviceAdapter = new DeviceAdapter(mActivity);
        RecycleUtil.getInstance(mActivity, mBinding.rvBluetoothList).setVertical().setAdapter(mDeviceAdapter);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.bt_open_dc:
                // 充电权限
                boolean batteryOptimizations =
                    SystemUtil.getInstance(App.getInstance()).isIgnoringBatteryOptimizations();
                if (!batteryOptimizations) {
                    // 3: 打开充电的权限
                    mLifecycleManager.checkBatteryPermissions(mActivity);
                } else {
                    ToastUtil.show("电池优化权限已经打开了！");
                }
                break;

            case R.id.bt_open_auto_qd:
                // 自动启动权限
                // 4：自动启动的权限
                mLifecycleManager.checkAutoStartupPermissions(mActivity);
                break;
            case R.id.bt_refresh_data:
                boolean serviceRunning = ServiceUtil.isServiceRunning(mActivity, AppLifecycleService.class);
                ToastUtil.show("刷新数据 ：" + serviceRunning);

//                List<String> read = mWriteUtil.read(FILE_LIFECYCLE_NAME);
//                if (read != null && read.size() > 0) {
//                    Collections.reverse(read);
//                    mAppLifecycleAdapter.setList(read);
//                }
                break;

            case R.id.bt_show_bluetooth:
                int visibility = mBinding.rvBluetoothList.getVisibility();
                if (visibility == View.GONE) {
                    mBinding.rvBluetoothList.setVisibility(View.VISIBLE);
                } else if (visibility == View.VISIBLE) {
                    mBinding.rvBluetoothList.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void checkPermission() {
        String[] list = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            list = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.GET_ACCOUNTS,
                "android.permission.AUTHENTICATE_ACCOUNTS"};
        } else {
            list = new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        }

        new RxPermissionsUtil.Builder(mActivity, list).setSinglePerMissionListener((status, permission) -> {
            if (status == 1) {
                LogUtil.e("异常的权限：" + permission);
            } else if (status == 2) {
                LogUtil.e("异常的权限：" + permission);
            } else if (status == 3) {
                LogUtil.e("拒绝的权限：" + permission);
            }
        }).build().startRequestPermission();
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

                    LogUtil.writeAll(CommonConstants.FILE_LIFECYCLE_NAME,
                        "-->蓝牙扫描回调---成功：" + name + "  描到的蓝牙地址为：" + address);

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

        // if (mLifecycleManager != null) {
        // mLifecycleManager.stopLifecycle(mContext);
        // }
    }

    @Override
    public ActivityAppLifecycleBinding getBinding(@NonNull @NotNull LayoutInflater inflater,
        @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ActivityAppLifecycleBinding.inflate(inflater, container, false);
    }
}