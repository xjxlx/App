package com.android.app.test.banner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.FragmentVpBannerBinding;
import com.android.helper.base.BaseBindingFragment;

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
            fragment = new VpBanner1Fragment();
        }
        return fragment;
    }

    @Override
    protected void initView(View view) {
        Bundle arguments = getArguments();
    }

    @Override
    protected void initData() {

    }

    @Override
    public FragmentVpBannerBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBannerBinding.inflate(inflater, container, false);
    }

}