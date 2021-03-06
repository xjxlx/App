package com.android.app.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.app.R;
import com.android.helper.base.BaseVH;
import com.android.helper.base.recycleview.BaseRecycleAdapter;

import org.jetbrains.annotations.NotNull;

public class SmsAdapter extends BaseRecycleAdapter<String, SmsAdapter.SmsVH> {
    private int type;

    public SmsAdapter(FragmentActivity activity, int type) {
        super(activity);
        this.type = type;
    }

    /**
     * @param viewType
     * @return 返回一个RecycleView的布局
     */
    @Override
    protected int getLayout(int viewType) {
        return R.layout.item_sms;
    }

    @Override
    protected SmsVH createViewHolder(View inflate, int viewType) {
        return new SmsVH(inflate);
    }

    @Override
    public void onBindHolder(@NonNull @NotNull SmsVH holder, int position) {
        if (type == 1) {
            holder.tv_content.setText("地址：" + mList.get(position));
        } else {
            holder.tv_content.setText("结果：" + mList.get(position));
        }
    }

    static class SmsVH extends BaseVH {
        private final TextView tv_content;

        public SmsVH(@NonNull View itemView) {
            super(itemView);
            tv_content = itemView.findViewById(R.id.tv_content);
        }
    }
}
