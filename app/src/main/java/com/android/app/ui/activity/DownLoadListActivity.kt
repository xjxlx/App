package com.android.app.ui.activity

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.app.R
import com.android.app.adapters.DownloadAdapter
import com.android.app.databinding.ActivityDownLoadListBinding
import com.android.helper.base.recycleview.PlaceholderResource
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.EncryptionUtil
import com.android.helper.utils.FileUtil
import com.android.helper.utils.LogUtil
import com.android.helper.utils.download.Download
import com.android.helper.utils.permission.RxPermissionsUtil
import java.io.File
import java.util.Arrays

class DownLoadListActivity : AppBaseBindingTitleActivity<ActivityDownLoadListBinding>() {

    private val mList = arrayListOf<Download>()
    private lateinit var file: File
    private val fileUtil: FileUtil by lazy {
        return@lazy FileUtil.getInstance()
    }

    override fun setTitleContent(): String {
        return "带进度条的下载文件"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityDownLoadListBinding {
        return ActivityDownLoadListBinding.inflate(layoutInflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        RxPermissionsUtil.Builder(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .setAllPerMissionListener {
                if (it) {
                    val rootFileForSd = fileUtil.commonTagPath + "/apk"
                    fileUtil.createFolder(rootFileForSd)

                    for (index in 0..2) {
                        val downLoadBean = Download()
                        downLoadBean.url = "http://cdn.smartservice.bjev.com.cn/apk/ZHGJ-dev-v2.0.1.apk"
                        // 下载路径
                        file = File(rootFileForSd, ("test_$index.apk"))
                        downLoadBean.outputPath = file.absolutePath
                        // 生成唯一id
                        downLoadBean.id = EncryptionUtil.enCodeBase64(downLoadBean.url + downLoadBean.outputPath + index)
                        mList.add(downLoadBean)
                    }
                    val adapter = DownloadAdapter(mActivity, mList)
                    mBinding.rvDownloadList.adapter = adapter
                    mBinding.rvDownloadList.layoutManager = LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false)
                    LogUtil.e(Arrays.toString(mList.toArray()))
                    val placeholder = PlaceholderResource.Builder()
                        .setListEmptyContent("我是测试哈哈哈")
                        .setListEmptyResource(R.mipmap.icon_face_authentication_bg)
                        .Build()

                    adapter.setPlaceholder(placeholder)
                }
            }
            .build()
            .startRequestPermission()
    }
}