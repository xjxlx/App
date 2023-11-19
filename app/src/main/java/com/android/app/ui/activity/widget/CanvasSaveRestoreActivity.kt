package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityCanvasSaveRestoreBinding
import com.android.common.base.BaseBindingTitleActivity

class CanvasSaveRestoreActivity : BaseBindingTitleActivity<ActivityCanvasSaveRestoreBinding>() {

    override fun getTitleContent(): String {
        return "Canvas - Save - Restore"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityCanvasSaveRestoreBinding {
        return ActivityCanvasSaveRestoreBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {}
}
