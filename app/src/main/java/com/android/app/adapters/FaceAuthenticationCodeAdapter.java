package com.android.app.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.app.R;
import com.android.common.base.recycleview.BaseRecycleViewAdapter;
import com.android.common.base.recycleview.BaseVH;
import com.android.helper.utils.TextViewUtil;

/**
 * 车库---补充信息---验证码的adapter
 */
public class FaceAuthenticationCodeAdapter extends BaseRecycleViewAdapter<String, FaceAuthenticationCodeAdapter.VH> {
    private FragmentActivity mActivity;

    public FaceAuthenticationCodeAdapter(FragmentActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public void bindHolder(@NonNull VH holder, int position) {
        String s = mList.get(position);
        TextViewUtil.setTextFont(mActivity, holder.tv_code, "DINCondensedBold.ttf");
        if (!TextUtils.isEmpty(s)) {
            TextViewUtil.setText(holder.tv_code, s);
        }
    }

    @Override
    public VH createVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return new VH(inflater.inflate(R.layout.item_face_authentication_code, parent, false));
    }

    static class VH extends BaseVH {
        private final TextView tv_code;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv_code = itemView.findViewById(R.id.tv_code);
        }
    }
}
