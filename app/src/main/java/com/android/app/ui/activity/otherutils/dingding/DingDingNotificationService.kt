package com.android.app.ui.activity.otherutils.dingding

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Notification
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.android.app.R
import com.android.app.app.Keepalive.AppJobService
import com.android.app.app.Keepalive.LifecycleManager
import com.android.helper.utils.LogUtil
import com.android.helper.utils.LogWriteUtil
import com.android.helper.utils.NotificationUtil
import com.android.helper.utils.ServiceUtil
import com.android.helper.utils.account.LifecycleAppEnum

class DingDingNotificationService : NotificationListenerService() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate() {
        super.onCreate()

        NotificationUtil.Builder(baseContext)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText("钉钉的前台服务")
            .setNotificationLevel(NotificationManager.IMPORTANCE_HIGH)
            .setActivity(OpenDingDingActivity::class.java)
            .setVibrate(true)
            .setWhen(System.currentTimeMillis())
            .setChannelImportance(NotificationManager.IMPORTANCE_LOW)
            .build()
            .startLoopForeground(1, 3000, this)
    }

    @SuppressLint("WrongConstant")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LogUtil.e("----------------->onStartCommand")

        // 如果JobService没有运行着的时候，顺便把它也唤醒
        val jobServiceName = LifecycleManager.getInstance().jobServiceName
        val jobServiceRunning = ServiceUtil.isJobServiceRunning(baseContext, jobServiceName)
        LogUtil.e("☆☆☆☆☆---我是广播通知，当前JobService的状态为：$jobServiceRunning")
        LogUtil.writeLifeCycle("☆☆☆☆☆---我是广播通知，当前JobService的状态为：$jobServiceRunning")

        // 启动主应用
        val serviceName = LifecycleManager.getInstance().serviceName
        if (!jobServiceRunning) {
            AppJobService.startJob(baseContext, serviceName, LifecycleAppEnum.FROM_ACCOUNT)
        }

        return super.onStartCommand(intent, START_REDELIVER_INTENT, startId)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        LogUtil.e("onListenerConnected --- 建立监听成功！")
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        LogUtil.e("onListenerDisconnected --- 监听断开！")
    }

    // 通知被移除时回调
    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
        LogUtil.e("onNotificationRemoved")
    }

    // 增加一条通知时回调
    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        // 获取包名
        val packageName = sbn.packageName
        LogUtil.e("获取到消息的包名为：$packageName")
        // 获取对象
        val notification = sbn.notification
        if (notification != null) {
            // 当 API > 18 时，使用 extras 获取通知的详细信息
            val extras = notification.extras
            if (extras != null) {
                // 获取通知标题
                val title = extras.getString(Notification.EXTRA_TITLE, "")
                // 获取通知内容
                val content = extras.getCharSequence(Notification.EXTRA_TEXT, "")
                LogUtil.e("获取到消息的title为：$title   获取的内容为：$content")

                // 接收到微信发送过来的指令
                if (TextUtils.equals(packageName, "com.tencent.mm")) {
                }



                if (content.contains("指令：打开钉钉")) {
                    // 移动应用到前台
                    setTopApp(baseContext)

                    // 打开钉钉
                    LogUtil.e("执行命令：打开钉钉！")
                    openApplication("com.alibaba.android.rimet")

                } else if (content.contains("指令：切换应用")) {
                    // 移动应用到前台
                    setTopApp(baseContext)

                    // 切换本身应用
                    val runningForeground = isRunningForeground(baseContext)
                    LogUtil.e("本应用已经位于最前端：$runningForeground")
                    if (!runningForeground) {
                        val topApp = setTopApp(baseContext)
                        LogUtil.e("将本应用置顶到最前端：$topApp")
                    }
                }

                // 存入接收到的标记
                if (TextUtils.equals(packageName, "com.xiaomi.bsp.gps.nps")) {
                }
                val logWriteUtil = LogWriteUtil()
                logWriteUtil.write("钉钉日志", content.toString())

            }
        }
    }

    /**
     * 判断本地是否已经安装好了指定的应用程序包
     *
     * @param packageNameTarget ：待判断的 App 包名，如 微博 com.sina.weibo
     * @return 已安装时返回 true,不存在时返回 false
     */
    fun appIsExist(context: Context, packageNameTarget: String): Boolean {
        if ("" != packageNameTarget.trim { it <= ' ' }) {
            val packageManager = context.packageManager
            val packageInfoList = packageManager.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES)
            for (packageInfo in packageInfoList) {
                val packageNameSource = packageInfo.packageName
                if (packageNameSource == packageNameTarget) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * 将本应用置顶到最前端
     * 当本应用位于后台时，则将它切换到最前端
     *
     * @param context
     */
    fun setTopApp(context: Context): Boolean {
        if (!isRunningForeground(context)) {
            /**获取ActivityManager */
            val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager

            /**获得当前运行的task(任务) */
            val taskInfoList = activityManager.getRunningTasks(100)
            for (taskInfo in taskInfoList) {
                /**找到本应用的 task，并将它切换到前台 */
                if (taskInfo.topActivity!!.packageName == context.packageName) {
                    activityManager.moveTaskToFront(taskInfo.id, ActivityManager.MOVE_TASK_WITH_HOME)
                    return true
                }
            }
        }
        return false
    }

    /**
     * 判断本应用是否已经位于最前端
     *
     * @param context
     * @return 本应用已经位于最前端时，返回 true；否则返回 false
     */
    fun isRunningForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val appProcessInfoList = activityManager.runningAppProcesses
        /**枚举进程 */
        for (appProcessInfo in appProcessInfoList) {
            if (appProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName == context.applicationInfo.processName) {
                    return true
                }
            }
        }
        return false
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
}