package com.android.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.app.R
import com.android.app.adapters.SmsAdapter.SmsVH
import com.android.common.base.recycleview.BaseRecycleViewAdapter
import com.android.common.base.recycleview.BaseVH

class SmsAdapter(activity: FragmentActivity, type: Int) : BaseRecycleViewAdapter<String, SmsVH>() {
    private var activity: FragmentActivity
    private var type: Int

    init {
        this.activity = activity
        this.type = type
    }

    override fun bindHolder(holder: SmsVH, position: Int) {
        if (type == 1) {
            holder.tv_content.text = "地址：" + mList[position]
        } else {
            holder.tv_content.text = "结果：" + mList[position]
        }
    }

    override fun createVH(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): SmsVH {
        return SmsVH(inflater.inflate(R.layout.item_sms, parent, false))
    }

    class SmsVH(itemView: View) : BaseVH(itemView) {
        val tv_content: TextView = itemView.findViewById(R.id.tv_content)
    }
}