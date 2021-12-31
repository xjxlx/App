package com.android.app.ui.activity

import android.Manifest
import android.os.Bundle
import android.view.View
import com.android.app.R
import com.android.helper.base.AppBaseActivity
import com.android.helper.utils.FileUtil
import com.android.helper.utils.LogUtil
import com.android.helper.utils.permission.RxPermissionsUtil
import com.android.helper.utils.photo.GlideUtil
import com.android.helper.utils.photo.PhotoUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_selector_image.*

class SelectorImageActivity : AppBaseActivity() {

    private val glideUtil: GlideUtil by lazy {
        return@lazy GlideUtil.Builder(mActivity).build()
    }

    override fun getBaseLayout(): Int {
        return R.layout.activity_selector_image
    }

    override fun initView() {
        setonClickListener(R.id.btn_selector_image, R.id.btn_selector_video)

        RxPermissionsUtil
            .Builder(this, Manifest.permission.CAMERA)
            .setAllPerMissionListener {
                LogUtil.e("当前是否拥有权限：" + it)
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
        PhotoUtil.getInstance()
            .SelectorImage(mActivity, false, 3, object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    LogUtil.e("result:$result")
                    val media = result?.get(0)
                    var urls: String?
                    if (media != null) {
                        val compressed: Boolean = media.isCompressed()
                        if (compressed) {
                            urls = media.compressPath
                            LogUtil.e("压缩选择图片的路径为：$urls")
                        } else {
                            urls = media.path
                            LogUtil.e("没有压缩选的图片的路径为：$urls")
                        }
                        val uriToPath = FileUtil.getInstance().UriToPath(mActivity, urls)
                        LogUtil.e("转换后的图片路径为：：$uriToPath")

                        glideUtil.loadUrl(iv_image, uriToPath)
                    }
                }

                override fun onCancel() {
                }
            })
    }

    private fun selectorVideo() {
        PhotoUtil.getInstance().SelectorVideo(
            mActivity,
            false,
            20,
            object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    // 结果回调
                    LogUtil.e("result:$result")
                    val media = result?.get(0)
                    var urls: String? = ""
                    if (media != null) {
                        val compressed: Boolean = media.isCompressed()
                        if (compressed) {
                            urls = media.compressPath
                            LogUtil.e("压缩选择视频的路径为：$urls")
                        } else {
                            urls = media.path
                            LogUtil.e("没有压缩选择视频的路径为：$urls")
                        }
                        val uriToPath = FileUtil.getInstance().UriToPath(mActivity, urls)
                        LogUtil.e("转换后的视频路径为：：$uriToPath")
                        PictureSelector.create(mActivity).externalPictureVideo(uriToPath);
                    }
                }

                override fun onCancel() {
                }
            })
    }
}