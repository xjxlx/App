package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.R
import com.android.app.databinding.ActivityTouchUnlockBinding
import com.android.common.base.BaseBindingTitleActivity

class TouchUnlockActivity : BaseBindingTitleActivity<ActivityTouchUnlockBinding>() {
    override fun getTitleContent(): String {
        return ""
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityTouchUnlockBinding {
        return ActivityTouchUnlockBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {}
}
