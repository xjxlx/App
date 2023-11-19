package com.android.app.ui.activity.personal

import android.view.View
import android.widget.TextView
import com.amap.api.services.core.PoiItem
import com.android.app.R
import com.android.common.base.recycleview.BaseRecycleViewAdapter
import com.android.common.base.recycleview.BaseVH

/**
 * @author : 流星
 * @CreateDate: 2021/12/4-16:34
 * @Description:
 */
class MapAddressAdapter() : BaseRecycleViewAdapter<PoiItem, MapAddressAdapter.VH>() {

    override fun bindViewHolder(holder: VH, position: Int) {
        val poiItem = mList[position]
        val title = poiItem.title
        holder.address.text = title
        mItemClickListener?.onItemClick(holder.address, position, poiItem)
    }

    class VH(root: View) : BaseVH(root) {
        val address = root.findViewById<TextView>(R.id.tv_address)
    }

    override fun createVH(viewType: Int): Int {
        return R.layout.item_map_address
    }
}