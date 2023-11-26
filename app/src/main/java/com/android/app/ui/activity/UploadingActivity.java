package com.android.app.ui.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.app.R;
import com.android.app.adapters.UploadAdapter;
import com.android.common.base.BaseActivity;
import com.android.http.download.Download;

import java.util.ArrayList;

public class UploadingActivity extends BaseActivity {

    private RecyclerView rv_upload_list;

    @Override
    public int getLayout() {
        return R.layout.activity_uploading;
    }

    @Override
    public void initView() {
        rv_upload_list = findViewById(R.id.rv_upload_list);
        ArrayList<Download> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Download downLoadBean = new Download();
            arrayList.add(downLoadBean);
        }
        UploadAdapter adapter = new UploadAdapter(mActivity, arrayList);
        rv_upload_list.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rv_upload_list.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
    }
}
