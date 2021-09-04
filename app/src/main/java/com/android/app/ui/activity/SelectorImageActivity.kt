package com.android.app.ui.activity

import android.Manifest
import android.view.View
import com.android.app.R
import com.android.helper.base.BaseTitleActivity
import com.android.helper.utils.FileUtil
import com.android.helper.utils.LogUtil
import com.android.helper.utils.RxPermissionsUtil
import com.android.helper.utils.photo.GlideUtil
import com.android.helper.utils.photo.PhotoUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_selector_image.*

class SelectorImageActivity : BaseTitleActivity() {

    override fun getTitleLayout(): Int {
        return R.layout.activity_selector_image
    }

    override fun initView() {
        super.initView()
        setTitleContent("图片选择器")

        setonClickListener(R.id.btn_selector_image, R.id.btn_selector_video)

        RxPermissionsUtil(mContext, Manifest.permission.CAMERA)
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
        PhotoUtil.SelectorImage(mContext, false, 3, object : OnResultCallbackListener<LocalMedia> {
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

                    val uriToPath = FileUtil.UriToPath(mContext, urls)
                    LogUtil.e("转换后的图片路径为：：$uriToPath")

                    GlideUtil.loadView(mContext, uriToPath, iv_image)
                }
            }

            override fun onCancel() {
            }
        })
    }

    private fun selectorVideo() {
        PhotoUtil.SelectorVideo(
            mContext,
            false,
            3,
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

                        val uriToPath = FileUtil.UriToPath(mContext, urls)
                        LogUtil.e("转换后的视频路径为：：$uriToPath")
                        PictureSelector.create(mContext).externalPictureVideo(uriToPath);
                    }
                }

                override fun onCancel() {

                }
            })
    }

}