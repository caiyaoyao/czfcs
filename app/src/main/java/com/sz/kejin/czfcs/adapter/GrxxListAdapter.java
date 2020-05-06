package com.sz.kejin.czfcs.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.sz.kejin.czfcs.R;
import com.sz.kejin.czfcs.bean.BasicTableItemBean;
import com.sz.kejin.czfcs.bean.GrxxListItemBeans;

import java.util.List;

import androidx.annotation.Nullable;


public class GrxxListAdapter extends BaseQuickAdapter<GrxxListItemBeans, BaseViewHolder> {

    public GrxxListAdapter(@Nullable List<GrxxListItemBeans> data) {
        super(R.layout.grxx_rv_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GrxxListItemBeans item) {
        helper.setText(R.id.tv_text,item.getText()).setImageResource(R.id.iv_grxx,item.getImgId());
    }
}
