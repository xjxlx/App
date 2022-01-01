package com.android.app.ui.activity.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.databinding.ActivityCustomRound2Binding;
import com.android.helper.base.title.BaseBindingTitleActivity;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

public class CustomRound2Activity extends BaseBindingTitleActivity<ActivityCustomRound2Binding,String> {

    @Override
    public ActivityCustomRound2Binding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ActivityCustomRound2Binding.inflate(inflater, container, true);
    }

    /**
     * Activity初始化view
     */
    @Override
    public void initView() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    protected String setTitleContent() {
        return "自定义圆形图片";
    }

    @Override
    public Observable<String> getObservable() {
        return null;
    }

    @Override
    public void onHttpSuccess(@NotNull String s) {

    }

    @Override
    public void onHttpError(@NotNull Throwable e) {

    }
}