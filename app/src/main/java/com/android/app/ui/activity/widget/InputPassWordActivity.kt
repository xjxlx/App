package com.android.app.ui.activity.widget

import com.android.app.R
import com.android.helper.base.BaseActivity

class InputPassWordActivity : BaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_input_pass_word
    }

    override fun initData() {

        setTitleContent("自定义密码输入框")
    }
}