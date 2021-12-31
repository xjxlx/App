package com.android.app.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.bean.GsonBean
import com.android.app.databinding.ActivityTestUtilBinding
import com.android.helper.base.title.BaseBindingTitleActivity
import com.android.helper.enums.DataEnum
import com.android.helper.httpclient.CountdownListener
import com.android.helper.httpclient.RxUtil
import com.android.helper.utils.DateUtil
import com.android.helper.utils.JsonUtil
import com.android.helper.utils.LogUtil
import io.reactivex.disposables.Disposable

class TestUtilActivity : BaseBindingTitleActivity<ActivityTestUtilBinding>() {
    
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityTestUtilBinding {
        return ActivityTestUtilBinding.inflate(inflater, container, true)
    }
    
    override fun setTitleContent(): String {
        return "工具的测试类"
    }
    
    /**
     * Activity初始化view
     */
    override fun initView() {
    
    }
    
    override fun initData(savedInstanceState: Bundle?) {
        mBinding.btnTest.setOnClickListener {
            // [{"pc": "http://cdn.smartservice.bjev.com.cn/2021/09/02/07bd10223f5c45a098b154e7a2d51d8e.gif", "isDefalut": 1}]
            val json = "[{\"pc\": \"http://cdn.smartservice.bjev.com.cn/2021/09/02/07bd10223f5c45a098b154e7a2d51d8e.gif\", \"isDefalut\": 1}]"
            val convertList = JsonUtil.convertList(json, GsonBean::class.java)
            for (i in 0 until convertList.size) {
                val gsonBean = convertList[i]
                LogUtil.e("gsonBean: $gsonBean")
            }
            LogUtil.e("convertList:$convertList")
            
            RxUtil
                .Builder(this)
                .build()
                .countdown(100 * 60 * 60 * 1000, 0, 1000, object : CountdownListener {
                    /**
                     * @param disposable 计时器的对象，可以用来中断计时器
                     * @param counter    当前的计数器
                     * @param countdown  当前的倒计时
                     */
                    override fun countdown(disposable: Disposable?, counter: Long, countdown: Long) {
                        
                        val timeToTimeMillis = DateUtil.getTimeToTimeMillis(countdown, DataEnum.AUTO_DIGITS)
                        LogUtil.e("timeToTimeMillis:    $timeToTimeMillis")
                    }
                })
        }
    }
}