package com.android.app.app.Keepalive;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.android.app.R;
import com.android.helper.common.CommonConstants;
import com.android.helper.utils.LogUtil;
import com.android.helper.utils.NotificationUtil;
import com.android.helper.utils.ServiceUtil;
import com.android.helper.utils.account.LifecycleAppEnum;

/**
 * 轮询的后台服务进程
 */
public class AppJobService extends JobService {

    /**
     * job的id
     */
    private static final int APP_JOB_ID = 100;

    /**
     * 间隔的时间
     */
    private static final int CODE_INTERVAL = 10 * 1000;
    private static JobScheduler mJobScheduler;
    private static String mServiceName;

    public AppJobService() {}

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        LogUtil.e("onStartJob");

        if (TextUtils.isEmpty(mServiceName)) {
            mServiceName = LifecycleManager.getInstance().getServiceName();
        }

        // 启动后台服务
        if (!TextUtils.isEmpty(mServiceName)) {
            boolean serviceRunning = ServiceUtil.isServiceRunning(getApplicationContext(), mServiceName);
            LogUtil.e("☆☆☆☆☆---我是JobService服务，当前后台服务的状态为:" + serviceRunning);
            LogUtil.writeAll(CommonConstants.FILE_LIFECYCLE_NAME,
                "☆☆☆☆☆---我是JobService服务，当前后台服务的状态为:" + serviceRunning);

            if (!serviceRunning) {
                /*启动应用*/
                Intent intent = new Intent();
                intent.setClassName(getPackageName(), mServiceName);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(CommonConstants.KEY_LIFECYCLE_FROM, LifecycleAppEnum.FROM_JOB.getFrom());
                ServiceUtil.startService(getApplicationContext(), intent);
            }

            // 重新开始执行
            startJob(getApplicationContext(), mServiceName, LifecycleAppEnum.FROM_JOB);

            // 任务已经结束了
            jobFinished(jobParameters, true);
        }
        return true;
    }

    /**
     * @param context
     *            上下文
     * @param serviceName
     *            希望拉起的后台服务
     * @param appEnum
     *            启动的数据类型
     */
    public static void startJob(Context context, String serviceName, LifecycleAppEnum appEnum) {
        mServiceName = serviceName;

        sendNotification(context, appEnum);

        if (mJobScheduler == null) {
            mJobScheduler = (JobScheduler)context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }

        // 创建JobService的类对象
        ComponentName appJobComponentName = new ComponentName(context, AppJobService.class);
        // 2：设置JobInfo 的参数信息
        JobInfo.Builder builder = new JobInfo.Builder(AppJobService.APP_JOB_ID, appJobComponentName);

        builder.setPersisted(true); // 设置设备重启时，执行该任务

        // 7.0 之前没有任何的限制
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            builder.setPeriodic(CODE_INTERVAL); // 轮询的间隔
        } else {
            builder.setMinimumLatency(CODE_INTERVAL); // 延时的时间
        }

        // 调用
        mJobScheduler.schedule(builder.build());
    }

    /**
     * 取消JobService
     */
    public static void cancel() {
        if (mJobScheduler != null) {
            mJobScheduler.cancel(APP_JOB_ID);
        }
    }

    private static void sendNotification(Context context, LifecycleAppEnum appEnum) {

        NotificationUtil.Builder builder = new NotificationUtil.Builder(context)
            .setChannelName(CommonConstants.KEY_LIFECYCLE_NOTIFICATION_CHANNEL_NAME).setSmallIcon(R.mipmap.ic_launcher);

        if (appEnum == LifecycleAppEnum.From_Intent) {
            builder.setContentText("我是JobService，我是被直接启动的");
            LogUtil.writeAll(CommonConstants.FILE_LIFECYCLE_NAME, "我是JobService，我是被直接启动的");

        } else if (appEnum == LifecycleAppEnum.FROM_ACCOUNT) {
            builder.setContentText("我是JobService，我是被账号拉活的");
            LogUtil.writeAll(CommonConstants.FILE_LIFECYCLE_NAME, "我是JobService，我是被账号拉活的");

        } else if (appEnum == LifecycleAppEnum.FROM_SERVICE) {
            builder.setContentText("我是JobService，我是后台服务拉活的");
            LogUtil.writeAll(CommonConstants.FILE_LIFECYCLE_NAME, "我是JobService，我是后台服务拉活的");
        }

        if (appEnum == LifecycleAppEnum.FROM_JOB) {
            return;
        }
        builder.setWhen(System.currentTimeMillis());
        NotificationUtil notificationUtil = builder.build();
        notificationUtil.sendNotification(222);

        // 启动主应用
        String serviceName = LifecycleManager.getInstance().getServiceName();
        if (!TextUtils.isEmpty(serviceName)) {
            // 后台服务
            boolean serviceRunning = ServiceUtil.isServiceRunning(context, serviceName);
            LogUtil.e("☆☆☆☆☆---我是广播通知，当前后台服务的状态为：" + serviceRunning);
            LogUtil.writeAll(CommonConstants.FILE_LIFECYCLE_NAME, "☆☆☆☆☆---我是广播通知，当前后台服务的状态为：" + serviceRunning);

            if (!serviceRunning) {
                Intent intentService = new Intent();
                intentService.setClassName(context, serviceName);
                intentService.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intentService.putExtra(CommonConstants.KEY_LIFECYCLE_FROM, LifecycleAppEnum.FROM_ACCOUNT.getFrom());
                ServiceUtil.startService(context, intentService);
            }
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        LogUtil.e("onStopJob");
        return false;
    }

}