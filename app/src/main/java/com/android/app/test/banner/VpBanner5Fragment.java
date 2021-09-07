package com.android.app.test.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.FragmentVpBanner5Binding;
import com.android.helper.base.BaseBindingFragment;

import org.jetbrains.annotations.NotNull;

public class VpBanner5Fragment extends BaseBindingFragment<FragmentVpBanner5Binding> {

    private static VpBanner5Fragment fragment;

    public VpBanner5Fragment() {
        // Required empty public constructor
    }

    public static VpBanner5Fragment newInstance() {
        if (fragment == null) {
            fragment = new VpBanner5Fragment();
        }
        return fragment;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public FragmentVpBanner5Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBanner5Binding.inflate(inflater, container, false);
    }
}