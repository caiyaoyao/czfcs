package com.sz.kejin.czfcs.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.SqBeans;


import java.util.List;

import androidx.annotation.Nullable;


public class SqRecyclerTabAdapter extends BaseQuickAdapter<SqBeans, BaseViewHolder> {

    public SqRecyclerTabAdapter(@Nullable List<SqBeans> data) {
        super(R.layout.sq_rv_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SqBeans item) {
        helper.setText(R.id.mTv_tab_title,item.getName());
    }
}
