package com.android.app.app.Keepalive;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.app.R;
import com.android.helper.base.BaseRecycleAdapter;
import com.android.helper.base.BaseVH;

import org.jetbrains.annotations.NotNull;

public class AppLifecycleAdapter extends BaseRecycleAdapter<String, AppLifecycleAdapter.VH> {

    public AppLifecycleAdapter(Activity mContext) {
        super(mContext);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_app_licycle;
    }

    @Override
    protected VH createViewHolder(View inflate) {
        return new VH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VH holder, int position) {
        String s = mList.get(position);
        holder.mTvTest.setText(s);
    }

    static class VH extends BaseVH {
        private TextView mTvTest;

        public VH(@NonNull View itemView) {
            super(itemView);
            mTvTest = itemView.findViewById(R.id.tv_test);
        }
    }
}
