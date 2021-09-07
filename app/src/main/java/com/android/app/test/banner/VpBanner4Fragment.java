package com.android.app.test.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.FragmentVpBanner4Binding;
import com.android.helper.base.BaseBindingFragment;

import org.jetbrains.annotations.NotNull;

public class VpBanner4Fragment extends BaseBindingFragment<FragmentVpBanner4Binding> {

    private static VpBanner4Fragment fragment;

    public VpBanner4Fragment() {
        // Required empty public constructor
    }

    public static VpBanner4Fragment newInstance() {
        if (fragment == null) {
            fragment = new VpBanner4Fragment();
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
    public FragmentVpBanner4Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBanner4Binding.inflate(inflater, container, false);
    }
}