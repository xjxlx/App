package com.android.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.app.R
import com.android.app.databinding.ItemTestAdapterBinding
import com.android.common.base.recycleview.BaseRecycleViewAdapter
import com.android.common.base.recycleview.BaseVH
import com.android.common.utils.LogUtil.e

/**
 * @author : 流星
 * @CreateDate: 2021/11/10-11:43 上午
 * @Description:
 */
class TestSingleAdapter(activity: FragmentActivity?, list: List<String>) : BaseRecycleViewAdapter<String, TestSingleAdapter.VH>() {

    fun getBinding(inflater: LayoutInflater, container: ViewGroup?): ItemTestAdapterBinding {
        return ItemTestAdapterBinding.inflate(inflater, container, false)
    }

    class VH(view: View) : BaseVH(view) {
        val tv_test: TextView = view.findViewById(R.id.tv_test)
    }

    override fun bindHolder(holder: VH, position: Int) {
        holder.tv_test.text = mList[position]

        holder.itemView.setOnClickListener { v: View? ->
            val bindingAdapterPosition = holder.getBindingAdapterPosition()
            e("binding :$bindingAdapterPosition  position:$position")
            mItemClickListener?.onItemClick(holder.tv_test, position, mList[position])
        }
    }

    override fun createVH(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH {
        return VH(inflater.inflate(R.layout.item_test_adapter, parent, false))
    }
}