package com.android.app.ui.activity.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.app.R;
import com.android.app.databinding.ActivityPageListBinding;
import com.android.common.base.BaseBindingTitleActivity;
import com.android.helper.utils.ConvertUtil;
import com.android.helper.widget.pagelistview.PageListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义横向的滑动列表
 */
public class PageListActivity extends BaseBindingTitleActivity<ActivityPageListBinding> {

    /**
     * 初始化数据
     *
     * @param savedInstanceState 保存的数据对象
     */
    @Override
    public void initData(Bundle savedInstanceState) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            list.add("position:" + i);
        }
        AdapterPage adapterPage = new AdapterPage(this, list);
        mBinding.plvList.setColumn(4);
        mBinding.plvList.setRwo(3);
        mBinding.plvList.setRowInterval((int) ConvertUtil.toDp(5));
        mBinding.plvList.setAdapter(adapterPage);
    }

    @NonNull
    @Override
    public ActivityPageListBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, boolean attachToRoot) {
        return ActivityPageListBinding.inflate(inflater, container, true);
    }

    @NonNull
    @Override
    public String getTitleContent() {
        return "自定义分页列表";
    }

    class AdapterPage extends PageListAdapter<String> {
        public AdapterPage(Activity activity, List<String> list) {
            super(activity, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint("ViewHolder")
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_page_list, parent, true);
            TextView tvTestView = inflate.findViewById(R.id.tv_test_view);
            String s = mList.get(position);
            tvTestView.setText(s);
            return inflate;
        }
    }

}