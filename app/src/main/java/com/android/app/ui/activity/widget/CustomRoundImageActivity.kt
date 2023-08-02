package com.android.app.ui.activity.widget

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.android.app.R
import com.android.app.app.App
import com.android.app.databinding.ActivityCustomRoundImageBinding
import com.android.common.utils.LogUtil
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.SystemUtil
import com.android.helper.utils.photo.GlideUtil

class CustomRoundImageActivity : AppBaseBindingTitleActivity<ActivityCustomRoundImageBinding>() {

    private val glideUtil: GlideUtil by lazy {
        return@lazy GlideUtil.Builder(mActivity)
            .setPlaceholderResource(R.mipmap.icon_head)
            .build()
    }

    val system: SystemUtil by lazy {
        return@lazy if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SystemUtil.getInstance(App.getInstance())
        } else {
            TODO("VERSION.SDK_INT < M")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData(savedInstanceState: Bundle?) {
        val url1 = "http://file.jollyeng.com/picture_book/201805/When I grow up.png"
        val url = "http://file.jollyeng.com/picture_book/201809/1537253778.png"
        val url3 = "http://file.jollyeng.com/picture_book/201809/1537253778.png"
        val url4 = "http://file.jollyeng.com/wan/baby_pic/20200903/1599115743677-2020-09-03ios_file"

        mBinding.btnStart.setOnClickListener { view ->
            glideUtil.loadUrl(mBinding.rvImage, url4)
            val ignoringBatteryOptimizations = system.isIgnoringBatteryOptimizations
            if (!ignoringBatteryOptimizations) {
                system.requestIgnoreBatteryOptimizations()
            }
            val xiaomi = system.isXiaomi
            val toLowerCase = Build.BRAND.toLowerCase()
            LogUtil.e("toLowerCase:" + toLowerCase)
            if (xiaomi) {
                system.goXiaomiSetting()
            }
        }
    }

    override fun setTitleContent(): String {
        return "测试事件分发"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCustomRoundImageBinding {
        return ActivityCustomRoundImageBinding.inflate(layoutInflater, container, true)
    }
}
