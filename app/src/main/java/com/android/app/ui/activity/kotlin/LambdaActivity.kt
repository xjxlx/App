package com.android.app.ui.activity.kotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityLambdaBinding
import com.android.helper.base.title.AppBaseBindingTitleActivity

class LambdaActivity : AppBaseBindingTitleActivity<ActivityLambdaBinding>() {

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityLambdaBinding {
        return ActivityLambdaBinding.inflate(inflater, container, true)
    }

    override fun setTitleContent(): String {
        return "Kotlin中的Lambda表达式"
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

}