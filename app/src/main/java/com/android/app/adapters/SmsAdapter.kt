package com.android.app.adapters

import android.view.View
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

    override fun bindViewHolder(holder: SmsVH, position: Int) {
        if (type == 1) {
            holder.tv_content.text = "地址：" + mList[position]
        } else {
            holder.tv_content.text = "结果：" + mList[position]
        }
    }

    override fun createVH(viewType: Int): Int {
        return R.layout.item_sms
    }

    class SmsVH(itemView: View) : BaseVH(itemView) {
        val tv_content: TextView = itemView.findViewById(R.id.tv_content)
    }
}