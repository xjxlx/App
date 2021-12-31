package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityKotlinMapBinding
import com.android.helper.base.AppBaseBindingActivity
import com.android.helper.utils.AppUtil
import com.android.helper.utils.LogUtil
import com.android.helper.utils.ToastUtil

/**
 * @author XJX
 * @CreateDate: 2021/9/19-16:52
 * @Description: Kotlin的项目
 */
class KotlinMap : AppBaseBindingActivity<ActivityKotlinMapBinding>() {

    var ssss: String = "sss"
    var pack: String = "com.autonavi.minimap"

     override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView() {
        val installedAppList = AppUtil.getInstalledAppList()
        if (installedAppList != null && installedAppList.size > 0) {
            for (a in installedAppList.indices) {
                val get = installedAppList[a]
                if (get != null) {
                    val packageName = get.packageName
                    LogUtil.e("packageName:" + packageName)
                    if (TextUtils.equals(packageName, pack)) {
                        LogUtil.e("检测到了高德地图")
                        ToastUtil.show("检测到了高德地图")
                        break
                    }
                }
            }
        }

//        val checkInstalledApp = AppUtil.checkInstalledApp(this, pack)
//        if (checkInstalledApp) {
//            LogUtil.e("检测到了高德地图222")
//            ToastUtil.show("检测到了高德地图222")
//        }

    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityKotlinMapBinding {
        return ActivityKotlinMapBinding.inflate(inflater, container, false)
    }
}