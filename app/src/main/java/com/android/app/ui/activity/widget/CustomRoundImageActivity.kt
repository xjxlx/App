package com.android.app.ui.activity.widget

import android.os.Build
import androidx.annotation.RequiresApi
import com.android.app.R
import com.android.app.app.App
import com.android.helper.base.BaseActivity
import com.android.helper.utils.LogUtil
import com.android.helper.utils.SystemUtil
import com.android.helper.utils.photo.GlideUtil
import kotlinx.android.synthetic.main.activity_custom_round_image.*
import kotlinx.android.synthetic.main.activity_selector_image.*

class CustomRoundImageActivity : BaseActivity() {
    
    private val glideUtil: GlideUtil by lazy {
        return@lazy GlideUtil
            .Builder(mContext)
            .setPlaceholderResource(R.mipmap.icon_head)
            .build()
    }
    
    override fun getBaseLayout(): Int {
        return R.layout.activity_custom_round_image
    }
    
    val system: SystemUtil by lazy {
        return@lazy if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SystemUtil.getInstance(App.getInstance())
        } else {
            TODO("VERSION.SDK_INT < M")
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initData() {
        
        setTitleContent("自定义任意圆角任意图形")
        
        val url1 = "http://file.jollyeng.com/picture_book/201805/When I grow up.png";
        val url = "http://file.jollyeng.com/picture_book/201809/1537253778.png";
        val url3 = "http://file.jollyeng.com/picture_book/201809/1537253778.png";
        val url4 = "http://file.jollyeng.com/wan/baby_pic/20200903/1599115743677-2020-09-03ios_file";
        
        
        btn_start.setOnClickListener { view ->
            glideUtil.loadUrl(rv_image, url4)
            
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
    
}