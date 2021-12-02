package com.android.app.ui.activity.personal

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.databinding.ActivityRouseDingDingBinding
import com.android.helper.base.title.BaseBindingTitleActivity

/**
 * 唤醒钉钉的页面
 * 设计规则：
 *      1：需要地图的定位页面，可以手动的选择定位的位置，并且把选中的地方逆地里解析出具体的定位信息
 *      2：需要后台定位信息，获取当前的具体位置信息
 *      3：查看是否可以控制轮询定位的时间，如果可以，就动态定位，不行的话，就使用handler之类的去控制
 *      4：对比当前的定位信息，设置轮询的时间
 *      5：唤醒钉钉
 */
class RouseDingDingActivity : BaseBindingTitleActivity<ActivityRouseDingDingBinding>() {
    
    override fun initData() {
    }
    
    override fun setTitleContent(): String {
        return "唤醒钉钉"
    }
    
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityRouseDingDingBinding {
        return ActivityRouseDingDingBinding.inflate(inflater, container, true)
    }
    
}