package com.android.app.test.banner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.app.databinding.FragmentQT2Binding;
import com.android.helper.base.AppBaseBindingFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QT2Fragment extends AppBaseBindingFragment<FragmentQT2Binding> {

    private static QT2Fragment fragment;

    public QT2Fragment() {
        // Required empty public constructor
    }

    public static QT2Fragment newInstance() {
        if (fragment == null) {
            fragment = new QT2Fragment();
        }
        return fragment;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData(Bundle savedInstanceState)  {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(VpBanner1Fragment.newInstance());
        fragments.add(VpBanner2Fragment.newInstance());
        fragments.add(VpBanner3Fragment.newInstance());

//        mBinding.banner
//                .setFragmentData(fragments)
//                .setItemClickListener(new BannerItemClickListener() {
//                    @Override
//                    public void onItemClick(Fragment fragment, View view, int position, Object object) {
//                        ToastUtil.show("position:" + position);
//                    }
//                })
//                .show(this, getChildFragmentManager());
    }

    @Override
    public FragmentQT2Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentQT2Binding.inflate(inflater, container, false);
    }
}