package com.android.app.test.app2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.app.R;
import com.android.app.app.App;
import com.android.app.test.app.LifecycleManager;
import com.android.app.test.app.account.AccountHelper;
import com.android.helper.base.BaseActivity;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.RxPermissionsUtil;
import com.android.helper.utils.SystemUtil;
import com.android.helper.utils.ToastUtil;

public class BhActivity extends BaseActivity {

    private Button mBtOpenDc;
    private Button mBtOpenAutoQd;
    private CheckBox mBtOpenBluetooth;
    private RecyclerView mRvBluetoothList;
    private RecyclerView mRvLogList;
    private LifecycleManager mLifecycleManager;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_bh;
    }

    @Override
    protected void initView() {
        super.initView();

        mBtOpenDc = findViewById(R.id.bt_open_dc);
        mBtOpenAutoQd = findViewById(R.id.bt_open_auto_qd);
        mBtOpenBluetooth = findViewById(R.id.bt_open_bluetooth);
        mRvBluetoothList = findViewById(R.id.rv_bluetooth_list);
        mRvLogList = findViewById(R.id.rv_log_list);

        mBtOpenDc.setOnClickListener(this);
        mBtOpenAutoQd.setOnClickListener(this);
        mBtOpenBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
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
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void checkPermission() {
        RxPermissionsUtil util = new RxPermissionsUtil(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.BLUETOOTH_PRIVILEGED,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
        );
        util.setAllPermissionListener((havePermission, permission) -> {
            LogUtil.e("SD卡的读写权限：" + havePermission);
        });
    }
}