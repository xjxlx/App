package com.android.app.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityOrFragmentLifecycleBinding
import com.android.app.test.fragment.ActivityOrFragmentFragment
import com.android.common.utils.LogUtil
import com.android.helper.base.BaseBindingActivity
import com.android.helper.utils.FragmentUtil

/**
 * activity 和 fragment 生命周期的监听
 */
class ActivityOrFragmentLifecycleActivity : BaseBindingActivity<ActivityOrFragmentLifecycleBinding>() {
    
    private val classTAG = "------> Activity "
    
    /**
     * 初始化数据
     */
    override fun initData(savedInstanceState: Bundle?) {
        //ActivityOrFragmentFragment
        val fragment = ActivityOrFragmentFragment();
        val util: FragmentUtil = FragmentUtil(this)
        util.replace(R.id.fl_content, fragment, "", null)
    }
    
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityOrFragmentLifecycleBinding {
        return ActivityOrFragmentLifecycleBinding.inflate(inflater, container, false)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e(classTAG + "onCreate")
    }
    
    override fun onStart() {
        super.onStart()
        LogUtil.e(classTAG + "onStart")
    }
    
    override fun onResume() {
        super.onResume()
        LogUtil.e(classTAG + "onResume")
    }
    
    override fun onPause() {
        super.onPause()
        LogUtil.e(classTAG + "onResume")
    }
    
    override fun onStop() {
        super.onStop()
        LogUtil.e(classTAG + "onStop")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        LogUtil.e(classTAG + "onDestroy")
        val fragments = supportFragmentManager.fragments
        if (fragments.size > 0) {
            for (f in fragments.indices) {
                fragments[f]?.onDestroy()
                LogUtil.e(classTAG + "销毁--->")
            }
        }
    }
    
    /**
     * Activity初始化view
     */
    override fun initView() {
    
    }
}