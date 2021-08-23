package com.android.app.test.app;

import static com.android.app.test.app.AppLifecycleService.FILE_NAME;
import static com.android.app.test.app.AppLifecycleService.KEY_LIFECYCLE_JOB;
import static com.android.app.test.app.AppLifecycleService.KEY_LIFECYCLE_TYPE;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.android.helper.utils.LogUtil;
import com.android.helper.utils.ServiceUtil;

/**
 * 轮询的后台服务进程
 */
public class AppJobService extends JobService {

    /**
     * job的id
     */
    private static final int AppJobId = 100;

    /**
     * 间隔的时间
     */
    private static final int CODE_INTERVAL = 15 * 1000;
    private static JobScheduler mJobScheduler;
    private static boolean mAutoSync;

    public AppJobService() {
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        LogUtil.e("------>:onStartJob");

        LogUtil.writeDe(FILE_NAME, "onStartJob ---> 我是JobService的服务，我在正常的运行着！");

        /*启动应用*/
        boolean serviceRunning = ServiceUtil.isServiceRunning(getBaseContext(), AppLifecycleService.class);
        if (!serviceRunning) {
            Intent intent = new Intent(getBaseContext(), AppLifecycleService.class);
            if (mAutoSync) {
                intent.putExtra(KEY_LIFECYCLE_TYPE, KEY_LIFECYCLE_JOB);
            }
            ServiceUtil.startService(getBaseContext(), intent);
            LogUtil.writeDe(FILE_NAME, "检测到后台服务被杀死了，JobService主动去拉起后台服务！");
        }
        return false;
    }

    /**
     * @param context 启动JobService
     */
    public static void startJob(Context context, boolean autoSync) {
        mAutoSync = autoSync;
        LogUtil.writeDe(FILE_NAME, "启动了startJob的后台服务！");
        LogUtil.e("启动了startJob的后台服务！");

        if (autoSync) {
            LogUtil.writeDe(FILE_NAME, "我是通过账号拉活的JobService!");
            LogUtil.e("我是通过账号拉活的JobService！");
        }

        if (mJobScheduler == null) {
            mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        }

        // 创建JobService的类对象
        ComponentName appJobComponentName = new ComponentName(context, AppJobService.class);
        // 2：设置JobInfo 的参数信息
        JobInfo.Builder builder = new JobInfo.Builder(AppJobService.AppJobId, appJobComponentName);

        builder.setPersisted(true);  // 设置设备重启时，执行该任务

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
            mJobScheduler.cancel(AppJobId);
        }
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        LogUtil.e("------>:onStopJob");
        return false;
    }

}