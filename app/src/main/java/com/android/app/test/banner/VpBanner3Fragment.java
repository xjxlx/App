package com.android.app.test.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.FragmentVpBanner3Binding;
import com.android.helper.base.BaseBindingFragment;

import org.jetbrains.annotations.NotNull;

public class VpBanner3Fragment extends BaseBindingFragment<FragmentVpBanner3Binding> {

    private static VpBanner3Fragment fragment;

    public VpBanner3Fragment() {
        // Required empty public constructor
    }

    public static VpBanner3Fragment newInstance() {
        if (fragment == null) {
            fragment = new VpBanner3Fragment();
        }
        return fragment;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initData() {

    }

    @Override
    public FragmentVpBanner3Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBanner3Binding.inflate(inflater, container, false);
    }
}