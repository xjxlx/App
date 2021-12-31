package com.android.app.test.banner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.FragmentVpBanner4Binding;
import com.android.helper.base.AppBaseBindingFragment;

import org.jetbrains.annotations.NotNull;

public class VpBanner4Fragment extends AppBaseBindingFragment<FragmentVpBanner4Binding> {

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
    public FragmentVpBanner4Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBanner4Binding.inflate(inflater, container, false);
    }
}