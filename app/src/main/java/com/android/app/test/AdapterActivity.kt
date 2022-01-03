package com.android.app.test

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.android.app.R
import com.android.app.adapters.TestSingleAdapter
import com.android.app.databinding.ActivityAdapterBinding
import com.android.helper.base.BaseBindingActivity
import com.android.helper.base.recycleview.PlaceholderResource
import com.android.helper.utils.LogUtil
import com.android.helper.utils.RecycleUtil

class AdapterActivity : BaseBindingActivity<ActivityAdapterBinding>() {
    
    private val list = arrayListOf<String>()
    
    /**
     * 初始化数据
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun initData(savedInstanceState: Bundle?) {
        LogUtil.e("⭐️", "initData")
        
        
        
        for (it in 0..10) {
            list.add("我是item $it")
        }
        val adapter = TestSingleAdapter(mActivity, list)
        RecycleUtil
            .getInstance(mActivity, mBinding.rvList)
            .setVertical()
            .setAdapter(adapter)
        val placeholder = PlaceholderResource
            .Builder()
            .setListEmptyContent("测试的数据")
            .setListEmptyResource(R.drawable.icon_default)
            .Build()
        
        adapter.setPlaceholderData(placeholder)
        
        adapter.setItemClickListener { view, binding, position, t ->
            list[position] = "" + System.currentTimeMillis()
            adapter.updateItem(position)
            startInstallPermissionSettingActivity()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e("⭐️", "onDestroy")
    }
    
    /**
     * Activity初始化view
     */
    override fun initView() {
    
    }
    
    /**
     * 打开未知应用界面
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startInstallPermissionSettingActivity() {
        val packageURI = Uri.parse("package:$packageName")
        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivityForResult(intent, 2001)
    }
    
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityAdapterBinding {
        return ActivityAdapterBinding.inflate(inflater, container, false)
    }
}