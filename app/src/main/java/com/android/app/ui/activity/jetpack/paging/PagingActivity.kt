package com.android.app.ui.activity.jetpack.paging

import android.os.Bundle
import com.android.app.databinding.ActivityPagingBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.helper.base.BaseBindingActivity

/**
 * 分页加载的paging
 */
class PagingActivity : BaseBindingActivity<ActivityPagingBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityPagingBinding {
        return ActivityPagingBinding.inflate(inflater, container, false)
    }

     override fun initData(savedInstanceState: Bundle?) {
     }
}