package com.android.app.app.Keepalive

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.android.app.R
import com.android.common.base.recycleview.BaseRecycleViewAdapter
import com.android.common.base.recycleview.BaseVH

class DeviceAdapter(activity: FragmentActivity) : BaseRecycleViewAdapter<Map.Entry<String, String>, DeviceAdapter.VH>() {

    override fun bindHolder(holder: VH, position: Int) {
        val (key, value) = mList[position]
        holder.mTvDeviceName.text = value
        holder.mTvDeviceAddress.text = key
    }

    override fun createVH(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): VH {
        return VH(inflater.inflate(R.layout.item_device_list, parent, false))
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