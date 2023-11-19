package com.android.app.test.banner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.FragmentVpBannerBinding;
import com.android.common.base.BaseBindingFragment;
import com.android.common.utils.LogUtil;

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
    public void initData(Bundle savedInstanceState) {
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

    @NonNull
    @Override
    public FragmentVpBannerBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return FragmentVpBannerBinding.inflate(inflater, container, false);
    }
}