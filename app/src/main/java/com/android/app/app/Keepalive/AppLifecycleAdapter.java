package com.android.app.app.Keepalive;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.app.R;
import com.android.helper.base.BaseVH;
import com.android.helper.base.recycleview.BaseRecycleAdapter;

import org.jetbrains.annotations.NotNull;

public class AppLifecycleAdapter extends BaseRecycleAdapter<String, AppLifecycleAdapter.VH> {

    public AppLifecycleAdapter(FragmentActivity activity) {
        super(activity);
    }

    /**
     * @param viewType
     * @return 返回一个RecycleView的布局
     */
    @Override
    protected int getLayout(int viewType) {
        return R.layout.item_app_licycle;
    }

    @Override
    protected VH createViewHolder(View inflate, int viewType) {
        return new VH(inflate);
    }

    @Override
    public void onBindHolder(@NonNull @NotNull VH holder, int position) {
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
