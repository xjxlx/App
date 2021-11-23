package com.android.app.test

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.app.R
import com.android.app.adapters.TestSingleAdapter
import com.android.app.databinding.ActivityAdapterBinding
import com.android.helper.base.BaseBindingActivity
import com.android.helper.base.recycleview.EmptyPlaceholder
import com.android.helper.utils.RecycleUtil

class AdapterActivity : BaseBindingActivity<ActivityAdapterBinding>() {
    
    private val list = arrayListOf<String>()
    
    /**
     * 初始化数据
     */
    override fun initData() {
        
        for (it in 0..10) {
            list.add("我是item $it")
        }
        
        val adapter = TestSingleAdapter(mContext, list)
        RecycleUtil
            .getInstance(mContext, mBinding.rvList)
            .setVertical()
            .setAdapter(adapter)
        
        val placeholder = EmptyPlaceholder
            .Builder()
            .setEmpty(R.drawable.icon_default, "测试的数据")
            .Build()
        
        adapter.setEmptyData(placeholder)

//        adapter.setItemClickListener { view, position, t ->
//            list[position] = "" + System.currentTimeMillis()
//            adapter.updateItem(position)
//        }
        
        adapter.setItemBindingClickListener { e, position, t ->
            list[position] = "" + System.currentTimeMillis()
            adapter.updateItem(position)
        }
        
    }
    
    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ActivityAdapterBinding {
        return ActivityAdapterBinding.inflate(inflater, container, false)
    }
}