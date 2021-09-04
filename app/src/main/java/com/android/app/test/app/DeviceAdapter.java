package com.android.app.test.app;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.app.R;
import com.android.helper.base.BaseRecycleAdapter;
import com.android.helper.base.BaseVH;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class DeviceAdapter extends BaseRecycleAdapter<Map.Entry<String, String>, DeviceAdapter.VH> {

    public DeviceAdapter(Activity mContext) {
        super(mContext);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_device_list;
    }

    @Override
    protected VH createViewHolder(View inflate) {
        return new VH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
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
