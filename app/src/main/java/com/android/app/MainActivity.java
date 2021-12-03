package com.android.app;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.app.databinding.ActivityMainBinding;
import com.android.app.ui.fragment.HomeFragment;
import com.android.app.ui.fragment.PersonalFragment;
import com.android.app.ui.fragment.TodoFragment;
import com.android.helper.base.BaseBindingActivity;
import com.android.helper.base.BaseFragmentPagerAdapter;
import com.android.helper.interfaces.listener.AllPermissionsListener;
import com.android.helper.utils.FileUtil;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.RxPermissionsUtil;
import com.android.helper.utils.dialog.DialogUtil;
import com.tbruyelle.rxpermissions.Permission;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    private final List<Fragment> mListFragments = new ArrayList<>();
    private final List<String> mListTitle = new ArrayList<>();
    private DialogUtil mDialogUtil;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("NonConstantResourceId")
    @Override
    public void initData(Bundle savedInstanceState) {
        String[] strings = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
//                Manifest.permission.FOREGROUND_SERVICE
        };

        RxPermissionsUtil permissionsUtil = new RxPermissionsUtil(mContext, strings);
        permissionsUtil.setAllPermissionListener(new AllPermissionsListener() {
            @Override
            public void onRxPermissions(boolean havePermission, Permission permission) {
                LogUtil.e("是否拥有读写权限：" + havePermission);
            }
        });

        mListFragments.add(new HomeFragment());
        mListFragments.add(new TodoFragment());
        mListFragments.add(new PersonalFragment());

        mListTitle.add("首页");
        mListTitle.add("待办");
        mListTitle.add("个人中心");

        BaseFragmentPagerAdapter pagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), mListFragments, mListTitle);

        mBinding.vpContent.setAdapter(pagerAdapter);
        // 避免重复创建加载数据
        mBinding.vpContent.setOffscreenPageLimit(mListFragments.size());
        // viewPager选中的监听
        mBinding.vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //设置默认选中item
                mBinding.navigation.getMenu().getItem(position).setChecked(true);
                switch (position) {
                    case 0:
                        setTitleContent("首页");
                        break;
                    case 1:
                        setTitleContent("代办");
                        break;
                    case 2:
                        setTitleContent("个人中心");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // 底部导航器选中的监听事件
        mBinding.navigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                // 首页
                case R.id.navigation_home:
                    mBinding.vpContent.setCurrentItem(0);
                    break;

                // 待办
                case R.id.navigation_moment:
                    mBinding.vpContent.setCurrentItem(1);
                    break;

                // 个人中心
                case R.id.navigation_personal:
                    mBinding.vpContent.setCurrentItem(2);
                    break;
            }
            return false;
        });

        // 设置默认数据
        mBinding.navigation.getMenu().getItem(1).setChecked(true);
        mBinding.vpContent.setCurrentItem(1);
        setTitleContent("代办");

        String sdTypePublicPath = FileUtil.getInstance().getSdTypePublicPath(Environment.DIRECTORY_DOWNLOADS);
        LogUtil.e("SD ---> 公共资源目录:  --- " + sdTypePublicPath);

        boolean permission = FileUtil.getInstance().checkAllFilesPermission(mContext);
        LogUtil.e("permission: " + permission);
    }

    private void setTitleContent(String value) {

    }

    @Override
    public ActivityMainBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ActivityMainBinding.inflate(inflater, container, false);
    }

}