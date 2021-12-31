package com.android.app.ui.activity.jetpack.paging

import android.os.Bundle
import com.android.app.databinding.ActivityPagingBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.AppBaseBindingActivity

/**
 * 分页加载的paging
 */
class PagingActivity : AppBaseBindingActivity<ActivityPagingBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityPagingBinding {
        return ActivityPagingBinding.inflate(inflater, container, false)
    }
    
    /**
     * Activity初始化view
     */
    override fun initView() {
    
    }
    
    override fun initData(savedInstanceState: Bundle?) {
     }
}