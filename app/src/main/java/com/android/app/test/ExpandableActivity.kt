package com.android.app.test

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ExpandableListView
import com.android.app.adapters.HighWordExpandableAdapter
import com.android.app.bean.ExpandableBean
import com.android.app.databinding.ActivityExpandableBinding
import com.android.common.base.BaseBindingActivity
import com.android.helper.utils.ExpandableUtil
import com.android.helper.utils.StreamUtil
import com.google.gson.Gson

/** 测试拓展listView的加载顺序 */
class ExpandableActivity : BaseBindingActivity<ActivityExpandableBinding>() {
    /** Activity初始化view */
    override fun initView() {}

    override fun initListener() {

        val assets = mActivity.assets
        val inputStream = assets.open("expandable.json")

        val json = StreamUtil.InputStreamToString(inputStream)

        if (!TextUtils.isEmpty(json)) {

            val gson = Gson()
            val bean = gson.fromJson(json, ExpandableBean::class.java)
            // LogUtil.e("bean:$bean")

            val content = bean.data.content
            val adapter2 = HighWordExpandableAdapter(mActivity, content)
            mBinding.evlLists.setAdapter(adapter2)

            ExpandableUtil.openSelfCloseOther(mBinding.evlLists)
            ExpandableUtil.openCurrent(mBinding.evlLists, 0)

            mBinding.evlLists.setOnGroupClickListener(ExpandableListView.OnGroupClickListener { parent, v, groupPosition, id ->
                if (parent.isGroupExpanded(groupPosition)) {
                    parent.collapseGroup(groupPosition)
                } else {
                    // 第二个参数false表示展开时是否触发默认滚动动画
                    parent.expandGroup(groupPosition, false)
                }
            })
        }
    }

    override fun initData(savedInstanceState: Bundle?) {

        val assets = mActivity.assets
        val inputStream = assets.open("expandable.json")

        val json = StreamUtil.InputStreamToString(inputStream)

        if (!TextUtils.isEmpty(json)) {

            val gson = Gson()
            val bean = gson.fromJson(json, ExpandableBean::class.java)

            val content = bean.data.content
            val adapter2 = HighWordExpandableAdapter(mActivity, content)
            mBinding.evlLists.setAdapter(adapter2)

            ExpandableUtil.openSelfCloseOther(mBinding.evlLists)
            ExpandableUtil.openCurrent(mBinding.evlLists, 0)
        }
    }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): ActivityExpandableBinding {
        return ActivityExpandableBinding.inflate(inflater, container, false)
    }
}
