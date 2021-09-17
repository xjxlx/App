package com.android.app.test.banner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.FragmentVpBannerBinding;
import com.android.helper.base.BaseBindingFragment;
import com.android.helper.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

/**
 * banner的Fragment
 */
public class VpBanner1Fragment extends BaseBindingFragment<FragmentVpBannerBinding> {

    private static VpBanner1Fragment fragment;

    public VpBanner1Fragment() {
    }

    public static VpBanner1Fragment newInstance() {
        if (fragment == null) {
        }
        fragment = new VpBanner1Fragment();
        return fragment;
    }

    @Override
    public void initView(View view) {
        Bundle arguments = getArguments();
    }

    @Override
    public void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle arguments = getArguments();
        if (arguments != null) {
            int position = arguments.getInt("position", 0);
             setPosition(position);
        }
    }

    @SuppressLint("SetTextI18n")
    public void setPosition(int position) {
        if (mBinding.tvPosition != null) {
            mBinding.tvPosition.setText(position + "");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.e("onHiddenChanged");
    }

    @Override
    public FragmentVpBannerBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBannerBinding.inflate(inflater, container, false);
    }

}