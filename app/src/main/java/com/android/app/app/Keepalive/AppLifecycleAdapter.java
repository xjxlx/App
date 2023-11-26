package com.android.app.app.Keepalive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.app.R;
import com.android.common.base.recycleview.BaseRecycleViewAdapter;
import com.android.common.base.recycleview.BaseVH;

public class AppLifecycleAdapter extends BaseRecycleViewAdapter<String, AppLifecycleAdapter.VH> {

    @Override
    public void bindHolder(@NonNull VH holder, int position) {
        String s = mList.get(position);
        holder.mTvTest.setText(s);
    }

    @Override
    public VH createVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.item_app_licycle, parent, false));
    }

    static class VH extends BaseVH {
        TextView mTvTest;

        public VH(@NonNull View itemView) {
            super(itemView);
            mTvTest = itemView.findViewById(R.id.tv_test);
        }
    }
}
