package com.android.app.test.banner;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
        }
        fragment = new VpBanner5Fragment();
        return fragment;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData(Bundle savedInstanceState)  {

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
    public FragmentVpBanner5Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBanner5Binding.inflate(inflater, container, false);
    }
}