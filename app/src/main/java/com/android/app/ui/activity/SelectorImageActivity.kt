package com.android.app.ui.activity

import android.Manifest
import android.os.Bundle
import android.view.View
import com.android.app.R
import com.android.common.utils.LogUtil
import com.android.helper.base.AppBaseActivity
import com.android.helper.utils.permission.RxPermissionsUtil
import com.android.helper.utils.photo.GlideUtil
import com.android.selector.interfaces.PhotoCallBackListener
import com.android.selector.photo.PhotoUtil

class SelectorImageActivity : AppBaseActivity() {
    val photoUtil = PhotoUtil()

    private val glideUtil: GlideUtil by lazy {
        return@lazy GlideUtil.Builder(mActivity)
            .build()
    }

    override fun getBaseLayout(): Int {
        return R.layout.activity_selector_image
    }

    override fun initView() {
        setonClickListener(R.id.btn_selector_image, R.id.btn_selector_video)

        RxPermissionsUtil.Builder(this, Manifest.permission.CAMERA)
            .setAllPerMissionListener {
                LogUtil.e("当前是否拥有权限：$it")
            }
            .build()
            .startRequestPermission()
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when (v?.id) {
            R.id.btn_selector_image -> {
                selectorImage()
            }
            R.id.btn_selector_video -> {
                selectorVideo()
            }
        }
    }

    private fun selectorImage() {
        photoUtil.openPhotoGallery(this, object : PhotoCallBackListener {
            override fun onCallBack(result: List<String>) {
                result.map {
                    LogUtil.e("压缩选择图片的路径为：$it")
                }
            }
        }, maxSize = 4)
    }

    private fun selectorVideo() {
        photoUtil.openVideoGallery(this, object : PhotoCallBackListener {
            override fun onCallBack(result: List<String>) {
                result.map {
                    LogUtil.e("选择视频的路径为：$it")
                }
            }
        }, maxSize = 4)
    }
}