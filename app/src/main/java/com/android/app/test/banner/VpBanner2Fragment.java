package com.android.app.test.banner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.app.databinding.FragmentVpBanner2Binding;
import com.android.helper.base.BaseBindingFragment;
import com.android.helper.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VpBanner2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VpBanner2Fragment extends BaseBindingFragment<FragmentVpBanner2Binding> {

    private static VpBanner2Fragment fragment;

    public VpBanner2Fragment() {
        // Required empty public constructor
    }

    public static VpBanner2Fragment newInstance() {
        if (fragment == null) {
            fragment = new VpBanner2Fragment();
        }
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
    public FragmentVpBanner2Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBanner2Binding.inflate(inflater, container, false);
    }
}