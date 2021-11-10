package com.android.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.android.app.databinding.ItemTestAdapterBinding;
import com.android.helper.base.BaseBindingVH;
import com.android.helper.base.recycleview.BaseBindingRecycleAdapter;
import com.android.helper.utils.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author : 流星
 * @CreateDate: 2021/11/10-11:43 上午
 * @Description:
 */
public class TestSingleAdapter extends BaseBindingRecycleAdapter<String, ItemTestAdapterBinding> {

    public TestSingleAdapter(FragmentActivity activity, List<String> list) {
        super(activity, list);
    }

    @Override
    public void onBindHolder(@NonNull @NotNull BaseBindingVH<ItemTestAdapterBinding> holder, int position) {

        String s = mList.get(position);
        mBinding.tvTest.setText(s);

        mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemBindingClickListener != null) {
                    int bindingAdapterPosition = holder.getBindingAdapterPosition();
                    int layoutPosition1 = holder.getLayoutPosition();
                    int absoluteAdapterPosition = holder.getAbsoluteAdapterPosition();

                    LogUtil.e("binding :" + bindingAdapterPosition + " " + "layout :" + layoutPosition1 + "  absolute :" + absoluteAdapterPosition);

                    mItemBindingClickListener.onItemClick(mBinding, bindingAdapterPosition, mList.get(bindingAdapterPosition));
                }
            }
        });
    }

    @Override
    public ItemTestAdapterBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ItemTestAdapterBinding.inflate(inflater, container, false);
    }


}
