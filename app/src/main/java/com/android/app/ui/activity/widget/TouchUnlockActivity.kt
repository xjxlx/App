package com.android.app.ui.activity.widget

import com.android.app.R
import com.android.helper.base.BaseTitleActivity

class TouchUnlockActivity : BaseTitleActivity() {

    override fun getTitleLayout(): Int {
        return R.layout.activity_touch_unlock
    }

    override fun initView() {
        super.initView()
        setTitleContent("自定义触摸解锁效果")
    }

}