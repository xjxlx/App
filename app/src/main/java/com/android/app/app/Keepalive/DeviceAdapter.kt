package com.android.app.app.Keepalive;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.app.R;
import com.android.helper.base.BaseVH;
import com.android.helper.base.recycleview.BaseRecycleAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DeviceAdapter extends BaseRecycleAdapter<Map.Entry<String, String>, DeviceAdapter.VH> {

    public DeviceAdapter(FragmentActivity activity) {
        super(activity);
    }

    /**
     * @param viewType
     * @return 返回一个RecycleView的布局
     */
    @Override
    protected int getLayout(int viewType) {
        return R.layout.item_device_list;
    }

    @Override
    protected VH createViewHolder(View inflate, int viewType) {
        return new VH(inflate);
    }
 
    @Override
    public void onBindHolder(@NonNull @NotNull VH holder, int position) {
        Map.Entry<String, String> entry = mList.get(position);
        holder.mTvDeviceName.setText(entry.getValue());
        holder.mTvDeviceAddress.setText(entry.getKey());
    }

    static class VH extends BaseVH {
        private final TextView mTvDeviceName;
        private final TextView mTvDeviceAddress;

        public VH(@NonNull View itemView) {
            super(itemView);
            mTvDeviceName = itemView.findViewById(R.id.tv_device_name);
            mTvDeviceAddress = itemView.findViewById(R.id.tv_device_address);
        }

    }
}
