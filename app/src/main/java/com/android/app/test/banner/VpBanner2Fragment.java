package com.android.app.test.banner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.app.databinding.FragmentVpBanner2Binding;
import com.android.helper.base.BaseBindingFragment;

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
    public void initData() {

    }

    @Override
    public FragmentVpBanner2Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentVpBanner2Binding.inflate(inflater, container, false);
    }
}