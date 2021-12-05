package com.android.app.ui.activity.personal;

import android.annotation.SuppressLint;

import com.android.helper.utils.ConvertUtil;
import com.android.helper.utils.FileUtil;
import com.android.helper.utils.LogUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : 流星
 * @CreateDate: 2021/12/5-18:34
 * @Description: 多个文件合并到一起
 */
public class MoreFileMerge {

    private final File rootFileForSd = FileUtil.getInstance().getRootFileForSd();
    private final List<String> mListPath = new ArrayList<>();

    @SuppressLint("CheckResult")
    public void main() {
        try {
            // 输入文件
            File inputFilePath = new File(rootFileForSd, "aaa");
            boolean exists = inputFilePath.exists();
            LogUtil.e("输入流文件是否存在：" + exists);

            // 输出文件
            File outputFile = new File(rootFileForSd, "aaaa");
            if (!outputFile.exists()) {
                outputFile.mkdirs();
            }
            // 输出文件的地址
            File outFile = new File(outputFile, "rz.txt");

            // 先栓除，后创建
            if (outFile.exists()) {
                boolean delete = outFile.delete();
            }

            boolean newFile = outFile.createNewFile();

            boolean exists1 = outFile.exists();
            LogUtil.e("输出流文件是否存在：" + exists1);

            mOut = new FileWriter(outFile, true);

            // 输入文件
            traverseFolder2(inputFilePath);

            LogUtil.e("数据采集完毕：" + Arrays.toString(mListPath.toArray()));

            // 转换为数组
            String[] strings = ConvertUtil.ListToStringArray(mListPath);

            Observable
                    .fromArray(strings)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe(s -> write(new File(s)));

        } catch (Exception exception) {
            LogUtil.e("exception:" + exception.getMessage());
        }
    }

    public void traverseFolder2(File file) {
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file2 : files) {
                    // 存入真实的路径
                    if (!file2.isDirectory()) {
                        mListPath.add(file2.getAbsolutePath());
                    }

                    if (file2.isDirectory()) {
                        traverseFolder2(file2);
                    }
                }
            }
        }
    }

    private FileWriter mOut;

    private void write(File file) {
        if ((file != null)) {
            FileReader mFileReader = null;
            try {

                mFileReader = new FileReader(file);
                char[] chars = new char[1];
                while (mFileReader.read(chars) != -1) {
                    mOut.write(chars);
                }

                mOut.write("\r\n");
                mOut.write("\r\n");

                //刷新IO内存流
                mOut.flush();

                LogUtil.e("当前写入的文件是：" + file.getName());

            } catch (IOException e) {
                e.printStackTrace();
                LogUtil.e("数据写入异常：" + e.getMessage());
            } finally {
                try {
                    //关闭
                    if (mFileReader != null) {
                        mFileReader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
