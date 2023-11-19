package com.android.app.ui.activity.personal;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.amap.api.services.core.PoiItem;
import com.android.app.databinding.ItemMapAddressBinding;
import com.android.helper.base.BaseBindingVH;
import com.android.helper.base.recycleview.BaseBindingRecycleAdapter;
import com.android.helper.utils.TextViewUtil;

import org.jetbrains.annotations.NotNull;

/**
 * @author : 流星
 * @CreateDate: 2021/12/4-16:34
 * @Description:
 */
public class MapAddressAdapter extends BaseBindingRecycleAdapter<PoiItem, ItemMapAddressBinding> {

    public MapAddressAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public void onBindHolder(@NonNull @NotNull BaseBindingVH<ItemMapAddressBinding> holder, ItemMapAddressBinding mBinding, int position) {
        PoiItem poiItem = mList.get(position);
        if (poiItem != null) {
            String title = poiItem.getTitle();
            TextViewUtil.setText(mBinding.tvAddress, title);

            mBinding.getRoot().setOnClickListener(v -> {
                if (mItemBindingClickListener != null) {
                    mItemBindingClickListener.onItemClick(mBinding.getRoot(), mBinding, position, poiItem);
                }
            });
        }
    }

    @Override
    public ItemMapAddressBinding getBinding(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container) {
        return ItemMapAddressBinding.inflate(inflater, container, false);
    }

}
