package com.android.app.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import com.android.app.R;
import com.android.app.databinding.ActivityWriteXmlBinding;
import com.android.helper.base.AppBaseActivity;
import com.android.helper.utils.DateUtil;
import com.android.helper.utils.FileUtil;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.XmlUtil;
import com.android.helper.utils.permission.RxPermissionsUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
//        List<Float> integers = new ArrayList<>();
//        for (float i = 0; i < 720; ) {
//            i += 0.5;
//            integers.add(i);
//        }

        List<String> list = new ArrayList<>();
//        date -s 20260101.000000

        Calendar instance = Calendar.getInstance();
        instance.set(Calendar.YEAR, 2039);
        instance.set(Calendar.MONTH, 0); // 从0开始，11 是最大的值
        instance.set(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 1);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);

        for (int i = 0; i < 10000; i++) {
            instance.add(Calendar.DAY_OF_MONTH, 1);
            String dateForCalendar = DateUtil.getDateForCalendar(instance, "yyyy-MM-dd HH:mm:ss");

            int year = instance.get(Calendar.YEAR);
            int month = instance.get(Calendar.MONTH);
            int day = instance.get(Calendar.DAY_OF_MONTH);

            String value = year + "";
            if (month < 10) {
                value += "0" + (month + 1);
            } else {
                value += "" + (month + 1);
            }

            if (day < 10) {
                value += "0" + day;
            } else {
                value += "" + day;
            }
            value += ".000000";

            list.add(value);
            LogUtil.e("转换后的日期为：" + dateForCalendar);
        }


        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileUtil fileUtil = new FileUtil();
//                File rootFileForApp = fileUtil.getRootFileForSd();

                String appFilesPath = fileUtil.getAppFilesPath();
                File file = new File(appFilesPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

//                file1 = new File(file, "dimens.txt");
                file1 = new File(file, "date2.txt");

                LogUtil.e("write --- path:" + file1.getPath());

                new RxPermissionsUtil
                        .Builder(mActivity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        .setAllPerMissionListener(haveAllPermission -> {
                            if (haveAllPermission) {
//                                xmlUtil.writeDat(file1.getAbsolutePath(), "<dimen name=\"dp_", "\">", "dp</dimen>\n", integers);

//                                xmlUtil.writeDatForString(file1.getAbsolutePath(), " adb shell date ", "\n", list);

                                xmlUtil.writeDatForString(file1.getAbsolutePath(), "echo 正在执行，请稍等...... \r\n"+"adb shell date -s ", "\r\nsleep 3s \r\n\r\n", list);

//                                FileUtil.getInstance().writeContentToFile(file1,"hahh");
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