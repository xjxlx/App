package com.android.app.adapters;

import android.view.LayoutInflater;
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
    public void onBindHolder(@NonNull @NotNull BaseBindingVH<ItemTestAdapterBinding> holder, ItemTestAdapterBinding mBinding, int position) {
        mBinding.tvTest.setText(mList.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (mItemBindingClickListener != null) {
                int bindingAdapterPosition = holder.getBindingAdapterPosition();
                LogUtil.e("binding :" + bindingAdapterPosition + "  position:" + position);
                mItemBindingClickListener.onItemClick(holder.mBinding, position, mList.get(position));
            }
        });
    }

    @Override
    public ItemTestAdapterBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ItemTestAdapterBinding.inflate(inflater, container, false);
    }

}
