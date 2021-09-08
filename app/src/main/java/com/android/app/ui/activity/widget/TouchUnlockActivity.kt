package com.android.app.ui.activity.widget

import com.android.app.R
import com.android.helper.base.BaseActivity

class TouchUnlockActivity : BaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_touch_unlock
    }

    override fun initView() {
        super.initView()
        setTitleContent("自定义触摸解锁效果")
    }

    override fun initData() {

    }

}