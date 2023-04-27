package com.android.app.ui.fragment

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.FragmentHomeBinding
import com.android.apphelper2.utils.permission.PermissionMultipleCallBackListener
import com.android.apphelper2.utils.permission.PermissionUtil
import com.android.helper.base.BaseBindingFragment
import com.android.helper.utils.FileUtil
import com.android.helper.utils.LogUtil
import com.android.helper.utils.dialog.DialogUtil
import java.io.File

/**
 * 首页
 */
class HomeFragment : BaseBindingFragment<FragmentHomeBinding>() {
    private val permissionUtil = PermissionUtil.PermissionFragment(this)

    val mBuilder_w = StringBuilder()
    val mBuilder_n = StringBuilder()
    val mFileUtil = FileUtil.getInstance()
    val tag_w = "外部空间"
    val tag_n = "内部空间"
    override fun initView(view: View?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFileUtil.checkAllFilesPermission(activity)
    }

    override fun initData(savedInstanceState: Bundle?) {
        // 外部空间 ，如果文件存储在外部存储空间中的目录内，则可以访问
        val appFilesPath = mFileUtil.getAppTypePath(Environment.DIRECTORY_DOWNLOADS)
        LogUtil.e("path ---外部空间：$appFilesPath")
        val fileW = File(appFilesPath, "abc.txt")
        // 内部空间 ，如果文件存储在内部存储空间中的目录内，则不能访问
        // 内部空间 ，如果文件存储在内部存储空间中的目录内，则不能访问
        val pathN = mFileUtil.appFilesPath
        LogUtil.e("path ---内部空间：$pathN")
        val fileN = File(pathN, "abc.txt")

        mBinding.button1.setOnClickListener {
            write(tag_w, "外部空间的地址为：$appFilesPath")
            mBinding.tvContentW.text = mBuilder_w.toString()
        }

        mBinding.button2.setOnClickListener {
            createFile(appFilesPath, tag_w)
            mBinding.tvContentW.text = mBuilder_w.toString()
        }

        mBinding.button22.setOnClickListener {
            writeContent(fileW, "123", tag_w)
            mBinding.tvContentW.text = mBuilder_w.toString()
        }

        mBinding.button3.setOnClickListener {
            readContent(fileW, tag_w)
            mBinding.tvContentW.text = mBuilder_w.toString()
        }

        mBinding.button4.setOnClickListener {
            deleteFile(fileW, tag_w)
            mBinding.tvContentW.text = mBuilder_w.toString()
        }

        mBinding.buttonClear1.setOnClickListener {
            mBuilder_w.clear()
            mBinding.tvContentW.text = mBuilder_w.toString()
        }



        mBinding.button5.setOnClickListener {
            write(tag_n, "内部空间的地址为：$pathN")
            mBinding.tvContentN.text = mBuilder_n.toString()
        }

        mBinding.button6.setOnClickListener {
            createFile(pathN, tag_n)
            mBinding.tvContentN.text = mBuilder_n.toString()
        }

        mBinding.button66.setOnClickListener {
            writeContent(fileN, "123", tag_n)
            mBinding.tvContentN.text = mBuilder_n.toString()
        }

        mBinding.button7.setOnClickListener {
            readContent(fileN, tag_n)
            mBinding.tvContentN.text = mBuilder_n.toString()
        }

        mBinding.button8.setOnClickListener {
            deleteFile(fileN, tag_n)
            mBinding.tvContentN.text = mBuilder_n.toString()
        }

        mBinding.buttonClear.setOnClickListener {
            mBuilder_n.clear()
            mBinding.tvContentN.text = mBuilder_n.toString()
        }

        mBinding.btnOpenAllFile.setOnClickListener {
            DialogUtil.Builder(this, R.layout.base_default_dialog)
                .setClose(R.id.tv_qx)
                .Build()
                .setOnClickListener(R.id.tv_qd) { _, _ -> mFileUtil.jumpAllFiles() }
                .setText(R.id.tv_msg, "是否进行文件权限授权？")
                .show();
        }
        val commonPath = mFileUtil.commonPath
        val commonTagPath = mFileUtil.commonTagPath
        LogUtil.e("commonPath:$commonPath")
        LogUtil.e("commonTagPath:$commonTagPath")

        mBinding.btnTestPermission.setOnClickListener {

//            permissionUtil.shouldShow(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionRationaleCallBackListener {
//                override fun onCallBack(permission: String, rationale: Boolean) {
//                    com.android.apphelper2.utils.LogUtil.e("------permission------>   rationale: $rationale")
//
//                    permissionUtil.setCallBackListener(object : PermissionCallBackListener {
//                        override fun onCallBack(permission: String, isGranted: Boolean) {
//                            com.android.apphelper2.utils.LogUtil.e("------permission------> call     isGranted: $isGranted")
//                        }
//                    })
//                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                }
//            })

            permissionUtil.requestArray(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                    object : PermissionMultipleCallBackListener {
                        override fun onCallBack(allGranted: Boolean, map: MutableMap<String, Boolean>) {
                            map.map {
                                LogUtil.e("permission --->: key: ${it.key}  value: ${it.value}  allGranted: $allGranted")
                            }
                        }
                    })
        }
    }

    private fun createFile(path: String, tag: String) {
        val file = File(path)
        val exists = file.exists()

        write(tag, "父级目录是否存在：$exists")

        if (!file.exists()) {
            val mkdirs = file.mkdirs()
            write(tag, "创建父级目录成功： $mkdirs")
        }

        if (file.exists()) {
            val childFile = File(file, "abc.txt")
            val childExists = childFile.exists()

            write(tag, "子文件是否存在： $childExists")

            if (!childExists) {
                val createNewFile = childFile.createNewFile()
                write(tag, "创建子文件是否成功： $createNewFile")
            }
        }
    }

    private fun writeContent(file: File, content: String, tag: String) {
        val exists = file.exists()
        write(tag, "写入的内容为：$content")
        write(tag, "指定文件是否存在：$exists")
        if (exists) {
            val writeContentToFile = mFileUtil.writeContentToFile(file, content)
            write(tag, "文件写入成功：$writeContentToFile")
        }
    }

    private fun readContent(file: File, tag: String) {
        write(tag, "读取的文件地址为：${file.path}")
        val contentForFile = mFileUtil.getContentForFile(file)
        write(tag, "读取到的内容为：$contentForFile")
    }

    private fun deleteFile(file: File, tag: String) {
        write(tag, "删除的文件地址为：${file.path}")
        val exists = file.exists()
        write(tag, "删除的文件是否存在：$exists")

        if (file.exists()) {
            val delete = file.delete()
            write(tag, "删除 文件是否成功：$delete")
        }
    }

    private fun write(tag: String, log: String) {
        LogUtil.e(tag, log)
        if (TextUtils.equals(tag, tag_w)) {
            mBuilder_w.append("$tag: $log \r\n ")
        }

        if (TextUtils.equals(tag, tag_n)) {
            mBuilder_n.append("$tag: $log \r\n ")
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }
}
