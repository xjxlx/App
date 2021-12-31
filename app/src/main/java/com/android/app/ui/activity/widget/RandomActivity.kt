package com.android.app.ui.activity.widget

import android.os.Bundle
import com.android.app.R
import android.widget.TextView
import com.android.helper.base.AppBaseActivity
import com.android.helper.utils.TextViewUtil
import com.android.helper.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_random.*

/**
 * 随机布局的activity
 */
class RandomActivity : AppBaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_random
    }
    
    /**
     * Activity初始化view
     */
    override fun initView() {
    
    }
    
    override fun initData(savedInstanceState: Bundle?) {
        val listData = arrayListOf<String>()

        listData.add("this")
        listData.add("in")
        listData.add("water")
        listData.add("live")
        listData.add("Many")
        listData.add("gnimals")
        val inflate = layoutInflater.inflate(R.layout.item_random, null)
        val randomContent = inflate.findViewById<TextView>(R.id.tv_random_content)
        TextViewUtil.setTextFont(mActivity, randomContent, "DINCondensedBold.ttf")

        rl_layout.textView = randomContent
        rl_layout.setRandomRotatingView(true)


        rl_layout.setRandomClickListener { _, position: Int, t ->
            ToastUtil.show("position:$position   value:$t")
        }

        btn_start.setOnClickListener {
            rl_layout.setDataList(listData)
        }
    }
}