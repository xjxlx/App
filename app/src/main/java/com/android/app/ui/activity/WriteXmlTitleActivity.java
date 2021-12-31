package com.android.app.ui.activity;

import android.Manifest;

import com.android.app.R;
import com.android.app.databinding.ActivityWriteXmlBinding;

import android.os.Bundle;
import android.view.View;

import com.android.helper.base.AppBaseActivity;
import com.android.helper.utils.FileUtil;
import com.android.helper.utils.permission.RxPermissionsUtil;
import com.android.helper.utils.XmlUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WriteXmlTitleActivity extends AppBaseActivity {

    private ActivityWriteXmlBinding binding;
    private File file1;

    @Override
    public void initView() {
        binding = ActivityWriteXmlBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initData(Bundle savedInstanceState) {

        XmlUtil xmlUtil = new XmlUtil();
        List<Float> integers = new ArrayList<>();
        for (float i = 0; i < 720; ) {
            i += 0.5;
            integers.add(i);
        }
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil fileUtil = new FileUtil();
                File rootFileForApp = fileUtil.getRootFileForSd();
                File file = new File(rootFileForApp, "write");
                if (!file.exists()) {
                    file.mkdirs();
                }

                file1 = new File(file, "dimens.txt");

                new RxPermissionsUtil.Builder(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setAllPerMissionListener(haveAllPermission -> {
                            if (haveAllPermission) {
                                xmlUtil.writeDat(file1.getAbsolutePath(), "<dimen name=\"dp_", "\">", "dp</dimen>\n", integers);
                            }
                        })
                        .build()
                        .startRequestPermission();
            }
        });
    }

    @Override
    protected int getBaseLayout() {
        return R.layout.activity_write_xml;
    }
}