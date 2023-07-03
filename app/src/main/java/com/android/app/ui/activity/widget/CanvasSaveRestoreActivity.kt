package com.android.app.ui.activity.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityCanvasSaveRestoreBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

class CanvasSaveRestoreActivity : AppBaseBindingTitleActivity<ActivityCanvasSaveRestoreBinding>() {

    override fun setTitleContent(): String {
        return "Canvas - Save - Restore"
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityCanvasSaveRestoreBinding {
        return ActivityCanvasSaveRestoreBinding.inflate(inflater, container, true)
    }

    override fun initData(savedInstanceState: Bundle?) {
    }
}