package com.android.app.adapters;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.android.app.R;
import com.android.helper.base.BaseActivity;
import com.android.helper.base.BaseVH;
import com.android.helper.base.recycleview.BaseRecycleAdapter;
import com.android.helper.utils.TextViewUtil;

import org.jetbrains.annotations.NotNull;

/**
 * 车库---补充信息---验证码的adapter
 */
public class FaceAuthenticationCodeAdapter extends BaseRecycleAdapter<String, FaceAuthenticationCodeAdapter.VH> {

    public FaceAuthenticationCodeAdapter(BaseActivity mContext) {
        super(mContext);
    }

    @Override
    protected int getLayout() {
        return R.layout.item_face_authentication_code;
    }

    @Override
    protected VH createViewHolder(View inflate) {
        return new VH(inflate);
    }

    @Override
    public void onBindHolder(@NonNull @NotNull VH holder, int position) {
        String s = mList.get(position);
        TextViewUtil.setTextFont(mActivity, holder.tv_code, "DINCondensedBold.ttf");
        if (!TextUtils.isEmpty(s)) {
            TextViewUtil.setText(holder.tv_code, s);
        }
    }

    static class VH extends BaseVH {
        private final TextView tv_code;

        public VH(@NonNull View itemView) {
            super(itemView);
            tv_code = itemView.findViewById(R.id.tv_code);
        }
    }
}
