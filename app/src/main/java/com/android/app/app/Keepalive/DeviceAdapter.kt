package com.android.app.app.Keepalive

import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.app.R
import com.android.common.base.recycleview.BaseRecycleViewAdapter
import com.android.common.base.recycleview.BaseVH

class DeviceAdapter(activity: FragmentActivity) : BaseRecycleViewAdapter<Map.Entry<String, String>, DeviceAdapter.VH>() {

    override fun bindViewHolder(holder: VH, position: Int) {
        val (key, value) = mList[position]
        holder.mTvDeviceName.text = value
        holder.mTvDeviceAddress.text = key
    }

    override fun createVH(viewType: Int): Int {
        return R.layout.item_device_list
    }

    class VH(itemView: View) : BaseVH(itemView) {
        val mTvDeviceName: TextView
        val mTvDeviceAddress: TextView

        init {
            mTvDeviceName = itemView.findViewById(R.id.tv_device_name)
            mTvDeviceAddress = itemView.findViewById(R.id.tv_device_address)
        }
    }
}