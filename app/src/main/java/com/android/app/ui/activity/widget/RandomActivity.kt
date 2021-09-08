package com.android.app.ui.activity.widget

import com.android.app.R
import android.widget.TextView
import com.android.helper.base.BaseActivity
import com.android.helper.utils.TextViewUtil
import com.android.helper.utils.ToastUtil
import kotlinx.android.synthetic.main.activity_random.*

/**
 * 随机布局的activity
 */
class RandomActivity : BaseActivity() {

    override fun getBaseLayout(): Int {
        return R.layout.activity_random
    }

    override fun initData() {


        setTitleContent("随机数的布局")

        val listData = arrayListOf<String>()

        listData.add("this")
        listData.add("in")
        listData.add("water")
        listData.add("live")
        listData.add("Many")
        listData.add("gnimals")

        val inflate = layoutInflater.inflate(R.layout.item_random, null)
        val randomContent = inflate.findViewById<TextView>(R.id.tv_random_content)
        TextViewUtil.setTextFont(mContext, randomContent, "DINCondensedBold.ttf")

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