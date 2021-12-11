package com.android.app.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.bean.GsonBean
import com.android.app.databinding.ActivityTestUtilBinding
import com.android.helper.base.title.BaseBindingTitleActivity
import com.android.helper.utils.JsonUtil
import com.android.helper.utils.LogUtil

class TestUtilActivity : BaseBindingTitleActivity<ActivityTestUtilBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityTestUtilBinding {
        return ActivityTestUtilBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "工具的测试类"
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
        }
    }
}