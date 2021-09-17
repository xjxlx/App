package com.android.app.test.banner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.FragmentVpBanner3Binding;
import com.android.helper.base.BaseBindingFragment;
import com.android.helper.utils.LogUtil;

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
    public void initView(View view) {

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
    public FragmentVpBanner3Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBanner3Binding.inflate(inflater, container, false);
    }
}