package com.android.app.test.banner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.app.databinding.FragmentBannerQTBinding;
import com.android.helper.base.BaseBindingFragment;
import com.android.helper.base.BaseFragmentPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BannerQTFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BannerQTFragment extends BaseBindingFragment<FragmentBannerQTBinding> {

    private static BannerQTFragment fragment;

    public BannerQTFragment() {
        // Required empty public constructor
    }

    public static BannerQTFragment newInstance() {
        if (fragment == null) {
            fragment = new BannerQTFragment();
        }
        return fragment;
    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData(Bundle savedInstanceState)  {
        QT2Fragment qt2Fragment = QT2Fragment.newInstance();

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(qt2Fragment);

        BaseFragmentPagerAdapter baseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments);
        mBinding.flContent2.setAdapter(baseFragmentPagerAdapter);
    }

    @Override
    public FragmentBannerQTBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return FragmentBannerQTBinding.inflate(inflater, container, false);
    }
}