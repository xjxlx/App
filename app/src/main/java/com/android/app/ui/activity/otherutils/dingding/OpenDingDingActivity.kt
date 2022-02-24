package com.android.app.ui.activity.otherutils.dingding

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.app.Keepalive.LifecycleManager
import com.android.app.databinding.ActivityOpenDingDingBinding
import com.android.app.services.QywxService
import com.android.helper.app.BaseApplication
import com.android.helper.base.title.AppBaseBindingTitleActivity
import com.android.helper.utils.LogUtil
import com.android.helper.utils.ServiceUtil

/**
 * 打开钉钉
 */
class OpenDingDingActivity : AppBaseBindingTitleActivity<ActivityOpenDingDingBinding>() {

    private val ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners"
    private lateinit var intentService: Intent

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityOpenDingDingBinding {
        return ActivityOpenDingDingBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "打开钉钉"
    }

    override fun initData(savedInstanceState: Bundle?) {
        toggleNotificationListenerService(baseContext)


        intentService = Intent(mActivity, DingDingNotificationService::class.java)

        mBinding.btnOpenDingidng.setOnClickListener {
            val packageName = "com.alibaba.android.rimet"
            openApplication(packageName)
        }

        mBinding.btnOpenControl.setOnClickListener {
            LogUtil.e("打开开关！ ")
            val enabled = isEnabled()
            if (!enabled) {
                val intents = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                startActivity(intents)
            }
        }

        mBinding.btnOpenNotification.setOnClickListener {
            LogUtil.e("打开服务！ ")
            ServiceUtil.startService(mActivity, intentService)
        }

        mBinding.btnCloseNotification.setOnClickListener {
            LogUtil.e("关闭服务！ ")
            stopService(intentService)
        }

        mBinding.btnOpenBh.setOnClickListener {
            val serviceName = "com.android.app.ui.activity.otherutils.dingding.DingDingNotificationService"
            val jobServiceName = "com.android.app.app.Keepalive.AppJobService"
            // 进行账号的保活
            LifecycleManager
                .getInstance()
                .startLifecycle(BaseApplication.getInstance().application, serviceName, jobServiceName)
        }

        mBinding.btnCloseBh.setOnClickListener {
            LifecycleManager
                .getInstance().stopLifecycle(BaseApplication.getInstance().application)
        }

        // 打开后台服务
        ServiceUtil.startService(mActivity, intentService)
    }

    /**
     * 打开钉钉
     */
    private fun openApplication(packageName: String) {
        val packageManager = packageManager
        var pi: PackageInfo? = null
        try {
            pi = packageManager.getPackageInfo(packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
        }
        val resolveIntent = Intent(Intent.ACTION_MAIN, null)
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        resolveIntent.setPackage(pi!!.packageName)
        val apps = packageManager.queryIntentActivities(resolveIntent, 0)
        val resolveInfo = apps.iterator().next()
        if (resolveInfo != null) {
            val className = resolveInfo.activityInfo.name
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val cn = ComponentName(packageName, className)
            intent.component = cn
            startActivity(intent)
        }
    }

    /**
     * 判断 Notification access 是否开启
     *
     * @return
     */
    private fun isEnabled(): Boolean {
        val pkgName = packageName
        val flat = Settings.Secure.getString(contentResolver, ENABLED_NOTIFICATION_LISTENERS)
        if (!TextUtils.isEmpty(flat)) {
            val names = flat.split(":").toTypedArray()
            for (i in names.indices) {
                val cn = ComponentName.unflattenFromString(names[i])
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.packageName)) {
                        return true
                    }
                }
            }
        }
        return false
    }

    /**
     * 强制打开和关闭
     */
    private fun toggleNotificationListenerService(context: Context) {
        val pm = context.packageManager
        // 先关闭，在打开
        pm.setComponentEnabledSetting(
            ComponentName(context, QywxService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        LogUtil.e("强制关闭！")

        pm.setComponentEnabledSetting(
            ComponentName(context, QywxService::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
        LogUtil.e("强制打开！")
    }

}