package com.android.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.app.R;
import com.android.app.bean.HomeBean;
import com.android.common.base.recycleview.BaseRecycleViewAdapter;
import com.android.common.base.recycleview.BaseVH;
import com.android.helper.utils.TextViewUtil;
import com.android.helper.utils.ToastUtil;
import com.android.helper.utils.photo.GlideUtil;

import java.util.List;

public class TestAdapter extends BaseRecycleViewAdapter<HomeBean.ReturnDataList.Data, TestAdapter.VHHome> {

    private GlideUtil mGlideUtil;
    private FragmentActivity mFragmentActivity;

    public TestAdapter(FragmentActivity activity) {
        this.mFragmentActivity = activity;
    }

    public TestAdapter(FragmentActivity activity, List<HomeBean.ReturnDataList.Data> list) {
        this.mFragmentActivity = activity;
        setList(list);
    }

    @Override
    public void bindHolder(@NonNull VHHome holder, int position) {
        HomeBean.ReturnDataList.Data data = mList.get(position);
        if (data == null) {
            return;
        }
        if (mGlideUtil == null) {
            mGlideUtil = new GlideUtil.Builder(mFragmentActivity).build();
        }
        // 封面
        mGlideUtil.loadUrl(holder.iv_activity, data.getImg());
        //  显示状态：-1不现实0已结束1进行中
        int showStatus = data.getShowStatus();
        switch (showStatus) {
            case -1:
                holder.tv_status.setVisibility(View.GONE);
                break;
            case 0:
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_status.setText("已结束");
                holder.tv_status.setBackgroundResource(R.drawable.bg_radius_left_50dp_gray);
                break;
            case 1:
                holder.tv_status.setVisibility(View.VISIBLE);
                holder.tv_status.setText("进行中");
                holder.tv_status.setBackgroundResource(R.drawable.bg_radius_left_50dp_gold);
                break;
        }
        // 活动的名字
        TextViewUtil.setText(holder.tv_activity_title, data.getName());
        holder.iv_share.setOnClickListener(view -> {
            ToastUtil.show("分享");
        });
    }

    @Override
    public VHHome createVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return new VHHome(inflater.inflate(R.layout.item_test, parent, false));
    }

    static class VHHome extends BaseVH {
        private final ImageView iv_activity;
        private final TextView tv_status;
        private final TextView tv_activity_title;
        private final View iv_share;

        public VHHome(@NonNull View itemView) {
            super(itemView);
            iv_activity = itemView.findViewById(R.id.iv_activity);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_activity_title = itemView.findViewById(R.id.tv_activity_title);
            iv_share = itemView.findViewById(R.id.iv_share);
        }
    }
}
