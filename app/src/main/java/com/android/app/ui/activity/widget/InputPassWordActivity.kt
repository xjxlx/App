package com.android.app.ui.activity.widget

import com.android.app.R
import com.android.helper.base.BaseTitleActivity

class InputPassWordActivity : BaseTitleActivity() {

    override fun getTitleLayout(): Int {
        return R.layout.activity_input_pass_word
    }

    override fun initData() {
        super.initData()
        setTitleContent("自定义密码输入框")
    }
}