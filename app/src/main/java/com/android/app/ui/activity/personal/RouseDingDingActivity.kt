package com.android.app.ui.activity.personal

import com.android.app.R
import com.android.helper.base.BaseTitleActivity

/**
 * 唤醒钉钉的页面
 */
class RouseDingDingActivity : BaseTitleActivity() {

    override fun initData() {
    }

    override fun setTitleContent(): String {
        return "唤醒钉钉"
    }

    override fun getTitleLayout(): Int {
        return R.layout.activity_rouse_ding_ding
    }
//    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityRouseDingDingBinding {
//        return ActivityRouseDingDingBinding.inflate(inflater, container, false)
//    }
}