package com.android.app.ui.activity;

import android.os.Bundle;

import com.android.app.R;
import com.android.app.adapters.UploadAdapter;
import com.android.helper.utils.download.Download;
import com.android.helper.base.BaseActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UploadingActivity extends BaseActivity {

    private RecyclerView rv_upload_list;

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_uploading;
    }

    @Override
    public void initView() {
        super.initView();

        rv_upload_list = findViewById(R.id.rv_upload_list);

        ArrayList<Download> arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Download downLoadBean = new Download();
            arrayList.add(downLoadBean);
        }
        UploadAdapter adapter = new UploadAdapter(mContext, arrayList);

        rv_upload_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rv_upload_list.setAdapter(adapter);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

}
