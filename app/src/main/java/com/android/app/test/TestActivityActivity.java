package com.android.app.test;

import android.os.Bundle;

import com.android.app.R;
import com.android.helper.base.title.AppBaseTitleActivity;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;

public class TestActivityActivity extends AppBaseTitleActivity<String> {

    @Override
    protected int getTitleLayout() {
        return R.layout.activity_test_activity;
    }

    @Override
    protected String setTitleContent() {
        return "测试Activity";
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

    @Override
    public void initData(Bundle savedInstanceState) {

    }
}